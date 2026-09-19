package com.dan.bank_backend.transactions.mapper;

import com.dan.bank_backend.products.entity.Product;
import com.dan.bank_backend.transactions.dtos.ConsignTransactionRequestDTO;
import com.dan.bank_backend.transactions.dtos.TransactionDTO;
import com.dan.bank_backend.transactions.dtos.TransferTransactionRequestDTO;
import com.dan.bank_backend.transactions.dtos.WithdrawTransactionRequestDTO;
import com.dan.bank_backend.transactions.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionDTO toDTO (Transaction transaction){
        return new TransactionDTO(
          transaction.getId(),
          transaction.getTransactionType(),
          transaction.getAmount(),
          transaction.getOriginProduct() != null
                ? transaction.getOriginProduct().getId()
                  : null,
          transaction.getOriginProduct() != null
                ? transaction.getOriginProduct().getId()
                  :null,
          transaction.getTransactionDate()
        );
    }
    public Transaction toEntity(ConsignTransactionRequestDTO dto, Product destinyProduct){
        Transaction transaction = new Transaction();
        transaction.setTransactionType(dto.transactionType());
        transaction.setAmount(dto.amount());
        transaction.setDestinyProduct(destinyProduct);
        return transaction;
    }
    public Transaction toEntity(WithdrawTransactionRequestDTO dto,Product originProduct){
        Transaction transaction = new Transaction();
        transaction.setTransactionType(dto.transactionType());
        transaction.setAmount(dto.amount());
        transaction.setOriginProduct(originProduct);
        return transaction;
    }
    public Transaction toEntity(TransferTransactionRequestDTO dto,
                                Product originProduct,
                                Product destinyProduct){
        Transaction transaction = new Transaction();
        transaction.setTransactionType(dto.transactionType());
        transaction.setAmount(dto.amount());
        transaction.setOriginProduct(originProduct);
        transaction.setDestinyProduct(destinyProduct);
        return transaction;
    }

}
