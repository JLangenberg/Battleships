package gameObjects.ships;

import java.util.ArrayList;

import gameObjects.Point;

/**
 * Superclass for a ship that tracks its location and its hits. The fields
 * occupied by a ship are calculated by getting its root, and counting the
 * length of the ship in the direction it is facing.
 * 
 * @author Julius Langenberg, AH811
 *
 */
public abstract class Ship {
	/**
	 * X location of the root of the ship.
	 */
	protected int xRoot;
	/**
	 * Y location of the root of the ship.
	 */
	protected int yRoot;
	/**
	 * Direction of the ship from the root. 1 = North 2 = East 3 = South 4 = West
	 */
	protected int direction;
	/**
	 * The length of the ship.
	 */
	protected int length;
	/**
	 * The id of the ship
	 */
	protected int shipId;
	/**
	 * ArrayList that stores all of the ships tiles.
	 */
	protected ArrayList<ShipTile> shipTiles = new ArrayList<ShipTile>();
	/**
	 * Boolean that indicates whether or not the ship is sunk or not.
	 */
	protected boolean isSunk = false;

	/**
	 * Calculates fields that the ship will take up.
	 * 
	 * @return An ArrayList<Point> with all the points the ship is on.
	 */
	public ArrayList<Point> getShipFields() {
		// The arrayList that contains all the points the ship is on
		ArrayList<Point> shipPoints = new ArrayList<Point>();
		// Add the root of the ship to the list of forbidden coordinates
		shipPoints.add(new Point(xRoot, yRoot));
		// Get all of the fields the ship is on by going i steps from the root in the
		// direction the ship is pointing
		for (int i = 1; i < length; i++) {
			// Current point to add
			Point point;
			// Do the math according to the orientation of the ship
			if (direction == 1) {
				point = new Point(xRoot, yRoot - i);
			} else if (direction == 2) {
				point = new Point(xRoot + i, yRoot);
			} else if (direction == 3) {
				point = new Point(xRoot, yRoot + i);
			} else if (direction == 4) {
				point = new Point(xRoot - i, yRoot);
			} else {
				point = new Point(-1, -1);
			}
			// Add the calculated point to the arrayList
			shipPoints.add(point);
		}

		// Delete the old shipTiles in case this method gets called more than once
		shipTiles = new ArrayList<ShipTile>();
		// Create shipTiles from the calculated points
		for (int i = 0; i < shipPoints.size(); i++) {
			Point currentPoint = shipPoints.get(i);
			shipTiles.add(new ShipTile(currentPoint.getX(), currentPoint.getY()));
		}
		// Return the calculated points for later usage
		return shipPoints;
	}

	/**
	 * Calculates fields that need to be free for the ship to be placed, aka the
	 * fields it will be bordering
	 * 
	 * @return An ArrayList<Point> with all the bordering points.
	 */
	public ArrayList<Point> getBorderingFields() {
		// The arrayList all the bordering points will be stored in
		ArrayList<Point> borderingPoints = new ArrayList<Point>();
		// Get the point behind the root
		Point pointBehindRoot;
		Point pointBehindRootLeft;
		Point pointBehindRootRight;
		Point pointFront;

		if (direction == 1) {
			// NORTH

			// The point behind the root, bordering the rear
			pointBehindRoot = new Point(xRoot, yRoot + 1);
			// The point on the left of the point behind the root.
			pointBehindRootLeft = new Point(xRoot - 1, yRoot - 1);
			// The point on the right of the point behind the root.
			pointBehindRootRight = new Point(xRoot + 1, yRoot + 1);
			// The point that borders the front end of the ship
			pointFront = new Point(xRoot, yRoot - length);

			for (int i = 1; i < length + 1; i++) {
				// Get the bordering point in the direction of the ship, left side
				Point pointLeft = new Point(pointBehindRootLeft.getX(), pointBehindRootLeft.getY() + i);
				// Get the bordering point in the direction of the ship, right side
				Point pointRight = new Point(pointBehindRootRight.getX(), pointBehindRootRight.getY() + i);
				// Add the point to the list of bordering points
				borderingPoints.add(pointLeft);
				borderingPoints.add(pointRight);
			}
		} else if (direction == 2) {
			// EAST

			// Get all the points at the front and back of the ship

			// The point behind the root, bordering the rear
			pointBehindRoot = new Point(xRoot - 1, yRoot);
			// The point on the left of the point behind the root.
			pointBehindRootLeft = new Point(xRoot - 1, yRoot - 1);
			// The point on the right of the point behind the root.
			pointBehindRootRight = new Point(xRoot - 1, yRoot + 1);
			// The point that borders the front end of the ship
			pointFront = new Point(xRoot + length, yRoot);

			for (int i = 1; i < length + 1; i++) {
				// Get the bordering point in the direction of the ship, left side
				Point pointLeft = new Point(pointBehindRootLeft.getX() + i, pointBehindRootLeft.getY());
				// Get the bordering point in the direction of the ship, right side
				Point pointRight = new Point(pointBehindRootRight.getX() + i, pointBehindRootRight.getY());
				// Add the point to the list of bordering points
				borderingPoints.add(pointLeft);
				borderingPoints.add(pointRight);
			}
		} else if (direction == 3) {
			// SOUTH

			// Get all the points at the front and back of the ship

			// The point behind the root, bordering the rear
			pointBehindRoot = new Point(xRoot, yRoot - 1);
			// The point on the left of the point behind the root.
			pointBehindRootLeft = new Point(xRoot + 1, yRoot - 1);
			// The point on the right of the point behind the root.
			pointBehindRootRight = new Point(xRoot - 1, yRoot - 1);
			// The point that borders the front end of the ship
			pointFront = new Point(xRoot, yRoot + length);

			for (int i = 1; i < length + 1; i++) {
				// Get the bordering point in the direction of the ship, left side
				Point pointLeft = new Point(pointBehindRootLeft.getX(), pointBehindRootLeft.getY() - i);
				// Get the bordering point in the direction of the ship, right side
				Point pointRight = new Point(pointBehindRootRight.getX(), pointBehindRootRight.getY() - i);
				// Add the point to the list of bordering points
				borderingPoints.add(pointLeft);
				borderingPoints.add(pointRight);
			}
		} else if (direction == 4) {
			// WEST

			// Get all the points at the front and back of the ship

			// The point behind the root, bordering the rear
			pointBehindRoot = new Point(xRoot + 1, yRoot);
			// The point on the left of the point behind the root.
			pointBehindRootLeft = new Point(xRoot + 1, yRoot + 1);
			// The point on the right of the point behind the root.
			pointBehindRootRight = new Point(xRoot + 1, yRoot - 1);
			// The point that borders the front end of the ship
			pointFront = new Point(xRoot - length, yRoot);

			for (int i = 1; i < length + 1; i++) {
				// Get the bordering point in the direction of the ship, left side
				Point pointLeft = new Point(pointBehindRootLeft.getX() - i, pointBehindRootLeft.getY());
				// Get the bordering point in the direction of the ship, right side
				Point pointRight = new Point(pointBehindRootRight.getX() - i, pointBehindRootRight.getY());
				// Add the point to the list of bordering points
				borderingPoints.add(pointLeft);
				borderingPoints.add(pointRight);
			}
		} else {
			// If no correct direction is given, return null.
			return null;
		}

		// Add all the special points to the list
		borderingPoints.add(pointBehindRoot);
		borderingPoints.add(pointBehindRootLeft);
		borderingPoints.add(pointBehindRootRight);
		borderingPoints.add(pointFront);

		return borderingPoints;
	}

	/**
	 * Returns the state of a tile at a point
	 * 
	 * @param x coordinate of the tile
	 * @param y coordinate of the tile
	 * @return The state of a tile at [x,y]. 0 = Not hit, 1 = hit
	 */
	public int getTileStateAtPoint(int x, int y) {
		// Loop through all tiles
		for (int i = 0; i < shipTiles.size(); i++) {
			// Get the current tile
			ShipTile currentTile = shipTiles.get(i);
			// Check if it is at the wanted coordinates
			if ((currentTile.getxCoordinate() == x) && (currentTile.getyCoordinate() == y)) {
				// If yes, return true
				return currentTile.getState();
			}
		}
		// If no match was found, return a default of 0.
		return 0;
	}

	/**
	 * Checks if a ship has a tile at the coordinates handed in
	 * 
	 * @param x The x coordinate to check
	 * @param y The y coordinate to check
	 * @return Whether or not the ship has a tile at P(x, y)
	 */
	public boolean hasTileAtPoint(int x, int y) {
		// Loop through all tiles
		for (int i = 0; i < shipTiles.size(); i++) {
			// Get the current tile
			ShipTile currentTile = shipTiles.get(i);
			// Check if it is at the wanted coordinates
			if ((currentTile.getxCoordinate() == x) && (currentTile.getyCoordinate() == y)) {
				// If yes, return true
				return true;
			}
		}
		// If no match was found, return false
		return false;
	}

	/**
	 * Shoot a ship at a coordinate. Sets the according tile to "HIT" Returns
	 * whether or not the ship was destroyed.
	 * 
	 * @param x of the shipTile that is being shot at
	 * @param y the shipTile that is being shot at
	 * @return false = Ship did not sink, true = Ship is sunk
	 */
	public boolean shootShip(int x, int y) {
		// Loop through all tiles to find the one that is being targeted
		for (int i = 0; i < shipTiles.size(); i++) {
			ShipTile currentTile = shipTiles.get(i);
			// Check if it is the current tile.
			if ((currentTile.getxCoordinate() == x) && (currentTile.getyCoordinate() == y)) {
				// Set the state of the targeted tile to "HIT"
				currentTile.setState(ShipTile.HIT);
				// Update the 'sunk' state.
				updateIsSunk();
			}
		}
		return this.getSunk();
	}

	/**
	 * Checks if all tiles of the ship are sunk and changes isSunk accordingly
	 */
	protected void updateIsSunk() {
		// Default to true
		boolean sunk = true;
		// Loop through all tiles
		for (int i = 0; i < shipTiles.size(); i++) {
			// Check if there is a tile that is not sunk yet
			if (shipTiles.get(i).getState() == ShipTile.NOTHIT) {
				// If yes, the ship is not sunk
				sunk = false;
			}
		}
		this.setSunk(sunk);
	}

	public boolean isSunk() {
		return isSunk;
	}

	public void setSunk(boolean isSunk) {
		this.isSunk = isSunk;
	}

	public boolean getSunk() {
		return isSunk;
	}

	public int getShipId() {
		return shipId;
	}
}
