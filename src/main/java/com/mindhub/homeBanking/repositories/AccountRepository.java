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

@RepositoryRestResource
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByNumber(String number);
    boolean existsByNumber(String number);
    boolean existsByAlias(String alias);
    long count();
    @Query("SELECT a FROM Account a WHERE a.client = :client AND a.isDisabled = false")
    List<Account> findNonDisabledAccountsByClient(@Param("client") Client client);
}
