package functionality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.SocketException;

import gameObjects.Map;
import gameObjects.ShipManager;
import network.ListenerUDP;
import network.SenderUDP;
import ships.shipTypes.Battleship;
import ships.shipTypes.Cruiser;
import ships.shipTypes.Destroyer;
import ships.shipTypes.Submarine;
import utility.Config;

/**
 * Main class of this project. Creates all needed objects and starts
 * communication.
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class GameLoop {

	public static void main(String[] args) {

		// Welcome message
		System.out.println("Welcome to Battleships.");
		// Create the map for the user to keep track of his shots
		Map map = new Map();
		// Create the ship manager for the user to place his ships in.
		ShipManager sm = new ShipManager();
		// Place a few ships for quick testing.
		// GameLoop.demoShips(sm);
		// Initialize the UDP-Listener and UDP-Sender
		GameLoop.initUDPCommunication();
		// Create a new game Loop tp access GameLoop's methods
		GameLoop gameLoop = new GameLoop();
		gameLoop.shipSetupMenu();
		// Let the user choose between being Host or Client
		String choice = gameLoop.inputString("Menu:\nHost: 0 \nClient: 1 or else");
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
	 * Prints a string to the console, lets the user input a string and return the
	 * string
	 * 
	 * @return The String that was put in.
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

	/**
	 * Create a datagramSocket for both Sender and Listener.
	 */
	private static void initUDPCommunication() {

		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(42069);
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println("Could not create DatagramSocket");
		}
		// Hand the socket to the objects. SenderUDP and ListenerUDP are both
		// singletons.
		SenderUDP sender = SenderUDP.getSender();
		sender.setDatagramSocket(ds);
		ListenerUDP listener = ListenerUDP.getListener();
		listener.setDatagramSocket(ds);
	}

	/**
	 * Demo method that places ships in a preset manner.
	 * 
	 * @param sm The ship manager that stores the ships.
	 */
	private static void demoShips(ShipManager sm) {
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

	private ShipManager shipSetupMenu() {
		System.out.println("Do you want to place ships on your own or use a preset?");
		String input = inputString("0: Place ships\nOther: Preset");
		// Create a ship manager
		ShipManager sm = new ShipManager();
		if (input.equals("0")) {
			shipPlacement(sm);
		} else {
			// Place a preset of ships
			demoShips(sm);
		}
		// Return the shipManager after setup
		return sm;
	}

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

	private int[] getShipSettings() {
		int[] settings = { 0, 0, 0 };
		String xCoord = inputString("Where do you want the x-Root of your ship to be? \nPlease enter a number.");
		settings[0] = Integer.parseInt(xCoord);
		String yCoord = inputString("Where do you want the y-Root of your ship to be?");
		settings[1] = Integer.parseInt(yCoord);
		String direction = inputString("Where do you want your ship to point?\n1:North\n2:East\n3:Sout\n4:West");
		settings[2] = Integer.parseInt(direction);
		return settings;
	}

	private void placeSubmarine(ShipManager sm) {
		boolean validPosition;
		do {
			System.out.println("Place a submarine");
			int[] shipSettings = getShipSettings();
			validPosition = sm.placeShip(new Submarine(shipSettings[0], shipSettings[1], shipSettings[2]));
			System.out.println(sm.getShipMap());
		} while (!validPosition);
	}

	private void placeDestroyer(ShipManager sm) {
		boolean validPosition;
		do {
			System.out.println("Place a destroyer");
			int[] shipSettings = getShipSettings();
			validPosition = sm.placeShip(new Destroyer(shipSettings[0], shipSettings[1], shipSettings[2]));
			System.out.println(sm.getShipMap());
		} while (!validPosition);
	}

	private void placeCruiser(ShipManager sm) {
		boolean validPosition;
		do {
			System.out.println("Place a cruiser");
			int[] shipSettings = getShipSettings();
			validPosition = sm.placeShip(new Cruiser(shipSettings[0], shipSettings[1], shipSettings[2]));
			System.out.println(sm.getShipMap());
		} while (!validPosition);
	}

	private void placeBattleship(ShipManager sm) {
		boolean validPosition;
		do {
			System.out.println("Place a battleship");
			int[] shipSettings = getShipSettings();
			validPosition = sm.placeShip(new Battleship(shipSettings[0], shipSettings[1], shipSettings[2]));
			System.out.println(sm.getShipMap());
		} while (!validPosition);
	}
}
