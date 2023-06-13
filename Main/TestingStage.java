package Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.URISyntaxException;

/**
 * Date: May 30, 2023
 * <p>
 * Version 2.1 of the Testing Stage. Added finishing details to core gameplay.
 * Details like scoring to be implemented. Graphics included. Provided by
 * JELTech Industries.
 * </p>
 * <h2>Class: ICS4UO with V.Krasteva</h2>
 * 
 * @author Lexi Han
 * @version 23.05.30
 */

public class TestingStage extends JComponent implements ActionListener, MouseListener, KeyListener {
	private CardLayout layoutManager;

	// Added by Ethan Andrew on June 6, 2023
	private Score score = new Score();
	private int failedAttempts = 0;

	// Constant variables
	private final String MICHAEL = "Michael";
	private final String EVAN = "Evan";

	// Variables describing hit boxes.
	private final int MICHAELWIDTH = 300;
	private final int MICHAELHEIGHT = 450;

	private final int EVANWIDTH = 150;
	private final int EVANHEIGHT = 225;
	private final int EVANMAZEWIDTH = 40;

	// Variables for graphics (colours, fonts, etc.)
	private final Color TEXTBOXCOLOR = new Color(0, 0, 0, 125);
	private final Color MAZEWALLCOLOR = new Color(94, 12, 54);

	private Font nameFont;
	private Font textFont;
	private Font smallFont;
	private Font successFont;
	private Font titleFont;

	private int currIndex = 0; // current message number in dialogue.

	// Variables for images
	private ImageIcon michaelHead, evanHead, evanMazeHead;
	private ImageIcon spiderImg, evanBodyLEFT, evanBodyRIGHT, evanBodyUP, evanBodyDOWN, michaelBody;
	private ImageIcon fullBedRoomBg, fullLivingRoomBg, smallLivingRoomBg, smallKitchenBg, smallBathroomBg;
	private ImageIcon inArrow, outArrow;
	private ImageIcon endImage;
	private ImageIcon behindCouchImg, behindPlantImg, underDrawerImg;

	private Player Evan, EvanMaze;
	private Room livingroom;
	private RoomArea currRoomArea;
	private Player currPlayer;
	private Spider spider;

	// ArrayLists keeping track of messages, and "zones" Evan can or cannot interact
	// with.
	private ArrayList<Message> msgList;
	private ArrayList<Rectangle> mazeWalls;

	// variables to track state of game
	private int keyPressedNum = 0; // June 6, 2023 - Ethan Andrew - Counts how many times a key was pressed in the
									// maze.
	private boolean firstDialogueComplete = false;
	private boolean secondDialogueComplete = false;
	private boolean thirdDialogueComplete = false;
	private boolean choosingRoom = false;
	private boolean inMaze = false;
	private boolean inNewRoom = false;
	private boolean beginningNewRoomMsgSpoken = false;
	private boolean firstPerson = false;
	private boolean spiderCaught = false;
	private boolean failedCatch = false;
	private boolean backFromRoom = false;
	private boolean gameOver = false;

	private boolean beatenLevel = false;

	// Variables to represent breakpoints of dialogue box.
	private Message lastFirstDialogueMsg;
	private Message lastSecondDialogueMsg;
	private Message lastThirdDialogueMsg;

	// Variables for choice section
	private JButton kitchen, livingRoom, bathroom;
	private boolean kitchenPressed, livingRoomPressed, bathroomPressed;

	// Variables for Michael's hit box
	private int michaelX = 50, michaelY = 200;

	// Variables for Evan's hit box during the room section

	// Buttons to be used for success screen.
	private JButton playAgain, returnToMenu;

	/**
	 * Constructor to initialize variables.
	 * 
	 * @param cl The CardLayout that helps users switch screens.
	 * @throws IOException
	 * @throws FontFormatException
	 * @throws URISyntaxException 
	 */
	public TestingStage(CardLayout cl) throws IOException, FontFormatException, URISyntaxException {
		layoutManager = cl;

		msgList = new ArrayList<Message>();
		mazeWalls = new ArrayList<Rectangle>();

		nameFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("PixeloidSans-mLxMm.ttf"));
		textFont = nameFont.deriveFont(20f);
		smallFont = nameFont.deriveFont(Font.ITALIC, 14f);
		successFont = nameFont.deriveFont(48f);
		nameFont = nameFont.deriveFont(Font.BOLD, 30f);
		titleFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("titleFont.otf"));
		titleFont = titleFont.deriveFont(200f);

		// Initializing images
		initImages();

		Evan = new Player(EVAN, EVANWIDTH, EVANHEIGHT, 25);
		EvanMaze = new Player(EVAN, 40, 40, 12);
		livingroom = new Room("Living Room", fullLivingRoomBg.getImage());
		spider = new Spider(300, 500, 70, new Rectangle(0, 360, 290, 420), spiderImg);

		currPlayer = Evan;

		Evan.setCoordinates(300, 500);
		Evan.setLowerHitBox();
		Evan.setDirection(2);

		Evan.addImage(evanBodyLEFT.getImage());
		Evan.addImage(evanBodyRIGHT.getImage());
		Evan.addImage(evanBodyDOWN.getImage());
		Evan.addImage(evanBodyUP.getImage());

		EvanMaze.setLowerHitBox(EvanMaze.getHitBox());
		EvanMaze.addImage(evanMazeHead.getImage());

		// initializing dialogue
		lastFirstDialogueMsg = new Message(MICHAEL,
				"So tell me, which room do you think is a good place to find a spider?", michaelHead);
		lastSecondDialogueMsg = new Message(MICHAEL, "Alright, now that you know where to look, start looking!",
				michaelHead);
		lastThirdDialogueMsg = new Message(MICHAEL, "Good job Evan, and good night!", michaelHead);

		// Initializing ArrayLists and spider.
		initMsgs();
		initZones();

		// initializing choice section
		kitchen = new WhiteButton("Kitchen");
		livingRoom = new WhiteButton("Living Room");
		bathroom = new WhiteButton("Bathroom");
		
		kitchen.setForeground(Color.white);
		kitchen.setFont(textFont);
		livingRoom.setForeground(Color.white);
		livingRoom.setFont(textFont);
		bathroom.setForeground(Color.white);
		bathroom.setFont(textFont);
		
		kitchen.setBounds(125, 500, 300, 100);
		livingRoom.setBounds(450, 500, 300, 100);
		bathroom.setBounds(775, 500, 300, 100);
		add(kitchen);
		add(livingRoom);
		add(bathroom);
		kitchen.setVisible(false);
		livingRoom.setVisible(false);
		bathroom.setVisible(false);

		// Initializing success screen buttons.
		returnToMenu = new WhiteButton("Menu");
		playAgain = new WhiteButton("Play Again");

		// adding listeners
		addMouseListener(this);
		addKeyListener(this);
		setFocusable(true);
		requestFocus(true);
	}

	/**
	 * Overridden method to paint the window.
	 */
	@Override
	public void paintComponent(Graphics g) {
		// draws background.
		if (!firstDialogueComplete) {
			g.drawImage(fullBedRoomBg.getImage(), 0, 0, this);
			displayEvan(g, currPlayer);
			displayMichael(g, michaelX, michaelY);
			displayFirstDialogue(g);
		} else if (choosingRoom) {
			displayChoices(g);
			// Checks for if correct button is pressed.
			if (kitchenPressed) {
				score.addScore(-30);
				displayMsg(g, new Message(MICHAEL,
						"I think the kitchen is too open and lit! Not very spider-friendly. Try again!", michaelHead));
			} else if (bathroomPressed) {
				displayMsg(g,
						new Message(MICHAEL,
								"I don't know about the bathroom, Evan! It's too bright and spacious! Try again!",
								michaelHead));
			} else if (livingRoomPressed) {
				score.addScore(100);
				choosingRoom = false;
				kitchen.setVisible(false);
				livingRoom.setVisible(false);
				bathroom.setVisible(false);
				repaint();
			}
		} else if (!secondDialogueComplete) {
			g.drawImage(fullBedRoomBg.getImage(), 0, 0, this);
			displayEvan(g, currPlayer);
			displayMichael(g, michaelX, michaelY);
			displaySecondDialogue(g);
		} else if (inMaze) {
			displayMaze(g);
			g.setFont(textFont);
			g.setColor(Color.white);
			g.drawString("Get to the end of the maze!", 10, 20);
			displayEvan(g, currPlayer);
			if (currPlayer.getX() >= 700 && currPlayer.getX() <= 800 && currPlayer.getY() <= 60
					&& currPlayer.getY() >= 20) {
				if (!beatenLevel) {
					score.addScore(150);
				}
				inMaze = false;
				inNewRoom = true;
				currPlayer = Evan;
				currPlayer.setCoordinates(800, 450);
				repaint();
			}
		} else if (inNewRoom) {
			loadRoom(g);
			displayEvan(g, currPlayer);
			// Beginning dialogue prompt by Evan, instructing user what to do.
			if (!beginningNewRoomMsgSpoken)
				displayMsg(g, new Message(EVAN, "I should look around the room and see where a spider could hide...",
						evanHead));

			g.setFont(textFont);
			g.setColor(Color.WHITE);

			// Executes if user is first person
			if (firstPerson) {
				loadRoomArea(g, currRoomArea);
				if (!currRoomArea.hasSpider()) {
					score.addScore(-30);
					displayMsg(g, new Message(EVAN, "There isn't any spider here...maybe somewhere else?", evanHead));
					g.drawString("Press \"R\" to return.", 850, 700);
				} else {
					if (failedCatch) {
						displayMsg(g, new Message(EVAN, "Come on! I can do this! Just do it like Michael taught you...",
								evanHead));
					} else if (!currRoomArea.spiderCaught()) {
						displayMsg(g, new Message(EVAN,
								"Ooh, a spider! It's so hairy...let's just catch it and get out of here!", evanHead));
					} else if (spiderCaught) {
						displayMsg(g, new Message(EVAN, "Nice! Let's get it back to Michael!", evanHead));
						g.setColor(Color.WHITE);
						g.drawString("Press \"R\" to return.", 850, 700);
					}
				}
			}
			// If Evan is in a check zone, text pops up to prompt him to check the area.
			else if (livingroom.inCheckZone(currPlayer)) {
				currRoomArea = livingroom.getCurrRoomArea(currPlayer);
				g.drawString("Press 'C' to check " + livingroom.getCurrRoomArea(currPlayer).getName() + ".", 250, 100);
			} else {
				// Guides user and instructs them
				if (beginningNewRoomMsgSpoken)
					g.drawString("Use WASD to move around and check different areas of the room!", 250, 100);
			}
		} else if (backFromRoom) {
			g.drawImage(fullBedRoomBg.getImage(), 0, 0, this);
			displayEvan(g, currPlayer);
			displayMichael(g, michaelX, michaelY);
			displayThirdDialogue(g);
		} else if (gameOver) {
			displaySuccess(g);
		}
		displayScore(g, score);
	}

	/**
	 * A method to display the rooms for Evan to choose to go to.
	 * 
	 * @param g The painting tool that helps paint the window.
	 */
	public void displayChoices(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1200, 800);

		kitchen.setVisible(true);
		livingRoom.setVisible(true);
		bathroom.setVisible(true);

		g.drawImage(smallKitchenBg.getImage(), 125, 150, 300, 300, this);
		g.drawImage(smallBathroomBg.getImage(), 775, 150, 300, 300, this);

		g.drawImage(smallLivingRoomBg.getImage(), 450, 150, 300, 300, this);

		kitchen.addActionListener(this);
		livingRoom.addActionListener(this);
		bathroom.addActionListener(this);
	}

	/**
	 * Method to display a dialogue message
	 * 
	 * @param g The painting tool that helps draw the window.
	 * @param m The message object containing information about what message to be
	 *          displayed.
	 */
	public void displayMsg(Graphics g, Message m) {
		// Retrieves attributes from the Message object.
		Image img = m.getIcon().getImage();
		String name = m.getName(), msg = m.getMessage();

		// Draws the message with the given attributes.
		g.setColor(TEXTBOXCOLOR);
		g.fillRect(0, 0, 1200, 200);
		g.drawImage(img, 80, 50, this);

		g.setColor(Color.WHITE);
		g.setFont(nameFont);
		g.drawString(name, 250, 50);

		g.setFont(textFont);
		g.drawString(msg, 250, 100);
	}

	/**
	 * The method to help draw Evan.
	 * 
	 * @param g The painting tool that helps draw the window.
	 * @param p The current Player.
	 */
	public void displayEvan(Graphics g, Player p) {
		Image img = (p.equals(Evan) ? p.getOrientedImage() : p.getImage());
		g.drawImage(img, p.getX(), p.getY(), this);
	}

	/**
	 * Method to draw Michael (npc).
	 * 
	 * @param g The painting tool that helps draw the window.
	 * @param x The top left x-coordinate.
	 * @param y The top left y-coordinate.
	 */
	public void displayMichael(Graphics g, int x, int y) {
		g.drawImage(michaelBody.getImage(), x, y, this);
	}

	/**
	 * Method to display the first set of dialogue (before Evan chooses a room)
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	private void displayFirstDialogue(Graphics g) {
		if (!firstDialogueComplete)
			displayMsg(g, msgList.get(currIndex));
		g.setFont(smallFont);
		g.drawString(
				"Click anywhere to " + (msgList.get(currIndex).equals(lastFirstDialogueMsg) ? "finish." : "continue."),
				250, 170);
	}

	/**
	 * Method to display the second set of dialogue (just after Evan chooses a room)
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	private void displaySecondDialogue(Graphics g) {
		if (!secondDialogueComplete)
			displayMsg(g, msgList.get(currIndex));
		g.setFont(smallFont);
		g.drawString(
				"Click anywhere to " + (msgList.get(currIndex).equals(lastSecondDialogueMsg) ? "finish." : "continue."),
				250, 170);
	}

	/**
	 * Method to display third and final set of dialogue (after Evan returns with a
	 * spider).
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	private void displayThirdDialogue(Graphics g) {
		if (!thirdDialogueComplete) {
			displayMsg(g, msgList.get(currIndex));
		}
	}

	/**
	 * Method to display a success screen.
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	private void displaySuccess(Graphics g) {
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
	 * Method to load the current room.
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	private void loadRoom(Graphics g) {
		Image img = livingroom.getBg();
		g.drawImage(img, 0, 0, 1200, 800, this);
	}

	private void loadRoomArea(Graphics g, RoomArea ra) {
		Image img = ra.getBg();
		Spider s = ra.getSpider();
		g.drawImage(img, 0, 0, 1200, 800, this);
		if (s != null) {
			Image sImg = s.getImg();
			if (!s.isRemoved()) {
				g.drawImage(sImg, spider.getX(), spider.getY(), spider.getWidth(), spider.getWidth(), this);
			}
		}
	}

	/**
	 * Method to reset all the variables that control the state of the game to their
	 * original states.
	 */
	private void resetLevel() {
		returnToMenu.setVisible(false);
		playAgain.setVisible(false);

		currIndex = 0;

		firstDialogueComplete = false;
		secondDialogueComplete = false;
		thirdDialogueComplete = false;
		choosingRoom = false;
		inMaze = false;
		inNewRoom = false;
		beginningNewRoomMsgSpoken = false;
		firstPerson = false;
		spiderCaught = false;
		failedCatch = false;
		backFromRoom = false;
		gameOver = false;

		kitchenPressed = false;
		livingRoomPressed = false;
		bathroomPressed = false;

		currRoomArea = null;
		keyPressedNum = 0;

		Evan.setCoordinates(300, 500);
		Evan.setDirection(2);
		EvanMaze.setCoordinates(100, 575);

		spider.setStatus(false);
		spider.randomizeCords();

		score.resetScore();
	}

	/**
	 * Method to check if user has beaten the level before.
	 * 
	 * @return True if user has beaten the level.
	 */
	public boolean beatenLevel() {
		return beatenLevel;
	}

	/**
	 * Method to draw maze and Evan in maze.
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	private void displayMaze(Graphics g) {
		// wall is 25 pixels wide
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1200, 800);

		g.drawImage(inArrow.getImage(), 50, 575, 30, 30, this);
		g.drawImage(outArrow.getImage(), 740, 20, 30, 30, this);
		drawMaze(g);
	}

	/**
	 * Method to draw the maze walls.
	 * 
	 * @param g The painting tool that helps draw the window.
	 */
	private void drawMaze(Graphics g) {
		// Draws borders of maze
		drawMazeWall(g, 100, 50, 25, 500);
		drawMazeWall(g, 100, 625, 25, 100);
		drawMazeWall(g, 100, 50, 600, 25);
		drawMazeWall(g, 790, 50, 325, 25);
		drawMazeWall(g, 1100, 50, 25, 700);
		drawMazeWall(g, 100, 725, 1025, 25);

		drawMazeWall(g, 100, 625, 80, 25);

		drawMazeWall(g, 180, 50, 25, 230);
		drawMazeWall(g, 180, 348, 80, 25);
		drawMazeWall(g, 180, 440, 25, 110);
		drawMazeWall(g, 180, 525, 100, 25);

		drawMazeWall(g, 260, 130, 100, 25);
		drawMazeWall(g, 260, 210, 25, 255);
		drawMazeWall(g, 260, 210, 350, 25);
		drawMazeWall(g, 260, 440, 100, 25);
		drawMazeWall(g, 260, 525, 25, 100);
		drawMazeWall(g, 260, 625, 100, 25);

		drawMazeWall(g, 335, 130, 25, 80);
		drawMazeWall(g, 335, 290, 365, 25);
		drawMazeWall(g, 335, 290, 25, 85);
		drawMazeWall(g, 335, 575, 390, 25);
		drawMazeWall(g, 335, 440, 25, 210);

		drawMazeWall(g, 425, 130, 275, 25);
		drawMazeWall(g, 425, 375, 300, 25);
		drawMazeWall(g, 425, 400, 25, 90);
		drawMazeWall(g, 425, 490, 125, 25);
		drawMazeWall(g, 425, 655, 25, 70);

		drawMazeWall(g, 515, 655, 25, 70);

		drawMazeWall(g, 630, 490, 70, 25);

		drawMazeWall(g, 700, 50, 25, 325);
		drawMazeWall(g, 700, 490, 25, 170);
		drawMazeWall(g, 700, 575, 115, 25);

		drawMazeWall(g, 790, 130, 310, 25);
		drawMazeWall(g, 790, 130, 25, 185);
		drawMazeWall(g, 790, 290, 100, 25);
		drawMazeWall(g, 790, 490, 310, 25);
		drawMazeWall(g, 790, 490, 25, 90);

		drawMazeWall(g, 865, 210, 235, 25);
		drawMazeWall(g, 865, 290, 25, 110);
		drawMazeWall(g, 865, 575, 25, 175);

		drawMazeWall(g, 960, 490, 25, 125);
		drawMazeWall(g, 960, 290, 140, 25);
		drawMazeWall(g, 960, 375, 140, 25);

		drawMazeWall(g, 960, 575, 25, 75);
	}

	/**
	 * Method to draw a maze wall
	 * 
	 * @param g      The painting tool that helps draw the window.
	 * @param x      The top left x-coordinate.
	 * @param y      The top left y-coordinate.
	 * @param height The length of the wall.
	 */
	private void drawMazeWall(Graphics g, int x, int y, int width, int height) {
		Rectangle mazeWall = new Rectangle(x, y, width, height);
		if (!mazeWalls.contains(mazeWall))
			mazeWalls.add(mazeWall);
		g.setColor(MAZEWALLCOLOR);
		g.fillRect(x, y, width, height);
	}

	/**
	 * Checks if the coordinate is within a wall.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return True if the pixel is the colour of a maze wall.
	 */
	private boolean isWall(int x, int y) {
		for (Rectangle r : mazeWalls) {
			if (r.contains(x, y))
				return true;
		}
		return false;
	}

	/**
	 * Method to initialize the room zones, with zones that are out of bounds to
	 * Evan, and zones that Evan can stand in to trigger checks.
	 */
	private void initZones() {
		livingroom.addBoundaries(new Rectangle(0, -10, 1200, 10));
		livingroom.addBoundaries(new Rectangle(-10, 0, 10, 800));
		livingroom.addBoundaries(new Rectangle(1200, 0, 10, 800));
		livingroom.addBoundaries(new Rectangle(0, 800, 1200, 10));
		livingroom.addBoundaries(new Rectangle(0, 0, 1200, 250));

		livingroom.addBoundaries(new Rectangle(0, 200, 400, 420));
		livingroom.addBoundaries(new Rectangle(620, 200, 200, 150));
		livingroom.addBoundaries(new Rectangle(1000, 150, 200, 200));
		livingroom.addBoundaries(new Rectangle(1050, 530, 150, 220));

		RoomArea behindCouch = new RoomArea("behind the couch", behindCouchImg.getImage(),
				new Rectangle(0, 520, 200, 220), spider);
		RoomArea underDrawer = new RoomArea("under the drawer", underDrawerImg.getImage(),
				new Rectangle(620, 280, 200, 200));
		RoomArea behindPlant = new RoomArea("behind the plant", behindPlantImg.getImage(),
				new Rectangle(960, 200, 250, 300));

		livingroom.addArea(behindCouch);
		livingroom.addArea(underDrawer);
		livingroom.addArea(behindPlant);
	}

	/**
	 * Method to initialize all the images.
	 */
	private void initImages() {
		michaelHead = new ImageIcon(getClass().getClassLoader().getResource("michael-profile.png"));
		evanHead = new ImageIcon(getClass().getClassLoader().getResource("evan-profile.png"));
		spiderImg = new ImageIcon(getClass().getClassLoader().getResource("spider_crawling.png"));

		evanMazeHead = new ImageIcon(getClass().getClassLoader().getResource("evan-maze.png"));
		evanMazeHead = new ImageIcon(
				evanMazeHead.getImage().getScaledInstance(EVANMAZEWIDTH, EVANMAZEWIDTH, Image.SCALE_SMOOTH));

		endImage = new ImageIcon(getClass().getClassLoader().getResource("end_screen.png"));
		endImage = new ImageIcon(endImage.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		fullBedRoomBg = new ImageIcon(getClass().getClassLoader().getResource("evans_room-full_view.png"));
		fullBedRoomBg = new ImageIcon(fullBedRoomBg.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		fullLivingRoomBg = new ImageIcon(getClass().getClassLoader().getResource("living-room.png"));

		smallLivingRoomBg = new ImageIcon(getClass().getClassLoader().getResource("living_room-300x300.png"));
		smallLivingRoomBg = new ImageIcon(smallLivingRoomBg.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));

		smallKitchenBg = new ImageIcon(getClass().getClassLoader().getResource("kitchen.png"));
		smallKitchenBg = new ImageIcon(smallKitchenBg.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));

		smallBathroomBg = new ImageIcon(getClass().getClassLoader().getResource("bathroom.png"));
		smallBathroomBg = new ImageIcon(smallBathroomBg.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));

		inArrow = new ImageIcon(getClass().getClassLoader().getResource("right_arrow.png"));
		outArrow = new ImageIcon(getClass().getClassLoader().getResource("up_arrow.png"));

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

		behindCouchImg = new ImageIcon(getClass().getClassLoader().getResource("behind_sofa.png"));
		behindPlantImg = new ImageIcon(getClass().getClassLoader().getResource("behind_the_flower_pot.png"));
		underDrawerImg = new ImageIcon(getClass().getClassLoader().getResource("under_the_drawer.png"));

	}

	/**
	 * Method to check whether or not Evan can move in a certain direction in the
	 * maze.
	 * 
	 * @param dir Evan's desired direction of movement.
	 * @return True if there are no walls in the way and Evan can move freely.
	 */
	private boolean validMazeMove(int dir) {
		// If Evan moves LEFT
		int evanMazeX = currPlayer.getX();
		int evanMazeY = currPlayer.getY();
		int move = currPlayer.getMoveDist();
		int wid = currPlayer.getWidth();

		if (dir == KeyEvent.VK_A) {
			if (evanMazeX - move < 100)
				return false;
			for (int i = evanMazeX; i >= evanMazeX - move; i--) {
				if (isWall(i, evanMazeY) || isWall(i, evanMazeY + wid)) {
					return false;
				}
			}
			return true;
		}
		// If Evan moves RIGHT
		else if (dir == KeyEvent.VK_D) {
			if (evanMazeX + move > 1100)
				return false;
			for (int i = evanMazeX + wid; i <= evanMazeX + wid + move; i++) {
				if (isWall(i, evanMazeY) || isWall(i, evanMazeY + wid)) {
					return false;
				}
			}
			return true;
		}
		// If Evan moves UP
		else if (dir == KeyEvent.VK_W) {
			if (evanMazeY - move < 50)
				return false;
			for (int j = evanMazeY; j >= evanMazeY - move; j--) {
				if (isWall(evanMazeX, j) || isWall(evanMazeX + wid, j)) {
					return false;
				}
			}
			return true;
		}
		// If Evan moves DOWN
		else {
			if (evanMazeY + move > 750)
				return false;
			for (int j = evanMazeY + wid; j <= evanMazeY + wid + move; j++) {
				if (isWall(evanMazeX, j) || isWall(evanMazeX + wid, j)) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * Method to initialize dialogue.
	 */
	private void initMsgs() {
		msgList.add(new Message(MICHAEL,
				"Now, this time, I want you to go through the house and catch a spider by yourself.", michaelHead));
		msgList.add(new Message(EVAN, "I don't know, Michael...", evanHead));
		msgList.add(new Message(MICHAEL, "Don't worry! I know that you can do this!", michaelHead));
		msgList.add(new Message(MICHAEL, "Sometimes, the best way to face a fear is to confront it head on!", michaelHead));
		msgList.add(new Message(MICHAEL, "If you are exposed to spiders, you soon feel less scared of them.", michaelHead));
		msgList.add(new Message(MICHAEL, "This is because you get to see for yourself that they aren't scary!", michaelHead));
		msgList.add(new Message(MICHAEL, "Can you do that Evan? Can you be brave for me?", michaelHead));
		msgList.add(new Message(EVAN, ".....", evanHead));
		msgList.add(new Message(EVAN, "Okay. I can do it.", evanHead));
		msgList.add(new Message(MICHAEL, "Amazing! I have faith you can do this!", michaelHead));
		msgList.add(new Message(MICHAEL, "However, I should first explain to you where a spider likes to hide.",
				michaelHead));
		msgList.add(new Message(MICHAEL, "Since spiders like to build webs and wait for their prey...", michaelHead));
		msgList.add(new Message(MICHAEL, "...a good place to find them is in SMALL, DARK corners!", michaelHead));
		msgList.add(new Message(EVAN, "Like the corner of my bedroom?", evanHead));
		msgList.add(new Message(MICHAEL,
				"Yes, like that. Now, the first thing that we have to do is find a spider to catch.", michaelHead));
		msgList.add(lastFirstDialogueMsg);

		// second batch of dialogue appears after choice graphic
		msgList.add(new Message(MICHAEL, "Nice! You'll be likely to find a spider in our living room!", michaelHead));
		msgList.add(new Message(MICHAEL, "It's dark, closed, and small, perfect for a spider to hide!", michaelHead));
		msgList.add(new Message(MICHAEL, "I want you to head there and bring me back a spider.", michaelHead));
		msgList.add(lastSecondDialogueMsg);

		// third batch of dialogue appears after Evan finds and returns with a spider.
		msgList.add(new Message(MICHAEL, "Not too hard now, was it?", michaelHead));
		msgList.add(new Message(EVAN, "I guess that wasn't too bad!", evanHead));
		msgList.add(new Message(MICHAEL, "Did this help you feel less afraid?", michaelHead));
		msgList.add(new Message(EVAN, "Yeah. Thanks, Michael.", evanHead));
		msgList.add(
				new Message(MICHAEL, "Good for you, little bro! Now, let’s try and get some sleep now.", michaelHead));
		msgList.add(lastThirdDialogueMsg);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(kitchen)) {
			kitchenPressed = true;
			bathroomPressed = false;
			livingRoomPressed = false;
			repaint();
		} else if (e.getSource().equals(bathroom)) {
			bathroomPressed = true;
			kitchenPressed = false;
			livingRoomPressed = false;
			repaint();
		} else if (e.getSource().equals(livingRoom)) {
			livingRoomPressed = true;
			kitchenPressed = false;
			bathroomPressed = false;
			repaint();
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

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (inMaze) {
			keyPressedNum++;
			if (keyPressedNum >= 30) {
				score.addScore(-5);
				keyPressedNum = 0;
			}
			if (keyCode == KeyEvent.VK_A) {
				if (validMazeMove(keyCode))
					currPlayer.moveHorizontally(-currPlayer.getMoveDist());
			} else if (keyCode == KeyEvent.VK_W) {
				if (validMazeMove(keyCode))
					currPlayer.moveVertically(-currPlayer.getMoveDist());
			} else if (keyCode == KeyEvent.VK_D) {
				if (validMazeMove(keyCode))
					currPlayer.moveHorizontally(currPlayer.getMoveDist());
			} else if (keyCode == KeyEvent.VK_S) {
				if (validMazeMove(keyCode))
					currPlayer.moveVertically(currPlayer.getMoveDist());
			}
			currPlayer.setLowerHitBox();
			repaint();
		} else if (inNewRoom && beginningNewRoomMsgSpoken) {
			if (livingroom.inCheckZone(currPlayer) && firstPerson) {
				if (!currRoomArea.hasSpider()) {
					if (keyCode == KeyEvent.VK_R) {
						firstPerson = false;
						repaint();
					}
				} else {
					if (keyCode == KeyEvent.VK_R) {
						if (spiderCaught) {
							backFromRoom = true;
							inNewRoom = false;
							firstPerson = false;
							currPlayer = Evan;
							Evan.setCoordinates(300, 500);
							Evan.setDirection(2);
						}
						repaint();
					}

				}
			}
			if (livingroom.inCheckZone(currPlayer)) {
				if (keyCode == KeyEvent.VK_C) {
					firstPerson = true;
					currRoomArea = livingroom.getCurrRoomArea(currPlayer);
					repaint();
				}
			}
			if (keyCode == KeyEvent.VK_A) {
				if (livingroom.validMove(currPlayer, -currPlayer.getMoveDist(), 0))
					currPlayer.move(-(currPlayer.getMoveDist()), 0);
				currPlayer.setDirection(0);
			} else if (keyCode == KeyEvent.VK_W) {
				if (livingroom.validMove(currPlayer, 0, -currPlayer.getMoveDist()))
					currPlayer.move(0, -(currPlayer.getMoveDist()));
				currPlayer.setDirection(3);
			} else if (keyCode == KeyEvent.VK_D) {
				if (livingroom.validMove(currPlayer, currPlayer.getMoveDist(), 0))
					currPlayer.move(currPlayer.getMoveDist(), 0);
				currPlayer.setDirection(1);
			} else if (keyCode == KeyEvent.VK_S) {
				if (livingroom.validMove(currPlayer, 0, currPlayer.getMoveDist()))
					currPlayer.move(0, currPlayer.getMoveDist());
				currPlayer.setDirection(2);
			}
			currPlayer.setLowerHitBox();
			repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!firstDialogueComplete) {
			if (msgList.get(currIndex).equals(lastFirstDialogueMsg)) {
				firstDialogueComplete = true;
				choosingRoom = true;
			}
			currIndex++;
			repaint();
		} else if (!secondDialogueComplete) {
			if (msgList.get(currIndex).equals(lastSecondDialogueMsg)) {
				secondDialogueComplete = true;
				inMaze = true;
				currPlayer = EvanMaze;
				currPlayer.setCoordinates(100, 575);
				currPlayer.setLowerHitBox(currPlayer.getHitBox());
			}
			currIndex++;
			repaint();
		} else if (inNewRoom && !beginningNewRoomMsgSpoken) {
			beginningNewRoomMsgSpoken = true;
			repaint();
		} else if (inNewRoom && firstPerson && currRoomArea.hasSpider() && !spiderCaught) {
			int mX = e.getX(), mY = e.getY();
			if (spider.isCaught(mX, mY)) {
				score.addScore(500 - 100 * failedAttempts);
				spiderCaught = true;
				failedCatch = false;
				spider.setStatus(true);
			} else {
				failedCatch = true;
				spider.randomizeCords();
				failedAttempts++;
			}
			repaint();
		} else if (backFromRoom && !thirdDialogueComplete) {
			if (msgList.get(currIndex).equals(lastThirdDialogueMsg)) {
				thirdDialogueComplete = true;
				gameOver = true;
				backFromRoom = false;
				beatenLevel = true;
			}
			currIndex++;
			repaint();
		}
	}

	// Unused methods.

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
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
