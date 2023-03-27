package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.dtos.ClientDTO;
import com.mindhub.homeBanking.models.Client;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClientsDTO();
    ClientDTO getClientDTO(Long id);
    Client findByEmail(String email);
    Client findClientByEmailExcludingDisabledAccounts(String email);
    Client findById(Long id);
    void saveNewClient(String fname,String lname, String email, String pwd);

}
