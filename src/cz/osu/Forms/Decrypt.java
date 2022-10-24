package cz.osu.Forms;

import cz.osu.DecryptMessage;
import cz.osu.FileToOpen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class Decrypt extends JFrame {
    private JPanel mainPanel;
    private JPanel imagePanel;
    private JLabel image;
    private JPanel middlePanel;
    private JButton decryptButton;
    private JPanel bottomPanel;
    private JButton loadImageButton;
    private JTextArea decryptedMessageField;
    private JPasswordField passwordField;
    private JButton goBackButton;

    public Decrypt(String title) {
        super(title);

        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.image.setVisible(false);
        this.pack();

        FileToOpen file = new FileToOpen(imagePanel.getWidth(), imagePanel.getHeight());

        loadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                file.chooseImage();

                Decrypt.this.image.setIcon(file.scaleImage());
                validate();
                Decrypt.this.image.setVisible(true);
                decryptedMessageField.setText("");
                passwordField.setText("");
            }
        });
        //Tlačítko na dekrypci
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Kontrola, zda je soubor vybrán úspěšně
                if (file.isFileIsLoaded()) {
                    DecryptMessage decryptMessage = new DecryptMessage();

                    try {
                        String[] decryptedMessage = decryptMessage.decrypt(file);
                        //Kontrola, jestli je součástí zprávy heslo
                        if (!Objects.equals(decryptedMessage, null)) {
                            if (!Objects.equals(decryptedMessage[1], "")) {
                                if (Objects.equals(passwordField.getText(), decryptedMessage[1])) {
                                    decryptedMessageField.setText(decryptedMessage[0]);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Wrong password");
                                }
                            } else {
                                decryptedMessageField.setText(decryptedMessage[0]);
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else JOptionPane.showMessageDialog(null, "ERROR: Please choose image before encrypting");
            }
        });

        //Tlačítko na vrácení se zpátky
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                JFrame encryptionOrDecryption = new EncryptionOrDecryption("Choose usage");
                encryptionOrDecryption.setLocationRelativeTo(null);
                encryptionOrDecryption.setVisible(true);
                dispose();
            }
        });
    }
}
