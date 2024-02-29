package com.example.service;

import com.example.model.Client;
import org.junit.jupiter.api.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        clientService = new ClientService();
    }

    @Test
    void testCreateClient() {
        String fullName = "Леонардо Ди Каприо";
        Date dateOfBirth = new Date();
        String email = "leo@example.com";

        long clientId = clientService.createClient(fullName, dateOfBirth, email);

        assertTrue(clientService.getClients().containsKey(clientId));
        assertEquals(fullName, clientService.getClients().get(clientId).getFullName());
        assertEquals(dateOfBirth, clientService.getClients().get(clientId).getDateOfBirth());
        assertEquals(email, clientService.getClients().get(clientId).getEmail());
    }

    @Test
    void testGetClientById() {
        String fullName = "Леонардо Ди Каприо";
        Date dateOfBirth = new Date();
        String email = "leo@example.com";

        long clientId = clientService.createClient(fullName, dateOfBirth, email);

        Client client = clientService.getClientById(clientId);

        assertNotNull(client);
        assertEquals(fullName, client.getFullName());
        assertEquals(dateOfBirth, client.getDateOfBirth());
        assertEquals(email, client.getEmail());
    }
}