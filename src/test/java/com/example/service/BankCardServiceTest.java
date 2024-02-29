package com.example.service;

import com.example.model.BankCard;
import com.example.model.Client;
import org.junit.jupiter.api.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BankCardServiceTest {
    private BankCardService bankCardService;

    @BeforeEach
    void setUp() {
        bankCardService = new BankCardService();
    }

    @Test
    void testCreateBankCard() {
        String fullName = "Леонардо Ди Каприо";
        Date dateOfBirth = new Date();
        String email = "leo@example.com";

        Client client = new Client(fullName, dateOfBirth, email);
        Date issueDate = new Date();

        long cardId = bankCardService.createBankCard(client, issueDate);
        BankCard bankCard = bankCardService.getBankCards().get(cardId);

        assertNotNull(bankCard);
        assertEquals(client, bankCard.getClient());
        assertNotNull(bankCard.getCardNumber());
        assertEquals(issueDate, bankCard.getIssueDate());
        assertNotNull(bankCard.getExpirationDate());
    }

    @Test
    void testCreateUpdatedCard() {
        String fullName = "Леонардо Ди Каприо";
        Date dateOfBirth = new Date();
        String email = "leo@example.com";

        Client client = new Client(fullName, dateOfBirth, email);
        Date issueDate = new Date();

        long cardId = bankCardService.createBankCard(client, issueDate);
        BankCard oldCard = bankCardService.getBankCards().get(cardId);

        BankCard newCard = bankCardService.createUpdatedCard(oldCard);

        assertNotNull(newCard);
        assertEquals(client, newCard.getClient());
        assertNotNull(newCard.getCardNumber());
        assertNotNull(newCard.getIssueDate());
        assertNotNull(newCard.getExpirationDate());
    }
}