package Functionality;

import gameObjects.Map;
import gameObjects.ShipManager;
import ships.Cruiser;
import ships.Destroyer;

public class GameLoop {
	//TODO: Make a thing to create your own map and set ships
	//TODO: Maybe make config file for stuff like ship length
	public static void main(String[] args) {
		System.out.println("Start");
		Map map = new Map();
		System.out.println(map.getMapAsText());
		map.testShots();
		System.out.println(map.getMapAsText());
		System.out.println("End");

		//TODO: TEST THIS: THIS IS WHERE YOU LEFT OFF
		ShipManager sm = new ShipManager();
		sm.placeShip(new Destroyer(0, 0, 2));
		sm.placeShip(new Cruiser (2, 2, 3));
		
	}

}
