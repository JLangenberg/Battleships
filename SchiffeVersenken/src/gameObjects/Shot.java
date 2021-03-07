package gameObjects;

public class Shot {
	private String x;
	private String y;

	public Shot(String x, String y) {
		this.x = x;
		this.y = y;
	}

	public String getX() {
		return x;
	}
 
	/**
	 * Returns the y Coordinate as a number for usage in logic.
	 * 
	 * @return y coordinate as int
	 */
	public int getYAsInt() {
		return Integer.parseInt(this.y);
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
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

	public void setY(String y) {
		this.y = y;
	}
}
