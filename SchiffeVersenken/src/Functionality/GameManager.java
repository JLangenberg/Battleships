package Functionality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import Utility.MessagePresets;
import gameObjects.Map;
import gameObjects.ShipManager;
import network.Sender;

public abstract class GameManager {
	// The ship manager that handles this clients ships
	ShipManager sm;
	// The map that keeps track of the opponents waters.
	Map map;
	// The address to send the messages to when playing
	InetAddress opponentAddress;
	
	public abstract void establishConnection(Map map, ShipManager sm);
	
	private void fireShot()	{
		Sender.getSender().sendMessage(opponentAddress, MessagePresets.FIRE);
		//this.receiveShot();
	}
	
	private void receiveShot()	{
		//this.fireShot();
	}
	
	//TODO: Put this into a seperate UI class. 
	private void getShotLocation()	{
		System.out.println("Where do you want to shoot?");
		
		char[] xCoordinates = {'A','B','C','D','E','F','G','H','I','J'};
		char[] yCoordinates = {'0','1','2','3','4','5','6','7','8','9'};

		String choice;
		// Booleans that get set to true when a valid coordinate has been found.
		boolean legalX = false;
		boolean legalY = false;

		// Construct the object needed for reading user input.
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		// Get a choice from the user.
		try {
			choice = in.readLine();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
