package com.dan.bank_backend.clients.dtos;

import com.dan.bank_backend.clients.model.IdTypes;

import java.time.LocalDate;

public record ClientDTO(
    Long id,
    IdTypes identificationType,
    String identificationNumber,
    String firstname,
    String lastName,
    String email,
    LocalDate birthDate
){
}
