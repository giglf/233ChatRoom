package chatroom.client.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class use for sending message encrypt and decrypt 
 */
public class AESTools {
	
	private static final String ALGORITHM = "AES";
	// 加解密算法/工作模式/填充方式
	private static final String ALGORITHM_STR  = "AES/ECB/PKCS5Padding";
	
	//用于构建密钥
	private SecretKeySpec keySpec;
	
	
	//初始化密钥
	public AESTools(String key){
		keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
	}
	
	
	//AES encrypt
	public byte[] encryptData(byte[] data) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		return cipher.doFinal(data);
	}
	
	
	//AES decrypt
	public byte[] decryptData(byte[] data) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		return cipher.doFinal(data);
		
	}
	
}