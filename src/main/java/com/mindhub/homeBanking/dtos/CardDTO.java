package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.Card;
import com.mindhub.homeBanking.models.CardColor;
import com.mindhub.homeBanking.models.CardType;
import com.mindhub.homeBanking.models.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class CardDTO {
    private long id;
    private CardType cardType;
    private CardColor cardColor;
    private String cardDigits;
    private String cardHolder;
    private String cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;

    public CardDTO(Card card) {
        this.cardHolder=card.getCardHolder().stringName();
        this.id=card.getId();
        this.cardType=card.getCardType();
        this.cardColor=card.getCardColor();
        this.cardDigits=card.getCardDigits();
        this.cvv=card.getCvv();
        this.fromDate=card.getFromDate();
        this.thruDate=card.getThruDate();
    }


}
