package com.dan.bank_backend.clients.exception;

public class ClientIdentificationNumberAlreadyExists extends RuntimeException{
    public ClientIdentificationNumberAlreadyExists(){
        super("Identification number already exists.");
    }
}
