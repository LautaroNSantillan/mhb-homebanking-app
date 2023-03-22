package com.mindhub.homeBanking.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.AccountType;
import com.mindhub.homeBanking.models.Client;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.persistence.Access;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EnableAutoConfiguration(exclude = {
//        DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class
//})
@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    PasswordEncoder pwdEncoder;
    @MockBean
    AccountServiceImpl accountService;

    @BeforeEach
    public void setUp()  {
        Client melba = new Client("Melba", "Morel", "melba@mindhub.com",pwdEncoder.encode("melba") );
        Account VIN001 = new Account (LocalDateTime.now(), 5000, accountService,melba, AccountType.SAVINGS);
        AccountDTO accDTO = new AccountDTO(VIN001);
        Account VIN002 = new Account (LocalDateTime.now(), 5000, accountService,melba, AccountType.SAVINGS);
        AccountDTO accDTO2 = new AccountDTO(VIN002);

        List<AccountDTO> accounts = Arrays.asList(accDTO,accDTO2);

//        Client client = mock(Client.class);
//        Account acc1 = mock(Account.class);
//        acc1.setClient(client);
//        Account acc2 = mock(Account.class);
//        acc2.setClient(client);
//        AccountDTO acc1DTO = new AccountDTO(acc1);
//        AccountDTO acc2DTO = new AccountDTO(acc2);
        AccountDTO acc1DTO = mock(AccountDTO.class);
        AccountDTO acc2DTO = mock(AccountDTO.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);


      //  List<AccountDTO> accounts = Arrays.asList(acc1DTO,acc2DTO);

       when(accountService.getAccountsDTO()).thenReturn(accounts);
    }
    @Test
    public void getAccount(){
     webTestClient.get()
             .uri("/api/accounts")
             .exchange()
             .expectStatus().isOk()
             .expectBodyList(AccountDTO.class)
             .consumeWith(response -> {
                 List<AccountDTO> accountDTOList = response.getResponseBody();
                 assertNotNull(accountDTOList);
                 assertFalse(accountDTOList.isEmpty());
             });
    }

    @Test
    void generateNewAccount() {
    }
}

//@ExtendWith(MockitoExtension.class)
//class AccountControllerTest {
//    @Autowired
//    WebTestClient webTestClient;
//    @Mock
//    AccountRepository accountRepository;
//
//    @InjectMocks
//    AccountServiceImpl accountService;
//
//    @BeforeEach
//    public void setUp(){
//        Account acc1 = mock(Account.class);
//        Account acc2 = mock(Account.class);
//        List<Account> accounts = Arrays.asList(acc1,acc2);
//
//        when(accountRepository.findAll()).thenReturn(accounts);
//
//    }
//    @Test
//    public void getAccount(){
//        webTestClient.get()
//                .uri("/api/accounts")
//                .exchange()
//                .expectStatus().isOk();
//        // .expectBodyList(AccountDTO.class);
//    }
//
//    @Test
//    void testGetAccounts() {
//        AccountDTO acc1 = mock(AccountDTO.class);
//        AccountDTO acc2 = mock(AccountDTO.class);
//        // Create a list of AccountDTO objects to be returned by the mock AccountService
//        List<AccountDTO> accounts = Arrays.asList(acc1,acc2);
//
//        // Configure the mock AccountService to return the list of accounts when getAccountsDTO() is called
//        when(accountService.getAccountsDTO()).thenReturn(accounts);
//
//        // Perform an HTTP GET request to /api/accounts and assert that the response body contains the expected list of accounts
//        webTestClient.get().uri("/api/accounts").exchange()
//                .expectStatus().isOk()
//                .expectBodyList(AccountDTO.class).isEqualTo(accounts);
//    }
//    @Test
//    void generateNewAccount() {
//    }
//}