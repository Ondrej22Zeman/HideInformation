package cz.osu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class EncryptMessage {
    private String message;
    private boolean passwordChosen = false;
    private String password;
    private FileToOpen file;
    private SaveLocation saveLocation;
    private final Charset charset = Charset.forName("iso-8859-2");
    private boolean containsAlpha;

    //Metoda, která převádí zprávu a heslo na binární dvojice, která jsou následně
    //ukryta do posledních 2 bitů každého barevného kanálu
    //Jedná se o metodu LSB - Least Significant Bit a pracuje na principu, kde
    //Lidské oko nerozpozná barevný rozdíl mezi například 255 červenou a 254
    public boolean encrypt() throws IOException {
        BufferedImage image = ImageIO.read(file.getFile());
        int rgb = image.getRGB(0, 0);

        ArrayList<String[]> convertedMessage = convertByteArraysToBinary(("OZ" + message).getBytes(charset));

        if (passwordChosen) {
            ArrayList<String[]> convertedPassword = convertByteArraysToBinary(password.getBytes(charset));
            convertedMessage.add(new String[]{"00", "00", "00", "00"});

            convertedMessage.addAll(convertedPassword);
        }
        convertedMessage.add(new String[]{"00", "00", "00", "01"});

        int blue;
        int green;
        int red;
        int alpha = 0;

        int hex = 0;
        int adjustedRGB;
        String pair;


        int letterCounter = 0;
        int pairCounter = 0;
        int channelCounter = 0;
        int currentHeight = 0;
        int currentWidth = 0;

        int counter = 0;

        //Zakódování 2 bitů z každého písmene(které tvoří 8 bitů) do posledních 2 bitů barevného kanálu každého pixelu
        while (true) {
            if (channelCounter == 3) {
                currentWidth++;
                if (currentWidth == image.getWidth()) {
                    currentWidth = 0;
                    currentHeight++;
                    if (currentHeight == image.getHeight()) {
                        JOptionPane.showMessageDialog(null, "Message too long, either choose larger image or shorter message");
                        break;
                    }
                }
                channelCounter = 0;
            }
            rgb = image.getRGB(currentWidth, currentHeight);
            blue = rgb & 0xFF;
            green = rgb >> 8 & 0xFF;
            red = rgb >> 16 & 0xFF;

            if (letterCounter == convertedMessage.size() - 1 && pairCounter == 4) {
                break;
            }

            if (pairCounter == 4) {
                pairCounter = 0;
                letterCounter++;
            }

            pair = convertedMessage.get(letterCounter)[pairCounter];
            pairCounter++;
            switch (pair) {
                case "00" -> hex = 0;
                case "01" -> hex = 1;
                case "10" -> hex = 2;
                case "11" -> hex = 3;
            }
            switch (channelCounter) {
                case 0 -> red = red & 0xFC ^ hex;
                case 1 -> green = green & 0xFC ^ hex;
                case 2 -> blue = blue & 0xFC ^ hex;
            }
            channelCounter++;

            adjustedRGB = red << 16 | green << 8 | blue;
            image.setRGB(currentWidth, currentHeight, adjustedRGB);
            counter++;
        }


        file.setFileExtension();
        saveLocation.copyImage(image, file.getFileExtension());
        return true;
    }

    //Převod bytu na 8 bitů
    public ArrayList<String[]> convertByteArraysToBinary(byte[] input) {

        StringBuilder result = new StringBuilder();
        ArrayList<String[]> list = new ArrayList<>();


        for (byte b : input) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                result.append((val & 128) == 0 ? 0 : 1);      // 128 = 1000 0000
                val <<= 1;
            }
            String[] pairArray = new String[4];

            int start = 0, end = 2;
            for (int i = 0; i < 4; i++) {
                String pair = (result.substring(start, end));
                pairArray[i] = pair;
                start += 2;
                end += 2;
            }
            list.add(pairArray);
            result.setLength(0);

        }

        return list;

    }

    public EncryptMessage(SaveLocation saveLocation, FileToOpen file, String message) {
        this.saveLocation = saveLocation;
        this.file = file;
        this.message = message;
    }

    public EncryptMessage(SaveLocation saveLocation, FileToOpen file, String message, String password) {
        this.saveLocation = saveLocation;
        this.file = file;
        this.message = message;
        this.password = password;
        passwordChosen = true;
    }
}
