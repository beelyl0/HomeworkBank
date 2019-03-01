package com.homework.repository;

import com.homework.models.Account;

import java.util.Optional;

public interface AccountRepository {

    Account save(Account obj);

    Optional<Account> get(String accountNumber);

    void clear();

}
