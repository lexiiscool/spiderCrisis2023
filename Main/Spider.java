package Main;

import java.awt.*;
import javax.swing.*;

/**
 * Date: June 8, 2023
 * <p>
 * Version 1.0 of the Spider class, a class that represents a spider.
 * Provided by JELTech Industries.
 * </p>
 * @author Lexi Han
 * 
 * <h2>
 * Class: ICS4UO with V. Krasteva
 * </h2>
 * 
 * @version 23.06.08
 */
public class Spider {
	private int x, y;
	private int width;
	
	private boolean caught = false;
	Rectangle bounds;
	
	ImageIcon img;
	
	/**
	 * Constructor to initialize variables.
	 * @param x The top left x-coordinate.
	 * @param y The top left y-coordinate.
	 * @param w The width of the spider.
	 * @param r the boundary in which the spider resides.
	 * @param img the image of the spider to display. 
	 */
	public Spider(int x, int y, int w, Rectangle r, ImageIcon img) {
		this.x = x;
		this.y = y;
		width = w;
		bounds = r;
		
		this.img = img;
		this.img = new ImageIcon(this.img.getImage().getScaledInstance(w, w, Image.SCALE_SMOOTH));
	}
	
	/**
	 * Method to randomize coordinates of spider.
	 */
	public void randomizeCords() {
		x = (int) (bounds.getX() + Math.random() * bounds.getWidth());
		y = (int) (bounds.getY() + Math.random() * bounds.getHeight());
	}
	
	/**
	 * Method to set whether the spider has been caught or not.
	 * @param b The Boolean value stating True if the spider is caught.
	 */
	public void setStatus(boolean b) {
		caught = b;
	}
	
	/**
	 * Method to set the coordinates of the spider's top left most point.
	 * @param x The top left most x-coordinate.
	 * @param y The top left most y-coordinate.
	 */
	public void setCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Method that returns the hit box of the spider.
	 * @return A Rectangle that represents the spider's hit box.
	 */
	public Rectangle getHitBox() {
		return new Rectangle(x, y, width, width);
	}
	
	/**
	 * Method that determines if the user catches the spider.
	 * @param mouseX The x-coordinate of the mouse point.
	 * @param mouseY The y-coordinate of the mouse point.
	 * @return true if the mouse point is within the hit box of the spider.
	 */
	public boolean isCaught(int mouseX, int mouseY) {
		if (getHitBox().contains(mouseX, mouseY)) caught = true;
		return getHitBox().contains(mouseX, mouseY);
	}
	
	/**
	 * Method that checks if the spider is not present in the room.
	 * @return True if the spider is already caught.
	 */
	public boolean isRemoved() {
		return caught;
	}
	
	/**
	 * Method to return the image of the spider.
	 * @return Image of spider.
	 */
	public Image getImg() {
		return img.getImage();
	}
	
	/**
	 * Method to return top left most x-coordinate.
	 * @return X position of spider.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Method to return top left most y-coordinate.
	 * @return Y position of spider.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Method to return the width of the spider.
	 * @return Width of spider.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Method to return boundaries where the spider can be found.
	 * @return Rectangle representing boundaries of spider.
	 */
	public Rectangle getBounds() {
		return bounds;
	}
}
