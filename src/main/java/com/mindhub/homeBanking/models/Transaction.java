package com.mindhub.homeBanking.models;

import com.mindhub.homeBanking.repositories.TransactionRepository;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id")
    @SequenceGenerator(name = "transaction_id", sequenceName = "transaction_id_seq", allocationSize = 1)
    private Long id;
    private TransactionType type;
    private double amount;
    private String description ;
    private LocalDateTime date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="sender_id", nullable = true)
    private Client sender;

    public Transaction(){}
    public Transaction(TransactionType type, double amount, String description, LocalDateTime date, Client sender) {
        this.setType(type);
        this.setAmount(amount);
        this.setDescription(description);
        this.setDate(date);
        this.setSender(sender);
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

    public Account getAccount() {
        return account;
    }
    public Client getSender() {
        return sender;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        if (this.getType().getTypeBool()){
            this.amount = amount;
        }else{
            this.amount = -amount;
        }
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    public void setSender(Client sender) {
        this.sender = sender;
    }
}
