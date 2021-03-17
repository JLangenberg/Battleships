package utility;

/**
 * Contains all the messages necessary for communication
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class MessagePresets {
	/**
	 * Send when hosting and searching for a client
	 */
	public static final String SEARCHV1 = "SVSearch,[1.0]";
	/**
	 * Send as client to tell a host that you are willing to play
	 */
	public static final String FOUND = "SVFound";
	/**
	 * Send as a host as response to SVFound
	 */
	public static final String ACK = "SVAck";
	/**
	 * Keyword for identifying and creating messages about shots
	 */
	public static final String FIRE = "Fire";
	/**
	 * Keyword for identifying and creating messages about shots that hit
	 */
	public static final String HIT = "Hit";
	/**
	 * Keyword for identifying and creating messages about shots that missed
	 */
	public static final String MISS = "Miss";
	/**
	 * Keyword for identifying and creating messages about shots that destroyed a
	 * ship
	 */
	public static final String DESTROYED = "Destroyed";
	/**
	 * Keyword for identifying and creating messages about shots that destroyed the
	 * last ship and ended the game.
	 */
	public static final String DESTROYEDLASTSHIP = "DestroyedLastShip";
}
