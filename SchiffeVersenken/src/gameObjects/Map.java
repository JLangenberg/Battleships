package gameObjects;

public class Map {
	int fieldHeight = 10;
	int fieldWidth = 10;
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

	public void setFieldHit(int x, int y) {
		fields[x][y].setFieldState(Field.HIT);
	}

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
