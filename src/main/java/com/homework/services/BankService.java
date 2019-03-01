package com.homework.services;

import com.homework.exceptions.AccountAmountValidationException;
import com.homework.exceptions.AccountFromNotFoundException;
import com.homework.exceptions.AccountOperationNotPermitted;
import com.homework.exceptions.AccountToNotFoundException;
import com.homework.models.Account;
import com.homework.repository.AccountRepository;
import com.homework.repository.impl.AccountInMemoryRepository;

import java.math.BigDecimal;

import static com.homework.generators.AccountNumberGenerator.generateAccountNumber;

public final class BankService {

    private final AccountRepository store = AccountInMemoryRepository.getInstance();

    public BigDecimal getAmount(String accountNumber) {
        Account account = store.get(accountNumber)
                .orElseThrow(() -> new AccountFromNotFoundException("Account not found"));
        synchronized (account) {
            return account.getAmount();
        }
    }
    
    public String createAccount(BigDecimal amount) {
        if (amount == null || amount.signum() == -1) {
            throw new AccountAmountValidationException("Amount is not exist or not positive");
        }
        String accountNumber = generateAccountNumber();
        store.save(new Account(accountNumber, amount));
        return accountNumber;
    }

    public void transferAmount(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        Account fromAccount = store.get(fromAccountNumber)
                .orElseThrow(() -> new AccountFromNotFoundException("From account not found"));

        Account toAccount = store.get(toAccountNumber)
                .orElseThrow(() -> new AccountToNotFoundException("Target account not found"));

        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            throw new AccountOperationNotPermitted("From and Target accounts the same");
        }

        if (amount == null || amount.signum() == -1) {
            throw new AccountAmountValidationException("Amount is not exist or not positive");
        }

        Account[] locks = normalizeAccountOrder(fromAccount, toAccount);
        synchronized (locks[0]) {
            synchronized (locks[1]) {
                BigDecimal newFromAmount = fromAccount.getAmount().subtract(amount);
                BigDecimal newToAmount = toAccount.getAmount().add(amount);
                fromAccount.setAmount(newFromAmount);
                toAccount.setAmount(newToAmount);
            }
        }
    }

    private Account[] normalizeAccountOrder(Account first, Account second) {
        if (second.getAccountNumber().compareTo(first.getAccountNumber()) > 0) {
            return new Account[] {second, first};
        }
        return new Account[] {first, second};
    }

}
