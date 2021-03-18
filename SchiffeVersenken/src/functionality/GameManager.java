package functionality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;

import gameObjects.Map;
import gameObjects.Shot;
import network.SocketConnection;
import utility.MessagePresets;

public abstract class GameManager {
	// The serverSocket for the connection. Only defined when user hosts.
	ServerSocket serverSocket;
	// The ship manager that handles this clients ships
	ShipManager sm;
	// The map that keeps track of the opponents waters.
	Map map;
	// The address to send the messages to when playing
	InetAddress opponentAddress;
	// The connection to communicate with when the initial communication has been
	// finished.
	SocketConnection connection;

	/**
	 * Establishes a connection with an opponent.
	 * 
	 * @param map Used in further gameplay
	 * @param sm  Used in further gameplay
	 */
	public abstract void establishConnection(Map map, ShipManager sm);

	/**
	 * Lets the user input where to shoot and then sends this shot to the opponent
	 * via SocektConnection
	 */
	protected void fireShot() {
		// Let the user input where to shoot
		Shot shot = getShotFromUser();
		// If a valid shot was entered, tell the user input was ok and shoot.
		System.out.println("Got it. Shooting at " + shot.getX() + shot.getY());
		// Tell the opponent where you're shooting.
		connection.sendMessage(MessagePresets.FIRE + ",[" + shot.getX() + "],[" + shot.getY() + "]");
		// Wait for opponents response
		getShotFeedback(shot);
	}

	/**
	 * Method that is called after this client sent a shot message. This method is
	 * responsible for handling the feedback, and acting accordingly.
	 * 
	 * @param shot The shot that was sent beforehand. Used for manipulating the map
	 *             later.
	 */
	private void getShotFeedback(Shot shot) {
		// Get the response of the opponents client.
		String response = connection.receiveMessage();

		// If the shot hit, update the map, notify the user and shoot again.
		if (response.contains(MessagePresets.HIT)) {
			// Update the map
			map.setFieldHit(shot.getXAsInt(), shot.getYAsInt());
			// Tell the user he hit
			System.out.println("HIT!");
			// Show the updated map
			System.out.println(map.getMapAsText());
			// Shoot another shot
			fireShot();
		}
		// If the shot missed, notify the user, update the map and wait for an enemy
		// shot.
		if (response.contains(MessagePresets.MISS)) {
			// Update the map
			map.setFieldWater(shot.getXAsInt(), shot.getYAsInt());
			// Tell the user he missed
			System.out.println("MISS!");
			// Show the updated map
			System.out.println(map.getMapAsText());
			// Wait for opponents shot
			getEnemyShot();
		}
		// When the shot destroyed the opponents last ship, update the map and tell the
		// user he won. It is important to check destroyedLast Ship first. Destroyed
		// would also be true if it was destroyed last ship.
		if (response.contains(MessagePresets.DESTROYEDLASTSHIP)) {
			map.setFieldHit(shot.getXAsInt(), shot.getYAsInt());
			System.out.println(map.getMapAsText());
			System.out.println("Hit\nDestroyed last Ship! \nYou won!");
			// Close the connections
			connection.close();
			// If the user is host, also close the serverSocket
			if (serverSocket != null) {
				closeServer(serverSocket);
			}
		} else
		// When the shot destroyed a ship, notify the user, update the map and shoot
		// again.
		if (response.contains(MessagePresets.DESTROYED)) {
			// Update the map
			map.setFieldHit(shot.getXAsInt(), shot.getYAsInt());
			// Tell the user he hit and destroyed a ship.
			System.out.println("HIT!\nShip destroyed!");
			// Show the updated map
			System.out.println(map.getMapAsText());
			// Shoot another shot
			fireShot();
		}
	}

	/**
	 * This method is called when the enemy sends a shot to this client. This method
	 * takes the message in, compares it to the location of the ships, checks if a
	 * ship has hit, and continues accordingly.
	 */
	protected void getEnemyShot() {
		// Receive the shot
		String shotMessage = connection.receiveMessage();
		// Extract the target coordinate from the message
		Shot shot = getShotFromMessage(shotMessage);
		// Compare the shot to location of own ships
		int response = sm.shootShip(shot);
		// MISS
		if (response == 0) {
			// Say miss, show map, tell opponent, and shoot own shot
			incomingShotResponse("Incoming shot missed. " + shot.getShotAsMessage(),
					MessagePresets.MISS + "," + shot.getShotAsMessage());
			fireShot();
		}
		// HIT
		else if (response == 1) {
			// Say hit, show map, tell opponent, and wait for next shot
			incomingShotResponse("Hit! " + shot.getShotAsMessage() + " Waiting for next shot.",
					MessagePresets.HIT + "," + shot.getShotAsMessage());
			getEnemyShot();
		}
		// DESTROYED
		else if (response == 2) {
			// Say destroyed, show map, tell opponent, and wait for next shot
			incomingShotResponse("Hit! " + shot.getShotAsMessage() + " Waiting for next shot.",
					MessagePresets.DESTROYED + "," + shot.getShotAsMessage());
			getEnemyShot();
		}
		// DESTROYED LAST SHIP
		else if (response == 3) {
			// Say destroyed last ship, show map, tell opponent, and do game over.
			incomingShotResponse("Hit! " + shot.getShotAsMessage() + " Last Ship was destroyed. \nYou lost!",
					MessagePresets.DESTROYEDLASTSHIP + "," + shot.getShotAsMessage());
			// End the connection.
			connection.close();
			// If the user is a host, close the server too.
			if (serverSocket != null) {
				closeServer(serverSocket);
			}
		}
	}

	/**
	 * Prints consoleOutput to the console, shows the shipMap and sends
	 * messageForOpponent to the opponent via TCP connection
	 * 
	 * @param consoleOutput      String to print to the console
	 * @param messageForOpponent String to send to the opponent
	 */
	private void incomingShotResponse(String consoleOutput, String messageForOpponent) {
		// Tell the user what happened
		System.out.println(consoleOutput);
		// Show the user the current state of the ship map
		System.out.println(sm.getShipMap());
		// Tell the opponent what happened
		connection.sendMessage(messageForOpponent);
	}

	/**
	 * Extracts shot coordinates from a message
	 * 
	 * @param message Message to get coordinates from
	 * @return The Shot object containing the shot coordinates
	 */
	private Shot getShotFromMessage(String message) {
		// The arrays to compare to
		CharSequence[] xCoordinates = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
		CharSequence[] yCoordinates = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		// The shot coordinates to extract
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
		// If a transition was unsuccessful, an error is bound to occur. Notify the
		// user of the why!
		if (shotX == "default" || shotY == "default") {
			System.out.println("Error incoming! Coordinates got as 'default'. YIKES!");
			if (shotX == "default") {
				System.out.println("ShotX is still default.");
			}
			if (shotY == "default") {
				System.out.println("ShotY is still default.");
			}
		}
		// Return the shot.
		return new Shot(shotX, shotY);
	}

	/**
	 * Gets where the user wants his shot to go
	 * 
	 * @return A Shot with the entered coordinates
	 */
	private Shot getShotFromUser() {
		// Tell the user what to do
		System.out.println("Where do you want to shoot?\nExample: 'A5'");
		// Array for comparing
		CharSequence[] xCoordinates = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
		CharSequence[] yCoordinates = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		// The var for the shot coordinates
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

	/**
	 * Starts the server
	 * 
	 * @return An initialized serverSocket
	 */
	protected ServerSocket startServer() {
		// Create an empty socket
		ServerSocket serverSocket = null;
		try {
			// Fill it.
			serverSocket = new ServerSocket(42069);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Return it.
		return serverSocket;
	}

	/**
	 * Closes the server socket stream
	 * 
	 * @param serverSocket The server socket that should be closed
	 */
	private void closeServer(ServerSocket serverSocket) {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
