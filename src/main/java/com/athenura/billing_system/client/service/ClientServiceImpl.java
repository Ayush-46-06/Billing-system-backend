package com.athenura.billing_system.client.service;

import com.athenura.billing_system.client.dto.ClientResponse;
import com.athenura.billing_system.client.dto.CreateClientRequest;
import com.athenura.billing_system.client.entity.Client;
import com.athenura.billing_system.client.exception.ClientAlreadyExistsException;
import com.athenura.billing_system.client.exception.ClientNotFoundException;
import com.athenura.billing_system.client.repository.ClientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;


    @Override
    public ClientResponse createClient(CreateClientRequest request) {

        if (clientRepository.findByEmail(request.email()).isPresent()) {
            throw new ClientAlreadyExistsException("Client already exists");
        }

        Client client = Client.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .gstNumber(request.gstNumber())
                .address(request.address())
                .createdAt(LocalDateTime.now())
                .build();

        Client saved = clientRepository.save(client);

        return mapToResponse(saved);
    }


    @Override
    public ClientResponse getClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        return mapToResponse(client);
    }

    @Override
    public List<ClientResponse> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client not found");
        }
        clientRepository.deleteById(id);
    }

    private ClientResponse mapToResponse(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getPhone(),
                client.getGstNumber(),
                client.getAddress(),
                client.getCreatedAt()
        );
    }


    @Override
    public ClientResponse updateClient(Long id, CreateClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        client.setName(request.name());
        client.setEmail(request.email());
        client.setPhone(request.phone());
        client.setGstNumber(request.gstNumber());
        client.setAddress(request.address());

        Client updated = clientRepository.save(client);
        return mapToResponse(updated);
    }

}