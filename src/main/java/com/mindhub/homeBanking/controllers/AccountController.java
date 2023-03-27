package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.services.AccountService;
import com.mindhub.homeBanking.services.ClientService;
import com.mindhub.homeBanking.services.TransactionService;
import com.mindhub.homeBanking.services.impl.AccountServiceImpl;
import com.mindhub.homeBanking.services.impl.ClientServiceImpl;
import com.mindhub.homeBanking.services.impl.TransactionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accService;
    private final ClientService clientService;
    private final TransactionService transactionService;

    public AccountController(AccountService accService, ClientService clientService, TransactionService transactionService) {
        this.accService = accService;
        this.clientService = clientService;
        this.transactionService = transactionService;
    }

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accService.getAccountsDTO();
    }
    @GetMapping("accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accService.getAccountDTO(id);
    }

    @PostMapping("clients/current/accounts")
    public ResponseEntity<Object> generateNewAccount(Authentication auth, @RequestParam String type) {
        Client currentClient = clientService.findByEmail(auth.getName());
        if (currentClient.getAccounts().size() < 3) {
            try {
                AccountType accountType = AccountType.valueOf(type.toUpperCase());
                accService.saveNewAccount(currentClient, accountType);
                return new ResponseEntity<>("Created",HttpStatus.CREATED);
            } catch (IllegalArgumentException ex) {
                return new ResponseEntity<>("Invalid account type", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Client already has 3 or more accounts", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("clients/current/delete-account--OBSOLETE")
    public ResponseEntity<Object> deleteAccountObsolete(@RequestParam long accId, Authentication auth) {
        Client currentClient = clientService.findByEmail(auth.getName());
        Optional<Account> optionalAccount = accService.findByIdOptional(accId);
        if (optionalAccount.isPresent()) {
            Account accountToDelete = optionalAccount.get();
            if (accountToDelete.getClient().equals(currentClient)) {
                List<Transaction> transactionsToDelete = transactionService.findByAccount(accountToDelete);
                //transactionService.deleteAll(transactionsToDelete);
                accService.deleteAccount(accountToDelete);
                return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("This account does not belong to the current client", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("clients/current/delete-account")
    public ResponseEntity<Object> deleteAccount(@RequestParam long accId, Authentication auth) {
        Client currentClient = clientService.findByEmail(auth.getName());
        Optional<Account> optionalAccount = accService.findByIdOptional(accId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (account.getClient().equals(currentClient)) {
                // check if the account being deleted is the last account for the client
                List<Account> accounts = accService.findNonDisabledAccountsByClient(currentClient);
                if (accounts.size() == 1 && accounts.get(0).equals(account)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot delete your only account.");
                }else if(account.getBalance()>0){
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You still have balance in this account.");
                }else {
                    accService.disableAcc(account);
                    accService.save(account);
                    return ResponseEntity.ok("Account deleted successfully");
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this account with.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account with id " + accId + " not found.");
        }
    }

}
