package com.mindhub.homeBanking.services.impl;

import com.mindhub.homeBanking.dtos.CardDTO;
import com.mindhub.homeBanking.models.Card;
import com.mindhub.homeBanking.models.CardColor;
import com.mindhub.homeBanking.models.CardType;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.repositories.CardsRepository;
import com.mindhub.homeBanking.services.CardService;
import com.mindhub.homeBanking.utilities.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {
private final CardsRepository cardRepo;

    public CardServiceImpl(CardsRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    @Override
    public Card findById(Long id){
      return  this.cardRepo.findById(id).orElse(null);
    }
    @Override
    public Optional<Card> findByIdOptional(Long id){
        return  this.cardRepo.findById(id);
    }
    @Override
    public Card findByCardDigits(String digits){
        return this.cardRepo.findByCardDigits(digits);
    }
    @Override
    public boolean existsByCardDigits(String digits){
        return this.cardRepo.existsByCardDigits(digits);
    }
    @Override
    public void save(Card card){
        this.cardRepo.save(card);
    }
    @Override
    public ResponseEntity<Object> saveNewCard(int acc,Card card){
        if (acc < 3){
            this.save(card);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>("Max owned cards reached", HttpStatus.FORBIDDEN);
        }
    }
    @Override
    public Card createCard( Client currentClient, CardType enumType, CardColor enumColor){
            String digits = Utils.generateCardsDigits();
            while (cardRepo.existsByCardDigits(digits)) {
                digits = Utils.generateCardsDigits();
            }
        return new Card(currentClient, enumType, enumColor, LocalDate.now(), LocalDate.now().plusYears(5), digits);

    }
    @Override
    public void delete(Card card){
        this.cardRepo.delete(card);
    }
}
