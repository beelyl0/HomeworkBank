package com.homework.generators;

import com.homework.exceptions.AccountNumbersExhaustion;

import java.util.concurrent.atomic.AtomicLong;

public class AccountNumberGenerator {

    public static final String ACCOUNT_NUMBER_PREFIX = "HOMEWORK";

    private static final AtomicLong number = new AtomicLong(1L);

    public static final long MAX_NUMBER = 999;

    public static String generateAccountNumber() {
        long nextNumber = number.getAndIncrement();

        if (nextNumber > MAX_NUMBER) {
            throw new AccountNumbersExhaustion("Account number was exceeded");
        }

        return ACCOUNT_NUMBER_PREFIX + String.format("%03d", nextNumber);
    }

}
