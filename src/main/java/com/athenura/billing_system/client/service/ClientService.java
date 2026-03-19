package com.athenura.billing_system.client.service;

import com.athenura.billing_system.client.dto.CreateClientRequest;
import com.athenura.billing_system.client.dto.ClientResponse;

import java.util.List;

public interface ClientService {

    ClientResponse createClient(CreateClientRequest request);

    ClientResponse getClient(Long id);

    List<ClientResponse> getAllClients();

    ClientResponse updateClient(Long id, CreateClientRequest request);

    void deleteClient(Long id);
}