package Functionality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.SocketException;

import gameObjects.Map;
import gameObjects.ShipManager;
import gameObjects.Shot;
import network.ListenerUDP;
import network.SenderUDP;
import ships.shipTypes.Battleship;
import ships.shipTypes.Cruiser;
import ships.shipTypes.Destroyer;
import ships.shipTypes.Submarine;

public class GameLoop {
	// TODO: Make a thing to create your own map and set ships
	// TODO: Maybe make config file for stuff like ship length
	public static void main(String[] args) {

		System.out.println("Start");
		Map map = new Map();
		System.out.println(map.getMapAsText());
		map.testShots();
		System.out.println(map.getMapAsText());
		System.out.println("End");

		ShipManager sm = new ShipManager();

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

		sm.shootShip(new Shot("F", "0"));
		sm.shootShip(new Shot("G", "0"));

		// Create a datagram socket for both Sender and Listener and create them for the
		// first time.
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(42069);
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println("Could not create DatagramSocket");
		}
		SenderUDP sender = SenderUDP.getSender();
		sender.setDatagramSocket(ds);
		ListenerUDP listener = ListenerUDP.getListener();
		listener.setDatagramSocket(ds);

		System.out.println("Menu:\nHost: 0 \nClient: 1");

		int choice = 0;

		// Construct the object needed for reading user input.
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		// Get a choice from the user.
		try {
			choice = Integer.parseInt(in.readLine());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		GameManager gameManager;
		if (choice == 0) {
			System.out.println("Starting Server.");
			gameManager = new GameManagerHost();
		} else {
			System.out.println("Searching for Server");
			gameManager = new GameManagerClient();
		}
		gameManager.establishConnection(map, sm);
	}
}
