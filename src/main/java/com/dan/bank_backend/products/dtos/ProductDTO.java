package com.dan.bank_backend.products.dtos;

import com.dan.bank_backend.products.model.AccountState;
import com.dan.bank_backend.products.model.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDTO (
        Long id,
        AccountType accountType,
        AccountState productState,
        String accountNumber,
        BigDecimal Balance,
        boolean gmfExcempt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long clientId

){
}
