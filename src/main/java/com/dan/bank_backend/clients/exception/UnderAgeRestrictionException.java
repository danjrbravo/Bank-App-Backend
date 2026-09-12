package com.dan.bank_backend.clients.exception;

public class UnderAgeRestrictionException extends RuntimeException {
    public UnderAgeRestrictionException() {
        super("Age is less than 18");
    }
}
