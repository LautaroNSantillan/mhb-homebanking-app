package com.mindhub.homeBanking.services.impl;

import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.repositories.TransactionRepository;
import com.mindhub.homeBanking.services.TransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepo;
    public TransactionServiceImpl(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    @Override
    public void save(Transaction transaction){
        this.transactionRepo.save(transaction);
    }
    @Override
    public Transaction createNewDebitTransaction(double amount, String description, Account acc){
        return new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now(), null,acc);
    }
    @Override
    public List<Transaction> findByAccount(Account acc){
    return this.transactionRepo.findByAccount(acc);
    }
    @Override
    public void deleteAll(List<Transaction> transactions){
        this.transactionRepo.deleteAll(transactions);
    }
    @Override
    public Transaction createNewCreditTransaction(double amount, String description, Account accountToBeDeposited, Client originClient){
        return new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now(),originClient, accountToBeDeposited);
    }
}
