package com.mindhub.homeBanking.repositories;

import com.mindhub.homeBanking.models.Card;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.models.Loan;

import static org.assertj.core.api.Assertions.*;

import com.mindhub.homeBanking.models.Transaction;
import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.extractor.Extractors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class RepositoriesTest {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CardsRepository cardsRepository;

// loan
    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans)
                .as("Is empty ?").isNotEmpty();
    }

    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans)
                .extracting("name")
                .contains("Personal");
    }

    // transaction
    @Test
    public void allTransactionsHaveAccount() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions)
                .extracting(Transaction::getAccount)
                .doesNotContainNull();
    }

    @Test
    public void transactionsHaveDescription() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions)
                .extracting(Transaction::getDescription)
                .allSatisfy(description -> assertThat(description).isNotEmpty());
    }

    // account
    @Test
    public void adminDoesntHaveAcc() {
        List<Client> clients = clientRepository.findAll()
                .stream()
                .filter(client -> client.getEmail().endsWith("@admin.mindhub"))
                .collect(Collectors.toList());

        assertThat(clients)
                .as("Admin has no accounts")
                .allSatisfy(client -> assertThat(client.getAccounts()).isEmpty());
    }

    @Test
    public void clientHasAtLeastOneAccount() {
        List<Client> clients = clientRepository.findAll()
                .stream()
                .filter(client -> !client.getEmail().equals("admin@mindhub.com"))
                .collect(Collectors.toList());

        assertThat(clients)
                .as("At least one client has at least one account")
                .anySatisfy(client -> assertThat(client.getAccounts()).isNotEmpty());
    }

   // card
   @Test
   public void cardTypeIsValid() {
       List<Card> allCards = cardsRepository.findAll();
       for (Card card : allCards) {
           assertThat(card.getCardType())
                   .as("Is valid ?").isInstanceOf(Enum.class);
       }
   }
    @Test
    public void noneInvalidCardType() {
        List<Card> allCards = cardsRepository.findAll();
        assertThat(allCards).filteredOn(card -> !(card.getCardType() instanceof Enum)).isEmpty();
    }

    @Test
    public void lengthCVVis3() {
        List<Card> allCards = cardsRepository.findAll();
        for (Card card : allCards) {
            assertThat(String.valueOf(card.getCvv()))
                    .hasSize(3);
        }
    }
    @Test
    public void lengthCVV() {
        List<Card> allCards = cardsRepository.findAll();
        assertThat(allCards).extracting(Card::getCvv)
                .allMatch(cvv -> cvv.matches("^\\d{3}$"));
    }
}
