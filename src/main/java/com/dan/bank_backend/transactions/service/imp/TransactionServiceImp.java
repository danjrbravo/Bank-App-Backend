package com.dan.bank_backend.transactions.service.imp;

import com.dan.bank_backend.products.entity.Product;
import com.dan.bank_backend.products.service.ProductService;
import com.dan.bank_backend.transactions.dtos.ConsignTransactionRequestDTO;
import com.dan.bank_backend.transactions.dtos.TransactionDTO;
import com.dan.bank_backend.transactions.dtos.TransferTransactionRequestDTO;
import com.dan.bank_backend.transactions.dtos.WithdrawTransactionRequestDTO;
import com.dan.bank_backend.transactions.entity.Transaction;
import com.dan.bank_backend.transactions.mapper.TransactionMapper;
import com.dan.bank_backend.transactions.repository.TransactionRepository;
import com.dan.bank_backend.transactions.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class TransactionServiceImp implements TransactionService {
    private final TransactionRepository transactionRepo;
    private final ProductService productService;
    private final TransactionMapper transactionMapper;

    @Transactional
    @Override
    public TransactionDTO consign(ConsignTransactionRequestDTO request) {
        productService.verifyAmountIsNotZero(request.amount());
        Product destinyProduct = productService.getProductEntityById(request.destinyProductId());
        productService.verifyProductIsActive(destinyProduct);

        Transaction transaction = transactionMapper.toEntity(request,destinyProduct);
        transaction.setTransactionDate(LocalDateTime.now());

        productService.addToBalance(destinyProduct,request.amount());
        transactionRepo.save(transaction);
        return transactionMapper.toDTO(transaction);
    }

    @Transactional
    @Override
    public TransactionDTO withdraw(WithdrawTransactionRequestDTO request) {
        productService.verifyAmountIsNotZero(request.amount());
        Product originProduct = productService.getProductEntityById(request.originProductId());
        productService.verifyProductIsActive(originProduct);
        productService.verifyFundsForTransaction(originProduct,request.amount());

        Transaction transaction = transactionMapper.toEntity(request,originProduct);
        transaction.setTransactionDate(LocalDateTime.now());

        productService.substractFromBalance(originProduct,request.amount());

        transactionRepo.save(transaction);
        return transactionMapper.toDTO(transaction);
    }
    @Transactional
    @Override
    public TransactionDTO transfer(TransferTransactionRequestDTO request) {
        BigDecimal amount = request.amount();

        productService.verifyAmountIsNotZero(amount);
        Product originProduct = productService.getProductEntityById(request.originProductId());
        Product destinyProduct = productService.getProductEntityById(request.destinyProductId());

        productService.verifyProductIsActive(originProduct);
        productService.verifyProductIsActive(destinyProduct);
        productService.verifyFundsForTransaction(originProduct, amount);
        productService.substractFromBalance(originProduct, amount);
        productService.addToBalance(destinyProduct, amount);

        Transaction transaction = transactionMapper.toEntity(request,originProduct,destinyProduct);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepo.save(transaction);
        return transactionMapper.toDTO(transaction);
    }


}
