package com.example.cache;


import com.example.entity.Transaction;

public interface TransactionCache {

    void addTransaction(Transaction transaction);

    Transaction getTransaction(String transactionId);

    void removeTransaction(String transactionId);

    void clear();
}
