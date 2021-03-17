package gameObjects;

/**
 * The map that keeps track of all shots this client made and what the result
 * was. 10*10 Field, each being able to be in the state of "unknown", "hit" or
 * "water"
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class Map {
	// The height of the playing field
	int fieldHeight = 10;
	// The width of the playing field
	int fieldWidth = 10;
	// The array that stores the fields
	private Field[][] fields = new Field[fieldHeight][fieldWidth];

	/**
	 * Constructor. Defines all slots of the Field Array
	 */
	public Map() {
		for (int i = 0; i < fieldHeight; i++) {
			for (int j = 0; j < fieldWidth; j++) {
				fields[i][j] = new Field();
			}
		}
	}

	/**
	 * Sets the field at the given coordinates to Hit-State
	 * 
	 * @param x of the field
	 * @param y of the field
	 */
	public void setFieldHit(int x, int y) {
		fields[x][y].setFieldState(Field.HIT);
	}

	/**
	 * Sets the field at the given coordinates to Water-State
	 * 
	 * @param x of the field
	 * @param y of the field
	 */
	public void setFieldWater(int x, int y) {
		fields[x][y].setFieldState(Field.WATER);
	}

	/**
	 * Returns the Field Array put into context in form of a map.
	 * 
	 * @return A map showing where was shot, where the shots did (not) hit, and
	 *         where was not shot yet.
	 */
	public String getMapAsText() {
		// The header
		String map = "***|A|B|C|D|E|F|G|H|I|J|\n***---------------------\n";

		for (int i = 0; i < fieldHeight; i++) {
			// Add spacers
			map += "**" + i;
			// Fill the column
			for (int j = 0; j < fieldWidth; j++) {
				map += "|" + fields[j][i].getSymbol();
			}
			map += "|\n";
		}
		return map;
	}
}
