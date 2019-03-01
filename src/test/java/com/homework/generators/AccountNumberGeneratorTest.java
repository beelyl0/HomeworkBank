package com.homework.generators;

import org.junit.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertTrue;

public class AccountNumberGeneratorTest {

    @Test
    public void testAccountNumberGenerator() {
        int threadCount = 5;
        int n = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        Set<String> accountNumbers = new ConcurrentHashMap<String, Integer>().newKeySet();

        for(int i = 0; i < threadCount; ++i) {
            executor.submit(() -> {
                for (int j = 0; j < n; ++j) {
                    String accountNumber = AccountNumberGenerator.generateAccountNumber();
                    accountNumbers.add(accountNumber);
                }
                latch.countDown();
            });
        }

        try {
            latch.await();

            for (int i = 1; i < threadCount * n; ++i) {
                String accountNumber = AccountNumberGenerator.ACCOUNT_NUMBER_PREFIX + String.format("%03d", i);
                assertTrue(accountNumbers.contains(accountNumber));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        executor.shutdown();

    }

}
