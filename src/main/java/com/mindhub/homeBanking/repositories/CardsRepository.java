package com.mindhub.homeBanking.repositories;

import com.mindhub.homeBanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CardsRepository extends JpaRepository<Card, Long> {
    boolean existsByCardDigits (String cardDigits);
   Card findByCardDigits(String cardDigits);
}
