package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.CardDTO;
import com.mindhub.homeBanking.dtos.PaymentDTO;
import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.repositories.TransactionRepository;
import com.mindhub.homeBanking.services.AccountService;
import com.mindhub.homeBanking.services.CardService;
import com.mindhub.homeBanking.services.ClientService;
import com.mindhub.homeBanking.services.TransactionService;
import com.mindhub.homeBanking.services.impl.AccountServiceImpl;
import com.mindhub.homeBanking.services.impl.CardServiceImpl;
import com.mindhub.homeBanking.services.impl.ClientServiceImpl;
import com.mindhub.homeBanking.services.impl.TransactionServiceImpl;
import com.mindhub.homeBanking.utilities.ErrorResponse;
import com.mindhub.homeBanking.utilities.InsufficientFundsException;
import com.mindhub.homeBanking.utilities.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;
    private final AccountService accountService;
    private final ClientService clientService;
    private final TransactionService transactionService;


    public CardController(CardService cardService, AccountService accountService, ClientService clientService, TransactionService transactionService) {
        this.cardService = cardService;
        this.accountService = accountService;
        this.clientService = clientService;
        this.transactionService = transactionService;
    }
    @Transactional
    @PostMapping("clients/current/cards")
    public ResponseEntity<Object> createNewCard(@RequestParam String type, @RequestParam String color, Authentication auth) {
        Client currentClient = clientService.findByEmail(auth.getName());
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
                            return cardService.saveNewCard(numCreditCards,cardService.createCard( currentClient, enumType, enumColor));
                        }break;

                    case DEBIT:
                        if (!hasSameColorDebit && numDebitCards<3){
                            return cardService.saveNewCard(numDebitCards,cardService.createCard( currentClient, enumType, enumColor));
                        }break;

                    default:
                       return new ResponseEntity<>("Invalid SWITCH ", HttpStatus.BAD_REQUEST);
                }
            return new ResponseEntity<>("You already own the same card or Max number of cards reached", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException iae) {
            // Handle invalid enum values
            ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),"Invalid or empty card type or color", "Valid card types: CREDIT || DEBIT ; valid card colors: SILVER || GOLD || TITANIUM ");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }
    }
    @Transactional
    @PostMapping("clients/current/renew-card")
    public ResponseEntity<Object> renewCard(@RequestParam long cardId, Authentication auth){
        Client currentClient = clientService.findByEmail(auth.getName());
        boolean hasCard = currentClient.getCards()
                .stream()
                .anyMatch(card -> card.getId() == cardId);
        Card expiringCard = cardService.findById( cardId);
        LocalDateTime thresholdDateTime = LocalDateTime.now().plusDays(7);
        if(expiringCard !=null && hasCard && ChronoUnit.DAYS.between(expiringCard.getThruDate(), thresholdDateTime) > 0){
            expiringCard.setFromDate(LocalDate.now());
            expiringCard.setThruDate(LocalDate.now().plusYears(5));
            cardService.save(expiringCard);
            return new ResponseEntity<>(new CardDTO(expiringCard), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Card not eligible for renewing", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("clients/current/delete-card")
    public ResponseEntity<Object> deleteCard(@RequestParam long cardId, Authentication auth) {
        Client currentClient = clientService.findByEmail(auth.getName());
        Optional<Card> optionalCard = cardService.findByIdOptional(cardId);
        if (optionalCard.isPresent()) {
            Card cardToDelete = optionalCard.get();
            if (cardToDelete.getCardHolder().equals(currentClient)) {
                cardService.delete(cardToDelete);
                return new ResponseEntity<>("Card deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("This card does not belong to the current client", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Card not found", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @PostMapping("clients/current/pay-with-card")
    public ResponseEntity<Object> payWithCard(@RequestBody PaymentDTO payment, Authentication auth) throws InsufficientFundsException {
        Client currentClient = clientService.findByEmail(auth.getName());

        if (!EnumSet.of(CardType.CREDIT, CardType.DEBIT).contains(payment.getCardType())) {
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.FORBIDDEN.value(),"Invalid card type", null)
                    , HttpStatus.FORBIDDEN);
        }

        if(!cardService.existsByCardDigits(payment.getNumber())){
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.FORBIDDEN.value(),"Invalid card", null)
                    , HttpStatus.FORBIDDEN);
        }

        Card cardUsed = cardService.findByCardDigits(payment.getNumber());

        boolean hasCard = currentClient.getCards()
                .stream()
                .anyMatch(card -> card.getId() == cardUsed.getId());

        if(!hasCard){
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.FORBIDDEN.value(),"You don't own this card", null)
                    , HttpStatus.FORBIDDEN);
        }

        if(cardUsed.isExpired()){
            return new ResponseEntity<>(
                    new ErrorResponse(HttpStatus.FORBIDDEN.value(),"This card is expired", null)
                    , HttpStatus.FORBIDDEN);
        }

        Account accountToBeDebited = currentClient.getAccounts()
                .stream()
                .filter(acc -> acc.getBalance() >= payment.getAmount())
                .findFirst()
                .orElseThrow(InsufficientFundsException::new);

        accountToBeDebited.withdraw(payment.getAmount(), payment.getDescription(), transactionService,accountService,  accountToBeDebited.getBalance());

        return new ResponseEntity<>(new SuccessResponse(HttpStatus.CREATED.value(), "Purchase successful", null), HttpStatus.CREATED);

    }

//    public ResponseEntity<Object> createCard(int acc, Client currentClient, CardType enumType, CardColor enumColor, CardsRepository cardRepo){
//        if (acc < 3) {
//            String digits = Utils.generateCardsDigits();
//            while (cardRepo.existsByCardDigits(digits)) {
//                digits = Utils.generateCardsDigits();
//            }
//            Card newCard = new Card(currentClient, enumType, enumColor, LocalDate.now(), LocalDate.now().plusYears(5), digits);
//            cardRepo.save(newCard);
//            return new ResponseEntity<>(new CardDTO(newCard), HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>("Max owned cards reached", HttpStatus.FORBIDDEN);
//        }
//    }

}

