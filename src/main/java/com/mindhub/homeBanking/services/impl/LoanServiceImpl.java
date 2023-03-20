package com.mindhub.homeBanking.services.impl;

import com.mindhub.homeBanking.models.Loan;
import com.mindhub.homeBanking.models.loans.LoanTypeInterface;
import com.mindhub.homeBanking.repositories.LoanRepository;
import com.mindhub.homeBanking.services.LoanService;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepo;
    public LoanServiceImpl(LoanRepository loanRepo) {
        this.loanRepo = loanRepo;
    }
    @Override
    public Loan findById(String id){
       return this.loanRepo.findById(id).orElse(null);
    }
    @Override
    public void saveLoan(Loan loan){
        this.loanRepo.save(loan);
    }
    @Override
    public void saveNewLoan(LoanTypeInterface loan){
        this.saveLoan(this.createNewLoan(loan));
    }

    private Loan createNewLoan(LoanTypeInterface loanInterface){
        return new Loan(loanInterface);
    }
}
