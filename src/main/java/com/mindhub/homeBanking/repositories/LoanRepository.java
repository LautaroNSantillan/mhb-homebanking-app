package com.mindhub.homeBanking.repositories;

import com.mindhub.homeBanking.models.Loan;
import com.mindhub.homeBanking.models.LoanType;
import com.mindhub.homeBanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findById(String id);
}
