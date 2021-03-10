package Functionality;

import Utility.MessagePresets;
import gameObjects.Map;
import gameObjects.ShipManager;
import network.Listener;
import network.Message;
import network.Sender;

public class GameManagerClient extends GameManager {
	
	@Override
	public void establishConnection(Map map, ShipManager sm) {
		// Get the ShipManager and the Map for the following game.
		this.map = map;
		this.sm = sm;
		
		// Listen for hosts searching a game. Stops here until fitting response was found.
		Message message = Listener.getListener().listenForKeyword(MessagePresets.SEARCHV1);
		// Notify the host that we want to play
		Sender.getSender().sendMessage(message.getSender(), MessagePresets.FOUND);
		// Wait for acknowledgement
		Listener.getListener().listenForKeyword(MessagePresets.ACK);
		// Save the address of the opponent for later usage.
		opponentAddress = message.getSender();
		// When everything is set, wait for the first shot.
		getEnemyShot();
	}
}
