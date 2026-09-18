package com.dan.bank_backend.transactions.entity;

import com.dan.bank_backend.products.entity.Product;
import com.dan.bank_backend.transactions.Model.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private TransactionType transactionType;
    @Column(nullable = false)
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "origin_product_id",
                foreignKey = @ForeignKey(name = "fk_origin_id"),
                nullable = false)
    private Product originProduct;
    @ManyToOne
    @JoinColumn(name = "destiny_product_id",
                foreignKey = @ForeignKey(name = "fk_destiny_id"),
                nullable = false)
    private Product destinyProduct;
    private LocalDateTime transactionDate;
}

