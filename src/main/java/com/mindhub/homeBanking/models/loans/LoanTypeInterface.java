package com.mindhub.homeBanking.models.loans;

import com.mindhub.homeBanking.utilities.LoanValidationException;

import java.util.List;
public interface LoanTypeInterface {
        String getName();
        String getId();
        int getMaxAmount();
        double getInterestRate();
        List<Byte> getPayments();
        void validate(String type) throws LoanValidationException;
        boolean isPredefinedLoan();
    }

