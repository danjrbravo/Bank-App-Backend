package com.dan.bank_backend.clients.exception;

public class ClientHasProductsException extends RuntimeException {
    public ClientHasProductsException(Long clientId) {
        super("Client with id "+clientId+ " cannot be deleted because it has financial products");
    }
}
