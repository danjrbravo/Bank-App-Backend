package com.dan.bank_backend.clients.exception;

public class ClientNotFoundException extends RuntimeException{
    public ClientNotFoundException(Long id){
        super("Client with "+id+" not found");
    }
}
