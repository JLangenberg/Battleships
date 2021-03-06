package Functionality;

import Utility.MessagePresets;
import network.Message;
import network.Sender;

/**
 * Simple thread that continuously broadcasts a "searching game" message until
 * stopped
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class SearchThread implements Runnable {

	private boolean keepRunning = true;
	Message message = null;

	public SearchThread(Message message) {
		// Get the object that will contain the SVFound once it was received.
		this.message = message;
	}

	@Override
	public void run() {
		//XXX: WHEN TESTING: This might be a thing that might not work, never tried it before.
		// Run this loop until a message was received.
		while (this.message == null) {
			// Broadcast a message every 8 Seconds.
			Sender sender = Sender.getSender();
			sender.broadcastMessage(MessagePresets.SEARCHV1);
			System.out.println("Searching...");
			try {
				Thread.sleep(8000);// Wait 5000 ms = 5 sek
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isKeepRunning() {
		return keepRunning;
	}

	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}
}
