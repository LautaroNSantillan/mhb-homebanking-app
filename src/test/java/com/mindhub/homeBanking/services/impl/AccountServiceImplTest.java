package com.mindhub.homeBanking.services.impl;

import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.AccountType;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.repositories.AccountRepository;
import com.mindhub.homeBanking.services.AccountService;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    PasswordEncoder pwdEncoder;
    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    AccountServiceImpl accountService;
    private Account mockAccount;
    private Client mockClient;
    private Account account;

    @BeforeEach
     void setUp(){
        account =  mock(Account.class);
        mockClient = mock(Client.class);
        mockAccount = new Account(LocalDateTime.now(), 5000, accountService,mockClient,AccountType.SAVINGS);
    }

    @Test
    void testGetAccountDTO() {
        // Set up mock behavior for account repository
        when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));

        // Set up expected account DTO
        AccountDTO expectedDTO = new AccountDTO(mockAccount);

        // Call the getAccountDTO() method with the mock account ID
        AccountDTO accountDTO = accountService.getAccountDTO(1L);

        // Verify that the AccountRepository was called with the correct ID
        verify(accountRepository).findById(1L);

        // Verify that the AccountDTO object has the correct values
        assertThat(accountDTO.getId()).isEqualTo(expectedDTO.getId());
        assertThat(accountDTO.getClientId()).isEqualTo(expectedDTO.getClientId());
        assertThat(accountDTO.getBalance()).isEqualTo(expectedDTO.getBalance());
    }

    @Test
    void findAll() {
        when(accountService.findAll()).thenReturn(Arrays.asList(account));
        assertNotNull(accountService.findAll());
    }

    @Test
    void findByIdOptional(){
        when(accountService.findByIdOptional(anyLong())).thenReturn(Optional.of(account));

        assertThat(accountService.findByIdOptional(anyLong()))
                .isNotEmpty();
    }
    @Test
    void findByIdOptionalEmpty(){
        when(accountService.findByIdOptional(anyLong())).thenReturn(Optional.of(account));

        Optional<Account> acc = accountService.findByIdOptional(1L);

        assertThat(acc)
                .isNotEmpty();
    }
}