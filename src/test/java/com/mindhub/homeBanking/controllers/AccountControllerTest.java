package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.ClientLoan;
import com.mindhub.homeBanking.repositories.*;
import com.mindhub.homeBanking.services.impl.*;
import org.assertj.core.api.ArraySortedAssert;
import static org.assertj.core.api.Assertions.*;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.persistence.Access;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Autowired
    WebTestClient webTestClient;
    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    AccountServiceImpl accountService;

    @BeforeEach
    public void setUp(){
        when(accountService.getAccountsDTO()).thenReturn((List<AccountDTO>) mock(AccountDTO.class));
    }
    @Test
    public void getAccount(){
     webTestClient.get()
             .uri("/api/accounts")
             .exchange()
             .expectBodyList(AccountDTO.class);
    }

    @Test
    void generateNewAccount() {
    }
}