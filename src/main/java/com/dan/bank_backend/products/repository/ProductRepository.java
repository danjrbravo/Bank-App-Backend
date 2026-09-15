package com.dan.bank_backend.products.repository;

import com.dan.bank_backend.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByAccountNumber(String accountNumber);
}
