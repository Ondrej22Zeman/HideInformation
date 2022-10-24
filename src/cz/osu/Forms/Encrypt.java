package cz.osu.Forms;

import cz.osu.FileToOpen;
import cz.osu.EncryptMessage;
import cz.osu.SaveLocation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class Encrypt extends JFrame {
    private JPanel mainPanel;
    private JTextField messageToEncrypt;
    private JLabel image;
    private JPanel imagePanel;
    private JPanel loadImagePanel;
    private JButton encryptButton;
    private JButton loadImageButton;
    private JTextField saveLocationField;
    private JButton chooseSaveLocationButton;
    private JPanel detailsPanel;
    private JPasswordField passwordField;
    private JButton goBackButton;

    public Encrypt(String title) {
        super(title);

        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.image.setVisible(false);
        this.pack();

        FileToOpen file = new FileToOpen(imagePanel.getWidth(), imagePanel.getHeight());
        SaveLocation saveLocation = new SaveLocation();

        //Tlačítko pro načtení obrázku
        loadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                file.chooseImage();
                //Vykreslení obrázku do aplikace
                Encrypt.this.image.setIcon(file.scaleImage());
                validate();
                Encrypt.this.image.setVisible(true);
            }
        });

        //Tlačítko pro výběr místa uložení a názvu souboru
        chooseSaveLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Kontrola, jestli byl vybrán soubor pro vytvoření kopie
                if (file.isFileIsLoaded()) {
                    saveLocation.chooseSaveLocation();
                    if (saveLocation.isChosen()) {
                        //Vytvoření kopie, která obsahuje zvolený název se stejnou příponou jako originál
                        if (!saveLocation.containsExtension()) {
                            saveLocation.setLocationWithExtension(saveLocation.getLocation() + ".png");
                        }else{
                            saveLocation.setLocationWithExtension(saveLocation.getLocation());
                        }
                        //Zobrazení cesty uložení do textového pole v aplikaci
                        saveLocationField.setText(saveLocation.getLocationWithExtension());
                    } else JOptionPane.showMessageDialog(null, "ERROR: Please choose save location");
                } else JOptionPane.showMessageDialog(null, "ERROR: Please choose image");
            }
        });

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Kontrola, jestli byl vybrán obrázek
                if (file.isFileIsLoaded()) {

                    //Kontrola, jestli Uživatel zadal text
                    if (!Objects.equals(messageToEncrypt.getText(), "")) {

                        //Kontrola, jestli uživatel vybral místo pro uložení
                        if (saveLocation.isChosen()) {
                            EncryptMessage encryptMessage;
                            double totalLength = messageToEncrypt.getText().length() + 3;

                            //Kontrola, jestli uživatel vložil heslo. Jestli ano, vloží se za zprávu
                            if (Objects.equals(passwordField.getText(), "")) {
                                encryptMessage = new EncryptMessage(
                                        saveLocation, file, messageToEncrypt.getText()
                                );
                            } else {
                                encryptMessage = new EncryptMessage(
                                        saveLocation, file, messageToEncrypt.getText(), passwordField.getText()
                                );
                                totalLength += passwordField.getText().length() + 1;
                            }

                            //Kontrola, zda se text vejde do obrázku
                            int pixelsNeeded = (int) Math.ceil((totalLength * 4) / 3);
                            if (pixelsNeeded > (file.getHeight() * file.getWidth())) {
                                JOptionPane.showMessageDialog(null, "ERROR: Please choose larger file, OR shorter message");
                            } else {
                                try {
                                    //Dialog informující uživatele o enkrypci
                                    if (encryptMessage.encrypt()) {
                                        JOptionPane.showMessageDialog(null, "Encrypted");
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }

                        } else JOptionPane.showMessageDialog(null, "ERROR: Please choose save location");
                    } else JOptionPane.showMessageDialog(null, "ERROR: Message is empty");
                } else JOptionPane.showMessageDialog(null, "ERROR: Please choose image before encrypting");
            }
        });
        //Tlačítko zpět
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
