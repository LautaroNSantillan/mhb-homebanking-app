package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.Transaction;
import com.mindhub.homeBanking.models.TransactionType;

import java.time.LocalDateTime;
import java.util.Set;

public class TransactionDTO {
    private Long id;
    private TransactionType type;
    private double amount;
    private String description ;
    private LocalDateTime date;

    public TransactionDTO(Transaction transaction) {
        this.setAmount(transaction.getAmount());
        this.setDate(transaction.getDate());
        this.setDescription(transaction.getDescription());
        this.setType(transaction.getType());
        this.setId(transaction.getId());
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
