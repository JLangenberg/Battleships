package functionality;

import network.Message;
import network.SenderUDP;
import utility.MessagePresets;

/**
 * Simple thread that continuously broadcasts a "searching game" message until
 * stopped
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class SearchThread implements Runnable {
	// The message that decides if the thread keeps on running. As long as its
	// content equals "Run", the thread will run
	volatile Message message = null;

	public SearchThread(Message message) {
		// Get the object that will contain the SVFound once it was received.
		this.message = message;
	}

	@Override
	public void run() {
		// Run this loop until a message was received.
		while (this.message.getContent().equals("Run")) {
			// Broadcast a message every 8 Seconds.
			SenderUDP sender = SenderUDP.getSender();
			sender.broadcastMessage(MessagePresets.SEARCHV1);
			System.out.println("Searching...");
			try {
				Thread.sleep(8000);// Wait 8000 ms = 8 sek
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
