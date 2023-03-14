package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.CardDTO;
import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.repositories.CardsRepository;
import com.mindhub.homeBanking.repositories.ClientRepository;
import com.mindhub.homeBanking.utilities.ErrorResponse;
import com.mindhub.homeBanking.utilities.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private CardsRepository cardRepo;

    @PostMapping("clients/current/cards")
    public ResponseEntity<Object> createNewCard(@RequestParam String type, @RequestParam String color, Authentication auth) {
        Client currentClient = clientRepo.findByEmail(auth.getName());
        try {
            CardType enumType = CardType.valueOf(type);
            CardColor enumColor = CardColor.valueOf(color);
            int numCreditCards = 0;
            int numDebitCards = 0;
            for (Card card : currentClient.getCards()) {
                if (card.getCardType() == CardType.CREDIT) {
                    numCreditCards++;
                } else if (card.getCardType() == CardType.DEBIT) {
                    numDebitCards++;
                }
            }
            Set<Card> creditCards = currentClient.getCards().stream()
                    .filter(card -> card.getCardType() == CardType.CREDIT)
                    .collect(Collectors.toSet());

            Set<Card> debitCards = currentClient.getCards().stream()
                    .filter(card -> card.getCardType() == CardType.DEBIT)
                    .collect(Collectors.toSet());

            boolean hasSameColorCredit = creditCards.stream().anyMatch(card -> card.getCardColor() == enumColor && card.getCardType()==enumType);
            boolean hasSameColorDebit = debitCards.stream().anyMatch(card -> card.getCardColor() == enumColor && card.getCardType()==enumType);

                switch (enumType) {
                    case CREDIT:
                        if(!hasSameColorCredit && numCreditCards<3){
                            return createCard(numCreditCards, currentClient, enumType, enumColor,cardRepo);
                        }break;

                    case DEBIT:
                        if (!hasSameColorDebit && numDebitCards<3){
                            return createCard(numDebitCards, currentClient, enumType, enumColor,cardRepo);
                        }break;

                    default:
                       return new ResponseEntity<>("Invalid SWITCH ", HttpStatus.BAD_REQUEST);// ME LO PIDE HERMANO QUE QUERES QUE HAGA
                }
            return new ResponseEntity<>("You already own the same card or Max number of cards reached", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException iae) {
            // Handle invalid enum values
            ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),"Invalid or empty card type or color", "Valid card types: CREDIT || DEBIT ; valid card colors: SILVER || GOLD || TITANIUM ");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("clients/current/renew-card")
    public ResponseEntity<Object> renewCard(@RequestParam long cardId, Authentication auth){
        Client currentClient = clientRepo.findByEmail(auth.getName());
        boolean hasCard = currentClient.getCards()
                .stream()
                .anyMatch(card -> card.getId() == cardId);
        Card expiringCard = cardRepo.findById( cardId).orElse(null);
        LocalDateTime thresholdDateTime = LocalDateTime.now().plusDays(7);
        if(expiringCard !=null && hasCard && ChronoUnit.DAYS.between(expiringCard.getThruDate(), thresholdDateTime) > 0){
            expiringCard.setFromDate(LocalDate.now());
            expiringCard.setThruDate(LocalDate.now().plusYears(5));
            cardRepo.save(expiringCard);
            return new ResponseEntity<>(new CardDTO(expiringCard), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Card not eligible for renewing", HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<Object> createCard(int acc, Client currentClient, CardType enumType, CardColor enumColor, CardsRepository cardRepo){
        if (acc < 3) {
            String digits = Utils.generateCardsDigits();
            while (cardRepo.existsByCardDigits(digits)) {
                digits = Utils.generateCardsDigits();
            }
            Card newCard = new Card(currentClient, enumType, enumColor, LocalDate.now(), LocalDate.now().plusYears(5), digits);
            cardRepo.save(newCard);
            return new ResponseEntity<>(new CardDTO(newCard), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Max owned cards reached", HttpStatus.FORBIDDEN);
        }
    }

}

