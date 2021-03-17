package gameObjects.ships.shipTypes;

import gameObjects.ships.Ship;

/**
 * A ship with the attributes of a destroyer
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class Destroyer extends Ship {
	
	public Destroyer(int xRoot, int yRoot, int direction) {
		shipId = 1;
		length = 3;
		this.xRoot = xRoot;
		this.yRoot = yRoot;
		this.direction = direction;
	}
}
