package org.docsapps.mobapps.systemdashboard;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * @class EncryptionDecryptionUtils
 * @brief Provides encryption / decryption tools to the application
 */
public class EncryptionDecryptionUtils {
	private static final String saltString="** PUT YOUR OWN KEY PHRASE HERE !! **";

	@SuppressLint("TrulyRandom")
	public static SecretKey getStandardSecretKey() throws NoSuchAlgorithmException {
	    final int outputKeyLength = 256;
	    SecureRandom secureRandom = new SecureRandom();
	    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
	    keyGenerator.init(outputKeyLength, secureRandom);
	    SecretKey secretkey = keyGenerator.generateKey();
	    return secretkey;
	}
	
	public static SecretKey getEnhancedSecretKey(char[] passphraseOrPin) throws NoSuchAlgorithmException, InvalidKeySpecException {
	    // Number of PBKDF2 hardening rounds to use. Larger values increase
	    // computation time. You should select a value that causes computation
	    // to take >100ms.
	    final int iterations = 1000; 

	    final int outputKeyLength = 256;
	    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	    KeySpec keySpec = new PBEKeySpec(passphraseOrPin, saltString.getBytes(), iterations, outputKeyLength);
	    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
	    return secretKey;
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
            c.init(Cipher.ENCRYPT_MODE,key);
            encodedBytes = c.doFinal(decryptedString.getBytes());
        } catch (Exception e) {}
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);	
	}
}