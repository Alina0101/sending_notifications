package com.example.model;

import java.util.Date;

public class BankCard {
    private Client client;
    private String cardNumber;
    private Date issueDate; // дата выдачи
    private Date expirationDate; // дата окончания действия

    // Конструктор
    public BankCard(Client client, String cardNumber, Date issueDate, Date expirationDate) {
        this.client = client;
        this.cardNumber = cardNumber;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
    }

    // Геттеры и сеттеры
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
