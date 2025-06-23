package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:

import java.io.IOException;
import java.util.HashMap;

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import edu.seg2105.server.ui.ServerConsole;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	
	static ServerConsole serverUI;

	private HashMap<Object, Boolean> sentMessage = new HashMap<Object, Boolean>(10);
	// Same number as the maximum allowed connected users.
	// The HashMap is used to keep track of what users logged in, and stop them from running the #login command if they've already sent messages.

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
		serverUI = new ServerConsole(this);
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String[] message = msg.toString().split(" ");
		System.out.println("message received: " + msg + " from " + client.getInfo("login"));
		if (message[0].equals("#login")) {
			//Checks for login command.
			if (!sentMessage.get(client)) {
				//Checks sentMessage hash table to verify this is the first command sent. If not, prints an error message.
				client.setInfo("login", message[1]);
				System.out.println(message[1] + " has logged on.");
				this.sendToAllClients(message[1] + " has logged on.");
				//Displays information that the client has logged on.
			} else {
				System.out.println("Error: #login can only be the first command sent.");
			}
		}
		this.sendToAllClients(msg);
		sentMessage.put(client, true);
		//Adds the client to the hash table to keep track that they have now sent a command and cannot send #login again.
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}
		
		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
		serverUI.readConsole();
	}

	/************** Modified code **************/
	/**
	 * Displays a message when connected to a client with its connection
	 * information.
	 * 
	 * @param client The connection of the client that just connected to the server.
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		sentMessage.put(client, false);
		//Adds the client to the hash table to avoid errors when checking client's chat history.
		System.out.println("New client connected.");
		this.sendToAllClients("Connected to client: " + client.toString());
	}

	/**
	 * Displays a message when disconnected from a client with its connection
	 * information.
	 * 
	 * @param client The connection of the client that just disconnected to the
	 *               server.
	 */
	@Override
	protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("Disconnected from client: " + client.toString());
	}

	/**
	 * This method handles all data that comes in from the server console.
	 *
	 * @param msg The message from the server.
	 */
	public void handleServerMessage(String msg) {
		String[] commands = msg.split(" ");
		// Saves the input into an array of all words according to spaces.
		if (commands[0].startsWith("#")) {
			//The if statement detects if the message is meant to be a server command.
			switch (commands[0]) {
			//The switch case checks for different cases of the first part of the command message.
			case "#quit":
				serverUI.display("Exiting...");
				stopListening();
				try {
					close();
				} catch (IOException e) {

				}
				break;
			case "#stop":
				stopListening();
				serverUI.display("Server is no longer listening for clients.");
				break;
			case "#close":
				serverUI.display("Server will stop listening and disconnect all clients.");
				stopListening();
				try {
					close();
				} catch (IOException e) {

				}
				break;
			case "#setport":
				if (isListening() || getNumberOfClients() != 0) {
					serverUI.display("Error: please close the server before changing ports.\nRun: \"#close\"");
				} else {
					if (commands[1].isEmpty()) {
						serverUI.display("Syntax error: no port given.\nUse: #setport [port]");
					} else {
						setPort(Integer.parseInt(commands[1]));
						serverUI.display("New port set.");
					}
				}
				break;
			case "#start":
				if (isListening()) {
					serverUI.display("Error: server is already started!");
				} else {
					try {
						listen();
					} catch (IOException e) {
						serverUI.display("Could not start server. Terminating client.");
						try {
							close();
						} catch (IOException f) {

						}
					}
				}
				break;
			case "#getport":
				serverUI.display("" + getPort());
				break;
			}
			//The cases are the implementations of the commands specified in the instructions document.
		} else {
			serverUI.display(msg);
			this.sendToAllClients("SERVER MSG> " + msg);
			//Sends the message to all clients with the special prefix.
		}
	}
}
//End of EchoServer class
