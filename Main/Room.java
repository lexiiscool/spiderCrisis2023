package Main;

import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

/**
 * Date: May 31, 2023
 * <p>
 * Version 1.0 of the Room class, a class to represent a room in Evan's house.
 * Provided by JELTech Industries.
 * </p>
 * @author Lexi Han
 * 
 * <h2>
 * Class: ICS4UO with V. Krasteva
 * </h2>
 * 
 * @version 23.06.08
 *
 */
public class Room {
	private String name;
	private Image bg;
	
	/**
	 * List of all spiders in the room.
	 */
	private ArrayList<Spider> allMobs;
	
	/**
	 * List of all boundaries in the room a Player cannot access.
	 */
	private ArrayList<Rectangle> boundaries;
	
	/**
	 * List of all zones where a Player can check for a spider.
	 */
	private ArrayList<Rectangle> checkZones;
	
	/**
	 * List of all RoomAreas which may include a spider to catch.
	 */
	private ArrayList<RoomArea> areasToCheck;
	
	/**
	 * List of all zones where a Player can leave the room.
	 */
	private ArrayList<Rectangle> exitZones;
	
	/**
	 * List of all the arrows corresponding to the exit zones (which way a Player exits).
	 */
	private ArrayList<Image> arrowList;
	
	/**
	 * Constructor to initialize room variables.
	 * @param n Name of room.
	 * @param bg Background image.
	 */
	public Room(String n, Image bg) {
		name = n;
		this.bg = bg;
		
		/**
		 * Initialize lists. 
		 */
		allMobs = new ArrayList<Spider>();
		boundaries = new ArrayList<Rectangle>();
		checkZones = new ArrayList<Rectangle>();
		exitZones = new ArrayList<Rectangle>();
		areasToCheck = new ArrayList<RoomArea>();
		arrowList = new ArrayList<Image>();
	}
	
	/**
	 * Method to check if a Player is in one of the exit zones of the Room.
	 * @param p the Player to check.
	 * @return True if the Player is in an exit zone.
	 */
	public boolean inExitZone(Player p) {
		Rectangle hb = p.getLowerHitBox();
		for (Rectangle r: exitZones) {
			if (r.intersects(hb)) return true;
		}
		return false;
	}
	
	/**
	 * Method to check if a Player is in one of the check zones of the Room.
	 * @param p The current Player.
	 * @return The current check zone the Player is in.
	 */
	public boolean inCheckZone(Player p) {
		Rectangle hb = p.getLowerHitBox();
		for (Rectangle r: checkZones) {
			if (r.intersects(hb)) return true;
		}
		return false;
	}
	
	/**
	 * Method to check if a player can move here. 
	 * @param p The player to investigate.
	 * @param xDir the amount moved in the x-direction.
	 * @param yDir the amount moved in the y-direction.
	 * @return True if the player can move in the direction given.
	 */
	public boolean validMove(Player p, int xDir, int yDir) {
		Rectangle hb = p.getLowerHitBox();
		hb.setBounds((int) hb.getX() + xDir, (int) hb.getY() + yDir, (int) hb.getWidth(), (int) hb.getHeight());
		for (Rectangle r: boundaries) {
			if (r.intersects(hb)) return false;
		}
		return true;
	}
	
	/**
	 * Method determining if a player has successfully cleared the room.
	 * @return True if there are no more spiders in the room.
	 */
	public boolean clearedRoom() {
		for (RoomArea ra: areasToCheck) {
			Spider e = ra.getSpider();
			if (e != null && !ra.spiderCaught()) return false;
		}
		return true;
	}
	
	/**
	 * Method adding spider to room.
	 * @param s Spider to be added.
	 */
	public void addSpider(Spider s) {
		allMobs.add(s);
	}
	
	/**
	 * Method to reset spiders in room.
	 */
	public void resetRoom() {
		for (RoomArea ra: areasToCheck) {
			Spider s = ra.getSpider();
			if (s != null) s.setStatus(false);
		}
	}
	
	/**
	 * Method to add check zone for player.
	 * @param ra RoomArea class representing the area to be checked.
	 */
	public void addCheckZone(RoomArea ra) {
		Rectangle checkZoneBox = ra.getCheckZone();
		areasToCheck.add(ra);
		checkZones.add(checkZoneBox);
	}
	
	/**
	 * Method to add an exit zone to the Room.
	 * @param r The boundaries where the exit zone is.
	 * @param arrow The direction the arrow is to face, indicating the direction of the exit.
	 */
	public void addExitZone(Rectangle r, Image arrow) {
		exitZones.add(r);
		arrowList.add(arrow);
	}
	
	/**
	 * Method to add boundaries for player.
	 * @param r Rectangle representing hit box.
	 */
	public void addBoundaries(Rectangle r) {
		boundaries.add(r);
	}
	
	/**
	 * Method to add RoomArea to room.
	 * @param ra the RoomArea.
	 */
	public void addArea(RoomArea ra) {
		areasToCheck.add(ra);
		checkZones.add(ra.getCheckZone());
	}

	/**
	 * Method to return the current exit zone a Player is standing in.
	 * @param p A player object.
	 * @return The exit zone the player is in, or null if they are not in an exit zone.
	 */
	public Rectangle getCurrExitZone(Player p) {
		Rectangle hb = p.getLowerHitBox();
		for (Rectangle r: exitZones) {
			if (hb.intersects(r)) return r;
		}
		return null;
	}
	
	/**
	 * Method to retrieve exit zone at an index.
	 * @param ind The index.
	 * @return the Exit Zone at that index.
	 */
	public Rectangle getExitZone(int ind) {
		return exitZones.get(ind);
	}
	
	/**
	 * Method to get the Player's current check zone.
	 * @param p the Player to check.
	 * @return a Rectangle representing the current check zone, or null if the Player is not in a check zone.
	 */
	public Rectangle getCheckZone(Player p) {
		Rectangle hb = p.getLowerHitBox();
		for (Rectangle r: checkZones) {
			if (hb.intersects(r)) return r;
		}
		return null;
	}
	
	/**
	 * Method to get the Player's current Room Area.
	 * @param p the Player to check.
	 * @return a RoomArea representing the current Room Area, or null if there is none.
	 */
	public RoomArea getCurrRoomArea(Player p) {
		Rectangle checkZone = getCheckZone(p);
		for (RoomArea r: areasToCheck) {
			if (checkZone.equals(r.getCheckZone())) return r;
		}
		return null;
	}
	
	/**
	 * Method to get mobs. 
	 * @return ArrayList of all spiders.
	 */
	public ArrayList<Spider> getMobs(){
		return allMobs;
	}
	
	/**
	 * Method to get all boundaries.
	 * @return ArrayList of all boundaries.
	 */
	public ArrayList<Rectangle> getBoundaries(){
		return boundaries;
	}
	
	/**
	 * Method to get all check zones.
	 * @return ArrayList of all check zones.
	 */
	public ArrayList<Rectangle> getCheckZones(){
		return checkZones;
	}
	
	/**
	 * Method to get all exit zones.
	 * @return ArrayList of all exit zones.
	 */
	public ArrayList<Rectangle> getExits(){
		return exitZones;
	}
	
	/**
	 * Method to get the background of the room.
	 * @return an Image of the background.
	 */
	public Image getBg() {
		return bg;
	}
	
	/**
	 * Method to return name of the room.
	 * @return Name of room.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Method to return the exit arrow at a certain index.
	 * @param ind the Index.
	 * @return an Exit Arrow at that index.
	 */
	public Image getExitArrow(int ind) {
		return arrowList.get(ind);
	}
}

/**
 * Date: June 5, 2023
 * <p>
 *  Helper class depicting an area of a room, which may contain a spider.
 * </p> 
 * @author Lexi Han
 * @version 23.06.05
 */
class RoomArea {
	private String name;
	private Image bg;
	private Spider entity;
	private Rectangle checkZone;
	
	/**
	 * Constructor for RoomArea class.
	 * @param n Name of area to be checked.
	 * @param b Image of area. 
	 * @param r Bounds of check zone in RoomArea's associated Room. 
	 */
	public RoomArea(String n, Image b, Rectangle r) {
		name = n;
		bg = b;
		checkZone = r;
	}
	
	/**
	 * Constructor for RoomArea with a spider.
	 * @param n Name of area to be checked.
	 * @param b Image of area. 
	 * @param r Bounds of check zone in RoomArea's associated Room. 
	 * @param s Spider entity.
	 */
	public RoomArea(String n, Image b, Rectangle r, Spider s) {
		name = n;
		bg = b;
		checkZone = r;
		entity = s;
	}
	
	/**
	 * Method to return the name of the RoomArea.
	 * @return Name of RoomArea
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Method to return the background of the RoomArea.
	 * @return Image of background.
	 */
	public Image getBg() {
		return bg;
	}
	
	/**
	 * Method to return the check zone that triggers the prompt to enter RoomArea.
	 * @return a Rectangle representing the check zone.
	 */
	public Rectangle getCheckZone() {
		return checkZone;
	}
	
	/**
	 * Method to check if the RoomArea contains a spider.
	 * @return True if this area has a spider.
	 */
	public boolean hasSpider() {
		return entity != null;
	}
	
	/**
	 * Method to check if the room had a spider but was removed.
	 * @return True if there is an entity, but it has been removed.
	 */
	public boolean spiderCaught() {
		return entity.isRemoved();
	}
	
	/**
	 * Method to return the spider in the RoomArea.
	 * @return the Spider entity, or null if there isn't one.
	 */
	public Spider getSpider() {
		return entity;
	}
}
