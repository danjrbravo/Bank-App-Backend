package com.dan.bank_backend.clients.exception;

public class ClientEmailAlreadyExistsException extends RuntimeException{
    public ClientEmailAlreadyExistsException(){
        super("Email already exists.");
    }
}
