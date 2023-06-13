package Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/**
 * Date: June 7, 2023
 * <p>
 * Version 1.2 of the Menu Screen. 
 * Modified by Ethan Andrew: Added image to menu.
 * Provided by JELTech Industries.
 * </p>
 * 
 * <h2>
 * Class: ICS4UO with V. Krasteva
 * </h2>
 * @author Lexi Han 
 * @version 23.06.07
 */
public class Menu extends JPanel implements ActionListener {
	//Initializing variables that will help user navigate through screens ("cards")
	JButton levelSelectButton, creditsButton, quitButton;
	private CardLayout layoutManager = new CardLayout();
	
	/**
	 * Becomes true if user clicks on quit button. Used to assist Driver.
	 * Added on May 26, 2023 by Ethan Andrew
	 */
	boolean onQuitScreen = false;

	/**
	 * Becomes true if user clicks on credits button. Used to assist Driver.
	 * Added on May 28, 2023 by Ethan Andrew
	 */
	boolean onCredits = false;

	/**
	 * The font used on the buttons.
	 * Added on May 28, 2023 by Ethan Andrew
	 */
	
	private Font textFont;

	/**
	 * The image used in the menu. Added on June 7, 2023 by Ethan Andrew.
	 */
	private ImageIcon spiderLight;

	/**
	 * Constructor to initialize all variables. 
	 * @param cardLayout the layout that will help users switch screens ("cards") from this screen
	 */
	public Menu(CardLayout cardLayout) throws IOException, FontFormatException {
		layoutManager = cardLayout;

		spiderLight = new ImageIcon(getClass().getClassLoader().getResource("light_on_spider.png"));
		spiderLight = new ImageIcon(spiderLight.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH));
		
		textFont = Font.createFont(Font.TRUETYPE_FONT,getClass().getClassLoader().getResourceAsStream("PixeloidSans-mLxMm.ttf"));
		textFont = textFont.deriveFont(45f);
		
		//Sets layout to null for manual positioning. 
		setLayout(null);
		
		//Initializing Buttons and setting their locations.
		// May 28, 2023 - Ethan Andrew - Changed types to new custom buttons.
		levelSelectButton = new WhiteButton("Level Select");
		creditsButton = new WhiteButton("Credits");
		quitButton = new WhiteButton("Quit Game");
		
		levelSelectButton.setBounds(650, 30, 500, 200);
		creditsButton.setBounds(650, 285, 500, 200);
		quitButton.setBounds(650, 550, 500, 200);

		// May 28, 2023 - Ethan Andrew - Changed text to different colour and font.
		levelSelectButton.setForeground(Color.white);
		levelSelectButton.setFont(textFont);
		creditsButton.setForeground(Color.white);
		creditsButton.setFont(textFont);
		quitButton.setForeground(Color.white);
		quitButton.setFont(textFont);
		
		levelSelectButton.addActionListener(this);
		creditsButton.addActionListener(this);
		quitButton.addActionListener(this);
		
		add(levelSelectButton);
		add(creditsButton);
		add(quitButton);
		setVisible(true);
	}
	
	/**
	 * Overridden method to paint the window. 
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0,  0,  1200,  800);
		g.drawImage(spiderLight.getImage(), 0, 0, this);
	}
	
	/**
	 * Checks if the user is on the exit screen.
	 * Added on May 26, 2023 by Ethan Andrew
	 * @return True if the user is on the exit screen, false otherwise.
	 */
	public boolean getOnQuitScreen() {
		return onQuitScreen;
	}

	/**
	 * Checks if the user is on the credits screen.
	 * Added on May 28, 2023 by Ethan Andrew
	 * @return True if the user is on the credits screen, false otherwise.
	 */
	public boolean isOnCredits() {
		return onCredits;
	}

	/**
	 * Sets onCredits to true or false.
	 * Added on May 28, 2023 by Ethan Andrew
	 * @param onCredits The inputted value.
	 */
	public void setOnCredits(boolean onCredits) {
		this.onCredits = onCredits;
	}

	/**
	 * Overridden method to handle events for buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// May 26, 2023 - Ethan Andrew - modified first if statement to take user to the exit screen.
		if (e.getSource().equals(quitButton)) {
			onQuitScreen = true;
			layoutManager.show(getParent(), "exit screen");
		// May 28, 2023 - Ethan Andrew - Modified else-if statement to take user to the credits screen.
		} else if (e.getSource().equals(creditsButton)) {
			onCredits = true;
			layoutManager.show(getParent(), "credits screen");
		} else if (e.getSource().equals(levelSelectButton)) {
			layoutManager.show(getParent(), "level select");
		}
		
	}
}