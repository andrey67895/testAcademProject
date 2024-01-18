package ru.account.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.account.models.Account;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT * FROM account", nativeQuery = true)
    List<Account> getAccounts(Pageable pageable);

    @Query(value = "SELECT * FROM account", nativeQuery = true)
    List<Account> getAccountsAll();

    @Query(value = "SELECT * FROM account WHERE account.id = :id", nativeQuery = true)
    Optional<Account> getAccountsById(@Param("id") Long id);

    @Query(value = "SELECT * FROM account WHERE first_name like '%:text%' OR last_name like '%:text%' ", nativeQuery = true)
    List<Account> getAccountsBySearchText(@Param("text") String text, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO public.account (first_name,last_name,passport_uuid) VALUES (:firstName, :lastName, :passportUUID); ", nativeQuery = true)
    void save(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("passportUUID") UUID passportUUID);
}
