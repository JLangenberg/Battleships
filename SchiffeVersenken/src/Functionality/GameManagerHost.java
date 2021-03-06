package Functionality;

import Utility.MessagePresets;
import gameObjects.Map;
import gameObjects.ShipManager;
import network.Listener;
import network.Message;
import network.Sender;

public class GameManagerHost extends GameManager {
	// ASKTEACHER: What should I do when I don't get an ACK back? Can I wait or
	// something like this?
	@Override
	public void establishConnection(Map map, ShipManager sm) {
		// Get the ShipManager and the Map for the following game.
		this.map = map;
		this.sm = sm;
		// Create a message objet to store the received message later on.
		Message message = null;
		// Tell everyone in the network that we are looking for a game. Spams a
		// broadcast.
		Thread searchThread = new Thread(new SearchThread(message));
		// Starts the broadcast. Will stop when message != null.
		searchThread.start();
		// Listen for responses. Stops here until fitting response was found.
		message = Listener.getListener().listenForKeyword(MessagePresets.SEARCHV1);
		// Notify the client that we want to play
		Sender.getSender().sendMessage(message.getSender(), MessagePresets.ACK);
		// Save the address of the opponent for later usage.
		opponentAddress = message.getSender();
	}
}
