package edu.seg2105.server.ui;

import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

public class ServerConsole implements ChatIF {

	EchoServer server;
	// Server object.

	Scanner scan;
	// Scanner to read input.

	/**
	 * Constructor for the ServerConsole class.
	 * @param server The EchoServer instance running.
	 */
	public ServerConsole(EchoServer server) {
		this.server = server;
		scan = new Scanner(System.in);
	}

	/**
	 * Reads the server console. Waits for inputs and sends the message to be handled by ServerEcho.
	 */
	public void readConsole() {
		try {

			String message;
			
			while (true) {
				message = scan.nextLine();
				server.handleServerMessage(message);
			}
		} catch (Exception ex) {
			System.out.println("Unexpected error while reading from console!");
		}
	}

	/**
	 * Displays the server message with the special prefix "SERVERE MSG> ".
	 */
	@Override
	public void display(String message) {
		System.out.println("SERVER MSG> " + message);
	}

}
