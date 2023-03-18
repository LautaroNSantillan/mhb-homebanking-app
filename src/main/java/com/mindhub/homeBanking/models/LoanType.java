package com.mindhub.homeBanking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum LoanType {
    MORTGAGE("MORTGAGE", "Mortgage", 500000,20,  new ArrayList<>(Arrays.asList((byte)12,(byte)24,(byte)36,(byte)48,(byte)60))),
    AUTO("PERSONAL","Personal",100000, 35,new ArrayList<>(Arrays.asList((byte)6,(byte)12,(byte)24))),
    PERSONAL("AUTO","Automobile",300000, 40,new ArrayList<>(Arrays.asList((byte)6,(byte)12,(byte)24,(byte)36))),
    LOAN_TEMPLATE("", "", 0, 0, new ArrayList<>());
    private String id;
    private String name;
    private int maxAmount;
    private double interestRate;
    private List<Byte> payments = new ArrayList<>();
}
