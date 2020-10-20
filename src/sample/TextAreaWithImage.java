package sample;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TextAreaWithImage extends JTextArea {
    private Image image;
    public TextAreaWithImage(int x, int y, String path) {
        super(x, y);
        try {
            image = ImageIO.read(new File(path));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image,0,0,null);

        super.paintComponent(g);
        this. setBackground(new Color(1,1,1, (float) 0.01));
    }

}

