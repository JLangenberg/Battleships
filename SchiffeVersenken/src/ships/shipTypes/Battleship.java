package ships.shipTypes;

import ships.Ship;

public class Battleship extends Ship{
	public Battleship(int xRoot, int yRoot, int direction)	{
		shipId = 3;
		length = 5;
		this.xRoot = xRoot;
		this.yRoot = yRoot;
		this.direction = direction;
	}
}
