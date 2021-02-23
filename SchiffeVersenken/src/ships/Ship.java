package ships;

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
	 * Calculates fields that need to be free for the ship to be placed
	 */
	public ArrayList<Point> getIllegalFields() {
		ArrayList<Point> borderingPoints = new ArrayList<Point>();
		// Get the point behind the root
		Point point;
		Point point2;
		Point point3;
		Point pointEnd;
		if (direction == 1) {
			//TODO: Do this for the other directions too
			// The point behind the root, bordering the rear
			point = new Point(xRoot, yRoot - 1);
			// The point that borders the front end of the ship
			pointEnd = new Point(xRoot, yRoot + length);
			// The point on the left of the point behind the root.
			point2 = new Point(xRoot - 1, yRoot - 1);
			// The point on the right of the point behind the root.
			point3 = new Point(xRoot + 1, yRoot - 1);
			
			borderingPoints.add(point);
			borderingPoints.add(point2);
			borderingPoints.add(point3);
			borderingPoints.add(pointEnd);
			
			for (int i = 0; i < length + 1; i++)	{
				// Get the bordering point in the direction of the ship, left side
				Point newPoint2 = new Point(point2.getX() + i, point2.getY());
				// Get the bordering point in the direction of the ship, right side
				Point newPoint3 = new Point(point3.getX() + i, point3.getY());
				// Add the point to the list of bordering points
				borderingPoints.add(newPoint2);
				borderingPoints.add(newPoint3);
			}
		} else if (direction == 2) {
			point = new Point(xRoot - 1, yRoot);
		} else if (direction == 3) {
			point = new Point(xRoot, yRoot + 1);
		} else if (direction == 4) {
			point = new Point(xRoot + 1, yRoot);
		} else {
			point = new Point(-1, -1);
		}
		// TODO:
		return borderingPoints;
	}

	/**
	 * Calculates fields that the ship will take up. Need to be free and on the
	 * field.
	 */
	public ArrayList<Point> getShipFields() {
		ArrayList<Point> shipPoints = new ArrayList<Point>();
		// TODO:
		// Add the root of the ship to the list of forbidden coordinates
		shipPoints.add(new Point(xRoot, yRoot));
		// Get all of the fields the ship is on
		for (int i = 0; i < length; i++) {
			Point point;
			if (direction == 1) {
				point = new Point(xRoot, yRoot + i);
			} else if (direction == 2) {
				point = new Point(xRoot + i, yRoot);
			} else if (direction == 3) {
				point = new Point(xRoot, yRoot - i);
			} else if (direction == 4) {
				point = new Point(xRoot - i, yRoot);
			} else {
				point = new Point(-1, -1);
			}
			shipPoints.add(point);
		}
		return shipPoints;
	}

	public int getxRoot() {
		return xRoot;
	}

	public void setxRoot(int xRoot) {
		this.xRoot = xRoot;
	}

	public int getyRoot() {
		return yRoot;
	}

	public void setyRoot(int yRoot) {
		this.yRoot = yRoot;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getShipId() {
		return shipId;
	}

	public void setShipId(int shipId) {
		this.shipId = shipId;
	}
}
