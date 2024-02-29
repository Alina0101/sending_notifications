package com.example.service;

import com.example.model.Client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientService {
    private Map<Long, Client> clients;
    private AtomicInteger idCounter;

    public ClientService() {
        clients = new HashMap<>();
        idCounter = new AtomicInteger(1);
    }

    public Map<Long, Client> getClients() {
        return this.clients;
    }

    public Client getClientById(long clientId) {
        return clients.get(clientId);
    }

    // Создание нового клиента
    public long createClient(String fullName, Date dateOfBirth, String email) {
        for (Map.Entry<Long, Client> entry : clients.entrySet()) {
            // если такой клиент уже есть, возвращаем его id
            if (entry.getValue().getFullName().equals(fullName) && entry.getValue().getDateOfBirth().equals(dateOfBirth)) {
                return entry.getKey();
            }
        }
        long new_id = idCounter.getAndIncrement();
        Client client = new Client(fullName, dateOfBirth, email);
        clients.put(new_id, client);
        return new_id;
    }
}