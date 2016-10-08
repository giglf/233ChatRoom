package chatroom.client.test;

import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import chatroom.client.util.User;

public class TestNetFramework {
	
	private static final int PORT = 23399;
	private static final String host = "localhost";
	
	private static String getMD5(String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest md5 = MessageDigest.getInstance("md5");
		return new HexBinaryAdapter().marshal(md5.digest(pwd.getBytes()));
	}
	
	
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		User testUser1 = new User();
		testUser1.setUsername("giglf");
		testUser1.setPassword(getMD5("123456"));
		testUser1.setSex(User.MALE);
		
		User testUser2 = new User();
		testUser2.setUsername("lin");
		testUser2.setPassword(getMD5("654321"));
		testUser2.setSex(User.MALE);
		
		
	}
}
