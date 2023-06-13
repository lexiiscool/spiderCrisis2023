package Main;

import javax.swing.*;
import java.awt.*;

/**
 * Date: May 30, 2023
 * <p>
 *     Version 1.01 of the class used to display the credits screen. Added 3 new sources.
 *     Provided by JELTech Industries.
 * </p>
 *
 * <h2>
 *     Class: ICS4U0 with V. Krasteva
 * </h2>
 *
 * @author Ethan Andrew
 * @version 23.05.30
 */
public class CreditsScreen extends JComponent {
    /**
     * A smaller font used in the graphics.
     */
    private final Font SMALLTEXTFONT = new Font("serif", Font.PLAIN, 25);

    /**
     * A font used for the bibliography.
     */
    private final Font BIBFONT = new Font("serif", Font.PLAIN, 12);

    public CreditsScreen() {
    }

    /**
     * Paints the credits screen.
     * @param g the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 1200, 800);

        g.setFont(SMALLTEXTFONT);
        g.setColor(Color.white);
        g.drawString("Project Leader - Lexi Han", 50, 100);
        g.drawString("Project Members:", 50, 170);
        g.drawString("Jayden Xue", 50, 205);
        g.drawString("Ethan Andrew", 50, 240);
        g.drawString("Contractors:", 50, 310);
        g.drawString("Lexi Han", 50, 345);
        g.drawString("Ethan Andrew", 50, 380);
        g.drawString("Storyboard Designer - Ethan Andrew", 50, 450);
        g.drawString("Graphics Designer - Jayden Xue", 50, 520);
        g.drawString("Lead Programmer - Lexi Han", 50, 590);

        g.drawString("Assistant Programmers:", 625, 100);
        g.drawString("Jayden Xue", 625, 135);
        g.drawString("Ethan Andrew", 625, 170);

        g.drawString("Bibliography", 625, 240);
        g.setFont(BIBFONT);
        g.drawString("National Geographic. (n.d.). Spiders. National Geographic.", 625, 275);
        g.drawString("https://www.nationalgeographic.com/animals/invertebrates/facts/spiders?loggedin=true&rnd", 650, 295);
        g.drawString("=1684169950168", 650, 315);
        g.drawString("Culin, J., Levi, H. W., & Levi, L. R. (2023, March 31). Spider | Description, Behavior, Species,", 625, 355);
        g.drawString("Classification, & Facts. Britannica. https://www.britannica.com/animal/spider-arachnid", 650, 375);
        g.drawString("Andrade, M., Blagoev, G., Hubley, B., & Mason, T. (2012). (rep.). Spiders of Toronto.", 625, 415);
        g.drawString("City of Toronto. https://www.toronto.ca/wp-content/uploads/2017/08/8f2a-Biodiversity_Spi", 650, 435);
        g.drawString("derBook-Division-Planning-And-Development.pdf", 650, 455);
        g.drawString("kues1. (n.d.). Free photo view of white crumpled paper. freepik. Freepik Company.", 625, 495);
        g.drawString("Retrieved May 29, 2023, from https://www.freepik.com/free-photo/view-white-crumpled-pape", 650, 515);
        g.drawString("r_1038689.htm#query=wrinkled%20paper&amp;position=0&amp;from_view=keyword&amp;track=ais.", 650, 535);

        g.drawString("Click anywhere to return to the main menu.", 500, 700);
    }
}