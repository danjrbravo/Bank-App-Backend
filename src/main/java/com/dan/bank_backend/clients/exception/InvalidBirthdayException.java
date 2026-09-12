package com.dan.bank_backend.clients.exception;

public class InvalidBirthdayException extends RuntimeException {
    public InvalidBirthdayException() {
        super("The birthdate day is invalid");
    }
}
