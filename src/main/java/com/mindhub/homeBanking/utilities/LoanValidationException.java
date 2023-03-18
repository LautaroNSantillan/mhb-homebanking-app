package com.mindhub.homeBanking.utilities;



public class LoanValidationException extends Exception {
    private static final String MESSAGE = "Invalid loan type";
    public LoanValidationException() {
        super(MESSAGE);
    }
}
