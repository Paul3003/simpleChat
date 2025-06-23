Readme.md

Paul Heckman, 300371106.

Testcases:

Testcase 2001: Project runs as expected. This case was passed.

Testcase 2002: Project runs as expected with the client displaying: "Login id cannot be blank. Terminating client." This case was passed.

Testcase 2003: Project runs as expected with the client displaying: "Error: Can't setup connection! Terminating client." This case was passed.

Testcase 2004: Project runs as expected with the server displaying: "New client connected.
message received: #login id1 from null
id1 has logged on."
The client displays: "id1 has logged on."
This case was passed.

Testcase 2005: Project runs as expected with client displaying: "> hello world" from input "hello world".
Server displays: "message received: hello world from id1".
This test case was passed.

Testcase 2006: Project runs as expected. Test case passed. Both clients receive the basic client-sent messages along with the server message with the special prefix "SERVER MSG>".

Testcase 2007: Project runs as expected. Test case passed. The server indicates it is disconnecting and quits.

Testcase 2008: Project runs as expected. Test case passed. Server console indicates it has stopped listening for connections and after "#close" indicates it is disconnecting all clients and shutting down. Client console then indicates the server was disconnected and terminates.

Testcase 2009: Project runs as expected. Test case passed. Server console indicates the server stops listening after "#close" command. Next, it indicates it starts listening on port 5555 after "#start" command. Finally, the client is able to connect to the server.

Testcase 2010: Project runs as expected. Test case passed. Server disconnects from clients and client programs terminate.

Testcase 2011: Project runs as expected. Test case passed. Client disconnects from server and the connection is closed.

Testcase 2012: Project runs as expected. Test case passed. Server console displays: "Server listening for connections on port 1234".

Testcase 2013: Project runs as expected. Test case passed. Consoles display proper log on messages and that "Server listening for connections on port 1234".