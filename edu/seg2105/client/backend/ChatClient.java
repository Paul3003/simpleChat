// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;

	String id;
	Boolean loggedIn = false;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String id, String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.id = id;
		this.clientUI = clientUI;
		openConnection();
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		// Modified to handle special commands.
		String[] commands = msg.toString().split(" ");
		// Saves the input into an array of all words according to spaces.
		if (commands[0].startsWith("#")) {
			//If the first character of the message is a "#" it treats it as a command:
			switch (commands[0]) {
			//The switch case checks for different cases of the first part of the command message.
			case "#quit":
				clientUI.display("Exiting...");
				quit();
				break;
			case "#logoff":
				clientUI.display("Disconnecting from server...");
				try {
					closeConnection();
				} catch (IOException e) {
					clientUI.display("Could not disconnect from server. Terminating client.");
					quit();
				}
				clientUI.display("Disconnected.");
				break;
			case "#sethost":
				if (this.isConnected()) {
					clientUI.display("Error: please log off before changing hosts.\nRun: \"#logoff\"");
				} else {
					if (commands[1].isEmpty()) {
						clientUI.display("Syntax error: host name not given.\nUse: #sethost [host]");
					} else {
						setHost(commands[1]);
						clientUI.display("New host set.");
					}
				}
				break;
			case "#setport":
				if (this.isConnected()) {
					clientUI.display("Error: please log off before changing ports.\nRun: \"#logoff\"");
				} else {
					if (commands[1].isEmpty()) {
						clientUI.display("Syntax error: no port given.\nUse: #setport [port]");
					} else {
						setPort(Integer.parseInt(commands[1]));
						clientUI.display("New port set.");
					}
				}
				break;
			case "#login":
				if (commands[1].isEmpty()) {
					if (this.isConnected()) {
						clientUI.display("Error: already connected to server!");
					} else {
						try {
							this.openConnection();
						} catch (IOException e) {
							clientUI.display("Could not connect to server.  Terminating client.");
							quit();
						}
					}
				}
				break;
				//Ensure this login command is different from the starting login command.
			case "#gethost":
				clientUI.display(getHost());
				break;
			case "#getport":
				clientUI.display("" + getPort());
				break;
			}
			//The cases are the implementations of the commands specified in the instructions document.
		} else {
			clientUI.display(msg.toString());
		}
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {
		try {
			sendToServer(message);
		} catch (IOException e) {
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	/************** Modified code **************/

	/**
	 * Overridden method called when the client-server connection is terminated by
	 * the client. Prints message indicating the server has shut down, and
	 * terminates the client.
	 */
	@Override
	protected void connectionClosed() {
		System.out.println("Disconnected from server. Exiting...");
		System.exit(0);
	}

	/**
	 * Overridden method called when the client-server connection is terminated by
	 * the server. Prints message indicating the server has shut down, prints the
	 * returned error and terminates the client.
	 */
	@Override
	protected void connectionException(Exception exception) {
		System.out.println("Server has disconnected. Exiting...");
		quit();
	}
	
	/**
	 * Overridden method called when the client-server connection is established. Attempts to send login command message.
	 */
	@Override
	protected void connectionEstablished() {
		try {
			sendToServer("#login " + this.id);
		} catch (IOException e) {
			System.out.println("Error logging in.");
		}
	}

}
//End of ChatClient class
