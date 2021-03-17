package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Handles all the networking related to the client connection. With this class
 * one can connect to the server, send requests, read answers and close all of
 * the streams when the connection should be terminated.
 * 
 * @author Julius Langenberg
 *
 */
public class SocketClient extends SocketConnection {
	// The Socket that connects to the server
	private Socket clientConnection = new Socket();

	// The output related objects
	private OutputStream clientOutputStream = null;
	private OutputStreamWriter clientOutputStreamWriter = null;
	private BufferedWriter clientBufferedWriter = null;
	// The input related objects
	private InputStream clientInputStream = null;
	private InputStreamReader clientInputStreamReader = null;
	private BufferedReader clientBufferedReader = null;

	/**
	 * Constructor. When the client is constructed, it will set up the connection
	 * with the server.
	 * 
	 * @param hostIn The host address that the connection should be made with
	 * @param portIn The Port that should be connected with.
	 */
	public SocketClient(String hostIn, int portIn) {

		// Create the connection with the server
		try {
			clientConnection = new Socket(hostIn, portIn);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get the output stream
		try {
			clientOutputStream = clientConnection.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Write on the output stream with a buffered writer for easier handling.
		clientOutputStreamWriter = new OutputStreamWriter(clientOutputStream);
		clientBufferedWriter = new BufferedWriter(clientOutputStreamWriter);

		// Get the input stream
		try {
			clientInputStream = clientConnection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Read on the output stream using a buffered reader for easier handling.
		clientInputStreamReader = new InputStreamReader(clientInputStream);
		clientBufferedReader = new BufferedReader(clientInputStreamReader);
	}

	@Override
	public void close() {
		// Close the clientBufferedReader
		try {
			clientBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Close the clientBufferedWriter
		try {
			clientBufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Close clientConnection
		try {
			clientConnection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMessage(String message) {
		// Writes the message on the stream
		try {
			clientBufferedWriter.write(message);

		} catch (IOException e) {
			e.printStackTrace();
		}
		// Make a new line.
		try {
			clientBufferedWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Sends all the accumulated letters to the server
		try {
			clientBufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String receiveMessage() {
		// Read the content of the bufferedReader
		try {
			return clientBufferedReader.readLine();
		} catch (IOException e) {
			// Return an error message in case the line couldn't be read
			System.out.println("Server closed unexpectedly.");
			this.close();
			return "ERROR";
		}
	}
}