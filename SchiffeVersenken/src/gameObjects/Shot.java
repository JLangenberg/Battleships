package gameObjects;

/**
 * Class used to store coordinates of a shot as strings. Can return them as
 * ints, given they are convertable
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class Shot {
	// Shot x coordinates
	private String x;
	// Shot y coordinates
	private String y;

	public Shot(String x, String y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Get the shot in square brackets
	 * 
	 * @return Shot coordinates like this: "[A],[5]"
	 */
	public String getShotAsMessage() {
		return "[" + x + "]," + "[" + y + "]";
	}

	/**
	 * Returns the x Coordinate as a number for usage in logic.
	 * 
	 * @return int equivalent of x coordinate
	 */
	public int getXAsInt() {
		String[] xCoordinates = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
		for (int i = 0; i < xCoordinates.length; i++) {
			if (xCoordinates[i] == this.x) {
				return i;
			}
		}
		// If no value was found, return 0 as standard.
		return 0;
	}

	/**
	 * Returns the y Coordinate as a number for usage in logic.
	 * 
	 * @return y coordinate as int
	 */
	public int getYAsInt() {
		return Integer.parseInt(this.y);
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}
}
