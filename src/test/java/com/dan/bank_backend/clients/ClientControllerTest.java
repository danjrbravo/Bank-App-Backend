package com.dan.bank_backend.clients;

import com.dan.bank_backend.clients.controller.ClientController;
import com.dan.bank_backend.clients.dtos.ClientDTO;
import com.dan.bank_backend.clients.dtos.CreateClientRequestDTO;
import com.dan.bank_backend.clients.dtos.UpdateClientRequestDTO;
import com.dan.bank_backend.clients.model.IdTypes;
import com.dan.bank_backend.clients.service.imp.ClientServiceImp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientServiceImp clientService;

    @InjectMocks
    private ClientController clientController;


    // =========================
    // GET /api/clients
    // =========================

    @Test
    void shouldGetAllClients() {

        ClientDTO clientDTO = new ClientDTO(
                1L,
                IdTypes.CEDULA_CIUDADANIA,
                "123456789",
                "Daniel",
                "Perez",
                "daniel@email.com",
                LocalDate.of(2000, 1, 1)
        );

        Page<ClientDTO> page =
                new PageImpl<>(
                        List.of(clientDTO),
                        PageRequest.of(0, 2),
                        1
                );

        when(clientService.getClients(0, 2))
                .thenReturn(page);


        ResponseEntity<Page<ClientDTO>> response =
                clientController.getAll(0, 2);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(clientDTO, response.getBody().getContent().get(0));

        verify(clientService).getClients(0, 2);
    }


    @Test
    void shouldGetAllClientsWithDifferentPagination() {

        Page<ClientDTO> page =
                new PageImpl<>(
                        List.of(),
                        PageRequest.of(2, 5),
                        0
                );

        when(clientService.getClients(2, 5))
                .thenReturn(page);


        ResponseEntity<Page<ClientDTO>> response =
                clientController.getAll(2, 5);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(clientService).getClients(2, 5);
    }


    // =========================
    // GET /api/clients/{id}
    // =========================

    @Test
    void shouldGetClientById() {

        ClientDTO clientDTO = new ClientDTO(
                1L,
                IdTypes.CEDULA_CIUDADANIA,
                "123456789",
                "Daniel",
                "Perez",
                "daniel@email.com",
                LocalDate.of(2000, 1, 1)
        );

        when(clientService.getClientById(1L))
                .thenReturn(clientDTO);


        ResponseEntity<ClientDTO> response =
                clientController.getById(1L);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(clientDTO, response.getBody());

        verify(clientService).getClientById(1L);
    }


    // =========================
    // POST /api/clients
    // =========================

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

        ClientDTO createdClient =
                new ClientDTO(
                        1L,
                        IdTypes.CEDULA_CIUDADANIA,
                        "123456789",
                        "Daniel",
                        "Perez",
                        "daniel@email.com",
                        LocalDate.of(2000, 1, 1)
                );

        when(clientService.createClient(request))
                .thenReturn(createdClient);


        ResponseEntity<ClientDTO> response =
                clientController.createClient(request);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdClient, response.getBody());

        verify(clientService).createClient(request);
    }


    // =========================
    // PUT /api/clients/{id}
    // =========================

    @Test
    void shouldUpdateClient() {

        UpdateClientRequestDTO request =
                new UpdateClientRequestDTO(
                        IdTypes.CEDULA_CIUDADANIA,
                        "987654321",
                        "Daniel",
                        "Gomez",
                        "nuevo@email.com"
                );

        ClientDTO updatedClient =
                new ClientDTO(
                        1L,
                        IdTypes.CEDULA_CIUDADANIA,
                        "987654321",
                        "Daniel",
                        "Gomez",
                        "nuevo@email.com",
                        LocalDate.of(2000, 1, 1)
                );

        when(clientService.updateClient(1L, request))
                .thenReturn(updatedClient);


        ResponseEntity<ClientDTO> response =
                clientController.updateClient(1L, request);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedClient, response.getBody());

        verify(clientService).updateClient(1L, request);
    }


    // =========================
    // DELETE /api/clients/{id}
    // =========================

    @Test
    void shouldDeleteClient() {

        doNothing()
                .when(clientService)
                .deleteClient(1L);


        ResponseEntity<Void> response =
                clientController.deleteClient(1L);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(clientService).deleteClient(1L);
    }
}