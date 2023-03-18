package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.dtos.LoanApplicationDTO;
import com.mindhub.homeBanking.dtos.LoanDTO;
import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.models.loans.DynamicLoan;
import com.mindhub.homeBanking.models.loans.PredefinedLoan;
import com.mindhub.homeBanking.repositories.AccountRepository;
import com.mindhub.homeBanking.repositories.ClientLoanRepository;
import com.mindhub.homeBanking.repositories.ClientRepository;
import com.mindhub.homeBanking.repositories.LoanRepository;
import com.mindhub.homeBanking.utilities.ErrorResponse;
import com.mindhub.homeBanking.utilities.LoanNotFoundException;
import com.mindhub.homeBanking.utilities.LoanValidationException;
import com.mindhub.homeBanking.utilities.SuccessResponse;
import net.bytebuddy.asm.Advice;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private ClientLoanRepository clientLoanRepo;
    @Autowired
    private LoanRepository loanRepo;
    @Autowired
    private AccountRepository accRepo;

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> requestLoan(@RequestBody LoanApplicationDTO requestedLoan, Authentication auth) {

        Client currentClient;
        Loan injectedLoan;
        Loan loanType;
        Account clientAcc;

        try {
            clientAcc = accRepo.findByNumber(requestedLoan.getDestinationAccNumber());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Invalid account", null),
                    HttpStatus.FORBIDDEN);
        }

        try {
            currentClient = clientRepo.findByEmail(auth.getName());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Invalid client", null),
                    HttpStatus.FORBIDDEN);
        }

        try {
            injectedLoan = loanRepo.findById(requestedLoan.getLoanId()).orElse(null);
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



        // Create a new loan object from loanType
        double paymentAmount = requestedLoan.getAmount() * (1 + (injectedLoan.getInterestRate() / 100));
        ClientLoan clientLoan = new ClientLoan(paymentAmount, requestedLoan.getPayments(),currentClient,injectedLoan);

        clientAcc.setBalance(clientAcc.getBalance()+ requestedLoan.getAmount());
        accRepo.save(clientAcc);

        // Save the loan to the database and return the response
        clientLoanRepo.save(clientLoan);

        return new  ResponseEntity<>(HttpStatus.CREATED);

    }


    @RequestMapping("/loans/{type}/DTO")
    @PreAuthorize("hasAuthority('CLIENT')")
        public LoanDTO getDTO(@PathVariable String type) throws LoanNotFoundException {
            return new LoanDTO(loanRepo.findById(type.toUpperCase()).orElseThrow(LoanNotFoundException::new));
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
        Loan createdLoan = new Loan(newLoan);

        loanRepo.save(createdLoan);

        String message = "Created new loan " + createdLoan.getName();
        SuccessResponse success = new SuccessResponse(HttpStatus.CREATED.value(),message, " ");
        return new ResponseEntity<>(success, HttpStatus.CREATED);
    }
}
