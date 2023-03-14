package com.mindhub.homeBanking.repositories;
import com.mindhub.homeBanking.models.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
}
