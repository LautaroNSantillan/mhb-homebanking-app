package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.repositories.AccountRepository;
import com.mindhub.homeBanking.repositories.ClientRepository;
import com.mindhub.homeBanking.repositories.TransactionRepository;
import com.mindhub.homeBanking.utilities.ErrorResponse;
import com.mindhub.homeBanking.utilities.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accRepo;
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private TransactionRepository transactionRepo;
    @Transactional
    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/transaction")
    public ResponseEntity<Object> makeTransaction (
            @RequestParam String originAccNumber,
            @RequestParam String destinationAccNumber,
            @RequestParam double amount,
            @RequestParam String description,
            Authentication auth)  {

        if(originAccNumber.isEmpty()){
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),"Missing origin account",null), HttpStatus.FORBIDDEN);
        }
        if(destinationAccNumber.isEmpty()){
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),"Missing destination account",null), HttpStatus.FORBIDDEN);
        }
        if(amount<1){
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),"Invalid amount",null), HttpStatus.FORBIDDEN);
        }
        if(description.length() > 100){
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),"Description exceeds maximum length of 100 characters",null), HttpStatus.FORBIDDEN);
        }
        if(description.isEmpty()){
            description="No description provided";
        }
        if(originAccNumber.equals(destinationAccNumber)){
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),"It is not possible to send money to the same account",null), HttpStatus.FORBIDDEN);
        }
        if(!accRepo.existsByNumber(originAccNumber)){
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),"Origin Account doesn't exist",null), HttpStatus.FORBIDDEN);
        }
        if(!accRepo.existsByNumber(destinationAccNumber)){
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),"Destination Account doesn't exist",null), HttpStatus.FORBIDDEN);
        }

        Client sender = clientRepo.findByEmail(auth.getName());
        Client destination = accRepo.findByNumber(destinationAccNumber).getClient();

        Account originAcc = accRepo.findByNumber(originAccNumber);
        Account destinationAcc = accRepo.findByNumber(destinationAccNumber);

        if(!sender.getAccounts().contains(originAcc)){
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),"You don't posses this account",null), HttpStatus.FORBIDDEN);
        }

        if(!accRepo.existsByNumber(destinationAccNumber)){
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),"Destination account doesn't exist",null), HttpStatus.FORBIDDEN);
        }
        try {
            Transaction withdrawTransaction = originAcc.withdraw(amount, description, originAcc, transactionRepo, accRepo, originAcc.getBalance());
            Transaction depositTransaction = destinationAcc.deposit(amount, description,originAcc.getClient(), destinationAcc, transactionRepo, accRepo);
            return new ResponseEntity<>("Transaction successful", HttpStatus.CREATED);
        } catch (InsufficientFundsException e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),"Insufficient funds",null), HttpStatus.FORBIDDEN);
        }

    }
}
