package chatroom.server.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import chatroom.client.util.Packet;
import chatroom.client.util.User;
import chatroom.server.ChatServer;

public class Client{
	
	private ChatServer server;
	private Socket clientSocket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	private User connectUser;
	
	public Client(ChatServer server, ServerSocket serverSocket) throws IOException{
		this.server = server;
		clientSocket = serverSocket.accept();
		outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		inputStream = new ObjectInputStream(clientSocket.getInputStream());
	}
	
	public Socket getSocket(){
		return clientSocket;
	}
	
	public ObjectInputStream getInputStream(){
		return inputStream;
	}
	
	public ObjectOutputStream getOutputStream(){
		return outputStream;
	}
	
	public User getConnectUser(){
		return connectUser;
	}
	
	public void listenToConnection(){
		Thread clientListener = new Thread(){
			boolean running = true;
			public void run() {
				while(running){
					try {
						Packet packet = (Packet)inputStream.readObject();
						switch (packet.getPacketType()) {
						case MESSAGE:
							server.broadcast(packet);
							break;
						case CLIENT_ENTER:
							connectUser = packet.source;
							server.sendClientLists();
							break;
						case CLIENT_EXIT:
							terminate();
							running = false;
							server.broadcast(Packet.sendExit(packet.source));
							break;
						case FILE_REQUEST:
							break;
						default:
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
						terminate();		//读失败时，可能是因为链接异常中断，需关闭该链接
						running = false;
					}
				}
			}
		};
		clientListener.start();
	}
	
	public void terminate(){
		server.dropClient(this);
		try {
			inputStream.close();
			outputStream.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
