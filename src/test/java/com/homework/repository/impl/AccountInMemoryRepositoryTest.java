package com.homework.repository.impl;


import com.homework.models.Account;
import com.homework.repository.AccountRepository;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class AccountInMemoryRepositoryTest {

    private final AccountRepository store = AccountInMemoryRepository.getInstance();

    @Test
    public void testSingletonInstance() {
        AccountInMemoryRepository firstStore = AccountInMemoryRepository.getInstance();
        AccountInMemoryRepository secondStore = AccountInMemoryRepository.getInstance();
        assertEquals(firstStore, secondStore);
    }

    @Test
    public void testGetAccount() {
        store.clear();
        store.save(new Account("1"));
        Optional<Account> account = store.get("1");
        assertTrue(account.isPresent());
        assertEquals("1", account.get().getAccountNumber());
    }

    @Test
    public void testGetAccountNotFound() {
        store.clear();
        Optional<Account> account = store.get("3");
        assertFalse(account.isPresent());
    }

    @Test
    public void testMultiSave() {
        store.clear();
        int threadCount = 20;
        int n = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicInteger counter = new AtomicInteger(1);

        for(int i = 0; i < threadCount; ++i) {
            executor.submit(() -> {
                for (int j = 0; j < n; ++j) {
                    store.save(new Account(Integer.toString(counter.getAndIncrement())));
                }
                latch.countDown();
            });
        }

        try {
            latch.await();
            for (int i = 1; i < threadCount * n; ++i) {
                String accountNum = Integer.toString(i);
                Optional<Account> account = store.get(accountNum);
                assertTrue(account.isPresent());
                assertEquals(accountNum, account.get().getAccountNumber());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        executor.shutdown();

    }
    
}
