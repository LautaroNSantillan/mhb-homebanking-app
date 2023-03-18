package com.mindhub.homeBanking.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id")
    @SequenceGenerator(name = "transaction_id", sequenceName = "transaction_id_seq", allocationSize = 1)
    private Long id;
    private TransactionType type;
    private double amount;
    private double remainingBalance;
    private String description ;
    private LocalDateTime date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="sender_id", nullable = true)
    private Client sender;
    public Transaction(TransactionType type, double amount, String description, LocalDateTime date, Client sender, Account acc) {
        this.setType(type);
        this.setAmount(amount);
        this.setDescription(description);
        this.setDate(date);
        this.setSender(sender);
        this.setAccount(acc);
        this.setRemainingBalance(this.account);
    }
    public void setAmount(double amount) {
        if (this.getType().getTypeBool()){
            this.amount = amount;
        }else{
            this.amount = -amount;
        }
    }
    public void setRemainingBalance(Account acc){
        this.remainingBalance =acc.getBalance()+this.amount;
    }

}
