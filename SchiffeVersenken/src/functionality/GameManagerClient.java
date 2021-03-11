package functionality;

import gameObjects.Map;
import gameObjects.ShipManager;
import network.Client;
import network.ListenerUDP;
import network.Message;
import network.SenderUDP;
import utility.Config;
import utility.MessagePresets;

public class GameManagerClient extends GameManager {
	
	@Override
	public void establishConnection(Map map, ShipManager sm) {
		// Get the ShipManager and the Map for the following game.
		this.map = map;
		this.sm = sm;
		// Listen for hosts searching a game. Stops here until fitting response was found.
		Message message = ListenerUDP.getListener().listenForKeyword(MessagePresets.SEARCHV1);
		// Notify the host that we want to play
		SenderUDP.getSender().sendMessage(message.getSender(), MessagePresets.FOUND);
		// Wait for acknowledgement
		ListenerUDP.getListener().listenForKeyword(MessagePresets.ACK);
		System.out.println("Got Opponent. Game starting.");
		// Save the address of the opponent for later usage.
		opponentAddress = message.getSender();
		connection = new Client(opponentAddress.getHostAddress(), Config.PORT);
		// When everything is set, wait for the first shot.
		getEnemyShot();
	}
}
