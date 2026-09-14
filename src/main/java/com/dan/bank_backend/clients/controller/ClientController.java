package com.dan.bank_backend.clients.controller;

import com.dan.bank_backend.clients.dtos.ClientDTO;
import com.dan.bank_backend.clients.dtos.CreateClientRequestDTO;
import com.dan.bank_backend.clients.dtos.UpdateClientRequestDTO;
import com.dan.bank_backend.clients.service.imp.ClientServiceImp;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientServiceImp clientService;
    public ClientController(ClientServiceImp clientService){
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> getAll(
            @RequestParam(value="page",defaultValue = "0") int page,
            @RequestParam(value="size",defaultValue = "2") int size)
    {
        Page<ClientDTO> clientPage = clientService.getClients(page,size);
        return ResponseEntity.ok(clientPage);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(clientService.getClientById(id));
    }
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody CreateClientRequestDTO client){
        ClientDTO createdClient = clientService.createClient(client);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdClient);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody UpdateClientRequestDTO client){
        return ResponseEntity.ok(clientService.updateClient(id,client));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

}
