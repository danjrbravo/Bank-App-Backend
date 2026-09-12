package com.dan.bank_backend.clients.entity;

import com.dan.bank_backend.clients.model.IdTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "identification_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private IdTypes identificationType;
    @Column(name = "identification_number",nullable = false,unique = true)
    private String identificationNumber;
    @Column(name = "first_name",nullable = false)
    private String firstname;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email",nullable = false)
    private String email;
    @Column(name = "birth_date",nullable = false)
    private LocalDate birthDate;
    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updatedAt;
}
