package gameObjects.ships.shipTypes;

import gameObjects.ships.Ship;

/**
 * A ship with the attributes of a submarine
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class Submarine extends Ship {

	public Submarine(int xRoot, int yRoot, int direction) {
		shipId = 0;
		length = 2;
		this.xRoot = xRoot;
		this.yRoot = yRoot;
		this.direction = direction;
	}
}
