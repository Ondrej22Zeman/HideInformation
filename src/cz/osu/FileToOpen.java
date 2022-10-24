package cz.osu;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileToOpen {
    private File file;
    private int width;
    private int height;
    private int panelWidth;
    private int panelHeight;
    private boolean fileIsLoaded;
    private String fileExtension;

    //Získání přípony soubory za účelem vytvoření kopie
    public void setFileExtension() {
        String path = file.getAbsolutePath();
        int counter = 0;
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '.') {
                counter++;
            }
        }
        if (counter == 1) {
            String extension = null;
            int i = path.lastIndexOf('.');
            if (i > 0) {
                extension = path.substring(i + 1);
            }
            fileExtension = extension;
        }else{
            fileExtension = "";
        }
    }

    public void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        boolean isImage = false;
        fileIsLoaded = false;

        //Při výběru jiného formátu než je formát obrázku, bude požadovat nový soubor
        while (!isImage) {

            //výběr místa obrázku
            int response = fileChooser.showOpenDialog(null);

            //Při kliknutí na "cancel" se dialog vypne
            if (response == JFileChooser.CANCEL_OPTION) {
                break;
            }

            //Ověření zda byl obrázek úspěšně vybrán
            if (response == JFileChooser.APPROVE_OPTION) {
                // Získání cesty k obrázku
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                setFileExtension();
                String mimetype = null;
                try {
                    mimetype = Files.probeContentType(file.toPath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //Ověřuje, zda byl otevřen obrázek
                if (mimetype != null && mimetype.split("/")[0].equals("image")) {
                    isImage = true;
                    fileIsLoaded = true;

                } else {
                    JOptionPane.showMessageDialog(null, "Please, choose image");
                    fileExtension = null;
                }
            }
        }
    }

    //Scalování obrázku a zachování poměru stran, pro zobrazení v aplikaci
    public ImageIcon scaleImage() {
        ImageIcon imageIcon = new ImageIcon(String.valueOf(this.file));
        Image image = imageIcon.getImage();

        width = image.getWidth(null);
        height = image.getHeight(null);

        double ratioX = (double) panelWidth / (double) width;
        double ratioY = (double) panelHeight / (double) height;
        double ratio = Math.min(ratioX, ratioY);

        int newWidth = (int) (width * ratio);
        int newHeight = (int) (height * ratio);

        Image newimg = image.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);

        imageIcon = new ImageIcon(newimg);

        return imageIcon;
    }

    public FileToOpen(int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public File getFile() {
        return file;
    }

    public boolean isFileIsLoaded() {
        return fileIsLoaded;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

;

