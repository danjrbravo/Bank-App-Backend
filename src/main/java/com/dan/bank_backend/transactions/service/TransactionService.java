package com.dan.bank_backend.transactions.service;

import com.dan.bank_backend.transactions.dtos.ConsignTransactionRequestDTO;
import com.dan.bank_backend.transactions.dtos.TransactionDTO;
import com.dan.bank_backend.transactions.dtos.TransferTransactionRequestDTO;
import com.dan.bank_backend.transactions.dtos.WithdrawTransactionRequestDTO;

public interface TransactionService {
    TransactionDTO consign(ConsignTransactionRequestDTO request);
    TransactionDTO withdraw(WithdrawTransactionRequestDTO request);
    TransactionDTO transfer(TransferTransactionRequestDTO request);
}
