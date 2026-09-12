package com.dan.bank_backend.clients.dtos;

import com.dan.bank_backend.clients.model.IdTypes;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.time.LocalDate;

public record CreateClientRequestDTO(

    @NotNull
    IdTypes identificationType,
    @NotBlank
    String identificationNumber,
    @NotBlank
    @Size(min = 2,message = "El nombre debe tener almenos 2 caracteres")
    String firstname,
    @NotBlank
    @Size(min = 2,message = "El apellido debe tener almenos 2 caracteres")
    String lastName,
    @NotBlank
    @Email(message = "El correo electronico no tiene un formato valido")
    String email,
    @NotNull
    LocalDate birthDate
){
}
