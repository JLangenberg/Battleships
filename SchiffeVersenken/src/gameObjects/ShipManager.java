package gameObjects;

import java.util.ArrayList;

import ships.Ship;

public class ShipManager {
	// TODO: Make an arrayList with all coordinates that need to be free. Like
	// "forbiddenTiles"
	// XXX: THIS IS WHERE I LEFT OF last time, not current
	private ArrayList<Point> forbiddenPoints = new ArrayList<Point>();
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

	/**
	 * Stores all ships and their location on the field
	 */
	private ArrayList<Ship> ships = new ArrayList<Ship>();

	// TODO: Maybe make it more flexible. Make JSON with 1 object per ship type,
	// including leagalAmount n stuff
	boolean placeShip(Ship ship) {
		// Check if this type of ship is still legal.
		if (legalAmountOfShips[ship.getShipId()] > amountOfShips[ship.getShipId()]) {
			System.out.println("Ship-type is still available.");
		} else {
			System.out.println("Maximum amount of ships of this type reached.");
			return false;
		}

		// Get all the points the ship will take up/be placed on
		ArrayList<Point> points = ship.getShipFields();
		// Check if all of them are on the map and on legal points.
		for (int i = 0; points.size() > i; i++) {
			if (points.get(i).isOnMap() == false) {
				return false;
			} else {
				// Check if none of the points are forbidden
				for (int j = 0; forbiddenPoints.size() > j; j++) {
					Point forbiddenPoint = forbiddenPoints.get(j);
					Point point = points.get(i);
					// Check if the current forbiddenPoint equals the current Point
					if ((point.getX() == forbiddenPoint.getX()) || (point.getY() == forbiddenPoint.getY())) {
						return false;
					}
				}
			}
		}
		// Get all the points the ship will border and that have to be free.
		//TODO
		ArrayList<Point> illegalFields = ship.getIllegalFields();
		// TODO: Is it at a legal place?

		amountOfShips[ship.getShipId()]++;
		return true;
	}
}
