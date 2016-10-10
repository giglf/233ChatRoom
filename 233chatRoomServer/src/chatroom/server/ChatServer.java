package chatroom.server;

import java.util.ArrayList;
import java.util.List;

import chatroom.server.util.Client;


/**
 * Main running in Server 
 *
 */
public class ChatServer {
	
	private static final int PORT = 23399;
	
	private List<Client> clientList = new ArrayList<Client>();
	//listening->accept()->Packet.Type->
	//switch 1.RegisterHandle 2.LoginHandler 3.sendMessage 4.sendFile (3 and 4 implement in TransferData.java)
	
	
}
