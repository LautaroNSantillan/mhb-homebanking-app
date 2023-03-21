package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.dtos.CardDTO;
import com.mindhub.homeBanking.dtos.ClientDTO;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.models.Loan;
import com.mindhub.homeBanking.services.impl.AccountServiceImpl;
import com.mindhub.homeBanking.services.impl.ClientServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class ClientController {

    private final ClientServiceImpl clientService;
    private final AccountServiceImpl accService;

    public ClientController(ClientServiceImpl clientService, AccountServiceImpl accService) {
        this.clientService = clientService;
        this.accService = accService;
    }


    @GetMapping("/clients")
    public List<ClientDTO> getClientsDTO() {
        return clientService.getClientsDTO();
    }

    @RequestMapping("clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientDTO(id);
    }

    @RequestMapping("clients/{id}/accounts")
    public List<AccountDTO> getClientAccounts(@PathVariable Long id){
       return  accService.mapAccountsSetToDTO(clientService.findById(id).getAccounts());
    }

    @RequestMapping("clients/{id}/loans")
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

       clientService.saveNewClient(incomingObj.getFirstName(), incomingObj.getLastName(), incomingObj.getEmail(), incomingObj.getPassword());
        return new ResponseEntity<>("Logged in",HttpStatus.CREATED);
    }

    @RequestMapping("clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }
    @RequestMapping("clients/current/cards")
    public List<CardDTO> getCurrentCards(Authentication authentication) {
        Client currentClient= clientService.findByEmail(authentication.getName());
        return currentClient.getCards().stream().map(CardDTO::new).collect(Collectors.toList());
    }


}
