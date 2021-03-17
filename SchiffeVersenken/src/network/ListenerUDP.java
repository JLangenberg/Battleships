package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The object to use for UDP Listening. Is a singleton
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class ListenerUDP {
	// This singleton listener
	private static volatile ListenerUDP listener;
	// The datagram socket used for listening
	private DatagramSocket datagramSocket;
	// Own address used for filtering out own messages
	InetAddress ownAddress = null;

	public ListenerUDP() {
	}

	/**
	 * Singleton getter
	 * 
	 * @return Instance of listener
	 */
	public static ListenerUDP getListener() {

		if (listener == null) {
			synchronized (ListenerUDP.class) {
				if (listener == null) {
					listener = new ListenerUDP();
				}
			}
		}
		return listener;
	}

	/**
	 * Listens for a message with a specific keyword, then returns the message
	 * 
	 * @param keyword The keyword to look out for
	 * @return The got message containing the keyword
	 */
	public Message listenForKeyword(String keyword) {
		// A loop that only ends when a relevant message was heard.
		while (true) {
			// Receive a packet
			DatagramPacket datagramPacket = getPackage();
			// Check if it contains the keyword
			boolean containsKeyword = containsKeyword(datagramPacket, keyword);
			// Check if the received message contains keywords and is not send by the client
			// itself.
			if (containsKeyword) {
				// Returns the received message
				return new Message(datagramPacket.getAddress(), datagramPacket.getData().toString());
			}
		}
	}

	/**
	 * Takes in a packet and a keyword, then checks if the packet-message contains
	 * the keyword.
	 * 
	 * @param datagramPacket The packet to check
	 * @param keyword        The keyword to look for
	 * @return True: packet contains keyword. False: Does not contain keyword.
	 */
	private boolean containsKeyword(DatagramPacket datagramPacket, String keyword) {
		// Get the contents of the message
		String message = new String(datagramPacket.getData());

		// Check if the received message contains keywords itself.
		if (message.contains(keyword)) {
			return true;
		}
		return false;
	}

	/**
	 * Listens for packages and receives then.
	 * 
	 * @return Returns the received package.
	 */
	private DatagramPacket getPackage() {
		// Create a new DatagramPacket to store the received packet in.
		DatagramPacket datagramPacket = new DatagramPacket(new byte[512], 512);
		try {
			// Wait for an incoming message
			this.datagramSocket.receive(datagramPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Return the incoming message.
		return datagramPacket;
	}

	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}

	public void setDatagramSocket(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
		// Get the clients own address to filter out own messages.
		try {
			ownAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
