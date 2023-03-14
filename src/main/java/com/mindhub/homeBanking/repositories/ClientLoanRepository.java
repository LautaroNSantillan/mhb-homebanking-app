package com.mindhub.homeBanking.repositories;

import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {
}
