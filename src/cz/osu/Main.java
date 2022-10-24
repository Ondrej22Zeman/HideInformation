package cz.osu;

import cz.osu.Forms.EncryptionOrDecryption;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {


        JFrame frame = new EncryptionOrDecryption("Choose usage");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
