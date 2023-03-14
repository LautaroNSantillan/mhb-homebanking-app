package com.mindhub.homeBanking.repositories;

import com.mindhub.homeBanking.models.Loan;
import com.mindhub.homeBanking.models.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Loan findById(LoanType type);
}
