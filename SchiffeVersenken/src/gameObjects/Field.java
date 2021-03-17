package gameObjects;

/**
 * Serves as a storage about information about a single tile of the map.
 * Contains it's state (Unknown/Water/Hit) and handles it
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class Field {
	/**
	 * int = 0
	 */
	public static final int UNKNOWN = 0;
	/**
	 * int = 1
	 */
	public static final int WATER = 1;
	/**
	 * int = 2
	 */
	public static final int HIT = 2;
	// The state of this field, default is unkwown.
	private int fieldState = Field.UNKNOWN;
	// The x and y position of this field
	private int[] position = new int[2];

	/**
	 * Gets a symbol that represents the state of this field. Used for assembling a
	 * map.
	 * 
	 * @return A String that indicates the state of the field. Blank = unkown, ~ =
	 *         water, + = hit
	 */
	public String getSymbol() {
		if (fieldState == Field.UNKNOWN) {
			return " ";
		}
		if (fieldState == Field.WATER) {
			return "~";
		}
		if (fieldState == Field.HIT) {
			return "+";
		}
		return "ERROR";
	}

	// GETTERS AND SETTERS \\
	public int getFieldState() {
		return fieldState;
	}

	public void setFieldState(int fieldState) {
		this.fieldState = fieldState;
	}

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}
}
