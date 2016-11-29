package chatroom.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
	private ServerSocket downloadSocket;
	private ServerSocket sendSocket;
	private List<Client> clientList = new ArrayList<Client>();
	
	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.startListening(server);
	}
	
	//����һ���߳̿�ʼ����
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
	
	//�������û��㲥��Ϣ
	public void broadcast(Packet packet){
		for(Client client : clientList){
			try {
				client.getOutputStream().writeObject(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//�����û��б�
	public void sendClientLists(){
		for(Client client : clientList){
			broadcast(Packet.sendEnter(client.getConnectUser()));
		}
	}
	
	//ɾ��ĳ�û�
	public void dropClient(Client client){
		clientList.remove(client);
	}
	
	//ͨ�������ҵ��û�������
	public Client findClient(String name){
		for(Client client : clientList){
			if(client.getConnectUser().getUsername().equals(name)){
				return client;
			}
		}
		return null;
	}
	
	//�ӿͻ��������ļ���ת��
	public byte[] downloadFile(Packet packet){
		try {
			System.out.println("Start downloading file " + packet.message + " now");
			if (downloadSocket == null) {
				downloadSocket = new ServerSocket(6505);
			}
			
			Socket newSocket = downloadSocket.accept();
			System.out.println("Connected to client on port 6505...");
			
			byte[] fileBytes = new byte[(int) packet.filesize];
			InputStream input = newSocket.getInputStream();
			
			//ÿ�ζ�д�����ǻ�������С�����ƣ�����ͨ��ѭ����д�������ļ�����Ȼ���ļ�̫��ʱ���׳��������ļ���β�Ĵ���
			int bytesRead = input.read(fileBytes, 0, fileBytes.length);
			int currentOffset = bytesRead;
			do{
				bytesRead = input.read(fileBytes, currentOffset, fileBytes.length-currentOffset);
				if(bytesRead >= 0){
					currentOffset += bytesRead;
				}
			}while(currentOffset < fileBytes.length);
			
			return fileBytes;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//�����ļ����ͻ�
	public void sendFile(Packet packet, byte[] fileBytes){
		try {
			if(sendSocket == null){
				sendSocket = new ServerSocket(6506);
			}
			Socket sendFileSocket = sendSocket.accept();
			OutputStream output = sendFileSocket.getOutputStream();
			
			output.write(fileBytes, 0, fileBytes.length);
			output.flush();
			
			System.out.println("Sending finish!");
			output.close();
			sendFileSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
