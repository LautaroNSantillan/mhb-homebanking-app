package com.mindhub.homeBanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
//    @GenericGenerator(name = "native", strategy = "native")
//    private long id;

    @Id
    @Enumerated(EnumType.STRING)
    private LoanType id;
    private String name;
    private int maxAmount;
    @ElementCollection
    @Column(name = "payments")
    private List<Byte> payments = new ArrayList<>();
    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan() {}

//    public Loan(LoanType id, String name, int maxAmount, List<Integer> payments) {
//        this.setName(name);
//        this.setMaxAmount(maxAmount);
//        this.setPayments(payments);
//    }

    public Loan(LoanType loanType) {
      this.setName(loanType.getName());
      this.setMaxAmount(loanType.getMaxAmount());
      this.setPayments(loanType.getPayments());
      this.setId(LoanType.valueOf(loanType.getId()));
   }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }

    public List<Client> getClient() {
        return clientLoans.stream().map(ClientLoan::getClient).collect(Collectors.toList());
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public String getName() {
        return name;
    }

//    public long getId() {
//        return id;
//    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public List<Byte> getPayments() {
        return payments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setPayments(List<Byte> payments) {
        this.payments = payments;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public LoanType getId() {
        return id;
    }

    public void setId(LoanType id) {
        this.id = id;
    }
}
