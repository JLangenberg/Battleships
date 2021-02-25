package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Class that handles the connection to the client.
 * 
 * @author Julius Langenberg
 *
 */
public class Server {
	// The accepted client connection
	Socket acceptedConnection = null;
	// The output stream and stream writers to send messages to the client
	OutputStream serverOutputStream = null;
	OutputStreamWriter serverOutputStreamWriter = null;
	BufferedWriter serverBufferedWriter = null;

	// The input stream and stream readers to read requests from the client.
	InputStream serverInputStream = null;
	InputStreamReader serverInputStreamReader = null;
	BufferedReader serverBufferedReader = null;

	/**
	 * Construct the clientConnection class and wait and accept a client connection,
	 * then prepare the In- and Output streams with Buffers to ensure communication.
	 * 
	 * @param serverSocket
	 */
	public Server(ServerSocket serverSocket) {

		// Accept and wait for a client
		try {
			acceptedConnection = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Prepare the output stream and writers
		try {
			serverOutputStream = acceptedConnection.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverOutputStreamWriter = new OutputStreamWriter(serverOutputStream);
		serverBufferedWriter = new BufferedWriter(serverOutputStreamWriter);

		// Prepare the input stream and readers
		try {
			serverInputStream = acceptedConnection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverInputStreamReader = new InputStreamReader(serverInputStream);
		serverBufferedReader = new BufferedReader(serverInputStreamReader);
	}

	/**
	 * Reads messages from the input stream
	 * 
	 * @return Messages from the input stream
	 */
	public String readInputStream() {
		try {
			return serverBufferedReader.readLine();
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("Client disconnected unexpectedly.");
			this.close();
			return "ERROR";
		}
	}

	/**
	 * Write messages onto the output stream to send answers to the client
	 * 
	 * @param message The message that will be sent
	 */
	public void writeOutputStream(String message) {
		// Write onto the buffered reader
		try {
			serverBufferedWriter.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Start a new line
		try {
			serverBufferedWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Send the message and flush the stream
		try {
			serverBufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes all streams and the connection to the client.
	 */
	public void close() {
		// Close Outputstream
		try {
			serverBufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Close Inputstream
		try {
			serverBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Close Connection to the client
		try {
			acceptedConnection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}