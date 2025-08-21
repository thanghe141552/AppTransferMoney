package com.example.cache.impl;

import com.example.cache.TransactionCache;
import com.example.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TransactionCacheImpl implements TransactionCache {

    private static final ConcurrentHashMap<UUID, Transaction> transactionCache = new ConcurrentHashMap<>();

    @Override
    public void addTransaction(Transaction transaction) {
        transactionCache.put(transaction.getId(), transaction);
        System.out.println(transaction.getId() + " added to cache, total number transaction cached: " + transactionCache.size());
    }

    @Override
    public Transaction getTransaction(String transactionId) {
        return transactionCache.get(UUID.fromString(transactionId));
    }

    @Override
    public void removeTransaction(String transactionId) {
        transactionCache.remove(UUID.fromString(transactionId));
    }

    @Override
    public void clear() {
        transactionCache.clear();
    }
}
