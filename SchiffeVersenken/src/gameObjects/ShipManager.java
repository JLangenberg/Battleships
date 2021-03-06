package gameObjects;

import java.util.ArrayList;

import ships.Ship;

public class ShipManager {
	// The list of forbidden points no ship should be placed on
	private ArrayList<Point> forbiddenPoints = new ArrayList<Point>();
	/**
	 * Stores all ships and their location on the field
	 */
	private ArrayList<Ship> ships = new ArrayList<Ship>();

	// TODO: All of this belongs in some kind of config. Make 1 with standard rules,
	// not changeable and one flexible one Maybe transfer config to other client to
	// match rules flexibly??
	private int legalAmountSubmarine = 4;
	private int legalAmountDestroyer = 3;
	private int legalAmountCruiser = 2;
	private int legalAmountBattleship = 1;
	private int[] legalAmountOfShips = new int[] { legalAmountSubmarine, legalAmountDestroyer, legalAmountCruiser,
			legalAmountBattleship };

	private int amountSubmarine = 0;
	private int amountDestroyer = 0;
	private int amountCruiser = 0;
	private int amountBattleship = 0;
	private int[] amountOfShips = new int[] { amountSubmarine, amountDestroyer, amountCruiser, amountBattleship };

	// TODO: Maybe make it more flexible. Make JSON with 1 object per ship type,
	// including legalAmount n stuff
	public boolean placeShip(Ship ship) {

		// Check if this type of ship is still legal.
		if (legalAmountOfShips[ship.getShipId()] <= amountOfShips[ship.getShipId()]) {
			System.out.println("Maximum amount of ships of this type reached. ShipID: " + ship.getShipId());
			return false;
		}

		// Get all the points the ship will take up/be placed on
		ArrayList<Point> shipFields = ship.getShipFields();
		// Get all the points the ship will border and that have to be free (in terms of
		// other ships).
		ArrayList<Point> borderingFields = ship.getBorderingFields();

		// Check if all of the taken up points are on the map
		// Loop through all of the points the ship is on
		for (int i = 0; shipFields.size() > i; i++) {
			// Check if the point is on the map
			if (shipFields.get(i).isOnMap() == false) {
				// If the ship is not completely on the map, return false.
				System.out.println("Ship is not on map. ShipID: " + ship.getShipId());
				return false;
			}
		}
		// Check if the ship is on any forbidden points or bordering any fields it
		// should not be bordering.
		if (isOverlapping(shipFields, forbiddenPoints) || isOverlapping(borderingFields, forbiddenPoints)) {
			System.out.println("Ship is placed on or bordering on illegal fields. ShipID: " + ship.getShipId());
			return false;
		}
		//
		/*
		 * If all checks have been passed, add the ships points to the list of forbidden
		 * points ## for future comparison, count it to the max ship number of its type,
		 * and save it in the ## ships arrayList.
		 */
		// Add shipFields to forbidden points
		for (int i = 0; i < shipFields.size(); i++) {
			forbiddenPoints.add(shipFields.get(i));
		}

		System.out.println("Successfully placed ship with ID: " + ship.getShipId());
		// Add to the number of ships of the current shipType
		amountOfShips[ship.getShipId()]++;
		// Add the ship to the list of ships in the game
		ships.add(ship);
		// Return true, indicating a successful placement.
		return true;
	}

	/**
	 * Takes in two ArrayLists<Point> and checks if they contain one or more points
	 * that are the same.
	 * 
	 * @param pointListA
	 * @param pointListB
	 * @return
	 */
	private boolean isOverlapping(ArrayList<Point> pointListA, ArrayList<Point> pointListB) {
		// Check if none of the points are forbidden
		for (int i = 0; pointListB.size() > i; i++) {
			// Loop through all of the forbidden points
			for (int j = 0; pointListA.size() > j; j++) {
				// Get the current forbidden point for better readability
				Point pointA = pointListA.get(j);
				Point pointB = pointListB.get(i);
				// Check if the current pointA equals the current PointB
				if ((pointB.getX() == pointA.getX()) && (pointB.getY() == pointA.getY())) {
					// If yes, theres an overlap
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Draws all ships of the player on this client on a map.
	 * 
	 * @return
	 */
	public String getShipMap() {
		int fieldHeight = 10;
		int fieldWidth = 10;

		String map = "***|A|B|C|D|E|F|G|H|I|J|\n***---------------------\n";

		// Go through all fields of the map
		for (int y = 0; y < fieldHeight; y++) {
			map += "**" + y;
			for (int x = 0; x < fieldWidth; x++) {
				map += "|";
				// Check for a ship.
				boolean foundShip = false;
				for (int l = 0; l < forbiddenPoints.size(); l++) {
					if ((forbiddenPoints.get(l).getY()) == y && (forbiddenPoints.get(l).getX() == x)) {
						foundShip = true;
						break;
					}
				}
				if (foundShip) {
					map += "S";
				} else {
					map += " ";
				}
			}
			map += "|\n";
		}
		return map;
	}
}
