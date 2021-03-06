package network;

import java.net.InetAddress;

/**
 * A simple container class that contains all relevant information about a
 * message.
 * 
 * @author Julius Langenberg, AH811
 *
 */
public class Message {
	private InetAddress sender;
	private String content;

	public Message(InetAddress sender, String content) {
		this.sender = sender;
		this.content = content;
	}

	public InetAddress getSender() {
		return sender;
	}

	public void setSender(InetAddress sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
