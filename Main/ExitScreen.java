package Main;

import java.awt.*;
import javax.swing.*;
import java.io.*;

/**
 * Date: May 26, 2023
 * <p>
 *     Version 1.0 of the class used to display the exit screen.
 *     Provided by JELTech Industries.
 * </p>
 *
 * <h2>
 *     Class: ICS4U0 with V. Krasteva
 * </h2>
 *
 * @author Ethan Andrew
 * @version 23.05.26
 */
public class ExitScreen extends JComponent {
    /**
     * A smaller font used in the graphics.
     */
    private Font smallTextFont;
    /**
     * The regular font used for the text.
     */
    private Font textFont;
    /**
     * A spider web image.
     */
    private final ImageIcon spiderWeb = new ImageIcon(getClass().getClassLoader().getResource("mainscreen.png"));

    /**
     * Constructor class to initialize exit screen.
     * @throws IOException
     * @throws FontFormatException
     */
    public ExitScreen() throws IOException, FontFormatException{
    	smallTextFont = Font.createFont(Font.TRUETYPE_FONT,getClass().getClassLoader().getResourceAsStream("PixeloidSans-mLxMm.ttf"));
    	textFont = smallTextFont.deriveFont(40f);
    	smallTextFont = smallTextFont.deriveFont(20f);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 1200, 800);

        g.drawImage(spiderWeb.getImage(), 50, 50, this);

        g.setFont(textFont);
        g.setColor(Color.white);
        g.drawString("Thank you for taking the", 600, 175);
        g.drawString("time to learn more about", 600, 235);
        g.drawString("spiders! If you have", 600,295);
        g.drawString("arachnophobia, we hope", 600, 355);
        g.drawString("this game helped with", 600, 415);
        g.drawString("your fears!", 600, 475);
        g.drawString("© JELTech Industries 2023", 600, 595);

        g.setFont(smallTextFont);
        g.drawString("Click anywhere to exit.", 50, 600);
    }
}