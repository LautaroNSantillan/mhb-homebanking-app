package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.dtos.CardDTO;
import com.mindhub.homeBanking.dtos.ClientDTO;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.models.Loan;
import com.mindhub.homeBanking.repositories.AccountRepository;
import com.mindhub.homeBanking.repositories.ClientRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private PasswordEncoder pwdEncoder;
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private AccountRepository accRepo;

    @GetMapping("/clients")
    public List<ClientDTO> getClientsDTO() {
        return clientRepo.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(Objects.requireNonNull(clientRepo.findById(id).orElse(null)));
    }

    @RequestMapping("clients/{id}/accounts")
    public List<AccountDTO> getClientAccounts(@PathVariable Long id){
        return Objects.requireNonNull(clientRepo.findById(id).orElse(null)).getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("clients/{id}/loans")
    public List<Loan> getClientLoans(@PathVariable Long id){
        return Objects.requireNonNull(clientRepo.findById(id).orElse(null)).getLoans();
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestBody Client incomingObj) {

        if (incomingObj.getFirstName().isEmpty() ) {
            return new ResponseEntity<>("Missing First Name", HttpStatus.BAD_REQUEST);
        }
        if (incomingObj.getLastName().isEmpty() ) {
            return new ResponseEntity<>("Missing Last Name", HttpStatus.BAD_REQUEST);
        }
        if (incomingObj.getEmail().isEmpty()) {
            return new ResponseEntity<>("Missing Email", HttpStatus.BAD_REQUEST);
        }
        else if (incomingObj.getEmail().endsWith("@admin.mindhub")) {
            return new ResponseEntity<>("Invalid Email, you can't use this email", HttpStatus.BAD_REQUEST);
        }
        if (incomingObj.getPassword().isEmpty() ) {
            return new ResponseEntity<>("Missing Password", HttpStatus.BAD_REQUEST);
        }

        if (clientRepo.findByEmail(incomingObj.getEmail()) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

       clientRepo.save(new Client(incomingObj.getFirstName(), incomingObj.getLastName(), incomingObj.getEmail(), pwdEncoder.encode(incomingObj.getPassword())));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return new ClientDTO(clientRepo.findByEmail(authentication.getName()));
    }
    @RequestMapping("clients/current/cards")
    public List<CardDTO> getCurrentCards(Authentication authentication) {
        Client currentClient= clientRepo.findByEmail(authentication.getName());
        return currentClient.getCards().stream().map(CardDTO::new).collect(Collectors.toList());
    }


}