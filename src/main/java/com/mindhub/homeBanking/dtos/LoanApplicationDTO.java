package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.LoanType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationDTO {
    private String loanId;
    private double amount;
    private byte payments;
    private String destinationAccNumber;

}
