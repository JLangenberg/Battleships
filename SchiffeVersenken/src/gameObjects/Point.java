package gameObjects;

/**
 * Serves as a pointer to a coordinate. Very simple.
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class Point {
	private int x;
	private int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Checks whether or not this point is on the map and returns true if it is.
	 * @return Whether or not the point is on the map.
	 */
	public boolean isOnMap() {
		if ((x < 0) || (x > 9)) {
			return false;
		} else if ((y < 0) || (y > 9)) {
			return false;
		}
		return true;
	}
}
