package com.dan.bank_backend.products.dtos;

import com.dan.bank_backend.products.model.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequestDTO (
        @NotNull
        AccountType accountType,
        @NotNull
        Long clientId,
        @DecimalMin(value = "0.0",inclusive = true)
        BigDecimal balance,
        boolean gmfExempt
){
}
