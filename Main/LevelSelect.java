package Main;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

/**
 * Date: June 7, 2023
 * <p> 
 * Version 2.2 of the class to display the "Level Selection Screen." Graphics mostly implemented.
 * Added custom image.
 * Provided by JELTech Industries. 
 * </p>
 * 
 * <h2>
 * Class: ICS4U0 with V. Krasteva
 * </h2>
 * 
 * @author Lexi Han
 * @version 23.06.07
 */

public class LevelSelect extends JComponent implements ActionListener{
	private CardLayout layoutManager;
	private JButton learning, testing, action, returnToMenu; 
	private LearningStage learningManager;
	private TestingStage testingManager;

	/**
	 * The font used on the buttons.
	 * Added on May 28, 2023 by Ethan Andrew
	 */
	private Font textFont;
	
	/**
	 * Font used on error Dialogs.
	 */
	private Font smallFont;
	/**
	 * Stores the image for the learning stage button.
	 * Added on June 5, 2023 by Ethan Andrew.
	 */
	private ImageIcon spiderJar = new ImageIcon(getClass().getClassLoader().getResource("learning_jar.png"));
	/**
	 * Stores the image for the testing stage button.
	 * Added on June 5, 2023 by Ethan Andrew.
	 */
	private ImageIcon spiderTest = new ImageIcon(getClass().getClassLoader().getResource("test-menu-spider.png"));
	/**
	 * Stores the image for the action stage button.
	 * Added on June 5, 2023 by Ethan Andrew.
	 */
	private ImageIcon spiderAction = new ImageIcon(getClass().getClassLoader().getResource("action-menu-spider.png"));
	/**
	 * The image used in the menu. Added on June 7, 2023 by Ethan Andrew.
	 */
	private ImageIcon spiderLight;

	/**
	 * Constructor for Level Selection
	 * @param cl CardLayout to help user switch screens.
	 * @param l LearningStage object. 
	 * @param t TestingStage object.
	 * @throws IOException
	 * @throws FontFormatException
	 */
	public LevelSelect(CardLayout cl, LearningStage l, TestingStage t) throws IOException, FontFormatException {
		//Parameters similar to LearningStage (ie. TestingStage and ActionStage objects) will be added upon creation.
		learningManager = l;
		testingManager = t;
		layoutManager = cl;

		spiderLight = new ImageIcon(getClass().getClassLoader().getResource("light_on_spider.png"));
		spiderLight = new ImageIcon(spiderLight.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH));
		
		textFont = Font.createFont(Font.TRUETYPE_FONT,getClass().getClassLoader().getResourceAsStream("PixeloidSans-mLxMm.ttf"));
		textFont = textFont.deriveFont(45f);
		smallFont = textFont.deriveFont(14f);
		
		// June 5, 2023 - Ethan Andrew - Initializes custom image buttons.
		learning = new WhiteButton("Learning Stage", spiderJar);
		testing = new WhiteButton("Testing Stage", spiderTest);
		action = new WhiteButton("Action Stage", spiderAction);
		returnToMenu = new WhiteButton("Return to menu");
		
		setLayout(null);
		
		learning.setBounds(650, 30, 500, 200);
		testing.setBounds(650, 285, 500, 200);
		action.setBounds(650, 550, 500, 200);
		returnToMenu.setBounds(100, 550, 400, 200);

		// May 28, 2023 - Ethan Andrew - Changed text to different colour and font.
		learning.setFont(textFont);
		testing.setFont(textFont);
		action.setFont(textFont);
		returnToMenu.setFont(textFont);
		
		learning.setVisible(true);
		testing.setVisible(true);
		action.setVisible(true);
		returnToMenu.setVisible(true);
		
		learning.addActionListener(this);
		testing.addActionListener(this);
		action.addActionListener(this);
		returnToMenu.addActionListener(this);
		
		add(learning);
		add(testing);
		add(action);
		add(returnToMenu);
	}
	
	/**
	 * Overridden method to paint the window.
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0,  0,  1200, 800);
		g.drawImage(spiderLight.getImage(), 0, 0, this);
	}

	/**
	 * Overridden method to handle events from buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(learning)) {
			//If the Learning Stage button is clicked, switch to learning Stage
			layoutManager.show(getParent(), "learning");
		} else if (e.getSource().equals(testing)) {
			//Checks if the learning stage is complete first. If not, error message appears.
			if (!learningManager.beatenLevel()) {
				JDialog failBox = new JDialog(new JFrame(), "Slow Down!");				
				failBox.getContentPane().setBackground(Color.black);
				failBox.setSize(300, 50);
				failBox.setLocationRelativeTo(null);
				failBox.setResizable(false);
				failBox.setLayout(null);
				failBox.setVisible(true);
				
				JLabel msg = new JLabel("Complete the Learning Stage first!");
				msg.setFont(smallFont);
				msg.setForeground(Color.white);
				msg.setBounds(10, 0, 300, 20);
				failBox.add(msg);
			} else {
				layoutManager.show(getParent(),  "testing");
			}
		} else if (e.getSource().equals(action)) {
			if (!testingManager.beatenLevel() || !learningManager.beatenLevel()) {
				JDialog failBox = new JDialog(new JFrame(), "Slow Down!");				
				failBox.getContentPane().setBackground(Color.black);
				failBox.setSize(400, 50);
				failBox.setLocationRelativeTo(null);
				failBox.setResizable(false);
				failBox.setLayout(null);
				failBox.setVisible(true);
				
				JLabel msg = new JLabel("Complete the Learning and Testing Stages first!");
				msg.setFont(smallFont);
				msg.setForeground(Color.white);
				msg.setBounds(5, 0, 400, 20);
				failBox.add(msg);
			} else {
				layoutManager.show(getParent(), "action");
			}
		}
		else if (e.getSource().equals(returnToMenu)) {
			//Returns to menu.
			layoutManager.show(getParent(),  "menu");
		}
	}
}