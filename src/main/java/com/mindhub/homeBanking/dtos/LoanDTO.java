package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.ClientLoan;
import com.mindhub.homeBanking.models.Loan;
import com.mindhub.homeBanking.models.LoanType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class LoanDTO {
    private LoanType id;
    private String name;
    private int maxAmount;
    private List<Byte> payments = new ArrayList<>();

    public LoanDTO(Loan loan){
        this.id=loan.getId();
        this.name= loan.getName();
        this.maxAmount= loan.getMaxAmount();
        this.payments=loan.getPayments();
    }
}
