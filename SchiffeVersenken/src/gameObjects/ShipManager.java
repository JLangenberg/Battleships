package gameObjects;

import java.util.ArrayList;

import ships.Ship;

public class ShipManager {
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
	// including legalAmount n stuff
	public boolean placeShip(Ship ship) {

		// Check if this type of ship is still legal.
		if (legalAmountOfShips[ship.getShipId()] > amountOfShips[ship.getShipId()]) {
			System.out.println("Ship-type is still available.");
		} else {
			System.out.println("Maximum amount of ships of this type reached.");
			return false;
		}

		// Check if the type is at a legal spot

		// Get all the points the ship will take up/be placed on
		ArrayList<Point> points = ship.getShipFields();

		// Check if all of the taken up points are on the map and on legal points.

		// Loop through all of the points the ship is on
		for (int i = 0; points.size() > i; i++) {
			// Check if the point is on the map
			if (points.get(i).isOnMap() == false) {
				return false;
			} else {
				// Check if none of the points are forbidden

				// Loop through all of the forbidden points
				for (int j = 0; forbiddenPoints.size() > j; j++) {
					// Get the current forbidden point for better readability
					Point forbiddenPoint = forbiddenPoints.get(j);
					Point point = points.get(i);
					// Check if the current forbiddenPoint equals the current Point
					if ((point.getX() == forbiddenPoint.getX()) || (point.getY() == forbiddenPoint.getY())) {
						// If yes, the ship can not be placed.
						return false;
					}
				}
			}
		}
		// Get all the points the ship will border and that have to be free.
		// TODO Check if the bordering fields are legal too
		ArrayList<Point> illegalFields = ship.getIllegalFields();
		// Check if all of the bordering points are on legal points.

		// Loop through all of the points the ship is on
		for (int i = 0; illegalFields.size() > i; i++) {
			// Check if the point is on the map
			if (illegalFields.get(i).isOnMap() == false) {
				return false;
			} else {
				// Check if none of the points are forbidden

				// Loop through all of the forbidden points
				for (int j = 0; forbiddenPoints.size() > j; j++) {
					// Get the current forbidden point for better readability
					Point forbiddenPoint = forbiddenPoints.get(j);
					Point point = illegalFields.get(i);
					// Check if the current forbiddenPoint equals the current Point
					if ((point.getX() == forbiddenPoint.getX()) || (point.getY() == forbiddenPoint.getY())) {
						// If yes, the ship can not be placed.
						return false;
					}
				}
			}
		}

		// TODO: Add forbidden points to list
		amountOfShips[ship.getShipId()]++;
		return true;
	}

	/**
	 * Takes in two ArrayLists<Point> and checks if they contain one or more
	 * points that are the same.
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
				if ((pointB.getX() == pointA.getX()) || (pointB.getY() == pointA.getY())) {
					// If yes, theres an overlap
					return false;
				}
			}
		}
		///XXX: Finish this/ check if it works again. Then use it in "placeShip" for the bordering and taken up points. Then check if it works. then do the testing.

		return true;
	}
}
