package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;
@Getter
@Setter
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

}

