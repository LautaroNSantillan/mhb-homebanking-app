package com.mindhub.homeBanking.repositories.tests;

import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.Loan;
import com.mindhub.homeBanking.repositories.AccountRepository;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

@SpringBootTest
class AccountRepositoryTest {
    @Autowired
    AccountRepository accRepo;

    @Test
    public void existAccounts(){
        List<Account> loans = accRepo.findAll();
        assertThat(loans)
                .as("It's empty").isNotEmpty();
    }

    @Test
    void existsByAlias() {
        Account acc = accRepo.findByNumber("VIN-001");
        assertThat(acc)
                .as("No account found").isNotNull();
    }

}