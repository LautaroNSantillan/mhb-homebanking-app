package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.ClientLoan;
import com.mindhub.homeBanking.models.LoanType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientLoanDTO {

    private long id;
    private String loanId;
    private String loanType;
    private double amount;
    private int payments;

    public ClientLoanDTO(ClientLoan clientLoan){
        this.setId(clientLoan.getId());
        this.setLoanId(clientLoan.getLoan().getId());
        this.setLoanType(clientLoan.getLoan().getName());
        this.setAmount(clientLoan.getAmount());
        this.setPayments(clientLoan.getPayments());
    }

}
