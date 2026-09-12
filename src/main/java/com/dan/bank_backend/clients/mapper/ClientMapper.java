package com.dan.bank_backend.clients.mapper;

import com.dan.bank_backend.clients.dtos.ClientDTO;
import com.dan.bank_backend.clients.dtos.CreateClientRequestDTO;
import com.dan.bank_backend.clients.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public ClientDTO toDTO(Client client){
        return new ClientDTO(
                client.getId(),
                client.getIdentificationType(),
                client.getIdentificationNumber(),
                client.getFirstname(),
                client.getLastName(),
                client.getEmail(),
                client.getBirthDate()
        );
    }
    public Client toClient(CreateClientRequestDTO dto){
        Client client = new Client();
        client.setIdentificationType(dto.identificationType());
        client.setIdentificationNumber(dto.identificationNumber());
        client.setFirstname(dto.firstname());
        client.setLastName(dto.lastName());
        client.setEmail(dto.email());
        client.setBirthDate(dto.birthDate());
        return client;
    }
}
