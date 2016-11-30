package chatroom.client.util;

import java.io.Serializable;

/**
 * A Class that use to pack and distinguish the send data
 */
public class Packet implements Serializable{

	//auto generate serial number
	//实现类序列化
	private static final long serialVersionUID = -4618691721228452054L;

	//定义发送包的类型
	public enum PACKET_TYPE{
		MESSAGE, FILE_REQUEST, CLIENT_ENTER, CLIENT_EXIT
	}
	
	PACKET_TYPE packet_type = null;
	public User source;
	public User destination;
	public String message;
	public long filesize;
	
	private Packet(PACKET_TYPE type){
		packet_type = type;
	}
	
	public PACKET_TYPE getPacketType(){
		return packet_type;
	}
	
	//发送Message包封装设置
	public static Packet sendMessage(User src, String message){
		Packet packet = new Packet(PACKET_TYPE.MESSAGE);
		packet.source = src;
		packet.message = message;
		return packet;
	}
	
	//发送用户进入消息包封装设置
	public static Packet sendEnter(User user){
		Packet packet = new Packet(PACKET_TYPE.CLIENT_ENTER);
		packet.source = user;
		return packet;
	}
	
	//发送用户退出包封装设置
	public static Packet sendExit(User src){
		Packet packet = new Packet(PACKET_TYPE.CLIENT_EXIT);
		packet.source = src;
		return packet;
	}
	
	//发送文件请求包封装设置
	public static Packet sendFileRequest(User src, User dest, String filename, long size){
		Packet packet = new Packet(PACKET_TYPE.FILE_REQUEST);
		packet.destination = dest;
		packet.source = src;
		packet.message = filename;
		packet.filesize = size;
		return packet;
	}
	
}
