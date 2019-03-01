package com.homework.models;

import java.math.BigDecimal;

public class Account {

    private final String accountNumber;

    private BigDecimal amount;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        amount = BigDecimal.ZERO;
    }

    public Account(String accountNumber, BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
