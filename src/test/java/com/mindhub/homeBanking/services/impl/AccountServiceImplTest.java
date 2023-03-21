package com.mindhub.homeBanking.services.impl;

import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.models.Account;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
//@ExtendWith(MockitoExtension.class)
//class AccountServiceImplTest {
//    @Mock
//    AccountRepository accountRepository;
//    @InjectMocks
//    AccountServiceImpl accountService;
//
//    private Account account;
//
//    @BeforeEach
//     void setUp(){
//        account =  mock(Account.class);
//    }
//
//    @Test
//    void getAccountsDTO() {
//// create a mock Account object
//        when(account.getId()).thenReturn(1L);
//        when(account.getBalance()).thenReturn(2000.d);
//        when(account.getNumber()).thenReturn("VIN-001");
//
//        Client mockClient = mock(Client.class);
//        when(mockClient.getId()).thenReturn(1L);
//
//        account.setClient(mockClient);
//
//        // create a mock AccountRepository that returns the mock Account object
//        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
//
//
//
//        // call the getAccountDTO() method with the mock Account ID
//        when(accountService.getAccountDTO(1L)).thenReturn(mock(AccountDTO.class));
//        AccountDTO accountDTO = accountService.getAccountDTO(1L);
//
//        // assert that the AccountDTO object has the correct values
//        assertNotNull(account);
//        assertEquals(1L, accountDTO.getId());
//        assertEquals("VIN-001", accountDTO.getNumber());
//        assertEquals(2000.d, accountDTO.getBalance());
//
//        // verify that the AccountRepository was called with the correct ID
//        verify(accountRepository).findById(1L);
//    }
//
//    @Test
//    void findAll() {
//        when(accountService.findAll()).thenReturn(Arrays.asList(account));
//        assertNotNull(accountService.findAll());
//    }
//
//    @Test
//    void save() {
//        doNothing().when(accountRepository).save(any(Account.class));
//
//        accountService.save(account);
//
//        verify(accountRepository).save(any(Account.class));
//    }
//
//    @Test
//    void findByIdOptional(){
//        when(accountService.findByIdOptional(anyLong())).thenReturn(Optional.of(account));
//
//        assertThat(accountService.findByIdOptional(anyLong()))
//                .isNotEmpty();
//    }
//    @Test
//    void findByIdOptionalEmpty(){
//        when(accountService.findByIdOptional(anyLong())).thenReturn(Optional.empty());
//
//        assertThat(accountService.findByIdOptional(anyLong()))
//                .isNotEmpty();
//    }
//}