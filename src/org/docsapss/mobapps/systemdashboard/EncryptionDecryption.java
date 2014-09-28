package org.docsapss.mobapps.systemdashboard;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncryptionDecryption {
	
	@SuppressLint("TrulyRandom")
	public static SecretKey getSecretKey() throws NoSuchAlgorithmException {
	    final int outputKeyLength = 256;
	    SecureRandom secureRandom = new SecureRandom();
	    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
	    keyGenerator.init(outputKeyLength, secureRandom);
	    SecretKey key = keyGenerator.generateKey();
	    return key;
	}

	public static String decryptString(String encryptedString, SecretKey key) {
		String decryptedString=null;
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] myBytes=Base64.decode(encryptedString, Base64.DEFAULT);
            decodedBytes = c.doFinal(myBytes);
            decryptedString=new String(decodedBytes);
        } catch (Exception e) {
        }
		return decryptedString;
	}
	
	public static String encryptString(String decryptedString, SecretKey key) {
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, EncryptionDecryption.getSecretKey());
            encodedBytes = c.doFinal(decryptedString.getBytes());
        } catch (Exception e) {}
        String encryptedString=Base64.encodeToString(encodedBytes, Base64.DEFAULT);
		return encryptedString;	
	}
}