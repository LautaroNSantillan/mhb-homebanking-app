package com.mindhub.homeBanking.models.loans;

import com.mindhub.homeBanking.utilities.LoanValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter(onMethod = @__(@Override))
@AllArgsConstructor
@NoArgsConstructor
public enum PredefinedLoan implements LoanTypeInterface {
    MORTGAGE("MORTGAGE", "Mortgage", 500000,20.00,  new ArrayList<>(Arrays.asList((byte)12,(byte)24,(byte)36,(byte)48,(byte)60)),true),
    AUTO("PERSONAL","Personal",100000, 35.00,new ArrayList<>(Arrays.asList((byte)6,(byte)12,(byte)24)),true),
    PERSONAL("AUTO","Automobile",300000, 40.00,new ArrayList<>(Arrays.asList((byte)6,(byte)12,(byte)24,(byte)36)), true);
    private String id;
    private String name;
    private int maxAmount;
    private double interestRate;
    private List<Byte> payments = new ArrayList<>();
    private boolean isPredefinedLoan;
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