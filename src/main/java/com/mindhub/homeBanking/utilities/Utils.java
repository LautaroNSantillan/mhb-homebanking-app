package com.mindhub.homeBanking.utilities;

import com.mindhub.homeBanking.dtos.CardDTO;
import com.mindhub.homeBanking.models.Card;
import com.mindhub.homeBanking.models.CardColor;
import com.mindhub.homeBanking.models.CardType;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.repositories.CardsRepository;
import com.mindhub.homeBanking.services.AccountService;
import com.mindhub.homeBanking.services.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class Utils {

    public static ResponseEntity<Object> createCard(int acc, Client currentClient, CardType enumType, CardColor enumColor, CardsRepository cardRepo){
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
    public static String generateCardsDigits() {
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

    public static String buildCardDigits(Client client) {    //   OBSOLETE
        String generatedCardDigits = generateCardsDigits();
        boolean foundMatch = false;
        List<Card> cardList = new ArrayList<>(client.getCards());
        while (!foundMatch) {
            for (int i = 0; i < cardList.size(); i++) {
                String clientCardNumber = cardList.get(i).getCardDigits();
                if (clientCardNumber.equals(generatedCardDigits)) {
                    generatedCardDigits = generateCardsDigits();
                    foundMatch = false;
                    break;
                }
                foundMatch = true;
            }
        }
        return generatedCardDigits;
    }

    public static String generateVin(Client client) {
        // Generate a random 6-digit number between 000000 and 999999
        Random rand = new Random();
        int num = rand.nextInt(100000000); // generates a number between 0 and 999999
        boolean isVinUnique = false;
        String uniqueVin = "";
        while (!isVinUnique) {
            // Build the VIN string
            uniqueVin = "VIN-" + String.format("%08d", num);
            String vinToCheck = uniqueVin;
            isVinUnique = client.getAccounts().stream().noneMatch(acc -> acc.getNumber().equals(vinToCheck));
        }
        return uniqueVin;
    }

    public static String generateUniqueAlias(AccountService accService) {
        String alias = Utils.aliasGenerator();
        do{
            alias = Utils.aliasGenerator();
        }while(accService.existsByAlias(alias));
        return alias;
    }
    public static String aliasGenerator() {
            Faker faker = new Faker();
            String alias = faker.animal().name().toUpperCase() + "."+ faker.book().author().toUpperCase() + "." + faker.superhero().name().toUpperCase();
            alias = alias.replace(" ", "");
            return alias;
    }
}



