package com.example.exception;

public class TransactionAmountExceed extends TransactionDeclinedException {
    public TransactionAmountExceed(String message) {
        super(message);
    }
}
