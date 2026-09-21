package com.dan.bank_backend.products.exceptions;

public class ProductAccountNotActiveException extends RuntimeException {
    public ProductAccountNotActiveException() {
        super("Product Not Active");
    }
}
