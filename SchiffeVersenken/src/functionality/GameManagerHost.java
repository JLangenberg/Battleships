package functionality;

import java.net.ServerSocket;

import gameObjects.Map;
import gameObjects.ShipManager;
import network.Host;
import network.ListenerUDP;
import network.Message;
import network.SenderUDP;
import utility.MessagePresets;

public class GameManagerHost extends GameManager {
	// ASKTEACHER: What should I do when I don't get an ACK back? Can I wait or
	// something like this?
	
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

		// Fill the server socket and start the server
		ServerSocket serverSocket = startServer();
		connection = new Host(serverSocket);

		// When everything is set up, shoot the first shot
		fireShot();
	}
}
