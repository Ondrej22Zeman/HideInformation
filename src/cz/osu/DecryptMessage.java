package cz.osu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;

public class DecryptMessage {
    private final Charset charset = Charset.forName("iso-8859-2");
    ArrayList<String> messageLetters = new ArrayList<>();
    ArrayList<String> passwordLetters = new ArrayList<>();
    private final int[] bitValue = {128, 64, 32, 16, 8, 4, 2, 1};

    //Metoda, která převadí písmena z binárních hodnot na Stringy
    //Vrací 2rozměrné pole kde se nachází [zpráva,heslo]
    //Pokud nebylo přidáno heslo, je reprezentováno pomocí ""
    public String[] decrypt(FileToOpen file) throws IOException {
        StringBuilder message = new StringBuilder();
        StringBuilder password = new StringBuilder();
        String[] ret = new String[2];

        messageLetters.clear();
        passwordLetters.clear();

        BufferedImage image = ImageIO.read(file.getFile());

        if (getLetters(image)) {
            for (String let :
                    messageLetters) {
                byte b = (byte) calcValueFromBites(let);
                byte[] b1 = {b};
                String letter = new String(b1, charset);
                message.append(letter);
            }
            ret[0] = message.toString();


            if (!(passwordLetters.size() == 0)) {
                for (String pass :
                        passwordLetters) {
                    byte b = (byte) calcValueFromBites(pass);
                    byte[] b1 = {b};
                    String letter = new String(b1, charset);
                    password.append(letter);
                    ret[1] = password.toString();
                }
            }else{
                ret[1] = "";
            }
            return ret;
        }
        return ret = null;
    }

    //Metoda pro získání 4 binárních párů(8bitů), pro vytvoření písmene
    public boolean getLetters(BufferedImage image) {
        boolean decryptable = true;

        //Hodnoty červené, modré a zelené barvy slouží pro získání posledních 2 bitů
        //Specifické hodnoty udávají pozici daných 2 bitů v metodě image.getRGB(), která vrací 4 byty
        //Kde tři byty jsou barevné kanály
        int red = 17;
        int green = 9;
        int blue = 1;

        int chosenChannel = 0;
        int currentWidth = 0;
        int currentHeight = 0;
        int rgb;
        int letterCounter = 0;
        int channelCounter = 0;
        boolean isMessage = true;
        boolean isDone = false;
        StringBuilder letter = new StringBuilder();


        while (!isDone) {
            //Cyklus pro procházení pixely a jednotlivými kanály, jehož výstupem je
            //jedno písmeno, které představuje 8 bitů
            for (int pairCounter = 0; pairCounter < 4; pairCounter++) {
                if (channelCounter == 3) {
                    currentWidth++;
                    if (currentWidth == image.getWidth()) {
                        currentWidth = 0;
                        currentHeight++;
                    }
                    channelCounter = 0;
                }
                rgb = image.getRGB(currentWidth, currentHeight);


                switch (channelCounter) {
                    case 0 -> chosenChannel = red;
                    case 1 -> chosenChannel = green;
                    case 2 -> chosenChannel = blue;
                }
                channelCounter++;

                letter.append(get2BitsFromChannel(chosenChannel, rgb));
            }

            letterCounter++;
            //Kontrola, jestli se jedná o zašifrovaný obrázek.
            //Zašifrovaný obrázek obsahuje v prvních 8 barevných kanálech iniciály "OZ"
            if (letterCounter == 1) {
                if (Objects.equals(letter.toString(), "01001111")){
                    letter.setLength(0);
                    continue;
                }else {
                    JOptionPane.showMessageDialog(null, "This isn't encrypted image");
                    return false;
                }
            }
            if (letterCounter == 2) {
                if (Objects.equals(letter.toString(), "01011010")){
                    letter.setLength(0);
                    continue;
                }else {
                    JOptionPane.showMessageDialog(null, "This isn't encrypted image");
                    return false;
                }
            }

            //Kontrola, zda se jedná o heslo, či text zprávy
            //Text zprávy se nachází za iniciály a končí buď
            //"00000000" jestli obsahuje i heslo, nebo "00000001"
            //Jestli se jedná o celou zprávu

            if (isMessage) {
                if (letter.toString().equals("00000001")) {
                    isDone = true;
                } else if (letter.toString().equals("00000000")) {
                    isMessage = false;
                } else {
                    messageLetters.add(letter.toString());
                }
            } else {
                if (letter.toString().equals("00000001")) {
                    isDone = true;
                    break;
                } else {
                    passwordLetters.add(letter.toString());
                }
            }
            letter.setLength(0);
        }
        return decryptable;
    }

    //převod 8 bitů na jeden byte
    private int calcValueFromBites(String bites) {
        int ret = 0;
        int bitLength = bitValue.length - 1;

        for (int i = bites.length() - 1; i >= 0; i--) {
            int x = 0;
            if (bites.charAt(i) == '1') x = 1;
            ret += x * bitValue[bitLength];
            bitLength--;
        }
        return ret;
    }

    //Metoda pro získání posledních 2 bitů zvoleného barevného kanálu
    private String get2BitsFromChannel(int chosenChannel, int rgb) {
        StringBuilder letter = new StringBuilder();
        for (int j = chosenChannel; j >= (chosenChannel - 1); j--) {
            int mask = 1 << j;
            letter.append((rgb & mask) != 0 ? "1" : "0");
        }
        return letter.toString();
    }
}
