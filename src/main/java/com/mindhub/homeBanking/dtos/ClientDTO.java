package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.Client;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;
@NoArgsConstructor
public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String userEmail;
    private Set<AccountDTO> accounts;
    private Set<ClientLoanDTO> loans;
    private Set<CardDTO> cards;

    public ClientDTO(Client client) {

        this.setId(client.getId());

        this.setFirstName(client.getFirstName());

        this.setLastName(client.getLastName());

        this.setUserEmail(client.getEmail());

        this.setAccounts(client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet()));

        this.setLoans(client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toSet()));

        this.setCards(client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet()));
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setAccounts(Set<AccountDTO> accounts) {
        this.accounts = accounts;
    }

    public void setLoans(Set<ClientLoanDTO> loans) {
        this.loans = loans;
    }

    public void setCards(Set<CardDTO> cards) {
        this.cards = cards;
    }
}

