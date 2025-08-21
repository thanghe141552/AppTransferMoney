package com.example.exception;

public class TransactionDeclinedException extends TransactionException {
    public TransactionDeclinedException(String message) {
        super(message);
    }
}
