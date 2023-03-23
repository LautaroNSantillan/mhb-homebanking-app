package com.mindhub.homeBanking.services.impl;

import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.dtos.ClientDTO;
import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.AccountType;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.repositories.AccountRepository;
import com.mindhub.homeBanking.services.AccountService;
import com.mindhub.homeBanking.utilities.Utils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accRepo;

    public AccountServiceImpl(AccountRepository accRepo) {
        this.accRepo = accRepo;
    }

    @Override
    public List<AccountDTO> getAccountsDTO(){
        return this.mapAccountsToDTOs(this.findAccounts());
    }
    @Override
    public List<Account> findAll(){
        return this.accRepo.findAll();
    }
    @Override
    public AccountDTO getAccountDTO(Long id){
        return this.generateAccountDTO(this.findById(id));
    }
    @Override
    public Account findById(Long id){
        return this.accRepo.findById(id).orElse(null);
    }
    @Override
    public Optional<Account> findByIdOptional(Long id){
        return this.accRepo.findById(id);
    }
    @Override
    public Account findByNumber(String number){
        return this.accRepo.findByNumber(number);
    }
    @Override
    public long countAccounts(){
       return this.accRepo.count();
    }
    @Override
    public void saveNewAccount(Client client, AccountType type){
        this.accRepo.save(this.generateNewAcc(client, type));
    }
    @Override
    public void deleteAccount(Account acc){
        this.accRepo.delete(acc);
    }
    @Override
    public boolean existsByAlias(String alias){
        return this.accRepo.existsByAlias(alias);
    }
    @Override
    public boolean existsByNumber(String number){
        return this.accRepo.existsByNumber(number);
    }
    @Override
    public List<AccountDTO> mapAccountsSetToDTO(Set<Account> accs){
        return accs.stream().map(AccountDTO::new).collect(Collectors.toList());
    }
    @Override
    public void save(Account acc){
        this.accRepo.save(acc);
    }
    @Override
    public void updateBalance(Account acc, double amount){
        acc.setBalance(acc.getBalance()+ amount);
    }
    @Override
    public void disableAcc(Account acc){
        acc.setDisabled(true);
    }

    private List<Account> findAccounts(){
        return this.accRepo.findAll();
    }
    public List<AccountDTO> mapAccountsToDTOs (List<Account> accs){
      return  accs.stream().map(AccountDTO::new).collect(Collectors.toList());
    }
    private AccountDTO generateAccountDTO(Account acc){
        return new AccountDTO(acc);
    }
    private Account generateNewAcc(Client client, AccountType type){
       return new Account(LocalDateTime.now(), 0, this, client, type);
    }
}
