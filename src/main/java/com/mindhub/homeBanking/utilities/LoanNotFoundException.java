package com.mindhub.homeBanking.utilities;

public class LoanNotFoundException extends Exception {
    private static final String MESSAGE = "Loan not found";
    public LoanNotFoundException() {
        super(MESSAGE);
    }
}

