package com.dan.bank_backend.products.entity;

import com.dan.bank_backend.clients.entity.Client;
import com.dan.bank_backend.products.model.AccountState;
import com.dan.bank_backend.products.model.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @Column(nullable = false,unique = true)
    private String accountNumber;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountState productState;
    @Column(nullable = false)
    private BigDecimal balance;
    @Column(name = "gmf_exempt")
    private boolean gmfExempt;
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "client_id",nullable = false)
    private Client client;
}
