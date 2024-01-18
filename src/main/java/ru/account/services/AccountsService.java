package ru.account.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.account.models.Account;
import ru.account.repositories.AccountsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountsService {
    private final AccountsRepository accountsRepository;

    public AccountsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public List<Account> getAccounts(Integer offset, Integer limit){
        return accountsRepository.getAccounts(PageRequest.of(offset, limit));
    }

    public List<Account> getAccountsAll(){
        return accountsRepository.getAccountsAll();
    }

    public List<Account> getAccountsBySearchText(String text, Integer offset, Integer limit){
        return accountsRepository.getAccountsBySearchText(text, PageRequest.of(offset, limit));
    }

    public Optional<Account> getAccountById(Long id){
        return accountsRepository.getAccountsById(id);
    }


    public void saveAccount(Account account){
        accountsRepository.save(account.getFirstName(), account.getLastName(), account.getPassportUUID());
    }

    @Transactional
    @Modifying
    public void deleteById(Long id){
        accountsRepository.deleteById(id);
    }
}
