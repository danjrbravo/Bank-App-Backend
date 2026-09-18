package com.dan.bank_backend.transactions.dtos;

import com.dan.bank_backend.transactions.Model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConsignTransactionRequestDTO(
        TransactionType transactionType,
        BigDecimal amount,
        Long destinyProduct
) {
}
