package com.dan.bank_backend.transactions.repository;

import com.dan.bank_backend.transactions.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

}
