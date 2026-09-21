package com.dan.bank_backend.clients.service;

import com.dan.bank_backend.clients.dtos.ClientDTO;
import com.dan.bank_backend.clients.dtos.CreateClientRequestDTO;
import com.dan.bank_backend.clients.dtos.UpdateClientRequestDTO;
import org.springframework.data.domain.Page;

public interface ClientService {
    Page<ClientDTO> getClients(int page, int size);
    ClientDTO getClientById(Long id);
    ClientDTO getClientByIdNum(String identification_number);
    ClientDTO createClient(CreateClientRequestDTO createClientDTO);
    ClientDTO updateClient(Long id, UpdateClientRequestDTO updateClientDTO);
    void deleteClient(Long id);
}
