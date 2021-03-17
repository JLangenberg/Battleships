package gameObjects.ships.shipTypes;

import gameObjects.ships.Ship;

/**
 * A ship with the attributes of a battleship
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class Battleship extends Ship {
	
	public Battleship(int xRoot, int yRoot, int direction) {
		shipId = 3;
		length = 5;
		this.xRoot = xRoot;
		this.yRoot = yRoot;
		this.direction = direction;
	}
}
