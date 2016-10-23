package chatroom.client.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class use for sending message encrypt and decrypt 
 */
public class CryptoTools {
	
	private static final String ALGORITHM = "AES";
	// 加解密算法/工作模式/填充方式
	private static final String ALGORITHM_STR  = "AES/ECB/PKCS5Padding";
	
	//用于构建密钥
	private SecretKeySpec keySpec;
	
	
	//初始化密钥
	public CryptoTools(String key){
		keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
	}
	
	
	//AES encrypt
	public byte[] aesEncryptData(byte[] data) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		return cipher.doFinal(data);
	}
	
	
	//AES decrypt
	public byte[] aesDecryptData(byte[] data) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		return cipher.doFinal(data);
		
	}
	
	//getMD5
	public static String getMD5(String message) {
		MessageDigest mDigest = null;
		try {
			mDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		mDigest.update(message.getBytes());
		return byte2HexString(mDigest.digest());
	}
	
	//change byte[] to hex String
	public static String byte2HexString(byte[] b) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
		}
		return sb.toString().toUpperCase().trim();
	}
	
}