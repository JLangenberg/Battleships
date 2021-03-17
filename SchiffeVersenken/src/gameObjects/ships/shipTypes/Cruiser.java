package gameObjects.ships.shipTypes;

import gameObjects.ships.Ship;

/**
 * A ship with the attributes of a cruiser
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class Cruiser extends Ship {
	
	public Cruiser(int xRoot, int yRoot, int direction) {
		shipId = 2;
		length = 4;
		this.xRoot = xRoot;
		this.yRoot = yRoot;
		this.direction = direction;
	}

}
