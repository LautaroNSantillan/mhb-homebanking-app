package com.mindhub.homeBanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_id")
    @SequenceGenerator(name = "card_id", sequenceName = "card_id_seq", allocationSize = 1)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String cardDigits;
    private String cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;

    public Card() {
    }

    public Card(Client cardHolder, CardType cardType, CardColor cardColor, LocalDate from, LocalDate thru,String digits) {
        this.addCard(cardHolder);
        this.setCardType(cardType);
        this.setCardColor(cardColor);
        this.setCardDigits(digits);
        this.setGeneratedCvv();
        this.setFromDate(from);
        this.setThruDate(thru);
    }

    public boolean isExpired() {
        LocalDate now = LocalDate.now();
        return now.isAfter(thruDate) || now.isEqual(thruDate);
    }

    public void addCard(Client cardHolder) {
        this.setCardHolder(cardHolder);
        cardHolder.getCards().add(this);
    }

    public void setGeneratedCvv() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000);
        String randomCvv = String.format("%03d", randomNumber);
        this.setCvv(randomCvv);
    }

    public String generateCardsDigits() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(String.format("%04d", random.nextInt(10000)));
            if (i < 3) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public long getId() {
        return id;
    }

    public Client getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(Client cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public String getCardDigits() {
        return cardDigits;
    }

    public void setCardDigits(String cardDigits) {
        this.cardDigits = cardDigits;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public void setId(long id) {
        this.id = id;
    }
}
