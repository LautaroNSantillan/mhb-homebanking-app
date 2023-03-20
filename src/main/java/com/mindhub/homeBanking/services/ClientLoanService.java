package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.models.ClientLoan;
import com.mindhub.homeBanking.models.Loan;

public interface ClientLoanService {
        void saveClientLoan(ClientLoan clientLoan);
        void saveNewClientLoan(double amount, int payments, Client client, Loan loan);
}
