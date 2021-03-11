package network;

public abstract class SocketConnection {
	
	public abstract void sendMessage(String message);
	
	public abstract String receiveMessage();
	
	public abstract void close();
}
