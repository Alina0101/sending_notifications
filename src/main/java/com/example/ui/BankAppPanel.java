package com.example.ui;

import com.example.service.BankCardService;
import com.example.service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankAppPanel extends JPanel {
    private ClientService clientService;
    private BankCardService bankCardService;

    private JTextField fullNameField;
    private JTextField dateOfBirthField;
    private JTextField emailField;

    public ClientService getClientService() {
        return clientService;
    }
    public BankCardService getBankCardService() {
        return bankCardService;
    }

    public BankAppPanel() {
        setLayout(new GridLayout(4, 2));

        clientService = new ClientService();
        bankCardService = new BankCardService();

        JLabel fullNameLabel = new JLabel("Введите ФИО:");
        fullNameField = new JTextField();
        add(fullNameLabel);
        add(fullNameField);

        JLabel dateOfBirthLabel = new JLabel("Введите дату рождения (dd-MM-yyyy):");
        dateOfBirthField = new JTextField();
        add(dateOfBirthLabel);
        add(dateOfBirthField);

        JLabel emailLabel = new JLabel("Введите Email:");
        emailField = new JTextField();
        add(emailLabel);
        add(emailField);

        JButton createButton = new JButton("Готово");
        createButton.addActionListener(e -> {
            String fullName = fullNameField.getText();
            String dateOfBirthStr = dateOfBirthField.getText().trim();
            String email = emailField.getText().trim();

            // Проверка email
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(null, "Неправильный формат адреса электронной почты.");
                emailField.setText("");
                return;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date dateOfBirth = dateFormat.parse(dateOfBirthStr);
                // Создаем клиента
                long clientId = clientService.createClient(fullName, dateOfBirth, email);
                // Создаем карту
                long cardId = generateBankCard(clientId);
                JOptionPane.showMessageDialog(null, "Успешно!\n" +
                        "Номер Вашей карты: " + bankCardService.getBankCards().get(cardId).getCardNumber() + "\n" +
                        "Дата окончания действия: " + bankCardService.getBankCards().get(cardId).getExpirationDate());

                // Очистка полей ввода
                fullNameField.setText("");
                dateOfBirthField.setText("");
                emailField.setText("");
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Неправильный ввод даты. Пожалуйста, введите дату в формате dd-MM-yyyy.");
            }
        });
        add(createButton);
    }

    private long generateBankCard(long clientId) {
        return bankCardService.createBankCard(clientService.getClientById(clientId), new Date());
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
