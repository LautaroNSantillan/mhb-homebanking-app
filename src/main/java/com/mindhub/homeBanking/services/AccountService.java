package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.AccountType;
import com.mindhub.homeBanking.models.Client;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountService {
    List<AccountDTO> getAccountsDTO();
    List<AccountDTO> mapAccountsSetToDTO(Set<Account> accs);
    List<Account> findAll();
    List<Account> findNonDisabledAccountsByClient(Client client);
    AccountDTO getAccountDTO(Long id);
    Account findById(Long id);
    Account findByNumber(String number);
    Optional<Account>findByIdOptional(Long id);
    long countAccounts();
    boolean existsByAlias(String alias);
    boolean existsByNumber(String number);
    void save(Account acc);
    void updateBalance(Account acc, double amount);
    void deleteAccount(Account acc);
    void saveNewAccount(Client client, AccountType type);
    void disableAcc(Account acc);
}
