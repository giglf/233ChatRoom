package chatroom.client.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class use for sending message encrypt and decrypt 
 */
public class AESTools {
	
	private static final String ALGORITHM = "AES";
	// �ӽ����㷨/����ģʽ/��䷽ʽ
	private static final String ALGORITHM_STR  = "AES/ECB/PKCS5Padding";
	
	//���ڹ�����Կ
	private SecretKeySpec keySpec;
	
	
	//��ʼ����Կ
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