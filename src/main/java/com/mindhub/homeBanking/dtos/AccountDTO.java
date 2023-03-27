package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private Set<TransactionDTO> transactions;
    private long clientId;
    private String alias;

    public AccountDTO(Account acc){
        this.setId(acc.getId());
        this.setNumber(acc.getNumber());
        this.setCreationDate(acc.getCreationDate());
        this.setBalance(acc.getBalance());
        this.setClientId(acc.getClient().getId());
        this.setTransactions(acc.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet()));
        this.setAlias(acc.getAlias());
    }
}
