package cz.osu.Forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EncryptionOrDecryption extends JFrame {
    private JPanel mainPanel;
    private JPasswordField passwordField;
    private JButton decryption;
    private JButton encryption;
    private JPanel topPanel;

    public EncryptionOrDecryption(String title) {
        super(title);

        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();

        //Tlačítko pro enkrypci
        encryption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                JFrame encrypt = new Encrypt("Encrypt");
                encrypt.setLocationRelativeTo(null);
                encrypt.setVisible(true);
                dispose();
            }
        });
        //Tlačítko pro dekrypci
        decryption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                JFrame decrypt = new Decrypt("Decrypt");
                decrypt.setLocationRelativeTo(null);
                decrypt.setVisible(true);
                dispose();
            }
        });
    }
}
