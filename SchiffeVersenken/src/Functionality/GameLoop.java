package Functionality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import gameObjects.Map;
import gameObjects.ShipManager;
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
//		private int legalAmountSubmarine = 4;
//		private int legalAmountDestroyer = 3;
//		private int legalAmountCruiser = 2;
//		private int legalAmountBattleship = 1;
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
		// TODO: Now do the communication. Easy, right...?

		System.out.println("Host: 0 \nClient: 1");

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
