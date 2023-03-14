package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.ClientLoan;
import com.mindhub.homeBanking.models.LoanType;

public class ClientLoanDTO {

    private long id;
    private LoanType loanId;
    private String loanType;

    private double amount;
    private int payments;
    public ClientLoanDTO(){}
    public ClientLoanDTO(ClientLoan clientLoan){
        this.setId(clientLoan.getId());
        this.setLoanId(clientLoan.getLoan().getId());
        this.setLoanType(clientLoan.getLoan().getName());
        this.setAmount(clientLoan.getAmount());
        this.setPayments(clientLoan.getPayments());
    }

    public long getId() {
        return id;
    }

    public LoanType getLoanId() {
        return loanId;
    }

    public String getLoanType() {
        return loanType;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLoanId(LoanType loanId) {
        this.loanId = loanId;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
}
