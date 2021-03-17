package functionality;

import java.util.ArrayList;

import gameObjects.Point;
import gameObjects.Shot;
import gameObjects.ships.Ship;
import utility.Config;

/**
 * Manages all functionality related to ships, placing ships, shooting ships,
 * checking if they are sunk and giving a map
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class ShipManager {
	// The list of forbidden points no ship should be placed on, since there is a
	// ship there.
	private ArrayList<Point> shipPoints = new ArrayList<Point>();
	/**
	 * Stores all ships and their location on the field
	 */
	private ArrayList<Ship> ships = new ArrayList<Ship>();

	// The legal amount of ships of a type. Index: 0 = submarine, 1 = destroyer, 2 =
	// cruiser, 3 = battleship
	private int[] legalAmountOfShips = Config.legalAmountOfShips;

	// Keeps track of the current amount of ships placed. Index: 0 = submarine, 1 =
	// destroyer, 2 = cruiser, 3 = battleship
	private int[] amountOfShips = new int[] { 0, 0, 0, 0 };

	/**
	 * Shoot a ship at the given coordinates. Check and return if it did (not) hit
	 * or destroy. Then change the ship tiles if a ship was shot.
	 * 
	 * @param shotX The x Coordinates of the shot
	 * @param shotY The y Coordinates of the shot
	 * @return 0 = Water, 1 = Hit, 2 = Destroyed, 3 = DestroyedLastShip
	 */
	public int shootShip(Shot shot) {
		// Check if the shot hits
		boolean shotHits = false;
		// The point that is currently being compared. After the loop, it contains the
		// one that is being shot.
		// Loop through all shitTiles and check if one matches the shot coordinates
		Point currentPoint;
		for (int i = 0; i < shipPoints.size(); i++) {
			// Get the current point
			currentPoint = shipPoints.get(i);
			// Check if the coordinates match
			if ((currentPoint.getX() == shot.getXAsInt()) && (currentPoint.getY() == shot.getYAsInt())) {
				// If the shot hits, store that info and stop the loop.
				shotHits = true;
				break;
			}
		}

		if (shotHits) {
			// Loop through all ships
			for (int i = 0; i < ships.size(); i++) {
				// Check if the current ship has a tile at the place that is being targeted
				if (ships.get(i).hasTileAtPoint(shot.getXAsInt(), shot.getYAsInt())) {
					// If yes, set the corresponding tile of this ship to hit
					Ship shotShip = ships.get(i);
					// "Shoot" the ship and get whether or not it was sunk.
					boolean isDestroyed = shotShip.shootShip(shot.getXAsInt(), shot.getYAsInt());
					// If a ship was destroyed, check if it was the last one. If not, just return
					// the int for "Destroyed"
					if (isDestroyed) {
						// If all ships are sunk, return "DestroyedLastShip" equivalent.
						if (isAllSunk()) {
							return 3;
						}
						// If there are other ships left, just return Destroyed
						return 2;
					}
					// If the ship was not destroyed, just return Hit
					return 1;
				}
			}
		}
		// If the shot doesn't hit, return a Miss/Water
		return 0;
	}

	/**
	 * Checks if all ships are sunk or if there are still some left.
	 * 
	 * @return true = All ships sunk, false = ships not sunk
	 */
	private boolean isAllSunk() {
		// The boolean that indicates if all ships are sunk or not
		boolean allSunk = true;
		// Loop through all ships and check if they are sunk.
		for (int i = 0; i < ships.size(); i++) {
			// If there is a ship that is not sunk, set allSunk = false.
			if (ships.get(i).isSunk() == false) {
				allSunk = false;
				break;
			}
		}
		return allSunk;
	}

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
		if (isOverlapping(shipFields, shipPoints) || isOverlapping(borderingFields, shipPoints)) {
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
			shipPoints.add(shipFields.get(i));
		}

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
	 * @param pointListA The first ArrayList to compare
	 * @param pointListB The second ArrayList to compare
	 * @return True = they have some overlap. False = there is no overlap.
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
					// If yes, there is an overlap
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Draws all ships of the player on this client on a map.
	 * 
	 * @return The map as a string
	 */
	public String getShipMap() {
		int fieldHeight = 10;
		int fieldWidth = 10;

		String map = "***|0|1|2|3|4|5|6|7|8|9|\n***|A|B|C|D|E|F|G|H|I|J|\n***---------------------\n";

		// Go through all fields of the map
		for (int y = 0; y < fieldHeight; y++) {
			map += "**" + y;
			for (int x = 0; x < fieldWidth; x++) {
				map += "|";
				// Check for a ship.
				boolean foundShip = false;
				int tileState = 0;
				for (int l = 0; l < shipPoints.size(); l++) {
					if ((shipPoints.get(l).getY()) == y && (shipPoints.get(l).getX() == x)) {
						foundShip = true;
						tileState = getStateOfTileAt(x, y);
						break;
					}
				}
				if (foundShip) {
					if (tileState == 1) {
						map += "+";
					} else {
						map += "S";
					}
				} else {
					map += " ";
				}
			}
			map += "|\n";
		}
		return map;
	}

	/**
	 * Get whether or not a tile at a the handed coordinates has been hit or not.
	 * 
	 * @param x The x Coordinates to check
	 * @param y The y Coordinates to check
	 * @return Whether or not the tile has been hit. 0 = Not hit, 1 = hit.
	 */
	private int getStateOfTileAt(int x, int y) {
		// Loop through all ships
		for (int i = 0; i < ships.size(); i++) {
			// Check if the current ship has the wanted tile
			if (ships.get(i).hasTileAtPoint(x, y)) {
				// If it does, return its state
				return ships.get(i).getTileStateAtPoint(x, y);
			}
		}
		// If nothing was found, default to 0.
		return 0;
	}
}
