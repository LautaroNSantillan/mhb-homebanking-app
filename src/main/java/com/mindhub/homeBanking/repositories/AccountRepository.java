package com.mindhub.homeBanking.repositories;
import com.mindhub.homeBanking.models.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
