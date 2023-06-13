package Main;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Date: May 30, 2023
 * <p>
 * Version 3.0 of the driver code to run the program, "Spider Crisis".
 * Included minor support for Action Stage. 
 * Provided by JELTech Industries. 
 * </p>
 * 
 * <h2>
 * Course: ICS4UO with V.Krasteva
 * </h2>
 * @author Lexi Han
 * @author Ethan Andrew
 * @version 23.05.30
 */

public class Driver extends JFrame {
	
	private final String INTRO = "intro";
	private final String MENU = "menu";
	private final String LEARNING = "learning";
	private final String TESTING = "testing";
	private final String LEVELSELECT = "level select";
	
	//Added by Lexi Han on May 31, 2023
	private final String ACTION = "action";
	
	// May 26, 2023 - Ethan Andrew - Added variable to control exit screen.
	private final String EXITSCREEN = "exit screen";

	// May 28, 2023 - Ethan Andrew - Added variable to control credits screen.
	private final String CREDITS = "credits screen";
	
	private boolean passedStartScreen = false;
	private CardLayout cl;
	
	IntroScreen introScreen;
	Menu menuScreen;
	LearningStage learningScreen;
	TestingStage testingScreen;
	LevelSelect levelSelectScreen;
	//added by Lexi 
	ActionStage actionScreen;
	
	// May 26, 2023 - Ethan Andrew - Added exit screen.
	ExitScreen exitScreen;

	// May 28, 2023 - Ethan Andrew - Added credits screen.
	CreditsScreen creditsScreen;
	
	JPanel cardPanel;
	
	/**
	 * Constructor of the Driver Class. Initializes and instantiates each screen, and adds to the CardLayout. 
	 * Creates the initial window. 
	 * @throws IOException Handles exceptions that occur when custom fonts are loaded.
	 * @throws FontFormatException Handles exceptions that occur when custom fonts are loaded.
	 * @throws URISyntaxException 
	 */
	public Driver() throws IOException, FontFormatException, URISyntaxException{
		cl = new CardLayout();
		menuScreen = new Menu(cl);
		introScreen = new IntroScreen();
		learningScreen = new LearningStage(cl);
		testingScreen = new TestingStage(cl);
		actionScreen = new ActionStage(cl);
		levelSelectScreen = new LevelSelect(cl, learningScreen, testingScreen);
		
		// May 26, 2023 - Ethan Andrew - Added exit screen.
		exitScreen = new ExitScreen();
		// May 28, 2023 - Ethan Andrew - Added credits screen.
		creditsScreen = new CreditsScreen();
		
		cardPanel = new JPanel();
		cardPanel.setLayout(cl);
		cardPanel.add(introScreen, INTRO);
		cardPanel.add(menuScreen, MENU);
		cardPanel.add(levelSelectScreen, LEVELSELECT);
		cardPanel.add(learningScreen, LEARNING);
		cardPanel.add(testingScreen, TESTING);
		cardPanel.add(actionScreen, ACTION);
	
		// May 26, 2023 - Ethan Andrew - Added exit screen.
		cardPanel.add(exitScreen, EXITSCREEN);
		// May 28, 2023 - Ethan Andrew - Added credits screen.
		cardPanel.add(creditsScreen, CREDITS);
		
		setTitle("Spider Crisis");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 800);
		setResizable(false);
		setLocationRelativeTo(null);
		add(cardPanel);
		addMouseListener(new ClickHandler());
		setVisible(true);
	}
	
	/**
	 * Main method to run the Driver class.
	 * @param args Command Line arguments.
	 * @throws IOException
	 * @throws FontFormatException
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws IOException, FontFormatException, URISyntaxException{
		new Driver();
	}
	
	/**
	 * <p> 
	 * The inner class that helps handle when the screen is clicked. 
	 * </p> 
	 * @author Lexi Han 
	 * @version 23.05.16
	 *
	 */
	class ClickHandler extends MouseAdapter {
		
		/**
		 * Method to initialize the game, and send the user into the main menu. 
		 */
		@Override
		public void mouseClicked (MouseEvent e) {
			if (!passedStartScreen) {
				((CardLayout) cardPanel.getLayout()).show(cardPanel, MENU);
				passedStartScreen = true;
			}
			
			// May 26, 2023 - Ethan Andrew - Added condition that checks if the user is on the exit screen.
			if (menuScreen.getOnQuitScreen()) {
				System.exit(0);
			}

			// May 26, 2023 - Ethan Andrew - Added condition that checks if the user is on the credits screen.
			if (menuScreen.isOnCredits()) {
				((CardLayout) cardPanel.getLayout()).show(cardPanel, MENU);
				menuScreen.setOnCredits(false);
			}
		}
	}
}