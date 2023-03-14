package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.Account;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private Set<TransactionDTO> transactions;
    private long clientId;

    public AccountDTO(Account acc){
        this.setId(acc.getId());
        this.setNumber(acc.getNumber());
        this.setCreationDate(acc.getCreationDate());
        this.setBalance(acc.getBalance());
        this.setClientId(acc.getClient().getId());
        this.setTransactions(acc.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet()));
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
    public long getClientId() {
        return clientId;
    }

    public long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
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

    public void setTransactions(Set<TransactionDTO> transactions) {
        this.transactions = transactions;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
