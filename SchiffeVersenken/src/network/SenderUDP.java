package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Utility.Config;

public class SenderUDP {

	public static final int PORT = 42069;
	private static volatile SenderUDP sender;

	private DatagramSocket datagramSocket;

	private SenderUDP() {
	}

	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}

	public void setDatagramSocket(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
	}

	/**
	 * Broadcast a message to all clients in a network.
	 * 
	 * @param message
	 */
	public void broadcastMessage(String message) {

		byte[] messageBytes = message.getBytes();

		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getByName("255.255.255.255");
			DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, inetAddress, PORT);
			DatagramSocket socket = new DatagramSocket();
			socket.setBroadcast(true);
			socket.send(packet);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send a message to an inetAddress
	 * 
	 * @param inetAddress
	 * @param message
	 * @param port
	 */
	public void sendMessage(InetAddress inetAddress, String message) {
		byte[] messageBytes = message.getBytes();
		try {
			DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, inetAddress, Config.PORT);
			DatagramSocket socket = new DatagramSocket();
			socket.send(packet);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
