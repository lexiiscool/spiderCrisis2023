package Main;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

/**
 * Date: June 8, 2023
 * <p>
 * Version 3.0 of the Testing Stage. Completed level, including scoring,
 * graphics, and gameplay. Provided by JELTech Industries.
 * </p>
 * <h2>Class: ICS4UO with V.Krasteva</h2>
 * 
 * @author Lexi Han
 * @version 23.06.06
 */
public class ActionStage extends JComponent implements ActionListener, MouseListener, KeyListener {
	private CardLayout layoutManager;

	// Added by Ethan Andrew on June 6, 2023
	private Score score = new Score();

	/**
	 * Constant variables
	 */
	private final String EVAN = "Evan";
	private final String MICHAEL = "Michael";
	private final Color TEXTBOXCOLOR = new Color(0, 0, 0, 125);

	private Font nameFont;
	private Font textFont;
	private Font smallFont;
	private Font successFont;
	private Font titleFont;

	/**
	 * Keeps track of all messages in the dialogue.
	 */
	private ArrayList<Message> msgList;

	/**
	 * Keeps track of all rooms in the dialogue.
	 */
	private ArrayList<Room> allRooms;

	/**
	 * Keeps track of frames in animations.
	 */
	private ArrayList<ImageIcon> firstAnimationFrames, lastAnimationFrames;

	/**
	 * Rooms to be explored.
	 */
	private Room evanBedRoom, hallway, kitchen, bathroom, livingRoom, exit;

	// Variables for images
	private ImageIcon michaelHead, evanHead, evanSleepingHead, spiderBody;
	private ImageIcon evanBodyLEFT, evanBodyRIGHT, evanBodyUP, evanBodyDOWN;
	private ImageIcon bedroomBg, kitchenBg, livingroomBg, bathroomBg, hallwayBg, exitBg, morningBedroomBg;
	private ImageIcon livingRoomBehindSofa, livingRoomUnderTable, livingRoomUnderDrawer;
	private ImageIcon kitchenCrevice, kitchenStove, kitchenCabinet;
	private ImageIcon bathroomCabinet, bathroomTub, bathroomToilet, bathroomVoid;
	private ImageIcon exitDoor;
	private ImageIcon keyImg;
	private ImageIcon endImage;

	private ImageIcon lArr, rArr, uArr, dArr;
	private ImageIcon fullHeart, halfHeart;
	private ImageIcon firstBufferFrame, lastBufferFrame;

	private Player evan, evanHallway, currPlayer;
	private Rectangle keyHitBox;

	private Room currRoom;
	private RoomArea currRoomArea, keyLocation;

	private Rectangle doorKnobHb;

	private Message lastFirstDialogue;
	private Message lastSecondDialogue;

	/**
	 * Variables to keep track of the game.
	 */
	private int currIndex = 0;
	private int animIndex = 0;
	private boolean inGameplay = false;
	private boolean loseGame = false;
	private boolean winGame = false;
	private boolean checkingArea = false;
	private boolean keyCreated = false;
	private boolean keyAcquired = false;
	private boolean inFirstDialogue = true;
	private boolean inLastDialogue = false;
	private boolean inFirstAnimation = false;
	private boolean inLastAnimation = false;

	/**
	 * Buttons to control state of game.
	 */
	JButton returnToMenu, playAgain;

	/**
	 * Action Stage constructor. Initializes variables that haven't been already.
	 * 
	 * @param cl Card layout that helps users switch screens.
	 * @throws IOException
	 * @throws FontFormatException
	 */
	public ActionStage(CardLayout cl) throws IOException, FontFormatException, URISyntaxException {
		layoutManager = cl;
		setLayout(null);

		msgList = new ArrayList<Message>();
		allRooms = new ArrayList<Room>();
		firstAnimationFrames = new ArrayList<ImageIcon>();
		lastAnimationFrames = new ArrayList<ImageIcon>();

		nameFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("PixeloidSans-mLxMm.ttf"));
		textFont = nameFont.deriveFont(20f);
		smallFont = nameFont.deriveFont(Font.ITALIC, 14f);
		successFont = nameFont.deriveFont(43f);
		nameFont = nameFont.deriveFont(Font.BOLD, 25f);
		titleFont = Font.createFont(Font.TRUETYPE_FONT,
				getClass().getClassLoader().getResourceAsStream("titleFont.otf"));
		titleFont = titleFont.deriveFont(200f);

		// initializing player
		evan = new Player(EVAN, 150, 225, 20);
		evan.setCoordinates(170, 350);
		evan.setLowerHitBox();

		evanHallway = new Player(EVAN, 70, 70, 15);
		evanHallway.setLowerHitBox();

		doorKnobHb = new Rectangle(30, 230, 410, 430);

		initImages();

		// initializing rooms
		initRooms();

		// initializing lists
		initMsgs();
		initBounds();

		currRoom = evanBedRoom;
		currPlayer = evan;

		returnToMenu = new WhiteButton("Menu");
		playAgain = new WhiteButton("Play Again");

		// Adding listeners
		returnToMenu.addActionListener(this);
		playAgain.addActionListener(this);

		addMouseListener(this);
		addKeyListener(this);
		requestFocus();
		setFocusable(true);
	}

	/**
	 * Overridden method to display graphics to window.
	 */
	@Override
	public void paintComponent(Graphics g) {
		if (loseGame) {
			displayGameOver(g);
		} else if (winGame) {
			displayTheEnd(g);
		} else if (inFirstDialogue) {
			displayFirstDialogue(g);
		} else if (inLastDialogue) {
			displayLastDialogue(g);
		} else if (inFirstAnimation) {
			displayFirstAnimation(g);
		} else if (inLastAnimation) {
			displayLastAnimation(g);
		} else if (inGameplay) {
			clearScreen();
			if (checkingArea) {
				loadRoomArea(g, currRoomArea);
				displayHearts(g, currPlayer);
				displayScore(g, score);
			} else {
				loadRoom(g, currRoom);
				displayPlayer(g);
				
				g.setColor(TEXTBOXCOLOR);
				g.fillRect(925, 200, 275, 75);
				g.setColor(Color.white);
				g.drawString("Objective: Get OUT!", 940, 245);
				
				g.drawString("Use the WASD keys to move around the house.", 700, 750);
				
				displayHearts(g, currPlayer);
				displayScore(g, score);
				if (currRoom.inExitZone(currPlayer)) {
					g.setFont(textFont);
					g.setColor(Color.WHITE);
					String newRoom = "";

					if (currRoom.equals(hallway)) {
						if (currRoom.getCurrExitZone(currPlayer).equals(currRoom.getExitZone(0))) {
							newRoom = livingRoom.getName();
						} else if (currRoom.getCurrExitZone(currPlayer).equals(currRoom.getExitZone(1))) {
							newRoom = bathroom.getName();
						} else if (currRoom.getCurrExitZone(currPlayer).equals(currRoom.getExitZone(2))) {
							newRoom = evanBedRoom.getName();
						} else if (currRoom.getCurrExitZone(currPlayer).equals(currRoom.getExitZone(3))) {
							newRoom = exit.getName();
						}

						g.drawString("Press 'E' to enter " + newRoom + ".", 100, 50);
					} else if (currRoom.equals(livingRoom)) {
						if (currRoom.getCurrExitZone(currPlayer).equals(currRoom.getExitZone(0))) {
							g.drawString("Press 'E' to exit " + currRoom.getName() + ".", 100, 50);
						} else if (currRoom.getCurrExitZone(currPlayer).equals(currRoom.getExitZone(1))) {
							newRoom = kitchen.getName();
							g.drawString("Press 'E' to enter " + newRoom + ".", 100, 50);
						}
					} else {
						g.drawString("Press 'E' to exit " + currRoom.getName() + ".", 100, 50);
					}
				} else if (currRoom.inCheckZone(currPlayer)) {
					g.setFont(textFont);
					g.setColor(Color.WHITE);
					g.drawString("Press 'C' to check " + currRoom.getCurrRoomArea(currPlayer).getName() + ".", 100, 50);
				}
			}
		}
	}

	/**
	 * Method to display the current Player.
	 * 
	 * @param g The tool to help paint the window.
	 */
	private void displayPlayer(Graphics g) {
		Rectangle hitbox = currPlayer.getHitBox();
		currPlayer.setLowerHitBox();
		Image img = (currPlayer == evan ? currPlayer.getOrientedImage() : currPlayer.getImage());
		g.drawImage(img, (int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight(),
				this);
	}

	/**
	 * Method to display the loss screen (if the Player dies).
	 * 
	 * @param g The tool to help paint the window.
	 */
	private void displayGameOver(Graphics g) {
		// Method to be completed.
		g.setColor(Color.white);
		g.fillRect(0, 0, 1200, 800);
		g.fillRect(0, 0, 1200, 800);

		g.setColor(Color.red);
		g.setFont(titleFont);
		g.drawString("GAME OVER...", 220, 350);
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
	 * Method to display the end screen (if Player wins the game).
	 * 
	 * @param g The tool to help paint the window.
	 */
	private void displayTheEnd(Graphics g) {
		g.drawImage(endImage.getImage(), 0, 0, this);

		g.setColor(Color.WHITE);
		g.setFont(nameFont.deriveFont(50f));
		g.drawString("Final Score", 700, 425);
		g.setFont(nameFont.deriveFont(72f));
		g.drawString(String.valueOf(score.getScore()), 700, 500);

		g.setFont(textFont);
		g.drawString("Click anywhere to return to the main menu.", 625, 650);
	}

	/**
	 * Method to initialize all messages to be said in the dialogue.
	 */
	private void initMsgs() {
		lastFirstDialogue = new Message(EVAN, "I'm getting out of here...there has to be a way out of this nightmare!",
				evanHead);
		lastSecondDialogue = new Message(EVAN, "Coming!", evanHead);

		msgList.add(new Message(EVAN, "What the...", evanHead));
		msgList.add(new Message(EVAN, "What's going on?", evanHead));
		msgList.add(new Message(EVAN, "I don't like this...", evanHead));
		msgList.add(lastFirstDialogue);
		msgList.add(new Message(EVAN, "Huh...? A dream?", evanHead));
		msgList.add(new Message(EVAN, "Thank goodness. Those spiders weren't as scary as I thought.", evanHead));
		msgList.add(new Message(EVAN, "Looks like Michael's advice worked! I feel a lot better!", evanHead));
		msgList.add(new Message(MICHAEL, "Evan! Breakfast!", michaelHead));
		msgList.add(lastSecondDialogue);
	}

	/**
	 * Method to initialize the images.
	 */
	private void initImages() {
		// initialize images
		michaelHead = new ImageIcon(getClass().getClassLoader().getResource("michael-profile.png"));
		evanHead = new ImageIcon(getClass().getClassLoader().getResource("evan-profile.png"));
		evanSleepingHead = new ImageIcon(getClass().getClassLoader().getResource("evan-sleeping_view.png"));
		spiderBody = new ImageIcon(getClass().getClassLoader().getResource("nightmare_spider.png"));
		endImage = new ImageIcon(getClass().getClassLoader().getResource("end_screen.png"));
		endImage = new ImageIcon(endImage.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		bedroomBg = new ImageIcon(getClass().getClassLoader().getResource("evans_nightmare_room.png"));
		bedroomBg = new ImageIcon(bedroomBg.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));
		kitchenBg = new ImageIcon(getClass().getClassLoader().getResource("nightmare_kitchen.png"));
		bathroomBg = new ImageIcon(getClass().getClassLoader().getResource("nightmare_bathroom.png"));
		livingroomBg = new ImageIcon(getClass().getClassLoader().getResource("nightmare_living_room.png"));
		morningBedroomBg = new ImageIcon(getClass().getClassLoader().getResource("evans_room_morning.png"));
		morningBedroomBg = new ImageIcon(morningBedroomBg.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		hallwayBg = new ImageIcon(getClass().getClassLoader().getResource("hallway.png"));
		exitBg = new ImageIcon(getClass().getClassLoader().getResource("exit_room.png"));

		endImage = new ImageIcon(getClass().getClassLoader().getResource("end_screen.png"));
		endImage = new ImageIcon(endImage.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH));

		livingRoomBehindSofa = new ImageIcon(getClass().getClassLoader().getResource("nightmare_living_room-sofa.png"));
		livingRoomUnderTable = new ImageIcon(
				getClass().getClassLoader().getResource("nightmare_living_room-table.png"));
		livingRoomUnderDrawer = new ImageIcon(
				getClass().getClassLoader().getResource("nightmare_living_room-drawer.png"));

		kitchenCrevice = new ImageIcon(getClass().getClassLoader().getResource("nightmare_kitchen-crevice.png"));
		kitchenStove = new ImageIcon(getClass().getClassLoader().getResource("nightmare_kitchen-stove.png"));
		kitchenCabinet = new ImageIcon(getClass().getClassLoader().getResource("nightmare_kitchen-cabinet.png"));

		bathroomCabinet = new ImageIcon(getClass().getClassLoader().getResource("nightmare_bathroom-cabinet.png"));
		bathroomTub = new ImageIcon(getClass().getClassLoader().getResource("nightmare_bathroom-bathtub.png"));
		bathroomToilet = new ImageIcon(getClass().getClassLoader().getResource("nightmare_bathroom-toilet.png"));
		bathroomVoid = new ImageIcon(getClass().getClassLoader().getResource("nightmare_bathroom-void.png"));

		exitDoor = new ImageIcon(getClass().getClassLoader().getResource("door_knob.png"));
		keyImg = new ImageIcon(getClass().getClassLoader().getResource("door_key.png"));

		evanBodyLEFT = new ImageIcon(getClass().getClassLoader().getResource("evan-left_view.png"));
		evanBodyLEFT = new ImageIcon(
				evanBodyLEFT.getImage().getScaledInstance(evan.getWidth(), evan.getHeight(), Image.SCALE_SMOOTH));

		evanBodyRIGHT = new ImageIcon(getClass().getClassLoader().getResource("evan-right_view.png"));
		evanBodyRIGHT = new ImageIcon(
				evanBodyRIGHT.getImage().getScaledInstance(evan.getWidth(), evan.getHeight(), Image.SCALE_SMOOTH));

		evanBodyUP = new ImageIcon(getClass().getClassLoader().getResource("evan-back_view.png"));
		evanBodyUP = new ImageIcon(
				evanBodyUP.getImage().getScaledInstance(evan.getWidth(), evan.getHeight(), Image.SCALE_SMOOTH));

		evanBodyDOWN = new ImageIcon(getClass().getClassLoader().getResource("evan-front_view.png"));
		evanBodyDOWN = new ImageIcon(
				evanBodyDOWN.getImage().getScaledInstance(evan.getWidth(), evan.getHeight(), Image.SCALE_SMOOTH));

		// adding images to Evan
		evan.addImage(evanBodyLEFT.getImage());
		evan.addImage(evanBodyRIGHT.getImage());
		evan.addImage(evanBodyDOWN.getImage());
		evan.addImage(evanBodyUP.getImage());

		evanHallway.addImage(evanHead.getImage());

		rArr = new ImageIcon(getClass().getClassLoader().getResource("right_arrow.png"));
		rArr = new ImageIcon(rArr.getImage().getScaledInstance(60, 30, Image.SCALE_SMOOTH));
		lArr = new ImageIcon(getClass().getClassLoader().getResource("left_arrow.png"));
		lArr = new ImageIcon(lArr.getImage().getScaledInstance(60, 30, Image.SCALE_SMOOTH));
		uArr = new ImageIcon(getClass().getClassLoader().getResource("up_arrow.png"));
		uArr = new ImageIcon(uArr.getImage().getScaledInstance(30, 60, Image.SCALE_SMOOTH));
		dArr = new ImageIcon(getClass().getClassLoader().getResource("down_arrow.png"));
		dArr = new ImageIcon(dArr.getImage().getScaledInstance(30, 60, Image.SCALE_SMOOTH));

		fullHeart = new ImageIcon(getClass().getClassLoader().getResource("full_heart.png"));
		fullHeart = new ImageIcon(fullHeart.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
		halfHeart = new ImageIcon(getClass().getClassLoader().getResource("half_heart.png"));
		halfHeart = new ImageIcon(halfHeart.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));

		firstAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_0.png")));
		firstAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_1.png")));
		firstAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_2.png")));
		firstAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_3.png")));
		firstAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_4.png")));

		firstBufferFrame = new ImageIcon(getClass().getClassLoader().getResource("ending_frame_4.png"));

		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_5.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_6.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_7.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_8.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_9.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_10.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_11.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_12.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_13.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_14.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_15.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_16.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_17.png")));
		lastAnimationFrames.add(new ImageIcon(getClass().getClassLoader().getResource("ending_frame_18.png")));

		lastBufferFrame = new ImageIcon(getClass().getClassLoader().getResource("ending_frame_18.png"));

		lastAnimationFrames.add(lastBufferFrame);

	}

	/**
	 * Method to initialize rooms.
	 */
	private void initRooms() {
		evanBedRoom = new Room("Bedroom", bedroomBg.getImage());
		kitchen = new Room("Kitchen", kitchenBg.getImage());
		bathroom = new Room("Bathroom", bathroomBg.getImage());
		livingRoom = new Room("Living Room", livingroomBg.getImage());
		hallway = new Room("Hallway", hallwayBg.getImage());
		exit = new Room("Entryway", exitBg.getImage());

		allRooms.add(evanBedRoom);
		allRooms.add(kitchen);
		allRooms.add(hallway);
		allRooms.add(bathroom);
		allRooms.add(livingRoom);
		allRooms.add(exit);
	}

	/**
	 * Method to initialize boundaries within each Room.
	 */
	private void initBounds() {
		evanBedRoom.addBoundaries(new Rectangle(0, 0, 1200, 500));
		evanBedRoom.addBoundaries(new Rectangle(0, 790, 1200, 100));
		evanBedRoom.addBoundaries(new Rectangle(0, 500, 10, 300));
		evanBedRoom.addBoundaries(new Rectangle(1200, 500, 100, 300));
		evanBedRoom.addBoundaries(new Rectangle(420, 0, 380, 650));

		hallway.addBoundaries(new Rectangle(0, 0, 545, 320));
		hallway.addBoundaries(new Rectangle(680, 0, 545, 320));
		hallway.addBoundaries(new Rectangle(0, 440, 545, 360));
		hallway.addBoundaries(new Rectangle(680, 440, 545, 360));
		hallway.addBoundaries(new Rectangle(-10, 0, 10, 800));
		hallway.addBoundaries(new Rectangle(0, -10, 1200, 10));
		hallway.addBoundaries(new Rectangle(1200, 0, 10, 800));
		hallway.addBoundaries(new Rectangle(0, 800, 1200, 10));

		kitchen.addBoundaries(new Rectangle(0, 0, 1200, 685));
		kitchen.addBoundaries(new Rectangle(-10, 0, 10, 800));
		kitchen.addBoundaries(new Rectangle(0, 800, 1200, 10));
		kitchen.addBoundaries(new Rectangle(1200, 0, 10, 800));

		bathroom.addBoundaries(new Rectangle(0, 0, 1200, 250));
		bathroom.addBoundaries(new Rectangle(-10, 0, 10, 800));
		bathroom.addBoundaries(new Rectangle(0, 800, 1200, 10));
		bathroom.addBoundaries(new Rectangle(1200, 0, 10, 800));
		bathroom.addBoundaries(new Rectangle(0, 0, 520, 365));
		bathroom.addBoundaries(new Rectangle(830, 0, 370, 315));
		bathroom.addBoundaries(new Rectangle(900, 300, 290, 450));

		livingRoom.addBoundaries(new Rectangle(0, 0, 1200, 250));
		livingRoom.addBoundaries(new Rectangle(-10, 0, 10, 800));
		livingRoom.addBoundaries(new Rectangle(0, 800, 1200, 10));
		livingRoom.addBoundaries(new Rectangle(1200, 0, 10, 800));
		livingRoom.addBoundaries(new Rectangle(0, 190, 360, 430));
		livingRoom.addBoundaries(new Rectangle(640, 165, 170, 200));

		exit.addBoundaries(new Rectangle(0, 0, 1, 800));
		exit.addBoundaries(new Rectangle(1200, 0, 1, 800));
		exit.addBoundaries(new Rectangle(0, 0, 1200, 1));
		exit.addBoundaries(new Rectangle(0, 780, 1200, 20));

		// exit zones
		evanBedRoom.addExitZone(new Rectangle(50, 700, 300, 200), dArr.getImage());

		hallway.addExitZone(new Rectangle(0, 320, 90, 120), lArr.getImage()); // living room
		hallway.addExitZone(new Rectangle(1110, 320, 90, 120), rArr.getImage()); // bathroom
		hallway.addExitZone(new Rectangle(545, 0, 135, 90), uArr.getImage()); // evan's room
		hallway.addExitZone(new Rectangle(545, 710, 135, 90), dArr.getImage());// exit

		bathroom.addExitZone(new Rectangle(380, 660, 350, 150), dArr.getImage());

		livingRoom.addExitZone(new Rectangle(350, 700, 320, 95), dArr.getImage());
		livingRoom.addExitZone(new Rectangle(1040, 400, 155, 100), rArr.getImage());

		kitchen.addExitZone(new Rectangle(0, 700, 50, 100), lArr.getImage());
		exit.addExitZone(new Rectangle(490, 20, 280, 120), uArr.getImage());

		// check zones
		RoomArea lrBehindCouch = new RoomArea("behind the sofa", livingRoomBehindSofa.getImage(),
				new Rectangle(0, 590, 85, 140));
		RoomArea lrUnderTable = new RoomArea("under the table", livingRoomUnderTable.getImage(),
				new Rectangle(340, 485, 80, 120));
		RoomArea lrUnderDrawer = new RoomArea("under the drawer", livingRoomUnderDrawer.getImage(),
				new Rectangle(630, 360, 170, 60),
				new Spider(200, 600, 80, new Rectangle(50, 600, 1000, 100), spiderBody));
		livingRoom.addArea(lrBehindCouch);
		livingRoom.addArea(lrUnderTable);
		livingRoom.addArea(lrUnderDrawer);

		RoomArea kCrevice = new RoomArea("the crevice", kitchenCrevice.getImage(), new Rectangle(950, 685, 90, 60),
				new Spider(500, 300, 80, new Rectangle(530, 380, 200, 280), spiderBody));
		RoomArea kCabinet = new RoomArea("the cabinets", kitchenCabinet.getImage(), new Rectangle(670, 690, 125, 60));
		RoomArea kStove = new RoomArea("the stove drawers", kitchenStove.getImage(), new Rectangle(170, 680, 155, 60));
		kitchen.addArea(kCrevice);
		kitchen.addArea(kCabinet);
		kitchen.addArea(kStove);

		RoomArea brCabinet = new RoomArea("the cabinets", bathroomCabinet.getImage(),
				new Rectangle(800, 555, 100, 200));
		RoomArea brBathtub = new RoomArea("the bathtub", bathroomTub.getImage(), new Rectangle(0, 350, 380, 120));
		RoomArea brToilet = new RoomArea("the toilet", bathroomToilet.getImage(), new Rectangle(790, 320, 120, 105));
		RoomArea brVoid = new RoomArea("the mysterious void...", bathroomVoid.getImage(),
				new Rectangle(0, 700, 185, 105),
				new Spider(600, 350, 70, new Rectangle(500, 300, 270, 230), spiderBody));
		bathroom.addArea(brCabinet);
		bathroom.addArea(brBathtub);
		bathroom.addArea(brToilet);
		bathroom.addArea(brVoid);

		RoomArea frontDoor = new RoomArea("the door", exitDoor.getImage(), new Rectangle(455, 635, 290, 150));
		exit.addArea(frontDoor);
	}

	/**
	 * Method to clear the screen.
	 */
	private void clearScreen() {
		removeAll();
		repaint();
	}

	/**
	 * Method to load a certain room.
	 * 
	 * @param r Room to be loaded.
	 */
	private void loadRoom(Graphics g, Room r) {
		Image background = r.getBg();
		String name = r.getName();

		ArrayList<Rectangle> exits = r.getExits();

		if (background != null) {
			g.drawImage(background, 0, 0, 1200, 800, this);
			g.setFont(textFont);
			g.setColor(TEXTBOXCOLOR);
			g.fillRect(925, 100, 275, 75);
			g.setColor(Color.WHITE);
			g.drawString("Room: " + name, 940, 145);
		}
		
		for (int i = 0; i < exits.size(); i++) {
			Image arr = r.getExitArrow(i);
			Rectangle rect = exits.get(i);
			g.drawImage(arr, (int) (rect.x
					+ ((arr.equals(uArr.getImage()) || arr.equals(dArr.getImage())) ? rect.getWidth() * 0.4 : 0)),
					(int) (rect.y
							+ ((arr.equals(lArr.getImage()) || arr.equals(rArr.getImage())) ? rect.getHeight() * 0.4
									: 0)),
					this);
		}
	}

	/**
	 * Method to load a current roomArea.
	 * 
	 * @param g The tool to help paint the window.
	 * @param r The current RoomArea.
	 */
	private void loadRoomArea(Graphics g, RoomArea r) {
		Image bg = r.getBg();
		g.drawImage(bg, 0, 0, 1200, 800, this);

		if (currRoom.equals(exit)) {
			if (!mapCleared()) {
				displayMsg(g, new Message(EVAN,"The door's locked! I should see if I could find something to open it...", evanHead));
				// Displaying helper font.
				g.setFont(textFont);
				g.setColor(Color.white);
				g.drawString("Press 'R' to return.", 930, 750);
			} else {
				displayMsg(g, new Message(EVAN, "Now let's get out of here!", evanHead));
				// Displaying helper font.
				g.setFont(textFont);
				g.setColor(Color.white);
				g.drawString("Click on the key hole to escape!", 850, 750);
			}
		} else if (r.getSpider() != null) {
			Spider entity = r.getSpider();
			if (!entity.isRemoved()) {
				displayMsg(g, new Message(EVAN, "A spider! Get it out of here!", evanHead));
				Image spiderImg = entity.getImg();
				g.drawImage(spiderImg, (int) entity.getX(), (int) entity.getY(), (int) entity.getWidth(),
						(int) entity.getWidth(), this);
				// Displaying helper font.
				g.setFont(textFont);
				g.setColor(Color.white);
				g.drawString("Use the mouse to click on the spider!", 800, 750);
			} else if (r.spiderCaught()) {
				if (mapCleared()) {
					if (!keyAcquired) {
						loadKey(g);
						// Displaying helper font.
						g.setFont(textFont);
						g.setColor(Color.white);
						g.drawString("Use the mouse to click on the key!", 800, 750);
					} else {
						displayMsg(g, new Message(EVAN, "Let's get out of this nightmare!", evanHead));
						// Displaying helper font.
						g.setFont(textFont);
						g.setColor(Color.white);
						g.drawString("Press 'R' to return.", 930, 750);
					}
				} else {
					displayMsg(g, new Message(EVAN, "Nothing else is left here...I should keep looking.", evanHead));
					// Displaying helper font.
					g.setFont(textFont);
					g.setColor(Color.white);
					g.drawString("Press 'R' to return.", 930, 750);
				}
			}
		} else if (keyLocation != null && !keyLocation.equals(currRoomArea)) {
			displayMsg(g, new Message(EVAN, "Nothing's here...I should keep looking.", evanHead));
			// Displaying helper font.
			g.setFont(textFont);
			g.setColor(Color.white);
			g.drawString("Press 'R' to return.", 930, 750);
		} else {
			displayMsg(g, new Message(EVAN, "Nothing else is left here...I should keep looking.", evanHead));
			// Displaying helper font.
			g.setFont(textFont);
			g.setColor(Color.white);
			g.drawString("Press 'R' to return.", 930, 750);
		}
	}

	/**
	 * Method to display the first batch of dialogue.
	 * 
	 * @param g The tool to help paint the window.
	 */
	private void displayFirstDialogue(Graphics g) {
		g.drawImage(evanBedRoom.getBg(), 0, 0, this);
		if (currIndex <= 0)
			g.drawImage(evanSleepingHead.getImage(), 560, 332, this);
		else
			displayPlayer(g);
		displayMsg(g, msgList.get(currIndex));
		g.setFont(smallFont);
		g.drawString(
				"Click anywhere to " + (msgList.get(currIndex).equals(lastFirstDialogue) ? "finish." : "continue."),
				250, 170);
	}

	/**
	 * Method to display the last batch of dialogue.
	 * 
	 * @param g The tool to help paint the window.
	 */
	private void displayLastDialogue(Graphics g) {
		g.drawImage(morningBedroomBg.getImage(), 0, 0, this);
		if (currIndex <= msgList.size() - 1)
			g.drawImage(evanSleepingHead.getImage(), 480, 332, this);
		displayMsg(g, msgList.get(currIndex));
		g.setFont(smallFont);
		g.drawString(
				"Click anywhere to " + (msgList.get(currIndex).equals(lastSecondDialogue) ? "finish." : "continue."),
				250, 170);
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
		g.drawString(name, 250, 80);

		g.setFont(textFont);
		g.drawString(msg, 250, 130);
	}

	/**
	 * Method to display the number of hearts Player currently has.
	 * 
	 * @param g The tool to help paint the window.
	 * @param p The current Player.
	 */
	private void displayHearts(Graphics g, Player p) {
		int hp = p.getHealth();
		int x = 900;
		int y = 650;
		for (int i = 0; i < hp / 2; i++) {
			g.drawImage(fullHeart.getImage(), x, y, this);
			x += 90;
		}
		if (hp % 2 == 1) {
			g.drawImage(halfHeart.getImage(), x, y, this);
		}
	}

	/**
	 * Method to display the first batch of animation.
	 * 
	 * @param g The tool to help paint the window.
	 */
	private void displayFirstAnimation(Graphics g) {
		try {
			if (firstAnimationFrames.get(animIndex).equals(firstBufferFrame)) {
				Thread.sleep(3000);
			} else {
				Thread.sleep(400);
			}
			g.drawImage(firstAnimationFrames.get(animIndex).getImage(), 0, 0, 1200, 800, this);
			animIndex = Math.min(firstAnimationFrames.size(), animIndex + 1);
			repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (animIndex == firstAnimationFrames.size()) {
			inLastDialogue = true;
			inFirstAnimation = false;
			animIndex = 0;
			return;
		}
	}

	/**
	 * Method to display the last batch of animation.
	 * 
	 * @param g The tool to help paint the window.
	 */
	private void displayLastAnimation(Graphics g) {
		try {
			if (lastAnimationFrames.get(animIndex).equals(lastBufferFrame)) {
				Thread.sleep(3000);
			} else {
				Thread.sleep(400);
			}
			g.drawImage(lastAnimationFrames.get(animIndex).getImage(), 0, 0, 1200, 800, this);
			animIndex = Math.min(lastAnimationFrames.size(), animIndex + 1);
			repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (animIndex == lastAnimationFrames.size()) {
			inLastAnimation = false;
			winGame = true;
			return;
		}
	}

	/**
	 * Method to reset variables that control state of the game to initial state.
	 */
	private void resetLevel() {
		inFirstDialogue = true;
		inLastDialogue = false;
		inFirstAnimation = false;
		inLastAnimation = false;
		inGameplay = false;
		loseGame = false;
		winGame = false;
		checkingArea = false;
		currIndex = 0;
		animIndex = 0;

		keyAcquired = false;
		keyCreated = false;

		evan.setCoordinates(170, 350);
		evan.setDirection(2);
		evan.setHealth(6);
		evanHallway.setHealth(6);
		currPlayer = evan;
		currRoom = evanBedRoom;
		score.resetScore();

		for (Room i : allRooms) {
			i.resetRoom();
		}
	}

	/**
	 * Method to load a key into the game.
	 * 
	 * @param g The tool to help paint the window.
	 */
	private void loadKey(Graphics g) {
		g.drawImage(keyImg.getImage(), (int) keyHitBox.getX(), (int) keyHitBox.getY(), (int) keyHitBox.getWidth(),
				(int) keyHitBox.getHeight(), this);
		displayMsg(g, new Message(EVAN, "A key! I can use it to get out of here!", evanHead));
	}

	/**
	 * Method to check if there are NO spiders in any room.
	 * 
	 * @return True if all spiders in all rooms are gone.
	 */
	public boolean mapCleared() {
		for (Room r : allRooms) {
			if (!r.clearedRoom())
				return false;
		}
		return true;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		int moveDist = currPlayer.getMoveDist();
		if (inGameplay) {
			if (currPlayer.equals(evanHallway))
				currPlayer.setLowerHitBox(currPlayer.getHitBox());

			if (currRoom.inExitZone(currPlayer)) {
				Rectangle currExitZone = currRoom.getCurrExitZone(currPlayer);
				if (keyCode == KeyEvent.VK_E) {
					evan.setHealth(currPlayer.getHealth());
					evanHallway.setHealth(currPlayer.getHealth());
					currPlayer = evan;
					currPlayer.setDirection(2);
					if (currRoom == evanBedRoom) {
						currRoom = hallway;
						currPlayer = evanHallway;
						evanHallway.setCoordinates(575, 130);
					} else if (currRoom == bathroom) {
						currRoom = hallway;
						currPlayer = evanHallway;
						evanHallway.setCoordinates(1050, 330);
					} else if (currRoom == exit) {
						currRoom = hallway;
						currPlayer = evanHallway;
						evanHallway.setCoordinates(550, 600);
					} else if (currRoom == livingRoom) {
						if (currExitZone.equals(currRoom.getExitZone(0))) {
							currRoom = hallway;
							currPlayer = evanHallway;
							evanHallway.setCoordinates(90, 330);
						} else if (currExitZone.equals(currRoom.getExitZone(1))) {
							currRoom = kitchen;
							evan.setHealth(currPlayer.getHealth());
							currPlayer = evan;
							evan.setCoordinates(50, 560);
						}
					} else if (currRoom == kitchen) {
						currRoom = livingRoom;
						currPlayer = evan;
						evan.setCoordinates(800, 200);
					} else if (currRoom == hallway) {
						if (currExitZone.equals(currRoom.getExitZone(0))) {
							currRoom = livingRoom;
							evan.setCoordinates(450, 400);
						} else if (currExitZone.equals(currRoom.getExitZone(1))) {
							currRoom = bathroom;
							evan.setCoordinates(500, 400);
						} else if (currExitZone.equals(currRoom.getExitZone(2))) {
							currRoom = evanBedRoom;
							evan.setCoordinates(170, 400);
						} else if (currExitZone.equals(currRoom.getExitZone(3))) {
							currRoom = exit;
							evan.setCoordinates(500, 100);
						}
					}

				}
			} else if (checkingArea) {
				if (!currRoomArea.hasSpider() && keyCode == KeyEvent.VK_R) {
					checkingArea = false;
				} else if (currRoomArea.hasSpider()) {
					Spider ent = currRoomArea.getSpider();
					if (ent.isRemoved() && keyCode == KeyEvent.VK_R) {
						checkingArea = false;
					}
				}
				clearScreen();
			} else if (currRoom.inCheckZone(currPlayer)) {
				if (keyCode == KeyEvent.VK_C) {
					checkingArea = true;
					currRoomArea = currRoom.getCurrRoomArea(currPlayer);
				}
				clearScreen();
			}
			// If Evan moves LEFT
			if (keyCode == KeyEvent.VK_A) {
				if (currRoom.validMove(currPlayer, -moveDist, 0)) {
					currPlayer.move(-moveDist, 0);
					currPlayer.setDirection(0);
				}
			}

			// If Evan moves RIGHT
			else if (keyCode == KeyEvent.VK_D) {
				if (currRoom.validMove(currPlayer, moveDist, 0)) {
					currPlayer.move(moveDist, 0);
					currPlayer.setDirection(1);
				}
			}

			// If Evan moves DOWN
			else if (keyCode == KeyEvent.VK_S) {
				if (currRoom.validMove(currPlayer, 0, moveDist)) {
					currPlayer.move(0, moveDist);
					currPlayer.setDirection(2);
				}
			}

			// If Evan moves UP
			else if (keyCode == KeyEvent.VK_W) {
				if (currRoom.validMove(currPlayer, 0, -moveDist)) {
					currPlayer.move(0, -moveDist);
					currPlayer.setDirection(3);
				}
			}
		}
		clearScreen();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (inFirstDialogue) {
			if (msgList.get(currIndex).equals(lastFirstDialogue)) {
				inGameplay = true;
				inFirstDialogue = false;
			}
			currIndex++;
			repaint();
		} else if (inLastDialogue) {
			if (msgList.get(currIndex).equals(lastSecondDialogue)) {
				inLastDialogue = false;
				inLastAnimation = true;
			}
			currIndex++;
			repaint();
		} else if (winGame) {
			resetLevel();
			layoutManager.show(getParent(), "menu");
		} else if (checkingArea) {
			Spider ent = currRoomArea.getSpider();
			if (keyLocation != null && keyCreated && currRoomArea.getName().equals(keyLocation.getName())) {
				if (keyHitBox.contains(e.getX(), e.getY())) {
					if (!keyAcquired)
						score.addScore(400);
					keyAcquired = true;
				}
			} else if (currRoom.equals(exit) && keyAcquired) {
				if (doorKnobHb.contains(e.getX(), e.getY())) {
					inGameplay = false;
					inFirstAnimation = true;
				}
			} else if (ent != null) {
				if (!ent.isCaught(e.getX(), e.getY())) {
					currPlayer.loseHealth();
					score.addScore(-50);
					if (currPlayer.getHealth() <= 0)
						loseGame = true;
					ent.randomizeCords();
				} else {
					ent.getHitBox();
					ent.setStatus(true);
					score.addScore(200);
					keyHitBox = ent.getHitBox();
					keyLocation = currRoomArea;
					if (!keyAcquired && !keyCreated) {
						keyCreated = true;
					}
				}
			}
			repaint();
		}
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(returnToMenu)) {
			clearScreen();
			resetLevel();
			layoutManager.show(getParent(), "menu");
		} else if (e.getSource().equals(playAgain)) {
			resetLevel();
			clearScreen();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
