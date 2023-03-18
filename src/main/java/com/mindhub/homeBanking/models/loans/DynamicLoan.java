package com.mindhub.homeBanking.models.loans;

import com.mindhub.homeBanking.utilities.LoanValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter(onMethod = @__(@Override))
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DynamicLoan implements LoanTypeInterface {
    private String id;
    private String name;
    private int maxAmount;
    private double interestRate;
    private List<Byte> payments = new ArrayList<>();
    private boolean isPredefinedLoan = false;
    @Override
    public boolean isPredefinedLoan() {
        return this.isPredefinedLoan;
    }
    @Override
    public void validate (String type) throws LoanValidationException {
        if(!this.id.equals(type)){
            throw new LoanValidationException();
        }
    }

}
