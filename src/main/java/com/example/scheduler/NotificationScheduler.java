package com.example.scheduler;

import com.example.model.BankCard;
import com.example.model.Client;
import com.example.service.BankCardService;
import com.example.ui.BankAppSwing;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class NotificationScheduler implements Runnable {
    private BankCardService bankCardService;
    private BankAppSwing bankAppSwing;

    public NotificationScheduler(BankCardService bankCardService, BankAppSwing bankAppSwing) {
        this.bankCardService = bankCardService;
        this.bankAppSwing = bankAppSwing;
    }

    @Override
    public void run() {
        while (bankAppSwing.isVisible()) {
            Map<Long, BankCard> bankCardsCopy = new HashMap<>(bankCardService.getBankCards());
            Date currentDate = new Date();
            for (Map.Entry<Long, BankCard> entry : bankCardsCopy.entrySet()) {
                BankCard bankCard = entry.getValue();
                if (currentDate.after(bankCard.getExpirationDate())) {
                    // Создание новой карты клиенту
                    BankCard newCard = bankCardService.createUpdatedCard(bankCard);
                    // Отправление уведомления клиенту о завершении срока действия карты
                    sendEmailNotification(bankCard.getClient(), bankCard, newCard);
                }
            }
            try {
                Thread.sleep(300000); // каждые 5 минут
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendEmailNotification(Client client, BankCard oldCard, BankCard newCard) {
        // Загрузка файла конфигурации
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String from = properties.getProperty("email");
        String password = properties.getProperty("password");
        String host = "smtp.gmail.com";

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        // Аутентификация на почтовом сервере
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        session.setDebug(true);
        try {
            // Создание сообщения
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(client.getEmail()));
            message.setSubject("Уведомление о завершении срока действия карты.");
            message.setText("Уважаемый клиент, у Вашей карты с номером " + oldCard.getCardNumber() + " истекает срок действия. " +
                    "\nДля Вас автоматически была сгенерирована новая карта. Обновленные данные: " +
                    "\nНомер карты: " + newCard.getCardNumber() +
                    "\nДата окончания срока действия: " + newCard.getExpirationDate() +
                    "\n Спасибо, что остаетесь с нами!");

            // Отправка сообщения
            Transport.send(message);
            System.out.println("Уведомление отправлено на адрес: " + client.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
