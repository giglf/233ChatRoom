package chatroom.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import chatroom.client.util.Packet;
import chatroom.server.util.Client;


/**
 * Main running in Server 
 *
 */
public class ChatServer {
	
	private static final int PORT = 23399;
	private ServerSocket serverSocket;

	private List<Client> clientList = new ArrayList<Client>();
	
	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.startListening(server);
	}
	
	public void startListening(ChatServer chatServer){
		Thread listen = new Thread(){
			public void run() {
				try {
					serverSocket = new ServerSocket(PORT);
					while(true){
						System.out.println("Listening on port " + PORT + " ...");
						Client newClient = new Client(chatServer, serverSocket);
						System.out.println("Connected: " + newClient.getSocket().getInetAddress().getHostName());
						clientList.add(newClient);
						newClient.listenToConnection();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		listen.start();
	}
	
	//向所有用户广播消息
	public void broadcast(Packet packet){
		for(Client client : clientList){
			try {
				client.getOutputStream().writeObject(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//发送用户列表
	public void sendClientLists(){
		for(Client client : clientList){
			broadcast(Packet.sendEnter(client.getConnectUser()));
		}
	}
	
	public void dropClient(Client client){
		clientList.remove(client);
	}
	
}
