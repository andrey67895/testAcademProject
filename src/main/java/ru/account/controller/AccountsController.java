package ru.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import ru.account.models.Account;
import ru.account.models.ErrorModel;
import ru.account.services.AccountsService;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@Log4j2
public class AccountsController {
    private AccountsService accountsService;

    @Autowired
    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @SneakyThrows
    @GetMapping("/accounts")
    public ResponseEntity<Object> getAccounts(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "searchText", required = false) String searchText
    ) {
        if (Objects.nonNull(searchText)) {
            return ResponseEntity.ok().body(accountsService.getAccountsBySearchText(searchText, offset, limit));
        } else {
            return ResponseEntity.ok().body(accountsService.getAccounts(offset, limit));
        }
    }

    @SneakyThrows
    @GetMapping("/accountsAll")
    public ResponseEntity<Object> getAccountsAll() {
        return ResponseEntity.ok().body(accountsService.getAccountsAll());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccountsById(
            @PathVariable(value = "id") String id
    ) {
        if (id.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorModel(HttpStatus.BAD_REQUEST.value(), "Некорректное значение: id равен пустой строке"));
        }
        try {
            Account account = accountsService.getAccountById(Long.parseLong(id)).orElse(null);
            if (account != null) {
                return ResponseEntity.ok().body(account);
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorModel(HttpStatus.NOT_FOUND.value(), String.format("Отсуствует юзер с id: %s", id)));

            }
        } catch (RestClientException | IllegalArgumentException e) {
            log.info(e);
            return ResponseEntity.badRequest().body(new ErrorModel(HttpStatus.BAD_REQUEST.value(), e.toString()));
        } catch (Exception e) {
            log.info(e);
            return ResponseEntity.internalServerError().body(new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString()));
        }
    }

    @PostMapping("/accounts")
    public ResponseEntity<Object> postAccount(
            @RequestBody Account account
    ) {
        account.checkAccount();
        account.setId(null);
        accountsService.saveAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<Object> putAccountById(
            @PathVariable(value = "id") Long id,
            @RequestBody Account account
    ) {
        try {
            if (accountsService.getAccountById(id).orElse(null) != null) {
                account.checkAccount();
                account.setId(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorModel(HttpStatus.NOT_FOUND.value(), String.format("Отсуствует юзер с id: %s", id)));

            }
        } catch (RestClientException | IllegalArgumentException e) {
            log.info(e);
            return ResponseEntity.badRequest().body(new ErrorModel(HttpStatus.BAD_REQUEST.value(), e.toString()));
        } catch (Exception e) {
            log.info(e);
            return ResponseEntity.internalServerError().body(new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString()));
        }
    }


    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Object> deleteAccountById(
            @PathVariable(value = "id") Long id
    ) {
        Account account = accountsService.getAccountById(id).orElse(null);
        if (account != null) {
            accountsService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorModel(HttpStatus.NOT_FOUND.value(), String.format("Отсуствует юзер с id: %s", id)));
        }
    }
}
