package Functionality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.SocketException;

import gameObjects.Map;
import gameObjects.ShipManager;
import network.Listener;
import network.Sender;
import ships.Battleship;
import ships.Cruiser;
import ships.Destroyer;
import ships.Submarine;

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

		// Create a datagram socket for both Sender and Listener and create them for the
		// first time.
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(42069);
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println("Could not create DatagramSocket");
		}
		Sender sender = Sender.getSender();
		sender.setDatagramSocket(ds);
		Listener listener = Listener.getListener();
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
		if (choice == 0) {
			System.out.println("Starting Server.");
		} else {
			System.out.println("Searching for Server");
		}
	}
}
