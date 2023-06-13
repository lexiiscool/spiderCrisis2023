package Main;

import javax.swing.*;
import java.awt.*;

/**
 * Date: June 5, 2023
 * <p>
 *     Version 2.0 of the WhiteButton class. Provides custom button design.
 *     Class can create custom buttons with images now. Used to create level select buttons.
 *     Provided by JELTech Industries
 * </p>
 *
 * <h2>
 *     Class: ICS4UO with V. Krasteva
 * </h2>
 *
 * @author Ethan Andrew
 * @version 23.06.05
 */
public class WhiteButton extends JButton {
    /**
     * Colour for button background.
     */
    private Color color = new Color(0,0,0);

    /**
     * Colour for button border.
     */
    private Color borderColour = new Color(255, 255, 255);

    /**
     * Variable to control the radius of the round rectangle.
     */
    private int radius = 50;

    /**
     * Controls the thickness of the border.
     */
    private int borderThickness = 3;

    /**
     * Stores image used in the button. Added June 5, 2023.
     */
    private ImageIcon icon;

    /**
     * The constructor for the WhiteButton class. 
     * @param text The text to display on the button.
     */
    public WhiteButton(String text) {
        super(text);
        setContentAreaFilled(false);
    }

    /**
     * The constructor for the WhiteButton class.
     * @param text The text to display on the button.
     * @param icon The icon to be displayed on the button.
     */
    public WhiteButton(String text, ImageIcon icon) {
        super(text);
        this.icon = icon;
    }

    /**
     * Paints the customized button.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        setBackground(color);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColour);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(getBackground());
        g2.fillRoundRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2, radius, radius);

        if (icon != null) {
            g2.drawImage(icon.getImage(), 0, 0, this);
        }

        //Adjustments made by Lexi Han on May 30, 2023. Altered methods to display text.
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(getText()), th = fm.getHeight();
        int x = (getWidth() - tw )/ 2, y = (getHeight() + th/2)/2;
        g.setColor(Color.white);
        g2.drawString(getText(), x, y);
    }
}