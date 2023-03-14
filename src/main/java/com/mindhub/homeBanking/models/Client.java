package com.mindhub.homeBanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Entity
public class Client {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
//    @GenericGenerator(name = "native", strategy = "native")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "client_id_seq", allocationSize = 1)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "cardHolder", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client() {
    }

    public Client(String fName, String lName, String email, String password) {
        this.setFirstName(fName);
        this.setLastName(lName);
        this.setEmail(email);
        this.setPassword(password);
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public List<Loan> getLoans() {
        return clientLoans.stream().map(ClientLoan::getLoan).collect(Collectors.toList());
    }

    public void addAccount(Account acc) {
        accounts.add(acc);
        acc.setClient(this);
    }

    public Boolean matchEmailPattern(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String stringName(){
        return  this.getFirstName() +" "+ this.getLastName();
    }

    public String getPassword() {
        return password;
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

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public Set<Account> getAccounts() {
        return accounts;
    }
    @JsonIgnore
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
    public void setEmail(String email) {
        if (matchEmailPattern(email)) this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}