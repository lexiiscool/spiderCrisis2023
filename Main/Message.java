package Main;

import javax.swing.*;

/**
 * Date: May 23, 2023
 * <p>
 * Version 2.0 of the Message class, a class that represents a dialogue message. 
 * Replaced JLabel with ImageIcon data types.
 * Provided by JELTech Industries. 
 * </p>
 * <h2>
 * Class: ICS4UO with V. Krasteva
 * </h2>
 * @author Lexi Han
 * @version 23.05.23
 */

public class Message{
	/**
	 * Method was modified on May 23, 2023, by Lexi Han. 
	 * Modified data types (switched from JLabel to ImageIcon)
	 */
	private String name, msg;
	private ImageIcon icon;
	
	/**
	 * Constructor for Message class.
	 * @param n The name of the entity speaking.
	 * @param m The message to display.
	 * @param i The icon (image) of the entity speaking.
	 */
	public Message(String n, String m, ImageIcon i) {
		name = n;
		msg = m;
		icon = i;
	}
	
	/**
	 * Method to return the name.
	 * @return Name of entity.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Method to return the message to be spoken.
	 * @return Message from entity.
	 */
	public String getMessage() {
		return msg;
	}
	
	/**
	 * Method to return the icon of the entity.
	 * @return Image of entity.
	 */
	public ImageIcon getIcon() {
		return icon;
	}
}
