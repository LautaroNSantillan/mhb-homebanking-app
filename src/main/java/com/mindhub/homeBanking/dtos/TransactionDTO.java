package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.Transaction;
import com.mindhub.homeBanking.models.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
public class TransactionDTO {
    private Long id;
    private TransactionType type;
    private double amount;
    private String description ;
    private LocalDateTime date;
    private String sender;
    private double remainingBalance;

    public TransactionDTO(Transaction transaction) {
        this.setAmount(transaction.getAmount());
        this.setDate(transaction.getDate());
        this.setDescription(transaction.getDescription());
        this.setType(transaction.getType());
        this.setId(transaction.getId());
        this.setSender(transaction);
        this.setRemainingBalance(transaction.getRemainingBalance());
    }

public void setSender(Transaction transaction){
        if (transaction.getSender()!=null){
            this.sender=transaction.getSender().getFirstName()+transaction.getSender().getLastName();
        }
        else{
            this.sender=null;
        }
}

}
