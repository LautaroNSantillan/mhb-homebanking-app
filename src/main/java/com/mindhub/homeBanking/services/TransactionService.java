package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.models.Transaction;

import java.util.List;

public interface TransactionService {
    void save(Transaction transaction);
    Transaction createNewDebitTransaction(double amount, String description, Account acc);
    Transaction createNewCreditTransaction(double amount, String description, Account accountToBeDeposited, Client originClient);
    List<Transaction> findByAccount(Account acc);
    void deleteAll(List<Transaction> transactions);
}
