package com.example.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BankAppSwing extends JFrame {
    private BankAppPanel bankAppPanel;

    public BankAppSwing() {
        setTitle("Рассылка от банка");
        setSize(600, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // загрузка изображения для иконки
        BufferedImage iconImage = null;
        try {
            iconImage = ImageIO.read(new File("assets/icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (iconImage != null) {
            setIconImage(iconImage);
        }

        bankAppPanel = new BankAppPanel();
        add(bankAppPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public BankAppPanel getBankAppPanel(){
        return this.bankAppPanel;
    }
}
