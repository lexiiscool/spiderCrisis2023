package Main;

import java.awt.*;
import java.util.*;

/**
 * Date: June 8, 2023
 * <p>
 * Version 2.0 of the Player class, a class that represents a player.
 * More methods were added to the Player class, modifications by Lexi Han. 
 * Provided by JELTech Industries.
 * </p>
 * 
 * <h2>
 * Course: ICS4UO with V. Krasteva
 * </h2>
 * 
 * @author Lexi Han
 * @version 23.05.31
 *
 */
public class Player {
	/**
	 * Variables to describe a player. 
	 */
	private String name;
	private int width, height;
	private int direction;
	private int x, y;
	private int moveAmt;
	private int health = 6;
	
	private Rectangle lowerHitBox;
	
	/**
	 * List of images that will display when player is displayed. Meant to display 
	 */
	private ArrayList<Image> imgs;
	
	/**
	 * Constructor for Player class.
	 * @param n Name of player.
	 * @param w Width of player. 
	 * @param h Height of player.
	 * @param m The amount a player can move.
	 */
	public Player(String n, int w, int h, int m) {
		name = n;
		width = w;
		height = h;
		direction = 2;
		moveAmt = m;
		
		imgs = new ArrayList<Image>();
	}
	
	/**
	 * Default constructor for Player. 
	 */
	public Player() {
		name = "Evan";
		width = 300;
		height = 200;
		direction = 2;
		moveAmt = 20;
		
		imgs = new ArrayList<Image>();
	}

	/**
	 * Method to add image to list of images.
	 * Precondition: img is not null.
	 * @param img Image to be added.
	 */
	public void addImage(Image img) {
		if (img != null) imgs.add(img);
	}
	
	/**
	 * Method to set new x-coordinates of player.
	 * @param newX new x-coordinates.
	 */
	public void setX(int newX) {
		x = newX;
	} 
	
	/**
	 * Method to set new y-coordinates of player.
	 * @param newY new y-coordinates.
	 */
	public void setY(int newY) {
		y = newY;
	}

	/**
	 * Method to set lower hit-box of Player.
	 */
	public void setLowerHitBox() {
		lowerHitBox = new Rectangle (x, (int) (y + (0.7 * height)), width, (int) (height-(0.7 * height)));
	}
	
	/**
	 * Method to set lower hit-box of Player.
	 * @param r Rectangle to set hit-box to.
	 */
	public void setLowerHitBox(Rectangle r) {
		lowerHitBox = r;
	}
	
	/**
	 * Method to set the coordinates of the Player's top left-most point.
	 * @param x The top-left most x-coordinate.
	 * @param y The top-left most y-coordinate.
	 */
	public void setCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Method to set Player's health.
	 * Precondition: h is between 0 and 6, inclusive.
	 * @param h HP to set Player's health to.
	 */
	public void setHealth(int h) {
		health = h;
	}
	
	/**
	 * Method to move player's hit box horizontally (along x-axis). 
	 * @param amt The amount of pixels to move them by. A positive quantity indicates rightwards movement, and a negative quantity indicates leftwards movement.
	 */
	public void moveHorizontally(int amt) {
		this.x += amt;
	}
	
	/**
	 * Method to move player's hit box vertically (along y-axis).
	 * @param amt The amount of pixels to move them by. A positive quantity indicates downwards movement, and a negative quantity indicates upwards movement.
	 */
	public void moveVertically(int amt) {
		this.y += amt;
	}
	
	/**
	 * Method to move the player in a certain direction. 
	 * @param xDir The amount of pixels in the x-direction.
	 * @param yDir The amount of pixels in the y-direction.
	 */
	public void move(int xDir, int yDir) {
		x += xDir;
		y += yDir;
	}
	
	/**
	 * Method to set direction player faces. 
	 * Precondition: dir is between 0 and 3, inclusive.
	 * @param dir An integer from 0 to 3, representing the four directions: 0 is left, 1 is right, 2 is down, 3 is up.
	 */
	public void setDirection(int dir) {
		direction = dir;
	}
	
	/**
	 * Method to lose Player's health by one hitpoint.
	 */
	public void loseHealth() {
		health = Math.max(0, health-1);
	}
	
	/**
	 * Method to get player's X coordinate.
	 * @return Top left X-coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Method to get player's Y coordinate.
	 * @return Top left Y-coordinate.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Method to get player's name.
	 * @return Name of player.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Method to return width of the player.
	 * @return Width of player.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Method to return height of the player.
	 * @return Height of player.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Method to return the distance a player can move each time a key is pressed.
	 * @return the amount the Player can move.
	 */
	public int getMoveDist() {
		return moveAmt;
	}

	/**
	 * Method to return the health of the Player.
	 * @return the health of the Player.
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Method to get hit box of player's full body.
	 * @return a Rectangle object, depicting the player's hitbox.
	 */
	public Rectangle getHitBox() {
		return new Rectangle(x, y, width, height);
	}
	
	/**
	 * Method to get hit box of a player's lower hit box (feet area). 
	 * @return a Rectangle object, depicting a smaller version of the player's hit box.
	 */
	public Rectangle getLowerHitBox() {
		return lowerHitBox;
	}
	
	/**
	 * Method to return the image to display of a player.
	 * @return the image to display.
	 */
	public Image getImage() {
		if (imgs.isEmpty()) return null;
		return imgs.get(0);
	}
	
	/**
	 * Method to return the image of a player when they face a certain direction.
	 * @return Image of player facing their current direction.
	 */
	public Image getOrientedImage() {
		return imgs.get(direction);
	}
}
