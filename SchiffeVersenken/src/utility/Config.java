package utility;

/**
 * Contains variables for settings of the game.
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class Config {
	/**
	 * The port to use for communication
	 */
	public static final int PORT = 42069;
	/**
	 * The amount of submarines there should be
	 */
	public static final int AMOUNTSUBMARINE = 4;
	/**
	 * The amount of destroyers there should be
	 */
	public static final int AMOUNTDESTROYER = 3;
	/**
	 * The amount of cruisers there should be
	 */
	public static final int AMOUNTCRUISER = 2;
	/**
	 * The amount of battleships there should be
	 */
	public static final int AMOUNTBATTLESHIP = 1;
	/**
	 * List of how many of a specific ship type there should be.
	 */
	public static final int[] legalAmountOfShips = new int[] { AMOUNTSUBMARINE, AMOUNTDESTROYER, AMOUNTCRUISER,
			AMOUNTBATTLESHIP };
}
