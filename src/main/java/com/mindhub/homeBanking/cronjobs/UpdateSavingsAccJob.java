package com.mindhub.homeBanking.cronjobs;

import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.repositories.AccountRepository;
import com.mindhub.homeBanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateSavingsAccJob {
    @Autowired
    AccountRepository accRepo;
    @Autowired
    TransactionRepository transactionRepo;
    @Scheduled(cron = "0 0 12 * * *", zone = "America/Argentina/Buenos_Aires")
    public void updateAccountBalances() {
        List<Account> accounts = accRepo.findAll();
        for (Account account : accounts) {
            account.depositInterest(accRepo, transactionRepo);
        }
    }
}
