package com.athenura.billing_system.client.controller;

import com.athenura.billing_system.client.dto.CreateClientRequest;
import com.athenura.billing_system.client.dto.ClientResponse;
import com.athenura.billing_system.client.service.ClientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/create")
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody CreateClientRequest request) {

        ClientResponse response = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET CLIENT BY ID
    @GetMapping("/fetch/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable Long id) {

        ClientResponse response = clientService.getClient(id);
        return ResponseEntity.ok(response);
    }

    // GET ALL CLIENTS
    @GetMapping("/fetch")
    public ResponseEntity<List<ClientResponse>> getAllClients() {

        List<ClientResponse> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    // DELETE CLIENT
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id,
            @Valid @RequestBody CreateClientRequest request) {

        ClientResponse response = clientService.updateClient(id, request);
        return ResponseEntity.ok(response);
    }

}