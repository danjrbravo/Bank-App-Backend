package com.dan.bank_backend.clients.service.imp;

import com.dan.bank_backend.clients.dtos.ClientDTO;
import com.dan.bank_backend.clients.dtos.CreateClientRequestDTO;
import com.dan.bank_backend.clients.dtos.UpdateClientRequestDTO;
import com.dan.bank_backend.clients.entity.Client;
import com.dan.bank_backend.clients.exception.*;
import com.dan.bank_backend.clients.mapper.ClientMapper;
import com.dan.bank_backend.clients.repository.ClientRepository;
import com.dan.bank_backend.clients.service.ClientService;
import com.dan.bank_backend.products.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Service
public class ClientServiceImp implements ClientService {
    private final ClientRepository clientRepo;
    private final ProductService productService;
    private final ClientMapper clientMapper;
    public ClientServiceImp(ClientRepository clientRepo,ClientMapper clientMapper,ProductService productService){
        this.clientRepo = clientRepo;
        this.productService = productService;
        this.clientMapper = clientMapper;
    }

    @Override
    public Page<ClientDTO> getClients(int page, int size) {
        if(page < 0){
            page = 0;
        }
        if(size < 0){
            size = 10;
        }
        Pageable pg = PageRequest.of(page, size);
        return clientRepo.findAll(pg).map(clientMapper::toDTO);
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return clientMapper.toDTO(client);
    }

    @Override
    public ClientDTO getClientByIdNum(String identification_number) {
        Client client = clientRepo.findByIdentificationNumber(identification_number)
                .orElseThrow(()-> new ClientNotFoundByIdentificationNumberException(identification_number));
        return clientMapper.toDTO(client);
    }

    @Override
    public ClientDTO createClient(CreateClientRequestDTO createClientDTO) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(createClientDTO.birthDate(),today);
        if(createClientDTO.birthDate().isAfter(today)){
            throw new InvalidBirthdayException();
        }
        if(period.getYears() < 18){
            throw new UnderAgeRestrictionException();
        }
        Client client = clientMapper.toClient(createClientDTO);
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
        clientRepo.save(client);
        return clientMapper.toDTO(client);
    }

    @Override
    public ClientDTO updateClient(Long id, UpdateClientRequestDTO updateClientDTO) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        client.setIdentificationType(updateClientDTO.identificationType());
        client.setIdentificationNumber(updateClientDTO.identificationNumber());
        client.setFirstname(updateClientDTO.firstname());
        client.setLastName(updateClientDTO.lastName());
        client.setEmail(updateClientDTO.email());
        client.setUpdatedAt(LocalDateTime.now());
        Client updatedClient = clientRepo.save(client);
        return clientMapper.toDTO(updatedClient);
    }


    @Override
    public void deleteClient(Long id) {
        if(!clientRepo.existsById(id)){
            throw new ClientNotFoundException(id);
        }
        if(productService.existsByClientId(id)){
            throw new ClientHasProductsException(id);
        }
        clientRepo.deleteById(id);
    }
}
