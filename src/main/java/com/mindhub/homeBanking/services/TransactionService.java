package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.models.Transaction;

public interface TransactionService {
    void save(Transaction transaction);
    Transaction createNewDebitTransaction(double amount, String description, Account acc);
    Transaction createNewCreditTransaction(double amount, String description, Account accountToBeDeposited, Client originClient);
}
