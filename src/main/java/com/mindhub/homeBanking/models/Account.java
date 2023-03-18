package com.mindhub.homeBanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homeBanking.repositories.AccountRepository;
import com.mindhub.homeBanking.repositories.TransactionRepository;
import com.mindhub.homeBanking.utilities.InsufficientFundsException;
import com.mindhub.homeBanking.utilities.Utils;

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
    @Enumerated(EnumType.STRING)
    private AccountType type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<>();

    public Account() {
    }

    public Account(String number, LocalDateTime creation, double balance, AccountRepository accRepo) {
        this.setBalance(balance);
        this.setNumber(number);
        this.setCreationDate(creation);
        this.setAlias(accRepo);
    }

    public void depositInterest(AccountRepository accRepo, TransactionRepository transactionRepo) {
        if (type == AccountType.SAVINGS) {
            double interest = balance * type.getAnnualPercentageYield() / 365;
            this.deposit(interest, "SAVING INTEREST", null, this,  transactionRepo, accRepo );
        }
    }

    public void setAlias(AccountRepository accRepo) {
        String alias;
        do{
            alias = Utils.aliasGenerator();
            this.alias = alias;
        }while(accRepo.existsByAlias(alias));

    }

    public Transaction withdraw(double amount, String description, Account senderAcc, TransactionRepository transactionRepo, AccountRepository accRepo, double accBalance) throws InsufficientFundsException {
        if (accBalance < amount) {
            throw new InsufficientFundsException();
        }
        this.setBalance(this.getBalance()-amount);
        Transaction withdraw = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now(), null,this);
        senderAcc.addTransaction(withdraw);
        transactionRepo.save(withdraw);
        accRepo.save(this);
        return withdraw;
    }

    public Transaction deposit(double amount, String description,Client originClient, Account destinationAcc, TransactionRepository transactionRepo, AccountRepository accRepo){
        this.setBalance(this.getBalance()+amount);
        Transaction deposit = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now(),originClient, this);
        destinationAcc.addTransaction(deposit);
        transactionRepo.save(deposit);
        accRepo.save(this);
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

}

