package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.CardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private String number;
    private byte cvv;
    private double amount;
    private String description;
    private CardType cardType;
}
