package com.dan.bank_backend.clients.exception;

public class ClientNotFoundByIdentificationNumberException extends RuntimeException {
    public ClientNotFoundByIdentificationNumberException(String identificationNumber) {
        super("Client with identification number "+identificationNumber+" already exists");
    }
}
