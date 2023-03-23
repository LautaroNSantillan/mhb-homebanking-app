package com.mindhub.homeBanking.utilities.cronjobs;

import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.services.impl.AccountServiceImpl;
import com.mindhub.homeBanking.services.impl.TransactionServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateSavingsAccJob {
    private final AccountServiceImpl accountService;
    private final TransactionServiceImpl transactionService;

    public UpdateSavingsAccJob(AccountServiceImpl accountService, TransactionServiceImpl transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Scheduled(cron = "0 0 12 * * *", zone = "America/Argentina/Buenos_Aires")
    public void updateAccountBalances() {
        List<Account> accounts = accountService.findAll();
        for (Account account : accounts) {
            account.depositInterest(accountService, transactionService);
        }
    }
}
