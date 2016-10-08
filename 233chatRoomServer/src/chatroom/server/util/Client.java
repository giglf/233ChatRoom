package chatroom.server.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import chatroom.client.util.User;

public class Client extends User{
	
	
	private Socket clientSocket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	public Client(User user, Socket gSocket) throws IOException {
		super(user);
		//clientSocket = serverSocket.accept();
		clientSocket = gSocket;
		outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		inputStream = new ObjectInputStream(clientSocket.getInputStream());
	}
	
	
}
