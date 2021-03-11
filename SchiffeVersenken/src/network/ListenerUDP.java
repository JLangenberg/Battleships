package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ListenerUDP {

	private DatagramSocket datagramSocket;
	InetAddress ownAddress = null;
	private static volatile ListenerUDP listener;

	public ListenerUDP() {
	}

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

	/**
	 * Listens for a message and hands it to the UI or something...
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
	 * Listens for a message by a specific sender and hands it to the UI or
	 * something...
	 */
	public Message listenForKeyword(String keyword, InetAddress sender) {
		// A loop that only ends when a relevant message was heard.
		while (true) {
			// Receive a packet
			DatagramPacket datagramPacket = getPackage();
			// Check if it contains the keyword
			boolean containsKeyword = containsKeyword(datagramPacket, keyword);
			// Check if the received message contains keywords and is sent by the wanted
			// client
			if (containsKeyword && isSentBySender(datagramPacket, sender)) {
				// Returns the received message
				return new Message(datagramPacket.getAddress(), datagramPacket.getData().toString());
			}
		}
	}

	private boolean containsKeyword(DatagramPacket datagramPacket, String keyword) {
		// Get the contents of the message
		String message = new String(datagramPacket.getData());

		// Check if the received message contains keywords itself.
		if (message.contains(keyword)) {
			return true;
		}
		return false;
	}

	private boolean containsKeyword(DatagramPacket datagramPacket, String[] keywords) {
		// Check the sender of this message, get all the data needed
		InetAddress senderAddress = datagramPacket.getAddress();
		// Get the contents of the message
		String message = new String(datagramPacket.getData());

		// Check if the received message contains keywords and is not send by this
		// client
		// itself.
		// XXX: the self-check might not be needed if not speaking and listening at the
		// same time.
		for (int i = 0; i < keywords.length; i++) {
			if (message.contains(keywords[i]) && !(senderAddress.toString().contains(ownAddress.toString()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Overload method of listenForKeywords. This method listens for a list of
	 * keywords by a specific sender and returns the message.
	 * 
	 * @param keywords The keywords to filter for
	 * @param sender   The wanted sender of the message
	 * @return The message that was received.
	 */
	public Message listenForKeywords(String[] keywords, InetAddress sender) {
		// A loop that only ends when a relevant message was heard.
		while (true) {
			// Receive a packet
			DatagramPacket datagramPacket = getPackage();
			// Check if it contains the keyword and was sent by the correct sender.
			boolean containsKeyword = containsKeyword(datagramPacket, keywords);
			boolean isFromSender = isSentBySender(datagramPacket, sender);

			// Check if the received message contains keywords and is not send by the client
			// itself.
			// XXX: the self-check might not be needed if not speaking and listening at the
			// same time.
			if (containsKeyword && isFromSender) {
				System.out.println("Message got:\n");
				System.out.println(datagramPacket.getData());
				System.out.println(datagramPacket.getAddress().toString());
				// Returns the received message
				return new Message(datagramPacket.getAddress(), datagramPacket.getData().toString());
			}
		}
	}

	private boolean isSentBySender(DatagramPacket datagramPacket, InetAddress sender) {
		if (datagramPacket.getAddress() == sender) {
			return true;
		}
		return false;
	}

	/**
	 * Method listens for a list of keywords and returns the message.
	 * 
	 * @param keywords The keywords to filter for
	 * @return The received message
	 */
	public Message listenForKeywords(String[] keywords) {
		// TODO: Finish this method.
		return null;

	}
}
