package ships;

public class ShipTile {
	// The x coordinate of this tile
	private int xCoordinate;
	// The x coordinate of this tile
	private int yCoordinate;
	/**
	 * The state the shipTile is in when it has not been hit with a shot
	 */
	public static final int NOTHIT = 0;
	/**
	 * The state the shipTile is in when it has been hit with a shot
	 */
	public static final int HIT = 1;
	/**
	 * The state of this shipTile
	 */
	private int state = NOTHIT;

	ShipTile(int x, int y) {
		this.xCoordinate = x;
		this.yCoordinate = y;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
