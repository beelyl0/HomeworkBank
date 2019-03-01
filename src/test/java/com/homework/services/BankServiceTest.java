package com.homework.services;

import com.homework.exceptions.AccountAmountValidationException;
import com.homework.exceptions.AccountFromNotFoundException;
import com.homework.exceptions.AccountToNotFoundException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BankServiceTest {

    private final BankService bankService = new BankService();

    @Test (expected = AccountFromNotFoundException.class)
    public void testGetAmount() {
        bankService.getAmount("BAD_ACCOUNT_NUMBER");
    }

    @Test
    public void testCreateAccount() {
        String accountNumber = bankService.createAccount(new BigDecimal(100));
        assertNotNull(accountNumber);
        BigDecimal amount = bankService.getAmount(accountNumber);
        assertEquals(new BigDecimal(100), amount);
    }

    @Test (expected = AccountAmountValidationException.class)
    public void testCreateAccountWithNegativeAmount() {
        bankService.createAccount(new BigDecimal(-100));
    }

    @Test
    public void testTransferAmount() {
        String fromAccountNumber = bankService.createAccount(BigDecimal.TEN);
        String toAccountNumber = bankService.createAccount(BigDecimal.TEN);
        bankService.transferAmount(fromAccountNumber, toAccountNumber, BigDecimal.TEN);
        assertEquals(BigDecimal.ZERO, bankService.getAmount(fromAccountNumber));
        assertEquals(new BigDecimal(20), bankService.getAmount(toAccountNumber));
    }

    @Test (expected = AccountFromNotFoundException.class)
    public void testTransferAmountFromAccountNotFound() {
        bankService.transferAmount("firstAccountNumber", "secondAccountNumber", BigDecimal.TEN);
    }

    @Test (expected = AccountToNotFoundException.class)
    public void testTransferAmountToAccountNotFound() {
        String fromAccountNumber = bankService.createAccount(BigDecimal.TEN);
        bankService.transferAmount(fromAccountNumber, "secondAccountNumber", BigDecimal.TEN);
    }

    @Test (expected = AccountAmountValidationException.class)
    public void testTransferNegativeAmount() {
        String fromAccountNumber = bankService.createAccount(BigDecimal.TEN);
        String toAccountNumber = bankService.createAccount(BigDecimal.TEN);
        bankService.transferAmount(fromAccountNumber, toAccountNumber, new BigDecimal(-10));
    }

    @Test
    public void testMultiTransfer() {
        int threadCount = 20;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        int amount = 1000;
        int amountPerThread = amount / threadCount;
        String firstAccountNumber = bankService.createAccount(new BigDecimal(amount));
        String secondAccountNumber = bankService.createAccount(new BigDecimal(amount));

        for(int i = 0; i < threadCount; ++i) {
            boolean isOdd = (i % 2 ==0);
            String fromAccount = isOdd ? firstAccountNumber : secondAccountNumber;
            String toAccount = isOdd ? secondAccountNumber : firstAccountNumber;
            executor.submit(() -> {
                for (int j = 0; j < amountPerThread; ++j) {
                    bankService.transferAmount(fromAccount, toAccount, BigDecimal.ONE);
                }
                latch.countDown();
            });
        }

        try {
            latch.await();
            assertEquals(new BigDecimal(1000), bankService.getAmount(firstAccountNumber));
            assertEquals(new BigDecimal(1000), bankService.getAmount(secondAccountNumber));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        executor.shutdown();
    }


}
