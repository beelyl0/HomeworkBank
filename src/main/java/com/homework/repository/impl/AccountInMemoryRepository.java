package com.homework.repository.impl;

import com.homework.models.Account;
import com.homework.repository.AccountRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class AccountInMemoryRepository implements AccountRepository {

    private static final ConcurrentHashMap<String, Account> store = new ConcurrentHashMap<>();

    private static final AccountInMemoryRepository ourInstance = new AccountInMemoryRepository();

    private AccountInMemoryRepository() {
    }

    public static AccountInMemoryRepository getInstance() {
        return ourInstance;
    }

    @Override
    public Account save(Account account) {
        return store.put(account.getAccountNumber(), account);
    }

    @Override
    public Optional<Account> get(String accountNumber) {
        return Optional.ofNullable(store.get(accountNumber));
    }

    @Override
    public void clear() {
        store.clear();
    }

}
