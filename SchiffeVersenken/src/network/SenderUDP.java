package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import utility.Config;

/**
 * Used to send UDP messages. Is a singleton.
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class SenderUDP {
	// The singleton object
	private static volatile SenderUDP sender;

	// Private constructor (singleton)
	private SenderUDP() {
	}

	/**
	 * Broadcast a message to all clients in a network.
	 * 
	 * @param message The message to broadcast
	 */
	public void broadcastMessage(String message) {
		// Turn the string into bytes
		byte[] messageBytes = message.getBytes();
		// The address to send to
		InetAddress inetAddress;
		try {
			// Set to broadcast address
			inetAddress = InetAddress.getByName("255.255.255.255");
			// Create the packet
			DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, inetAddress, Config.PORT);
			// Create the socket to send over
			DatagramSocket socket = new DatagramSocket();
			// Set to broadcast
			socket.setBroadcast(true);
			// Send the packet
			socket.send(packet);
			// Close the packet
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send a message to an inetAddress
	 * 
	 * @param inetAddress The inetAddress to send to
	 * @param message     The message to send
	 */
	public void sendMessage(InetAddress inetAddress, String message) {
		// Turn the message into bytes to make them sendable
		byte[] messageBytes = message.getBytes();
		try {
			// Create the packet
			DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, inetAddress, Config.PORT);
			// Create the socket
			DatagramSocket socket = new DatagramSocket();
			// Send the packet
			socket.send(packet);
			// Close the socket
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Singleton getter. Returns the instance of SenderUDP
	 * 
	 * @return
	 */
	public static SenderUDP getSender() {
		if (sender == null) {
			synchronized (SenderUDP.class) {
				if (sender == null) {
					sender = new SenderUDP();
				}
			}
		}
		return sender;
	}
}
