package com.mindhub.homeBanking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AccountType {
    CHECKING(0),
    SAVINGS(0.1);
    private double annualPercentageYield;
}
