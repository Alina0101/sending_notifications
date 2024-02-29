package com.example.logger;

import com.example.model.BankCard;
import com.example.model.Client;
import com.example.service.BankCardService;
import com.example.service.ClientService;

public class CacheLogger implements Runnable {
    private ClientService clientService;
    private BankCardService bankCardService;

    public CacheLogger(ClientService clientService, BankCardService bankCardService) {
        this.clientService = clientService;
        this.bankCardService = bankCardService;
    }

    @Override
    public void run() {
        while (true) {
            // Проверяем кеш клиентов
            System.out.println("---Клиенты в кеше:---");
            for (Client client : clientService.getClients().values()) {
                System.out.println("Name - " + client.getFullName());
            }

            // Проверяем кеш карт
            System.out.println("---Карты в кеше:---");
            for (BankCard bankCard : bankCardService.getBankCards().values()) {
                System.out.println("CardNumber - " + bankCard.getCardNumber());
            }

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

