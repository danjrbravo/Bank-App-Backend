package com.dan.bank_backend.transactions.controller;

import com.dan.bank_backend.transactions.dtos.ConsignTransactionRequestDTO;
import com.dan.bank_backend.transactions.dtos.TransactionDTO;
import com.dan.bank_backend.transactions.dtos.TransferTransactionRequestDTO;
import com.dan.bank_backend.transactions.dtos.WithdrawTransactionRequestDTO;
import com.dan.bank_backend.transactions.service.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAll(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }
    @PostMapping("/consign")
    public ResponseEntity<TransactionDTO> consign(
            @Valid
            @RequestBody ConsignTransactionRequestDTO consignDTO){
        TransactionDTO transaction = transactionService.consign(consignDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDTO> withdraw(
            @Valid
            @RequestBody WithdrawTransactionRequestDTO withdrawDTO){
        TransactionDTO transaction = transactionService.withdraw(withdrawDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }
    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> transfer(
            @Valid
            @RequestBody TransferTransactionRequestDTO transferDTO){
        TransactionDTO transaction = transactionService.transfer(transferDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }
}
