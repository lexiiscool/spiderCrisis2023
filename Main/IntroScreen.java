package Main;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Date: May 23, 2023
 * <p>
 * Version 3.0 of the Intro Screen. Graphics imported. Now includes Splash
 * Screen displaying logo (created by Ethan Andrew). Provided by JELTech
 * Industries.
 * </p>
 *
 * <h2>Class: ICS4U0 with V.Krasteva</h2>
 *
 * @author Lexi Han
 * @author Ethan Andrew
 * @version 23.05.23
 */
public class IntroScreen extends JComponent {
	private Font titleFont;
	private Font smallFont;

	// The logo for the spider web used in the intro screen.
	private ImageIcon spiderWeb = new ImageIcon(getClass().getClassLoader().getResource("mainscreen.png"));

	// May 23, 2023 - Ethan Andrew - Added variable used in the Splash Screen
	// animation.
	/**
	 * A variable to control the vertical location of the top image.
	 */
	private int y = -400;

	// May 23, 2023 - Ethan Andrew - Added variable used in the Splash Screen
	// animation.
	/**
	 * A variable to control the horizontal location of the bottom image.
	 */
	private int x;

	// May 23, 2023 - Ethan Andrew - Added variable used in the Splash Screen
	// animation.
	/**
	 * A variable to control the transparency of the black screen overlay.
	 */
	private int a = 0;

	// May 23, 2023 - Ethan Andrew - Added variable used in the Splash Screen
	// animation.
	/**
	 * A variable used to control the sequencing of the animation and the graphics
	 * printed to the JFrame.
	 */
	int action = 0;

	// May 23, 2023 - Ethan Andrew - Added variable used in the Splash Screen
	// animation.
	/**
	 * A variable that records the system time.
	 */
	long initTime = -1;

	// May 23, 2023 - Ethan Andrew - Added variable used in the Splash Screen
	// animation.
	/**
	 * A variable that records the time passed since the time recorded in initTime.
	 */
	long delayTime = 0;

	// May 23, 2023 - Ethan Andrew - Added variable used in the Splash Screen
	// animation.
	/**
	 * A Timer to run the screen animation.
	 */
	Timer timer = new Timer(16, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Moves the TV down.
			if (y < 100) {
				y += 25;
			} else if (action < 2) {
				// If-else statement creates 1 second delay.
				if (initTime < 0) {
					initTime = System.currentTimeMillis();
				} else {
					delayTime = System.currentTimeMillis() - initTime;
					if (delayTime > 1000) {
						// Changes image to static.
						if (action == 0) {
							try {

								// file = new
								// File(getClass().getClassLoader().getResource("logo-top-static.png").toURI());

								logotop = ImageIO
										.read(getClass().getClassLoader().getResourceAsStream("logo-top-static.png"));
							} catch (IOException er) {
								throw new RuntimeException(er);
							}
						} else if (action == 1) {
							try {
								// file = new
								// File(getClass().getClassLoader().getResource("logo-yellow-top.png").toURI());

								logotop = ImageIO
										.read(getClass().getClassLoader().getResourceAsStream("logo-yellow-top.png"));
							} catch (IOException e3) {
								throw new RuntimeException(e3);
							}
						}

						initTime = -1;
						action++;
					}
				}

			} else if (action < 20) {
				x = (int) ((Math.random() * 21) + 320);
				action++;
			} else if (action == 20) {
				x = 330;
				action++;
			} else if (action == 21) {
				// If-else statement creates 1 second delay.
				if (initTime < 0) {
					initTime = System.currentTimeMillis();
				} else {
					delayTime = System.currentTimeMillis() - initTime;
					if (delayTime > 1000) {
						initTime = -1;
						action++;
					}
				}
			} else if (action <= 73) {
				a += 5;
				action++;
			} else if (action == 74) {
				action++;
			} else {
				timer.stop();
			}
			repaint();
		}
	});

	// May 23, 2023 - Ethan Andrew - Added variable used in the Splash Screen
	// animation.
	/**
	 * Stores URL paths for files (paths will be replaced in later versions).
	 */
	File file;

	// May 23, 2023 - Ethan Andrew - Added variable used in the Splash Screen
	// animation.
	/**
	 * Stores image to be printed.
	 */
	Image logotop;

	// May 23, 2023 - Ethan Andrew - Added variable used in the Splash Screen
	// animation.
	Image logobot;

	// May 23, 2023 - Ethan Andrew - Modified constructor to include loading of
	// logotop and logobot images, along with starting a Timer.

	/**
	 * Constructor to initialize images and custom fonts.
	 *
	 * @throws IOException         Handles exceptions created during the custom
	 *                             fonts.
	 * @throws FontFormatException Handles exceptions created during the custom
	 *                             fonts.
	 * @throws URISyntaxException
	 */
	public IntroScreen() throws IOException, FontFormatException, URISyntaxException {
		try {
			titleFont = Font.createFont(Font.TRUETYPE_FONT,
					getClass().getClassLoader().getResourceAsStream("titleFont.otf"));
			
			smallFont = Font.createFont(Font.TRUETYPE_FONT,getClass().getClassLoader().getResourceAsStream("PixeloidSans-mLxMm.ttf"));
			smallFont = smallFont.deriveFont(25f);

			titleFont = titleFont.deriveFont(200f);

			logotop = ImageIO.read(getClass().getClassLoader().getResourceAsStream("logo-top-blank.png"));

			logobot = ImageIO.read(getClass().getClassLoader().getResource("logo-yellow-bottom.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		timer.start();
	}

	// May 23, 2023 - Ethan Andrew - Modified method to include graphics for the
	// Splash Screen.

	/**
	 * Overridden method to paint window.
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 1200, 800);
		g.drawImage(logotop, 350, y, this);
		if (action >= 2) {
			g.drawImage(logobot, x, 580, this);
		}
		if (action >= 22 && action <= 73) {
			g.setColor(new Color(0, 0, 0, a));
			g.fillRect(0, 0, 1200, 800);
		}
		if (action >= 74) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 1200, 800);
			g.setColor(Color.RED);

			g.drawImage(spiderWeb.getImage(), 50, 50, this);

			g.setFont(titleFont);
			g.drawString("SPIDER CRISIS", 150, 710);

			g.setFont(smallFont);
			g.setColor(Color.WHITE);
			g.drawString("Click anywhere to continue.", 750, 350);
		}
	}

	/**
	 * Method to load custom fonts used for game
	 *
	 * @param path The path to the file containing a font.
	 * @throws IOException
	 * @throws FontFormatException
	 * @return the newly loaded font.
	 */
	public Font loadCustomFont(String path) throws IOException, FontFormatException {
		Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File(path));
		return newFont;
	}

	public Font loadCustomFontURI(URI path) throws IOException, FontFormatException {
		System.out.println(path);
		Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File(path));
		return newFont;
	}
}