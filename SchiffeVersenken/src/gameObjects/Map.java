package gameObjects;

public class Map {
	int fieldHeight = 10;
	int fieldWidth = 10;
	private Field[][] fields = new Field[fieldHeight][fieldWidth];

	public Map() {
		for (int i = 0; i < fieldHeight; i++) {
			for (int j = 0; j < fieldWidth; j++) {
				fields[i][j] = new Field();
			}
		}
	}

	public void testShots() {
		fields[4][4].setFieldState(Field.HIT);
		fields[4][3].setFieldState(Field.HIT);
		fields[4][0].setFieldState(Field.WATER);
		fields[2][4].setFieldState(Field.WATER);
		fields[0][0].setFieldState(Field.WATER);
		fields[0][1].setFieldState(Field.HIT);
		fields[1][0].setFieldState(Field.HIT);
		fields[2][0].setFieldState(Field.WATER);
		fields[1][1].setFieldState(Field.WATER);
		fields[0][2].setFieldState(Field.WATER);
		fields[9][9].setFieldState(Field.HIT);
	}

	public String getMapAsText() {
		String map = "***|A|B|C|D|E|F|G|H|I|J|\n***---------------------\n";

		for (int i = 0; i < fieldHeight; i++) {
			map += "**" + i;
			for (int j = 0; j < fieldWidth; j++) {
				map += "|" + fields[i][j].getSymbol();
			}
			map += "|\n";
		}
		return map;
	}
}
