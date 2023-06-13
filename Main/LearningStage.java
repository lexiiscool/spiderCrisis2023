package Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Date: June 7, 2023
 * <p>
 * Version 4.0 of the Learning Stage of "Spider Crisis". Finalized level with
 * gameplay and graphics complete. Provided by JELTech Industries.
 * </p>
 * <h2>Course: ICS4U0 with V. Krasteva</h2>
 * 
 * @author Lexi Han, Ethan Andrew
 * @version 23.06.04
 */

public class LearningStage extends JComponent implements KeyListener, MouseListener {
	// Variable to handle cardLayout (will enable user to switch screens from this
	// screen)
	private CardLayout layoutManager;

	// Variables for fonts/colors.
	private final String EVAN = "Evan";
	private final String MICHAEL = "Michael";
	private final Color TEXTBOXCOLOR = new Color(0, 0, 0, 125);

	private Font nameFont;
	private Font textFont;
	private Font smallFont;
	private Font successFont;
	private Font titleFont;

	// indices to facilitate animation and dialogues.
	private int currIndex = 0;
	private int animInd = 0;

	// Variables for Michael's hit box
	private int michaelX = 50, michaelY = 200;
	private final int MICHAELWIDTH = 300;
	private final int MICHAELHEIGHT = 450;

	private int failedAttempts = 0; // June 6, 2023 - Ethan Andrew - Keeps track of the number of failed attempts to
									// catch the spider.

	// Variables for Evan's hit box.
	private final int EVANWIDTH = 150;
	private final int EVANHEIGHT = 225;

	// Variables for Evan's destination/"end zone"
	private int endZoneX = 800, endZoneY = 300;

	// Variables for Spider
	private int spiderX = 950, spiderY = 600;
	private final int SPIDERWIDTH = 50;

	// Added by Lexi on June 4, 2023. Objects representing aspects of game.
	private Player Evan;
	private Room bedroom;
	private RoomArea bedroomSide;
	private Spider spider;

	// Added by Ethan Andrew on June 6, 2023
	private Score score = new Score();

	// Booleans to help track state of game
	private boolean firstDialogueComplete = false;
	private boolean secondDialogueComplete = false;
	private boolean inGraphic = false;
	private boolean inAnimation = false;
	private boolean animationOver = false;
	private boolean evanIsWalking = false;
	private boolean firstPerson = false;
	private boolean gameOver = false;
	private boolean failedToClickSpider = false;

	// Boolean to track if the user completed this stage
	private boolean completeStage = false;

	// Variables for images
	private ImageIcon michaelHead, evanHead;
	private ImageIcon spiderImg, evanBodyLEFT, evanBodyRIGHT, evanBodyUP, evanBodyDOWN, michaelBody;
	private ImageIcon fullBedRoomBg, sideBedRoomBg;
	private ImageIcon frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8, frame9, frame10, frame11;

	// JLabels for images to add them to panels

	// Variables for buttons
	private JButton returnToMenu = new WhiteButton("Menu");
	private JButton playAgain = new WhiteButton("Play Again");

	// Messages to trigger different actions.
	private Message lastPreGraphicMsg, lastGraphicMsg, lastFirstDialogueMsg, lastSecondDialogueMsg;

	// ArrayList of Messages to be displayed.
	private ArrayList<Message> msgList;
	private ArrayList<ImageIcon> animationImgs;

	/**
	 * Constructor for LearningStage. Initializes variables used in the class.
	 * 
	 * @param cl The CardLayout that will help with switching cards.
	 * @throws IOException
	 * @throws FontFormatException
	 * @throws URISyntaxException
	 */
	public LearningStage(CardLayout cl) throws IOException, FontFormatException, URISyntaxException {
		layoutManager = cl;

		// Sets layout to null (for manual positioning)
		setLayout(null);

		initImages();

		nameFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("PixeloidSans-mLxMm.ttf"));
		textFont = nameFont.deriveFont(20f);
		smallFont = nameFont.deriveFont(Font.ITALIC, 14f);
		successFont = nameFont.deriveFont(48f);
		nameFont = nameFont.deriveFont(Font.BOLD, 30f);

		// June 1, 2023 - Ethan Andrew - Initializing title font.
		titleFont = Font.createFont(Font.TRUETYPE_FONT,
				getClass().getClassLoader().getResourceAsStream("titleFont.otf"));
		titleFont = titleFont.deriveFont(180f);

		Evan = new Player(EVAN, EVANWIDTH, EVANHEIGHT, 25);
		Evan.setCoordinates(300, 500);
		Evan.setLowerHitBox();
		Evan.setDirection(2);

		Evan.addImage(evanBodyLEFT.getImage());
		Evan.addImage(evanBodyRIGHT.getImage());
		Evan.addImage(evanBodyDOWN.getImage());
		Evan.addImage(evanBodyUP.getImage());

		bedroom = new Room("Bedroom", sideBedRoomBg.getImage());
		spider = new Spider(spiderX, spiderY, SPIDERWIDTH, new Rectangle(200, 300, 800, 25), spiderImg);

		// Initializes parallel array, preparing dialogue
		msgList = new ArrayList<Message>();
		initMessages();
		initRoom();

		// Adding listeners
		addMouseListener(this);
		addKeyListener(this);
		setFocusable(true);
		requestFocus(true);
		setVisible(true);
	}

	/**
	 * Overridden method that draws the window.
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draws bedroom image.
		g.drawImage(fullBedRoomBg.getImage(), 0, 0, this);

		// Will run first set of dialogue if the dialogue is not complete
		if (!firstDialogueComplete) {
			displayEvan(g, Evan);
			drawSpider(g, spider);
			if (currIndex >= 2)
				drawMichael(g, michaelX, michaelY); // after a portion of dialogue is complete, Michael will appear.
			displayFirstDialogue(g);
		}

		// If Evan is currently being controlled by the user (walking), this block will
		// execute.
		else if (evanIsWalking) {
			drawMichael(g, michaelX, michaelY);
			drawSpider(g, spider);
			displayEvanWalking(g);
		}
		// If Evan is in first person POV, this block executes.
		else if (firstPerson) {
			displayFirstPerson(g);
			drawSpider(g, spider);
		}

		// If Evan is done, and the second set of dialogue is not complete, this
		// executes.
		else if (!secondDialogueComplete) {
			if (inAnimation) {
				displayAnimations(g, animInd);
			} else if (animationOver) {
				drawMichael(g, michaelX, michaelY);
				displaySecondDialogue(g);
			}
		}

		// If everything else is done, this is executed.
		else if (gameOver) {
			displaySuccessScreen(g);
		}

		// June 6, 2023 - Ethan Andrew - Displays the score.
		displayScore(g, score);
	}

	/**
	 * Method to initialize the bedroom's boundaries and entities.
	 */
	private void initRoom() {
		bedroom.addBoundaries(new Rectangle(0, 0, 1200, 500));
		bedroom.addBoundaries(new Rectangle(0, 790, 1200, 100));
		bedroom.addBoundaries(new Rectangle(0, 500, 10, 300));
		bedroom.addBoundaries(new Rectangle(1200, 500, 100, 300));
		bedroom.addBoundaries(new Rectangle(420, 0, 380, 650));
		bedroom.addBoundaries(new Rectangle(michaelX, michaelY, MICHAELWIDTH, MICHAELHEIGHT));

		bedroomSide = new RoomArea("this area", sideBedRoomBg.getImage(), new Rectangle(endZoneX, endZoneY, 400, 400),
				spider);
		bedroom.addCheckZone(bedroomSide);
	}

	/**
	 * Method to display a special graphic during the dialogue
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	public void displayExplanation(Graphics g) {
		Image graphic = new ImageIcon(getClass().getClassLoader().getResource("spider-explain.png")).getImage();
		g.drawImage(graphic, 0, 0, 1200, 800, this);
	}

	/**
	 * Method to display the first set of dialogue between characters.
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	public void displayFirstDialogue(Graphics g) {
		// If current dialogue is not the special explanation graphic
		if (inGraphic) {
			displayExplanation(g);
		}
		// Displays the dialogue found in the message list.
		displayMsg(g, msgList.get(currIndex));

		// Small helper font (to guide the user).
		g.setFont(smallFont);
		g.drawString("Click anywhere to " + (msgList.get(currIndex).equals(lastFirstDialogueMsg)? "finish." : "continue."), 250, 170);
	}

	/**
	 * Method to display the second set of dialogue between characters.
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	public void displaySecondDialogue(Graphics g) {
		// Displays dialogue found in message list.
		displayMsg(g, msgList.get(currIndex));

		// Small helper font.
		g.setFont(smallFont);
		g.drawString("Click anywhere to " + (msgList.get(currIndex).equals(lastSecondDialogueMsg)? "finish." : "continue."), 1000, 700);
		displayEvan(g, Evan);
	}

	/**
	 * Method to display the scene shown when Evan is moving.
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	public void displayEvanWalking(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(textFont);

		// Michael explains instructions to you, the user.
		if (bedroom.inCheckZone(Evan)) {
			displayMsg(g, new Message(MICHAEL, "Press \"C\" to check this zone for spiders!", michaelHead));
		} else
			displayMsg(g, new Message(MICHAEL, "use the \"WASD\" keys to move. Head towards the spider to capture it!",
					michaelHead));
		displayEvan(g, Evan);
	}

	/**
	 * Method to display the scene shown when Evan is in first person, grabbing the
	 * spider.
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	public void displayFirstPerson(Graphics g) {
		// Draws image of the side of Evan's bedroom.
		g.drawImage(sideBedRoomBg.getImage(), 0, 0, this);

		// Message by Michael (our guide) on what to do
		if (!failedToClickSpider)
			displayMsg(g, new Message(MICHAEL, "Click on the spider to capture it!", michaelHead));
		else
			displayMsg(g, new Message(MICHAEL, "Keep trying! The spider is crafty! Just click on it to catch it!",
					michaelHead));
	}

	/**
	 * Method to display a dialogue message
	 * 
	 * @param g The painting tool that helps draw the window.
	 * @param m The message object containing information about what message to be
	 *          displayed.
	 */
	public void displayMsg(Graphics g, Message m) {
		/**
		 * Method was modified on May 23, 2023 by Lexi Han. Replaced JLabel type of
		 * "img" to Image, and modified drawing method of img.
		 */

		// Retrieves attributes from the Message object.
		Image img = m.getIcon().getImage();
		String name = m.getName(), msg = m.getMessage();

		// Draws the message with the given attributes.
		g.setColor(TEXTBOXCOLOR);
		g.fillRect(0, 0, 1200, 200);
		g.drawImage(img, 80, 50, this);

		g.setColor(Color.WHITE);
		g.setFont(nameFont);
		g.drawString(name, 250, 80);

		g.setFont(textFont);
		g.drawString(msg, 250, 130);
	}

	/**
	 * Method to display the final screen, prompting the user to leave.
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	public void displaySuccessScreen(Graphics g) {
		// Method to be completed.
		g.setColor(Color.white);
		g.fillRect(0, 0, 1200, 800);
		g.fillRect(0, 0, 1200, 800);

		// June 1, 2023 - Ethan Andrew - Added "Success!" text.
		g.setColor(Color.red);
		g.setFont(titleFont);
		g.drawString("SUCCESS!", 280, 350);
		g.setColor(Color.WHITE);

		// Sets location of two buttons.
		returnToMenu.setBounds(200, 500, 300, 100);
		playAgain.setBounds(700, 500, 300, 100);

		// June 1, 2023 - Ethan Andrew - Sets button font.
		returnToMenu.setForeground(Color.black);
		returnToMenu.setFont(successFont);
		playAgain.setForeground(Color.black);
		playAgain.setFont(successFont);

		// Buttons to prompt the user to exit out.

		// Adding buttons to container.
		add(returnToMenu);
		returnToMenu.setVisible(true);
		returnToMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// resets the level and sends the user to the menu.
				resetLevel();
				layoutManager.show(getParent(), "menu");
			}
		});

		add(playAgain);
		playAgain.setVisible(true);
		playAgain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// resets the level and lets the user play again.
				resetLevel();
				repaint();
			}
		});

	}

	/**
	 * Method to draw the Evan sprite.
	 * 
	 * @param g The painting tool that helps draw the window.
	 * @param p The Player.
	 */
	public void displayEvan(Graphics g, Player p) {
		Image img = p.getOrientedImage();
		p.setLowerHitBox();
		int x = p.getX(), y = p.getY();
		g.drawImage(img, x, y, p.getWidth(), p.getHeight(), this);
	}

	/**
	 * Method to draw the Michael sprite.
	 * 
	 * @param g The painting tool that helps draw the window.
	 * @param x The top left most x-coordinate.
	 * @param y The top left most y-coordinate.
	 */
	public void drawMichael(Graphics g, int x, int y) {
		g.drawImage(michaelBody.getImage(), x, y, MICHAELWIDTH, MICHAELHEIGHT, this);
	}

	/**
	 * Method to draw the spider mob.
	 * 
	 * @param g The painting tool that helps draw the window.
	 * @param s The spider.
	 */
	public void drawSpider(Graphics g, Spider s) {
		Image img = s.getImg();
		int x = s.getX(), y = s.getY(), width = s.getWidth();
		g.drawImage(img, x, y, width, width, this);
	}

	/**
	 * Method to display the animation of Michael releasing the spider.
	 * 
	 * @param g The painting tool that helps draw the window.
	 * @parm ind The index of a certain frame to be displayed.
	 */
	public void displayAnimations(Graphics g, int ind) {
		try {
			if (animationImgs.get(ind).equals(frame1))
				Thread.sleep(200);
			else if (animationImgs.get(ind).equals(frame11))
				Thread.sleep(3000);
			else
				Thread.sleep(300);
			g.drawImage(animationImgs.get(ind).getImage(), 0, 0, 1200, 800, this);
			animInd = Math.min(animationImgs.size(), ind + 1);
			repaint();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (animInd == animationImgs.size()) {
			inAnimation = false;
			animationOver = true;
			return;
		}
	}

	/**
	 * Displays the score in the top right corner of the screen.
	 * 
	 * @param g     Draws the score to the component.
	 * @param score The score.
	 */
	public void displayScore(Graphics g, Score score) {
		g.setColor(TEXTBOXCOLOR);
		g.fillRect(925, 0, 275, 75);
		g.setFont(nameFont);
		g.setColor(Color.WHITE);
		g.drawString("Score: " + score.getScore(), 940, 50);
	}

	/**
	 * The method to check if the user has beaten this level at least once. Renamed
	 * to beatenLevel() by Lexi Han on May 30, 2023
	 * 
	 * @return Whether or not the user has beaten this level at least once.
	 */
	public boolean beatenLevel() {
		return completeStage;
	}

	/**
	 * Private method to initialize the list that holds the dialogue.
	 */
	private void initMessages() {
		//Modified by Lexi on June 12, 2023. Improved quality of dialogue. 
		
		lastPreGraphicMsg = new Message(EVAN, "But why do they need to be in our home?", evanHead);
		lastFirstDialogueMsg = new Message(MICHAEL, "Don't worry, I'll slide the paper and release it for you. Go get 'em!", michaelHead);
		lastSecondDialogueMsg = new Message(MICHAEL, "Tonight, we get rid of your fear of spiders for GOOD!", michaelHead);
		lastGraphicMsg = new Message(MICHAEL, "...you would see a LOT more of these pests!", michaelHead);
				
		// initializing message list.
		msgList.add(new Message(EVAN, "AAAAAAH! Get away from me! I don't want you here!", evanHead));
		msgList.add(new Message(EVAN, "STOP! DON'T COME ANY CLOSER! MIIICHAELLL!", evanHead));
		msgList.add(new Message(MICHAEL, "Evan! It's okay! It's just a spider!", michaelHead));
		msgList.add(new Message(EVAN, "I'm scared, Michael!", evanHead));
		msgList.add(new Message(MICHAEL, "Calm down, Evan. Everything's going to be okay.", michaelHead));
		msgList.add(new Message(MICHAEL, "These emotions are perfectly normal to be feeling.", michaelHead));
		msgList.add(new Message(MICHAEL, "I used to be scared of spiders too, like you are.", michaelHead));
		msgList.add(new Message(EVAN, "Really?", evanHead));
		msgList.add(new Message(MICHAEL, "Yes. I was horrified, and I had these breakdowns like yours.", michaelHead));
		msgList.add(new Message(MICHAEL, "But it's important to grow and learn how to overcome them.", michaelHead));
		msgList.add(new Message(MICHAEL, "Spiders aren't as scary as they seem!", michaelHead));
		msgList.add(new Message(EVAN, "But what if it bites me? I'll be poisoned and die!", evanHead));
		msgList.add(new Message(MICHAEL,
				"No, you won't! Out of the 40,000 spider species in the world...", michaelHead));
		msgList.add(new Message(MICHAEL,"...only around 12 can ACTUALLY hurt you!", michaelHead));
		msgList.add(new Message(MICHAEL, "Where we live, only TWO types of spiders can harm you...", michaelHead));
		msgList.add(new Message(MICHAEL, "...and none of them can be found in our home!", michaelHead));
		msgList.add(new Message(MICHAEL, "Not to mention, spiders are generally not aggressive towards humans.", michaelHead));
		msgList.add(new Message(MICHAEL, "So the chances of them attacking you are very low!", michaelHead));
		msgList.add(new Message(MICHAEL, "They're only willing to bite people in an act of defense...", michaelHead));
		msgList.add(new Message(MICHAEL, "...whether to save their life or to protect an egg sac!", michaelHead));
		msgList.add(lastPreGraphicMsg);

		msgList.add(new Message(MICHAEL, "The reason why spiders end up in our homes is quite simple.", michaelHead));
		msgList.add(new Message(MICHAEL, "Our homes are just a more calm place to be in than the outside world!", michaelHead));
		msgList.add(new Message(MICHAEL, "They also find food here, which can help us!", michaelHead));
		msgList.add(new Message(EVAN, "Really? How do they help us?", evanHead));
		msgList.add(
				new Message(MICHAEL, "Well, spiders aren't the only insects that can be found...", michaelHead));
		msgList.add(new Message(MICHAEL, "FLIES are also found in homes, along with the occasional MOSQUITO!",
				michaelHead));
		msgList.add(new Message(EVAN, "EWWW, mosquitoes are gross!", evanHead));
		msgList.add(
				new Message(MICHAEL, "I know! But what spiders do to control the insects...", michaelHead));
		msgList.add(new Message(MICHAEL, "...is CATCHING and EATING them. This means that if not for spiders...",
				michaelHead));
		msgList.add(lastGraphicMsg);

		// index 19 goes back to regular again
		msgList.add(new Message(EVAN, "I guess that's cool, but I still don't want it in my room!", evanHead));
		msgList.add(new Message(MICHAEL, "If you don't want spiders in your house...", michaelHead));
		msgList.add(new Message(MICHAEL, "sealing off windows and closing doors can keep them outside!", michaelHead));
		msgList.add(new Message(MICHAEL, "If a spider is already in the house...", michaelHead));
		msgList.add(new Message(MICHAEL, "Don't worry! Getting rid of one is very quick and easy!", michaelHead));
		msgList.add(new Message(MICHAEL,
				"We can catch them in a cup, slide some paper under them, and release them outside.", michaelHead));
		msgList.add(new Message(MICHAEL, "This way, it doesn't harm the spider as well!", michaelHead));
		
		msgList.add(new Message(MICHAEL, "What you can also do is vacuum the house...", michaelHead));
		msgList.add(new Message(MICHAEL, "...to remove spiders, their webs, and their eggs.", michaelHead));
		
		msgList.add(new Message(MICHAEL, "It's very important to have a clean house to not attract pests!", michaelHead));

		msgList.add(new Message(MICHAEL, "Now, what I want you to do is...", michaelHead));
		msgList.add(new Message(MICHAEL, "...go to that spider, and put it in this cup!", michaelHead));
		msgList.add(lastFirstDialogueMsg);

		// index 26 starts the second batch of dialogue.
		msgList.add(new Message(MICHAEL, "Great job Evan! Now, I have one more test for you to do...", michaelHead));
		msgList.add(lastSecondDialogueMsg);
	}

	/**
	 * Method to initialize images used in level and animations.
	 */
	private void initImages() {
		michaelHead = new ImageIcon(getClass().getClassLoader().getResource("michael-profile.png"));

		evanHead = new ImageIcon(getClass().getClassLoader().getResource("evan-profile.png"));

		spiderImg = new ImageIcon(getClass().getClassLoader().getResource("spider_crawling.png"));
		spiderImg = new ImageIcon(spiderImg.getImage().getScaledInstance(SPIDERWIDTH, SPIDERWIDTH, Image.SCALE_SMOOTH));

		michaelBody = new ImageIcon(getClass().getClassLoader().getResource("michael-front_view.png"));
		michaelBody = new ImageIcon(
				michaelBody.getImage().getScaledInstance(MICHAELWIDTH, MICHAELHEIGHT, Image.SCALE_SMOOTH));

		evanBodyLEFT = new ImageIcon(getClass().getClassLoader().getResource("evan-left_view.png"));
		evanBodyLEFT = new ImageIcon(
				evanBodyLEFT.getImage().getScaledInstance(EVANWIDTH, EVANHEIGHT, Image.SCALE_SMOOTH));

		evanBodyRIGHT = new ImageIcon(getClass().getClassLoader().getResource("evan-right_view.png"));
		evanBodyRIGHT = new ImageIcon(
				evanBodyRIGHT.getImage().getScaledInstance(EVANWIDTH, EVANHEIGHT, Image.SCALE_SMOOTH));

		evanBodyUP = new ImageIcon(getClass().getClassLoader().getResource("evan-back_view.png"));
		evanBodyUP = new ImageIcon(evanBodyUP.getImage().getScaledInstance(EVANWIDTH, EVANHEIGHT, Image.SCALE_SMOOTH));

		evanBodyDOWN = new ImageIcon(getClass().getClassLoader().getResource("evan-front_view.png"));
		evanBodyDOWN = new ImageIcon(
				evanBodyDOWN.getImage().getScaledInstance(EVANWIDTH, EVANHEIGHT, Image.SCALE_SMOOTH));

		fullBedRoomBg = new ImageIcon(getClass().getClassLoader().getResource("evans_room-full_view.png"));
		fullBedRoomBg = new ImageIcon(fullBedRoomBg.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		sideBedRoomBg = new ImageIcon(getClass().getClassLoader().getResource("evans_room-side_view.png"));
		sideBedRoomBg = new ImageIcon(sideBedRoomBg.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		frame1 = new ImageIcon(getClass().getClassLoader().getResource("release_frame_1.png"));
		frame1 = new ImageIcon(frame1.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		frame2 = new ImageIcon(getClass().getClassLoader().getResource("release_frame_2.png"));
		frame2 = new ImageIcon(frame2.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		frame3 = new ImageIcon(getClass().getClassLoader().getResource("release_frame_3.png"));
		frame3 = new ImageIcon(frame3.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		frame4 = new ImageIcon(getClass().getClassLoader().getResource("release_frame_4.png"));
		frame4 = new ImageIcon(frame4.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		frame5 = new ImageIcon(getClass().getClassLoader().getResource("release_frame_5.png"));
		frame5 = new ImageIcon(frame5.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		frame6 = new ImageIcon(getClass().getClassLoader().getResource("release_frame_6.png"));
		frame6 = new ImageIcon(frame6.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		frame7 = new ImageIcon(getClass().getClassLoader().getResource("release_frame_7.png"));
		frame7 = new ImageIcon(frame7.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		frame8 = new ImageIcon(getClass().getClassLoader().getResource("release_frame_8.png"));
		frame8 = new ImageIcon(frame8.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		frame9 = new ImageIcon(getClass().getClassLoader().getResource("release_frame_9.png"));
		frame9 = new ImageIcon(frame9.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		frame10 = new ImageIcon(getClass().getClassLoader().getResource("release_frame_10.png"));
		frame10 = new ImageIcon(frame10.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		frame11 = new ImageIcon(getClass().getClassLoader().getResource("release_frame_11.png"));
		frame11 = new ImageIcon(frame11.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		// Initializing animations.
		animationImgs = new ArrayList<ImageIcon>();
		animationImgs.add(frame1);
		animationImgs.add(frame2);
		animationImgs.add(frame3);
		animationImgs.add(frame4);
		animationImgs.add(frame5);
		animationImgs.add(frame6);
		animationImgs.add(frame7);
		animationImgs.add(frame8);
		animationImgs.add(frame9);
		animationImgs.add(frame10);
		animationImgs.add(frame11);
		animationImgs.add(frame11); // buffer
	}

	/**
	 * Method that resets all the variables that affect the status of the game, back
	 * to their initial state. Allows user to play again.
	 */
	private void resetLevel() {
		returnToMenu.setVisible(false);
		playAgain.setVisible(false);

		spider.setCoordinates(950, 600);
		spider.setStatus(false);
		spiderX = 950;
		spiderY = 600;

		Evan.setCoordinates(300, 500);

		currIndex = 0;
		animInd = 0;
		firstDialogueComplete = false;
		evanIsWalking = false;
		firstPerson = false;
		gameOver = false;
		failedToClickSpider = false;
		secondDialogueComplete = false;
		inAnimation = false;
		animationOver = false;
		score.resetScore();
	}

	/**
	 * Method to handle what happens if a key is pressed.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// If Evan is currently walking, using the WASD keys.
		if (firstDialogueComplete && evanIsWalking) {
			int keyCode = e.getKeyCode();

			if (bedroom.inCheckZone(Evan)) {
				if (keyCode == KeyEvent.VK_C) {
					firstPerson = true;
					evanIsWalking = false;
					spider.randomizeCords();
				}
			}

			// If Evan moves LEFT
			if (keyCode == KeyEvent.VK_A) {
				Evan.setDirection(0);
				// Shifts Evan's position 35 pixels left if possible.
				if (bedroom.validMove(Evan, -Evan.getMoveDist(), 0)) {
					Evan.moveHorizontally(-Evan.getMoveDist());
				}
			}

			// If Evan moves RIGHT
			else if (keyCode == KeyEvent.VK_D) {
				Evan.setDirection(1);
				// Shifts Evan's position 35 pixels right if possible.
				if (bedroom.validMove(Evan, Evan.getMoveDist(), 0)) {
					Evan.moveHorizontally(Evan.getMoveDist());
				}
			}

			// If Evan moves DOWN
			else if (keyCode == KeyEvent.VK_S) {
				Evan.setDirection(2);
				if (bedroom.validMove(Evan, 0, Evan.getMoveDist())) {
					Evan.setCoordinates(Evan.getX(), Evan.getY() + Evan.getMoveDist());
				}
			}

			// If Evan moves UP
			else if (keyCode == KeyEvent.VK_W) {
				Evan.setDirection(3);
				if (bedroom.validMove(Evan, 0, -Evan.getMoveDist())) {
					Evan.moveVertically(-Evan.getMoveDist());
				}
			}
			repaint();
		}
	}

	/**
	 * Method to handle when a mouse clicks on the screen.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// If the first set of dialogue is occurring, this executes.
		if (!firstDialogueComplete) {
			if (msgList.get(currIndex).equals(lastPreGraphicMsg)) {
				inGraphic = true;
				currIndex++;
				repaint();
			} else if (msgList.get(currIndex).equals(lastGraphicMsg)) {
				inGraphic = false;
				currIndex++;
				repaint();
			} else if (msgList.get(currIndex).equals(lastFirstDialogueMsg)) {
				// Concludes the first set of dialogue, and prompts Evan to start walking.
				firstDialogueComplete = true;
				evanIsWalking = true;
				currIndex++;
				repaint();
			} else {
				currIndex++;
				repaint();
			}

		}

		// If Evan is now in first person, trying to click the spider.
		else if (firstPerson) {
			// If user fails to click spider.
			if (!spider.isCaught(e.getX(), e.getY())) {
				failedToClickSpider = true;
				spider.randomizeCords();
				failedAttempts++;
				repaint();
			} else {
				// initiates second batch of dialogue.
				failedToClickSpider = false;
				firstPerson = false;
				inAnimation = true;

				// June 6, 2023 - Ethan Andrew - Adds points depending on how many failed
				// attempts there were.

				score.addScore(500 - 100 * failedAttempts);

				// Minimum score in Learning Stage is 200.
				if (score.getScore() < 200)
					score.addScore(200 - score.getScore());

				failedAttempts = 0;
				spider.setStatus(true);
				Evan.setDirection(2);
				repaint();
			}

		}

		// If second dialogue is occurring, this executes.
		else if (!secondDialogueComplete) {
			if (animationOver) {
				if (msgList.get(currIndex).equals(lastSecondDialogueMsg)) {
					secondDialogueComplete = true;
					gameOver = true;
					completeStage = true;
					currIndex++;
					repaint();
				}
				currIndex++;
				repaint();
			}
		}
	}

	// Unused methods.

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
