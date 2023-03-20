package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.models.Loan;
import com.mindhub.homeBanking.models.loans.LoanTypeInterface;

public interface LoanService {
    Loan findById(String id);
    void saveLoan(Loan loan);
    void saveNewLoan(LoanTypeInterface loan);
}
