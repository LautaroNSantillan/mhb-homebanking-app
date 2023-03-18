package com.mindhub.homeBanking.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.dtos.ClientDTO;
import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.repositories.AccountRepository;
import com.mindhub.homeBanking.repositories.ClientRepository;
import com.mindhub.homeBanking.repositories.TransactionRepository;
import com.mindhub.homeBanking.utilities.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accRepo;
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private TransactionRepository transactionRepo;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accRepo.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("accounts/{id}")
    public Optional<AccountDTO> getAccount(@PathVariable Long id) {
        return accRepo.findById(id).map(AccountDTO::new);
    }

    @PostMapping("clients/current/accounts")
    public ResponseEntity<Object> generateNewAccount(Authentication auth) {
        Client currentClient = clientRepo.findByEmail(auth.getName());
        if (currentClient.getAccounts().size() < 3) {
            Account newAcc = new Account(Utils.generateVin(currentClient), LocalDateTime.now(), 0, accRepo);
            currentClient.addAccount(newAcc);
            accRepo.save(newAcc);
            return new ResponseEntity<>(new AccountDTO(newAcc), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Client already has 3 or more accounts", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("clients/current/delete-account")
    public ResponseEntity<Object> deleteAccount(@RequestParam long cardId, Authentication auth) {
        Client currentClient = clientRepo.findByEmail(auth.getName());
        Optional<Account> optionalAccount = accRepo.findById(cardId);
        if (optionalAccount.isPresent()) {
            Account accountToDelete = optionalAccount.get();
            if (accountToDelete.getClient().equals(currentClient)) {
                List<Transaction> transactionsToDelete = transactionRepo.findByAccount(accountToDelete);
                transactionRepo.deleteAll(transactionsToDelete);
                accRepo.delete(accountToDelete);
                return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("This account does not belong to the current client", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
    }

}
