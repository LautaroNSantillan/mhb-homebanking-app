package com.mindhub.homeBanking.repositories;

import com.mindhub.homeBanking.models.Loan;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.extractor.Extractors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
public class RepositoriesTest {
    @Autowired
    LoanRepository loanRepository;


    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans)
                .isNotEmpty();
    }

    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans)
                .extracting("name")
                .contains("Personal");
    }
}
