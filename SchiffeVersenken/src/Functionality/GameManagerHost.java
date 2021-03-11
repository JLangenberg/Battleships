package Functionality;

import java.io.IOException;
import java.net.ServerSocket;

import Utility.MessagePresets;
import gameObjects.Map;
import gameObjects.ShipManager;
import network.Host;
import network.ListenerUDP;
import network.Message;
import network.SenderUDP;

public class GameManagerHost extends GameManager {
	// ASKTEACHER: What should I do when I don't get an ACK back? Can I wait or
	// something like this?
	
	ServerSocket serverSocket;
	Host host;
	
	@Override
	public void establishConnection(Map map, ShipManager sm) {
		// Get the ShipManager and the Map for the following game.
		this.map = map;
		this.sm = sm;
		// Create a message object to store the received message later on.
		Message threadMessage = new Message(null, "Run");
		// Tell everyone in the network that we are looking for a game. Spams a
		// broadcast.
		Thread searchThread = new Thread(new SearchThread(threadMessage));
		// Starts the broadcast. Will stop when message != null.
		searchThread.start();
		// Listen for responses. Stops here until fitting response was found.
		Message message = ListenerUDP.getListener().listenForKeyword(MessagePresets.FOUND);

		threadMessage.setContent(message.getContent());
		threadMessage.setSender(message.getSender());
		// Notify the client that we want to play
		SenderUDP.getSender().sendMessage(message.getSender(), MessagePresets.ACK);
		// Save the address of the opponent for later usage.
		opponentAddress = message.getSender();

//		// Fill the server socket and start the server
//		ServerSocket serverSocket = startServer();
//		host = new Host(serverSocket);

		// When everything is set up, shoot the first shot
		fireShot();
	}

	/**
	 * Closes the server socket stream
	 * 
	 * @param serverSocket The server socket that should be closed
	 */
	private void closeServer(ServerSocket serverSocket) {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the server
	 * 
	 * @param data The object containing the required information to start the
	 *             server
	 * @return An initialized serverSocket
	 */
	private ServerSocket startServer() {
		// Create an empty socket
		ServerSocket serverSocket = null;
		try {
			// Fill it.
			serverSocket = new ServerSocket(42069);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Return it.
		return serverSocket;
	}
}
