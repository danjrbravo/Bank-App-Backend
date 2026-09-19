package com.dan.bank_backend.transactions.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException()
    {
        super("Transaction not found");
    }
}
