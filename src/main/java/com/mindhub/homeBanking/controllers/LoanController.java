package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.LoanApplicationDTO;
import com.mindhub.homeBanking.dtos.LoanDTO;
import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.models.loans.DynamicLoan;
import com.mindhub.homeBanking.services.*;
import com.mindhub.homeBanking.utilities.ErrorResponse;
import com.mindhub.homeBanking.utilities.LoanNotFoundException;
import com.mindhub.homeBanking.utilities.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class LoanController {

    private final LoanService loanService;
    private final ClientLoanService clientLoanService;
    private final ClientService clientService;
    private final AccountService accService;
    private final TransactionService transactionService;


    public LoanController(LoanService loanService, ClientLoanService clientLoanService, ClientService clientService, AccountService accService, TransactionService transactionService) {
        this.loanService = loanService;
        this.clientLoanService = clientLoanService;
        this.clientService = clientService;
        this.accService = accService;
        this.transactionService = transactionService;
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> requestLoan(@RequestBody LoanApplicationDTO requestedLoan, Authentication auth) {

        Client currentClient;
        Loan injectedLoan;
        Loan loanType;
        Account clientAcc;

        try {
            clientAcc = accService.findByNumber(requestedLoan.getDestinationAccNumber());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Invalid account", null),
                    HttpStatus.FORBIDDEN);
        }

        try {
            currentClient = clientService.findByEmail(auth.getName());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Invalid client", null),
                    HttpStatus.FORBIDDEN);
        }

        try {
            injectedLoan = loanService.findById(requestedLoan.getLoanId());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Invalid loan type", null),
                    HttpStatus.FORBIDDEN);
        }


            // Check if amount and payments are positive
            if (requestedLoan.getAmount() <= 0 || requestedLoan.getPayments() <= 0) {
                return new ResponseEntity<>(
                        new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Amount and payments must be positive", null),
                        HttpStatus.FORBIDDEN);
            }

            if (currentClient.getLoans().stream().anyMatch(loan -> loan.getId().equals(requestedLoan.getLoanId()))) {
                return new ResponseEntity<>(
                        new ErrorResponse(HttpStatus.FORBIDDEN.value(), "You've already taken out a loan of this category", null),
                        HttpStatus.FORBIDDEN);
            }

        assert injectedLoan != null;
        if (requestedLoan.getAmount() > injectedLoan.getMaxAmount()) {
                return new ResponseEntity<>(
                        new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Amount surpasses max amount available", null),
                        HttpStatus.FORBIDDEN);
            }

            // Check if destination account is valid
            if (requestedLoan.getDestinationAccNumber() == null) {
                return new ResponseEntity<>(
                        new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Invalid destination account", null),
                        HttpStatus.FORBIDDEN);
            }

            if (currentClient.getAccounts().stream().noneMatch(account -> account.getId()==clientAcc.getId())) {
                return new ResponseEntity<>(
                        new ErrorResponse(HttpStatus.FORBIDDEN.value(), "You don't own this account", null),
                        HttpStatus.FORBIDDEN);
            }

            if (!injectedLoan.getPayments().contains(requestedLoan.getPayments())) {
                return new ResponseEntity<>(
                        new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Invalid number of payments", null),
                        HttpStatus.FORBIDDEN);
            }

     clientAcc.deposit(requestedLoan.getAmount(), requestedLoan.getLoanId(), null, transactionService, accService);

        // Save the loan to the database and return the response
        clientLoanService.saveNewClientLoan(this.calculatePayments(requestedLoan.getAmount(), injectedLoan.getInterestRate()), requestedLoan.getPayments(),currentClient,injectedLoan);

        return new  ResponseEntity<>(HttpStatus.CREATED);

    }
    @RequestMapping("/loans/{type}/DTO")
    @PreAuthorize("hasAuthority('CLIENT')")
        public LoanDTO getDTO(@PathVariable String type) throws LoanNotFoundException {
            return new LoanDTO(loanService.findById(type.toUpperCase()));
        }

    @PostMapping("/loans/final-payments")
    public double getPaymentAmount(@RequestParam int amount, @RequestParam int payments) {
        double paymentAmount = (amount * 1.2) / payments;
        return Math.round(paymentAmount * 100.0) / 100.0;
    }

    @PostMapping("/loans/create")
    public ResponseEntity<Object> createLoan(@RequestBody DynamicLoan newLoan) {
        if (newLoan.getId().isEmpty() ) {
            return new ResponseEntity<>("Missing ID", HttpStatus.BAD_REQUEST);
        }
        if (newLoan.getPayments().isEmpty() ) {
            return new ResponseEntity<>("Missing payments", HttpStatus.BAD_REQUEST);
        }
        if (newLoan.getName().isEmpty() ) {
            return new ResponseEntity<>("Missing name", HttpStatus.BAD_REQUEST);
        }
        if (Double.isNaN(newLoan.getInterestRate()) || newLoan.getInterestRate() <= 0 ) {
            return new ResponseEntity<>("Invalid interest rate", HttpStatus.BAD_REQUEST);
        }
        if (newLoan.getMaxAmount()<=0 ) {
            return new ResponseEntity<>("Invalid max amount", HttpStatus.BAD_REQUEST);
        }

        newLoan.setPredefinedLoan(false);
       loanService.saveNewLoan(newLoan);

        String message = "Created new loan " + newLoan.getName();
        SuccessResponse success = new SuccessResponse(HttpStatus.CREATED.value(),message, " ");
        return new ResponseEntity<>(success, HttpStatus.CREATED);
    }

    private double calculatePayments(double loanAmount, double interestRate){
      return  loanAmount * (1 + (interestRate/ 100));
    }
}
