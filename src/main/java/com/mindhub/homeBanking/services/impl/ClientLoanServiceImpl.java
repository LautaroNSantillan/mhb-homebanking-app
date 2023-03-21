package com.mindhub.homeBanking.services.impl;

import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.models.ClientLoan;
import com.mindhub.homeBanking.models.Loan;
import com.mindhub.homeBanking.repositories.ClientLoanRepository;
import com.mindhub.homeBanking.services.ClientLoanService;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImpl implements ClientLoanService {
    private final ClientLoanRepository clientLoanRepo;

    public ClientLoanServiceImpl(ClientLoanRepository clientLoanRepo) {
        this.clientLoanRepo = clientLoanRepo;
    }
    @Override
    public void saveClientLoan(ClientLoan clientLoan){
        this.clientLoanRepo.save(clientLoan);
    }
    @Override
    public void saveNewClientLoan(double amount, int payments, Client client, Loan loan){
        this.clientLoanRepo.save(this.createClientLoan(amount, payments, client, loan));
    }
    private ClientLoan createClientLoan(double amount, int payments, Client client, Loan loan){
        return new ClientLoan(amount, payments, client, loan);
    }
}
