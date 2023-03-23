package com.mindhub.homeBanking.services.impl;

import static org.assertj.core.api.Assertions.*;

import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.AccountType;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.services.AccountService;
import com.mindhub.homeBanking.utilities.cronjobs.UpdateSavingsAccJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
//@SpringBootTest
//public class CronjobTest {
//    @Autowired
//    AccountServiceImpl accountService;
//    @Autowired
//    UpdateSavingsAccJob updateSavingsAccJob;
//
//    @BeforeEach
//    public void setUp() {
//    }
//    @Test
//    public void testUpdateAccountBalances() {
//
//        List<Account> accounts = accountService.findAll();
//        Account account = accounts.get(0);
//        double prevBalance = account.getBalance();
//
//        updateSavingsAccJob.updateAccountBalances();
//
//
//        double newBalance = accountService.findById(account.getId()).getBalance();
//
//
//        assertThat(prevBalance).isNotEqualTo(newBalance);
//
//    }
//}
