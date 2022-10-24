package cz.osu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class SaveLocation {
    private String location;
    private String locationWithExtension;
    private boolean isChosen = false;
    private boolean containsExtension = false;

    //
    public boolean containsExtension() {
        return containsExtension;
    }

    //Zobrazení okna pro výběr místa pro uložení obrázku
    public void chooseSaveLocation() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        JFrame parentFrame = new JFrame();

        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            isChosen = true;
            File fileToSave = fileChooser.getSelectedFile();
            location = fileToSave.getAbsolutePath();
            if (location.contains(".")) {
                containsExtension = true;
            }
        }
    }

    //Vytvoření upravené kopie obrázku
    public void copyImage(BufferedImage image, String extension) throws IOException {
        File newFile = new File(locationWithExtension);
        ImageIO.write(image, "png", newFile);
    }

    public String getLocationWithExtension() {
        return locationWithExtension;
    }

    public void setLocationWithExtension(String locationWithExtension) {
        this.locationWithExtension = locationWithExtension;
    }

    public SaveLocation() {
    }

    public boolean isChosen() {
        return isChosen;
    }

    public String getLocation() {
        return location;
    }
}
