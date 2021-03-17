package network;

/**
 * The parent-class for the TCP-Socket communication object SocketHost and
 * SocketClient.
 * 
 * @author Julius Langenberg, AH811
 *
 */
public abstract class SocketConnection {
	/**
	 * Sends a message to the connected client via SocketConnection
	 * 
	 * @param message The message to be sent
	 */
	public abstract void sendMessage(String message);

	/**
	 * Reads a message from the connected client via SocketConnection
	 * 
	 * @return The message receives
	 */
	public abstract String receiveMessage();

	/**
	 * Closes the connection
	 */
	public abstract void close();
}
