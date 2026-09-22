package com.dan.bank_backend.clients;

import com.dan.bank_backend.clients.dtos.ClientDTO;
import com.dan.bank_backend.clients.dtos.CreateClientRequestDTO;
import com.dan.bank_backend.clients.dtos.UpdateClientRequestDTO;
import com.dan.bank_backend.clients.entity.Client;
import com.dan.bank_backend.clients.exception.*;
import com.dan.bank_backend.clients.mapper.ClientMapper;
import com.dan.bank_backend.clients.repository.ClientRepository;
import com.dan.bank_backend.clients.model.IdTypes;
import com.dan.bank_backend.clients.service.imp.ClientServiceImp;
import com.dan.bank_backend.products.service.ProductService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepo;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ClientServiceImp clientService;


    // =========================
    // GET CLIENTS
    // =========================

    @Test
    void shouldGetClients() {

        Client client = new Client();

        ClientDTO clientDTO = new ClientDTO(
                1L,
                IdTypes.CEDULA_CIUDADANIA,
                "123456789",
                "Daniel",
                "Perez",
                "daniel@email.com",
                LocalDate.of(2000, 1, 1)
        );

        Page<Client> clientPage =
                new PageImpl<>(List.of(client));

        when(clientRepo.findAll(PageRequest.of(0, 2)))
                .thenReturn(clientPage);

        when(clientMapper.toDTO(client))
                .thenReturn(clientDTO);


        Page<ClientDTO> result =
                clientService.getClients(0, 2);


        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(clientDTO, result.getContent().get(0));

        verify(clientRepo).findAll(PageRequest.of(0, 2));
        verify(clientMapper).toDTO(client);
    }


    @Test
    void shouldUseDefaultPageWhenPageIsNegative() {

        Client client = new Client();

        Page<Client> clientPage =
                new PageImpl<>(List.of(client));

        when(clientRepo.findAll(PageRequest.of(0, 2)))
                .thenReturn(clientPage);

        when(clientMapper.toDTO(client))
                .thenReturn(new ClientDTO(
                        1L,
                        IdTypes.CEDULA_CIUDADANIA,
                        "123456789",
                        "Daniel",
                        "Perez",
                        "daniel@email.com",
                        LocalDate.of(2000, 1, 1)
                ));


        clientService.getClients(-1, 2);


        verify(clientRepo).findAll(PageRequest.of(0, 2));
    }


    @Test
    void shouldUseDefaultSizeWhenSizeIsNegative() {

        Page<Client> clientPage =
                new PageImpl<>(List.of());

        when(clientRepo.findAll(PageRequest.of(0, 10)))
                .thenReturn(clientPage);


        clientService.getClients(0, -1);


        verify(clientRepo).findAll(PageRequest.of(0, 10));
    }

    // GET CLIENT BY ID

    @Test
    void shouldGetClientById() {

        Client client = new Client();

        ClientDTO clientDTO = new ClientDTO(
                1L,
                IdTypes.CEDULA_CIUDADANIA,
                "123456789",
                "Daniel",
                "Perez",
                "daniel@email.com",
                LocalDate.of(2000, 1, 1)
        );

        when(clientRepo.findById(1L))
                .thenReturn(Optional.of(client));

        when(clientMapper.toDTO(client))
                .thenReturn(clientDTO);


        ClientDTO result =
                clientService.getClientById(1L);


        assertNotNull(result);
        assertEquals(clientDTO, result);

        verify(clientRepo).findById(1L);
        verify(clientMapper).toDTO(client);
    }


    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        when(clientRepo.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(
                ClientNotFoundException.class,
                () -> clientService.getClientById(1L)
        );


        verify(clientRepo).findById(1L);
        verify(clientMapper, never()).toDTO(any());
    }

    // GET CLIENT BY ID NUMBER

    @Test
    void shouldGetClientByIdentificationNumber() {

        Client client = new Client();

        ClientDTO clientDTO = new ClientDTO(
                1L,
                IdTypes.CEDULA_CIUDADANIA,
                "123456789",
                "Daniel",
                "Perez",
                "daniel@email.com",
                LocalDate.of(2000, 1, 1)
        );

        when(clientRepo.findByIdentificationNumber("123456789"))
                .thenReturn(Optional.of(client));

        when(clientMapper.toDTO(client))
                .thenReturn(clientDTO);


        ClientDTO result =
                clientService.getClientByIdNum("123456789");


        assertEquals(clientDTO, result);

        verify(clientRepo)
                .findByIdentificationNumber("123456789");

        verify(clientMapper)
                .toDTO(client);
    }


    @Test
    void shouldThrowExceptionWhenIdentificationNumberDoesNotExist() {

        when(clientRepo.findByIdentificationNumber("999999999"))
                .thenReturn(Optional.empty());


        assertThrows(
                ClientNotFoundByIdentificationNumberException.class,
                () -> clientService.getClientByIdNum("999999999")
        );


        verify(clientRepo)
                .findByIdentificationNumber("999999999");
    }

    // CREATE CLIENT

    @Test
    void shouldCreateClient() {

        CreateClientRequestDTO request =
                new CreateClientRequestDTO(
                        IdTypes.CEDULA_CIUDADANIA,
                        "123456789",
                        "Daniel",
                        "Perez",
                        "daniel@email.com",
                        LocalDate.of(2000, 1, 1)
                );

        Client client = new Client();

        ClientDTO expectedDTO =
                new ClientDTO(
                        1L,
                        IdTypes.CEDULA_CIUDADANIA,
                        "123456789",
                        "Daniel",
                        "Perez",
                        "daniel@email.com",
                        LocalDate.of(2000, 1, 1)
                );

        when(clientMapper.toClient(request))
                .thenReturn(client);

        when(clientRepo.save(client))
                .thenReturn(client);

        when(clientMapper.toDTO(client))
                .thenReturn(expectedDTO);


        ClientDTO result =
                clientService.createClient(request);


        assertNotNull(result);
        assertEquals(expectedDTO, result);

        verify(clientMapper).toClient(request);
        verify(clientRepo).save(client);
        verify(clientMapper).toDTO(client);
    }


    @Test
    void shouldNotCreateClientWhenBirthdayIsInFuture() {

        CreateClientRequestDTO request =
                new CreateClientRequestDTO(
                        IdTypes.CEDULA_CIUDADANIA,
                        "123456789",
                        "Daniel",
                        "Perez",
                        "daniel@email.com",
                        LocalDate.now().plusDays(1)
                );


        assertThrows(
                InvalidBirthdayException.class,
                () -> clientService.createClient(request)
        );


        verify(clientRepo, never()).save(any());
    }


    @Test
    void shouldNotCreateClientWhenClientIsUnderAge() {

        CreateClientRequestDTO request =
                new CreateClientRequestDTO(
                        IdTypes.CEDULA_CIUDADANIA,
                        "123456789",
                        "Daniel",
                        "Perez",
                        "daniel@email.com",
                        LocalDate.now().minusYears(17)
                );


        assertThrows(
                UnderAgeRestrictionException.class,
                () -> clientService.createClient(request)
        );


        verify(clientRepo, never()).save(any());
    }

    // UPDATE CLIENT

    @Test
    void shouldUpdateClient() {

        Client client = new Client();

        UpdateClientRequestDTO request =
                new UpdateClientRequestDTO(
                        IdTypes.CEDULA_CIUDADANIA,
                        "987654321",
                        "Daniel",
                        "Gomez",
                        "nuevo@email.com"
                );

        ClientDTO expectedDTO =
                new ClientDTO(
                        1L,
                        IdTypes.CEDULA_CIUDADANIA,
                        "987654321",
                        "Daniel",
                        "Gomez",
                        "nuevo@email.com",
                        LocalDate.of(2000, 1, 1)
                );

        when(clientRepo.findById(1L))
                .thenReturn(Optional.of(client));

        when(clientRepo.save(client))
                .thenReturn(client);

        when(clientMapper.toDTO(client))
                .thenReturn(expectedDTO);


        ClientDTO result =
                clientService.updateClient(1L, request);


        assertEquals(expectedDTO, result);

        verify(clientRepo).findById(1L);
        verify(clientRepo).save(client);
        verify(clientMapper).toDTO(client);
    }


    @Test
    void shouldNotUpdateClientWhenClientDoesNotExist() {

        UpdateClientRequestDTO request =
                new UpdateClientRequestDTO(
                        IdTypes.CEDULA_CIUDADANIA,
                        "987654321",
                        "Daniel",
                        "Gomez",
                        "nuevo@email.com"
                );

        when(clientRepo.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(
                ClientNotFoundException.class,
                () -> clientService.updateClient(1L, request)
        );


        verify(clientRepo, never()).save(any());
    }

    // DELETE CLIENT

    @Test
    void shouldDeleteClient() {

        when(clientRepo.existsById(1L))
                .thenReturn(true);

        when(productService.existsByClientId(1L))
                .thenReturn(false);


        clientService.deleteClient(1L);


        verify(clientRepo).existsById(1L);
        verify(productService).existsByClientId(1L);
        verify(clientRepo).deleteById(1L);
    }


    @Test
    void shouldNotDeleteClientWhenClientDoesNotExist() {

        when(clientRepo.existsById(1L))
                .thenReturn(false);


        assertThrows(
                ClientNotFoundException.class,
                () -> clientService.deleteClient(1L)
        );


        verify(clientRepo, never()).deleteById(any());
        verify(productService, never()).existsByClientId(any());
    }


    @Test
    void shouldNotDeleteClientWhenClientHasProducts() {

        when(clientRepo.existsById(1L))
                .thenReturn(true);

        when(productService.existsByClientId(1L))
                .thenReturn(true);


        assertThrows(
                ClientHasProductsException.class,
                () -> clientService.deleteClient(1L)
        );


        verify(clientRepo, never()).deleteById(any());
    }
}