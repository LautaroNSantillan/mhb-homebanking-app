package com.mindhub.homeBanking.repositories;
import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.accounts a LEFT JOIN FETCH a.transactions WHERE c.email = :clientEmail AND (a.isDisabled IS NULL OR a.isDisabled = false)")
    Client findClientByEmailExcludingDisabledAccounts(@Param("clientEmail") String clientEmail);

}

