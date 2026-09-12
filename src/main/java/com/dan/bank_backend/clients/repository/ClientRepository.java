package com.dan.bank_backend.clients.repository;

import com.dan.bank_backend.clients.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findByIdentificationNumber(String identificationNumber);
}
