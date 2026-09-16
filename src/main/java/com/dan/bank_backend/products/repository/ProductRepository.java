package com.dan.bank_backend.products.repository;

import com.dan.bank_backend.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByClientId(Long clientId);
}
