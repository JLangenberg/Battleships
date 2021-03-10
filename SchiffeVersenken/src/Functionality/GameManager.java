package Functionality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import Utility.MessagePresets;
import gameObjects.Map;
import gameObjects.ShipManager;
import gameObjects.Shot;
import network.Listener;
import network.Message;
import network.Sender;

//TODO: You could do an anti-cheating thing. Like when you destroyed more ships than there should be, do a notification or something.
public abstract class GameManager {
	// The ship manager that handles this clients ships
	ShipManager sm;
	// The map that keeps track of the opponents waters.
	Map map;
	// The address to send the messages to when playing
	InetAddress opponentAddress;

	public abstract void establishConnection(Map map, ShipManager sm);

	protected void fireShot() {
		// Let the user input where to shoot
		Shot shot = getShotLocation();
		// If a valid shot was entered, tell the user input was ok and shoot.
		System.out.println("Got it. Shooting at " + shot.getX() + shot.getY());
		/// XXX: Debug message.
		System.out.println("Sending: " + MessagePresets.FIRE + ",[" + shot.getX() + "],[" + shot.getY() + "]");
		// Tell the opponent where you're shooting.
		Sender.getSender().sendMessage(opponentAddress,
				MessagePresets.FIRE + ",[" + shot.getX() + "],[" + shot.getY() + "]");
		// Wait for opponents response
		getShotFeedback(shot);
	}

	private void getShotFeedback(Shot shot) {
		// Get the response of the opponents client.
		Message response = Listener.getListener().listenForKeywords(MessagePresets.SHOTRESPONSES, opponentAddress);

		// When the shot hit, update the map, notify the user and shoot again.
		if (response.getContent().contains(MessagePresets.HIT)) {
			// Update the map
			map.setFieldHit(shot.getXAsInt(), shot.getYAsInt());
			// Tell the user he hit
			System.out.println("HIT!");
			// Show the updated map
			System.out.println(map.getMapAsText());
			// Shoot another shot
			fireShot();
		}
		// When the shot missed, notify the user, update the map and wait for an enemy
		// shot.
		if (response.getContent().contains(MessagePresets.MISS)) {
			// Update the map
			map.setFieldWater(shot.getXAsInt(), shot.getYAsInt());
			// Tell the user he missed
			System.out.println("MISS!");
			// Show the updated map
			System.out.println(map.getMapAsText());
			// Wait for opponents shot
			getEnemyShot();
		}
		// When the shot destroyed a ship, notify the user, update the map and shoot
		// again.
		if (response.getContent().contains(MessagePresets.DESTROYED)) {
			// Update the map
			map.setFieldHit(shot.getXAsInt(), shot.getYAsInt());
			// Tell the user he hit and destroyed a ship.
			System.out.println("HIT!\nShip destroyed!");
			// Show the updated map
			System.out.println(map.getMapAsText());
			// Shoot another shot
			fireShot();

		}
		// When the shot destroyed the opponents last ship, update the map and tell the
		// user he won.
		if (response.getContent().contains(MessagePresets.DESTROYEDLASTSHIP)) {
			map.setFieldHit(shot.getXAsInt(), shot.getYAsInt());
			System.out.println(map.getMapAsText());
			System.out.println("Hit\nDestroyed last Ship! \nYou won!");
			// TODO: Game end, won. Does something have to happen here?
		}

	}

	protected void getEnemyShot() {
		// TODO: Make/Take method that only receives from opponent.
		Message shotMessage = Listener.getListener().listenForKeyword(MessagePresets.FIRE, opponentAddress);
		// Extract the target coordinate from the message
		Shot shot = getShotFromMessage(shotMessage.getContent());
		// Shoot the shot at the ship.
		int response = sm.shootShip(shot);
		if (response == 0) {
			// Say miss and shoot own shot
			System.out.println("Incoming shot missed. " + shot.getShotAsMessage());
			Sender.getSender().sendMessage(opponentAddress, MessagePresets.MISS + "," + shot.getShotAsMessage());
			fireShot();
		} else if (response == 1) {
			// Say hit and wait for next shot
			System.out.println("Hit! " + shot.getShotAsMessage() + " Waiting for next shot.");
			Sender.getSender().sendMessage(opponentAddress, MessagePresets.HIT + "," + shot.getShotAsMessage());
			getEnemyShot();
		} else if (response == 2) {
			// say destroyed and wait for next shot
			System.out.println("Hit! " + shot.getShotAsMessage() + " Waiting for next shot.");
			Sender.getSender().sendMessage(opponentAddress, MessagePresets.DESTROYED + "," + shot.getShotAsMessage());
			getEnemyShot();
		} else if (response == 3) {
			// say destroyed last ship and do game over.
			Sender.getSender().sendMessage(opponentAddress,
					MessagePresets.DESTROYEDLASTSHIP + "," + shot.getShotAsMessage());
			System.out.println("Hit! " + shot.getShotAsMessage() + " Last Ship was destroyed. \nYou lost!");

		}
	}

	private Shot getShotFromMessage(String message) {
		CharSequence[] xCoordinates = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
		CharSequence[] yCoordinates = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

		String shotX;
		String shotY;

		// Reset variables
		shotX = "default";
		shotY = "default";

		// Check if input is legal
		// Check if the message contains a valid x Coordinate.
		for (int i = 0; i < xCoordinates.length; i++) {
			if (message.contains(",[" + xCoordinates[i] + "]")) {
				shotX = xCoordinates[i].toString();
			}
		}
		// Check if the message contains a valid y Coordinate.
		for (int i = 0; i < yCoordinates.length; i++) {
			if (message.contains(",[" + yCoordinates[i] + "]")) {
				shotY = yCoordinates[i].toString();
			}
		}
		return new Shot(shotX, shotY);
	}

	// TODO: Put this into a seperate UI class.
	private Shot getShotLocation() {
		System.out.println("Where do you want to shoot?\nExample: 'A5'");

		CharSequence[] xCoordinates = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
		CharSequence[] yCoordinates = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

		String shotX;
		String shotY;
		// Booleans that get set to true when a valid coordinate has been found.
		boolean legalX;
		boolean legalY;
		do {
			// String for the user input
			String choice = null;
			// Reset variables
			shotX = "default";
			shotY = "default";
			legalX = false;
			legalY = false;

			// Construct the object needed for reading user input.
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			// Get a choice from the user.
			try {
				choice = in.readLine();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Check if input is legal
			// Check if the message contains a valid x Coordinate.
			for (int i = 0; i < xCoordinates.length; i++) {
				if (choice.contains(xCoordinates[i])) {
					shotX = xCoordinates[i].toString();
					legalX = true;
				}
			}
			// Check if the message contains a valid y Coordinate.
			for (int i = 0; i < yCoordinates.length; i++) {
				if (choice.contains(yCoordinates[i])) {
					shotY = yCoordinates[i].toString();
					legalY = true;
				}
			}
			// If no legal input was found, notify the user.
			if (!legalX || !legalY) {
				System.out.println("Invalid input. Please try again.");
			}
			// Run until both a legalX and a legalY has been found
		} while (!legalX || !legalY);
		// Return the got shot coordinates
		return new Shot(shotX, shotY);
	}
}
