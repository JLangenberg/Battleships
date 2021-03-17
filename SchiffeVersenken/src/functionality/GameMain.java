package functionality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.SocketException;

import gameObjects.Map;
import gameObjects.ships.shipTypes.Battleship;
import gameObjects.ships.shipTypes.Cruiser;
import gameObjects.ships.shipTypes.Destroyer;
import gameObjects.ships.shipTypes.Submarine;
import network.ListenerUDP;
import utility.Config;

/**
 * Main class of this project. Creates all needed objects and starts
 * communication.
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class GameMain {

	public static void main(String[] args) {

		// Welcome message
		System.out.println("Welcome to Battleships.");
		// Create the map for the user to keep track of his shots
		Map map = new Map();
		// Create a new game Loop to access GameMain's methods
		GameMain gameMain = new GameMain();
		// Initialize the UDP-Listener and UDP-Sender
		gameMain.initUDPCommunication();
		// Create the ship manager for the user to place his ships in. Then make the
		// user place his ships.
		ShipManager sm = gameMain.shipSetupMenu();
		// Let the user choose between being Host or Client
		String choice = gameMain.inputString("Menu:\nHost: 0 \nClient: 1 or else");
		// Create a gameManager and start the game.
		GameManager gameManager;

		// If the user wants to be host, create a GameManager with the functionality a
		// host needs.
		if (choice.contains("0")) {
			System.out.println("Starting Server.");
			gameManager = new GameManagerHost();
		} else {
			// If the user wants to be client, create a GameManager with the functionality a
			// client needs.
			System.out.println("Searching for Server");
			gameManager = new GameManagerClient();
		}
		// Start the gameManager and establish a connection to an opponent.
		gameManager.establishConnection(map, sm);
	}

	/**
	 * Create a datagramSocket for the UDPListener
	 */
	private void initUDPCommunication() {

		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(42069);
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println("Could not create DatagramSocket");
		}
		// Hand the socket to the listener. It is a singleton and needs the socket only
		// once.
		ListenerUDP listener = ListenerUDP.getListener();
		listener.setDatagramSocket(ds);
	}

	/**
	 * Demo method that places ships in a preset manner.
	 * 
	 * @param sm The ship manager that stores the ships.
	 */
	private void demoShips(ShipManager sm) {
		sm.placeShip(new Destroyer(0, 0, 2));
		sm.placeShip(new Destroyer(0, 2, 2));
		sm.placeShip(new Destroyer(0, 4, 2));

		sm.placeShip(new Cruiser(0, 6, 2));
		sm.placeShip(new Cruiser(0, 8, 2));

		sm.placeShip(new Battleship(5, 9, 2));

		sm.placeShip(new Submarine(4, 0, 2));
		sm.placeShip(new Submarine(7, 0, 2));
		sm.placeShip(new Submarine(4, 2, 2));
		sm.placeShip(new Submarine(4, 4, 2));

		System.out.println(sm.getShipMap());
	}

	/**
	 * Starts the process of letting the user decide the position of their ships.
	 * Also offers a preset-placement.
	 * 
	 * @return The shipManager with the new ships
	 */
	private ShipManager shipSetupMenu() {
		// Tell the user what to do
		System.out.println("Do you want to place ships on your own or use a preset?");
		String input = inputString("0: Place ships\nOther: Preset");
		// Create a ship manager
		ShipManager sm = new ShipManager();
		if (input.equals("0")) {
			// Let the user set their ships
			shipPlacement(sm);
		} else {
			// Place a preset of ships
			demoShips(sm);
		}
		// Return the shipManager after setup
		return sm;
	}

	/**
	 * Method that lets the user place ships on the shipManager handed to the method
	 * 
	 * @param sm The shipManager to place the ships on
	 */
	private void shipPlacement(ShipManager sm) {
		// Let the user place Battleships
		for (int i = 0; i < Config.AMOUNTBATTLESHIP; i++) {
			placeBattleship(sm);
		}
		// Let the user place Cruisers
		for (int i = 0; i < Config.AMOUNTCRUISER; i++) {
			placeCruiser(sm);
		}
		// Let the user place Destroyers
		for (int i = 0; i < Config.AMOUNTDESTROYER; i++) {
			placeDestroyer(sm);
		}
		// Let the user place Submarines
		for (int i = 0; i < Config.AMOUNTSUBMARINE; i++) {
			placeSubmarine(sm);
		}
	}

	/**
	 * Place a submarine in the ship manager
	 * 
	 * @param sm shipManager to place ships in
	 */
	private void placeSubmarine(ShipManager sm) {
		// Whether or not the ship is at a valid position
		boolean validPosition;
		do {
			System.out.println("Place a submarine");
			// Let the user enter the configuration of the ship
			int[] shipSettings = getShipSettings();
			// Try to place the ship and check if the position is legal
			validPosition = sm.placeShip(new Submarine(shipSettings[0], shipSettings[1], shipSettings[2]));
			System.out.println(sm.getShipMap());
		} while (!validPosition);
	}

	/**
	 * Place a destroyer in the ship manager
	 * 
	 * @param sm shipManager to place ships in
	 */
	private void placeDestroyer(ShipManager sm) {
		// Whether or not the ship is at a valid position
		boolean validPosition;
		do {
			System.out.println("Place a destroyer");
			// Let the user enter the configuration of the ship
			int[] shipSettings = getShipSettings();
			// Try to place the ship and check if the position is legal
			validPosition = sm.placeShip(new Destroyer(shipSettings[0], shipSettings[1], shipSettings[2]));
			System.out.println(sm.getShipMap());
		} while (!validPosition);
	}

	/**
	 * Place a cruiser in the ship manager
	 * 
	 * @param sm shipManager to place ships in
	 */
	private void placeCruiser(ShipManager sm) {
		// Whether or not the ship is at a valid position
		boolean validPosition;
		do {
			System.out.println("Place a cruiser");
			// Let the user enter the configuration of the ship
			int[] shipSettings = getShipSettings();
			// Try to place the ship and check if the position is legal
			validPosition = sm.placeShip(new Cruiser(shipSettings[0], shipSettings[1], shipSettings[2]));
			System.out.println(sm.getShipMap());
		} while (!validPosition);
	}

	/**
	 * Place a battleship in the ship manager
	 * 
	 * @param sm shipManager to place ships in
	 */
	private void placeBattleship(ShipManager sm) {
		// Whether or not the ship is at a valid position
		boolean validPosition;
		do {
			System.out.println("Place a battleship");
			int[] shipSettings = getShipSettings();
			// Try to place the ship and check if the position is legal
			validPosition = sm.placeShip(new Battleship(shipSettings[0], shipSettings[1], shipSettings[2]));
			System.out.println(sm.getShipMap());
		} while (!validPosition);
	}

	/**
	 * Gets the attributes every ship needs to have from the user via console input.
	 * Crashes when user doesn't enter correct values.
	 * 
	 * @return The settings in an int array
	 */
	private int[] getShipSettings() {
		// Array for storing settings
		int[] settings = { 0, 0, 0 };
		// Get the x Root
		String xCoord = inputString("Where do you want the x-Root of your ship to be? \nPlease enter a number.");
		settings[0] = Integer.parseInt(xCoord);
		// Get the y root
		String yCoord = inputString("Where do you want the y-Root of your ship to be?");
		settings[1] = Integer.parseInt(yCoord);
		// Get the orientation of the shop
		String direction = inputString("Where do you want your ship to point?\n1:North\n2:East\n3:Sout\n4:West");
		settings[2] = Integer.parseInt(direction);
		// Return the settings.
		return settings;
	}

	/**
	 * Prints a message (string) to the console, lets the user input a string and
	 * returns the input as string
	 * 
	 * @param message The message to display on the console before prompting input
	 * @return The String that was put in by the user
	 */
	private String inputString(String message) {
		// Tell the user what input will do what
		System.out.println(message);
		// The var for the choice
		String choice = "0";

		// Construct the object needed for reading user input.
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		// Get a choice from the user.
		try {
			// Read the choice from the bufferedReader
			choice = in.readLine();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Return the input
		return choice;
	}
}
