package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.models.Card;
import com.mindhub.homeBanking.models.CardColor;
import com.mindhub.homeBanking.models.CardType;
import com.mindhub.homeBanking.models.Client;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface CardService {
    Card findById(Long id);
   Optional<Card>findByIdOptional(Long id);
    Card findByCardDigits(String digits);
    Card createCard(Client currentClient, CardType enumType, CardColor enumColor);
    boolean existsByCardDigits(String digits);
    void save(Card card);
    ResponseEntity<Object> saveNewCard(int acc, Card card);
    void delete(Card card);

}
