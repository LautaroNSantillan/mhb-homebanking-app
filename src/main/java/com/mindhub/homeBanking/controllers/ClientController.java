package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.dtos.CardDTO;
import com.mindhub.homeBanking.dtos.ClientDTO;
import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.models.Loan;
import com.mindhub.homeBanking.repositories.ClientRepository;
import com.mindhub.homeBanking.services.AccountService;
import com.mindhub.homeBanking.services.ClientService;
import com.mindhub.homeBanking.services.impl.AccountServiceImpl;
import com.mindhub.homeBanking.services.impl.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final ClientService clientService;
    private final AccountService accService;

    public ClientController(ClientService clientService, AccountService accService) {
        this.clientService = clientService;
        this.accService = accService;
    }


    @GetMapping("/clients")
    public List<ClientDTO> getClientsDTO() {
        return clientService.getClientsDTO();
    }

    @GetMapping("clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientDTO(id);
    }

    @GetMapping("clients/{id}/accounts")
    public List<AccountDTO> getClientAccounts(@PathVariable Long id){
       return  accService.mapAccountsSetToDTO(clientService.findById(id).getAccounts());
    }

    @GetMapping("clients/{id}/loans")
    public List<Loan> getClientLoans(@PathVariable Long id){
        return Objects.requireNonNull(clientService.findById(id).getLoans());
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

        if (clientService.findByEmail(incomingObj.getEmail()) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(incomingObj.getFirstName(), incomingObj.getLastName(), incomingObj.getEmail(), passwordEncoder.encode(incomingObj.getPassword()));

        clientRepository.save(client);

      // clientService.saveNewClient(incomingObj.getFirstName(), incomingObj.getLastName(), incomingObj.getEmail(), incomingObj.getPassword());
        return new ResponseEntity<>("Logged in",HttpStatus.CREATED);
    }

    @GetMapping("clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
//        System.out.println(authentication.getName());
//        Client client = clientService.findByEmail(authentication.getName());
//        Set<Account> notDisabledAccs =  client.getAccounts().stream().filter(account -> !account.isDisabled()).collect(Collectors.toSet());
//        client.setAccounts(notDisabledAccs);
//        return new ClientDTO(client);

        return new ClientDTO(this.clientService.findClientByEmailExcludingDisabledAccounts(authentication.getName()));

    }
    @GetMapping("clients/current/cards")
    public List<CardDTO> getCurrentCards(Authentication authentication) {
        Client currentClient= clientService.findByEmail(authentication.getName());
        return currentClient.getCards().stream().map(CardDTO::new).collect(Collectors.toList());
    }


}
