package com.example.service;

import com.example.model.BankCard;
import com.example.model.Client;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BankCardService {
    private Map<Long, BankCard> bankCards;
    private AtomicInteger idCounter;

    public BankCardService() {
        bankCards = new HashMap<>();
        idCounter = new AtomicInteger(1);
    }

    public Map<Long, BankCard> getBankCards() {
        return this.bankCards;
    }

    // Создание новой карты
    public long createBankCard(Client client, Date issueDate) {
        String cardNumber = generateCardNumber();
        if (isCardNumberUnique(cardNumber)) {
            Date expirationDate = calculateExpirationDate(issueDate);
            BankCard bankCard = new BankCard(client, cardNumber, issueDate, expirationDate);
            long cardId = idCounter.getAndIncrement();
            bankCards.put(cardId, bankCard);
            return cardId;
        } else {
            throw new IllegalArgumentException("Номер карты не уникален!");
        }
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int digit = (int) (Math.random() * 10);
            cardNumber.append(digit);

            if ((i + 1) % 4 == 0 && i < 15) {
                cardNumber.append("-");
            }
        }
        return cardNumber.toString();
    }

    // создание обновленной карты при окончании срока действия старой (id сохраняется)
    public BankCard createUpdatedCard(BankCard oldCard) {
        String cardNumber = generateCardNumber();
        if (isCardNumberUnique(cardNumber)) {
            var newCard = new BankCard(oldCard.getClient(), cardNumber, new Date(), calculateExpirationDate(oldCard.getExpirationDate()));
            long cardId = getCardIdByCardNumber(oldCard.getCardNumber());
            cancelBankCard(oldCard);
            bankCards.put(cardId, newCard);
            return newCard;
        } else {
            throw new IllegalArgumentException("Номер карты не уникален!!!");
        }
    }

    // срок действия карты 3 года (для проверки можно возвращать текущую дату, тогда уведомление придет сразу)
    private Date calculateExpirationDate(Date issueDate) {
        LocalDate localIssueDate = issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate expirationDate = localIssueDate.plusYears(3);
        return Date.from(expirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        //return new Date();
    }

    // проверка уникальности номера карты
    private boolean isCardNumberUnique(String cardNumber) {
        for (BankCard card : bankCards.values()) {
            if (card.getCardNumber().equals(cardNumber)) {
                return false;
            }
        }
        return true;
    }

    // получение id по номеру карты для создания обновленной карты
    private Long getCardIdByCardNumber(String cardNumber) {
        for (Map.Entry<Long, BankCard> entry : bankCards.entrySet()) {
            if (entry.getValue().getCardNumber().equals(cardNumber)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // закрытие карты
    private void cancelBankCard(BankCard card) {
        bankCards.values().remove(card);
    }
}
