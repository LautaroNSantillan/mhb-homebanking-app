package com.mindhub.homeBanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homeBanking.repositories.AccountRepository;
import com.mindhub.homeBanking.repositories.TransactionRepository;
import com.mindhub.homeBanking.services.AccountService;
import com.mindhub.homeBanking.services.TransactionService;
import com.mindhub.homeBanking.services.impl.AccountServiceImpl;
import com.mindhub.homeBanking.services.impl.TransactionServiceImpl;
import com.mindhub.homeBanking.utilities.InsufficientFundsException;
import com.mindhub.homeBanking.utilities.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id")
    @SequenceGenerator(name = "account_id", sequenceName = "account_id_seq", allocationSize = 1 )
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private String alias;
    private boolean isDisabled;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Transaction> transactions = new HashSet<>();

    public Account() {
    }

    public Account(LocalDateTime creation, double balance, AccountServiceImpl accService, Client client, AccountType accountType) {
        this.setBalance(balance);
        this.setNumber(this.generateAccountNumber(accService));
        this.setCreationDate(creation);
        this.setAlias(accService);
        this.setClient(client);
        this.setType(accountType);
        this.setDisabled(false);
    }

    public void depositInterest(AccountServiceImpl accountService, TransactionServiceImpl transactionService) {
        if (type == AccountType.SAVINGS) {
            double interest = balance * type.getAnnualPercentageYield() / 365;
            this.deposit(interest, "SAVING INTEREST", null,transactionService, accountService);
        }
    }
    public String generateAccountNumber(AccountServiceImpl accountService) {
        String vinPrefix = "VIN-";
        long accountNumber = accountService.countAccounts() + 1;
        String accountNumberString = String.valueOf(accountNumber);
        int numZeros = 3 - accountNumberString.length();
        String zeros = "0".repeat(Math.max(0, numZeros));
        return vinPrefix + zeros + accountNumberString;
    }
    public void setAlias(AccountService accService) {
        this.alias=(Utils.generateUniqueAlias(accService));
    }

    public Transaction withdraw(double amount, String description, TransactionService transactionService, AccountService accService, double accBalance) throws InsufficientFundsException {
        if (accBalance < amount) {
            throw new InsufficientFundsException();
        }
        this.setBalance(this.getBalance()-amount);
        Transaction withdraw = transactionService.createNewDebitTransaction(amount, description, this);
        transactionService.save(withdraw);
        accService.save(this);
        return withdraw;
    }

    public Transaction deposit(double amount, String description,Client originClient,TransactionService transactionService, AccountService accService){
        this.setBalance(this.getBalance()+amount);
        Transaction deposit = transactionService.createNewCreditTransaction(amount, description, this, originClient);
        transactionService.save(deposit);
        accService.save(this);
        return deposit;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }
    public long getId() {
        return id;
    }
    @JsonIgnore
    public Set<Transaction> getTransactions() {
        return transactions;
    }
    public String getNumber() {
        return number;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public double getBalance() {
        return balance;
    }
    public String getAlias() {
        return alias;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public AccountType getType() {
        return type;
    }
    public void setType(AccountType type) {
        this.type = type;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}

