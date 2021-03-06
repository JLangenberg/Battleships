package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Listener {

	private DatagramSocket datagramSocket;
	InetAddress ownAddress = null;
	private static volatile Listener listener;

	public Listener() {
	}

	public static Listener getListener() {

		if (listener == null) {
			synchronized (Listener.class) {
				if (listener == null) {
					listener = new Listener();
				}
			}
		}
		return listener;
	}

	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}

	public void setDatagramSocket(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
		// Get the clients own address to filter out own messages.
		// XXX: Might not be needed if you are not sending and listening at the same
		// time.
		try {
			ownAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Listens for a message and hands it to the UI or something...
	 */
	public Message listenForKeyword(String keyword) {
		// A loop that only ends when a relevant message was heard.
		while (true) {
			// Create a new DatagramPacket to use for listening
			DatagramPacket datagramPacket = new DatagramPacket(new byte[512], 512);
			try {
				// Wait for an incoming message
				this.datagramSocket.receive(datagramPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Check the sender of this message, get all the data needed
			InetAddress senderAddress = datagramPacket.getAddress();
			// Get the contents of the message
			String message = new String(datagramPacket.getData());

			// Check if the received message contains keywords and is not send by the client
			// itself.
			// XXX: the self-check might not be needed if not speaking and listening at the
			// same time.
			if (message.contains(keyword) && !(senderAddress.toString().contains(ownAddress.toString()))) {
				System.out.println("Message got:\n");
				System.out.println(message);
				System.out.println(senderAddress.toString());
				// Returns the received message
				return new Message(senderAddress, message);
			}
		}
	}
}
