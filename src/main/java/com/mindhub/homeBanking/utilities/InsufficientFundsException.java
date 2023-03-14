package com.mindhub.homeBanking.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class InsufficientFundsException extends Exception {
    private static final String MESSAGE = "Insufficient funds";
    public InsufficientFundsException() {
        super(MESSAGE);
    }
}
