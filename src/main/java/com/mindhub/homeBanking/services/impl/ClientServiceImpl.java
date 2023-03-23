package com.mindhub.homeBanking.services.impl;

import com.mindhub.homeBanking.dtos.ClientDTO;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.repositories.ClientRepository;
import com.mindhub.homeBanking.services.ClientService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepo;
    private final PasswordEncoder pwdEncoder;

    public ClientServiceImpl(ClientRepository clientRepo, PasswordEncoder pwdEncoder) {
        this.clientRepo = clientRepo;
        this.pwdEncoder = pwdEncoder;
    }
    @Override
    public List<ClientDTO> getClientsDTO(){
        return this.mapClientsToDTOs(this.findClients());
    }
    @Override
    public ClientDTO getClientDTO(Long id){
        return this.generateClientDTO(this.findClientById(id));
    }
    @Override
    public void saveNewClient(String fname,String lname, String email, String pwd){
        this.clientRepo.save(new Client(fname, lname, email, pwdEncoder.encode(pwd)));
    }
    @Override
    public Client findByEmail(String email){
        return this.clientRepo.findByEmail(email);
    }
    @Override
    public Client findClientByEmailExcludingDisabledAccounts(String email){
        return this.clientRepo.findClientByEmailExcludingDisabledAccounts(email);
    }
    @Override
    public Client findById(Long id){
        return this.clientRepo.findById(id).orElse(null);
    }

    private List<Client> findClients(){
        return this.clientRepo.findAll();
    }
    private List<ClientDTO> mapClientsToDTOs(List<Client> clients) {
        return clients.stream().map(ClientDTO::new).collect(Collectors.toList());
    }
    private Client findClientById(Long id){
       return this.clientRepo.findById(id).orElse(null);
    }
    private ClientDTO generateClientDTO(Client client){
        return new ClientDTO(client);
    }
}
