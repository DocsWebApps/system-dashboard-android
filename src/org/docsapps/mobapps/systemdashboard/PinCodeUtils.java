package org.docsapps.mobapps.systemdashboard;

import javax.crypto.SecretKey;

import android.content.Context;

/**
 * @class PinCodeUtils
 * @brief Provides utility methods for dealing and validating piCodes and for reading/writing pinCodes to files
 */
public class PinCodeUtils {
	
	public static final String PIN_CODE_ERROR="PinCodeError";
	private static final String pinFile="application.pin"; 
	
	public static boolean pinIsNotValid(Context context) {
		String pinCode=readPinFromFile(context);
		if (pinCode.equals(PIN_CODE_ERROR)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getPinCode(Context context) {
		return readPinFromFile(context);
	}
	
	public static boolean writePinToFile(Context context, String pinCode) {
		SecretKey key=null;
	
		if (validatePin(pinCode)) {
			try {
				key=EncryptionDecryptionUtils.getEnhancedSecretKey(pinCode.toCharArray());
			} catch (Exception e1) {
				return false;
			}
		} else {
			return false;
		}
		
		if (StorageUtils.writeStringToFile(context, pinFile, EncryptionDecryptionUtils.encryptString(pinCode, key))) {
			return true;
		} else {
			return false;
		}
	}
	
	private static String readPinFromFile(Context context) {
		String pinCode=null;
		pinCode=StorageUtils.readFirstLineFromFile(context, pinFile);
		if (pinCode.equals(StorageUtils.FILE_READ_ERROR)) {
			return PIN_CODE_ERROR;
		} else {
			return pinCode;
		}
	}
	
	private static boolean validatePin(String pin) {
		if 	(pin.length()==4 && isDigit(pin.charAt(0)) && isDigit(pin.charAt(1)) && isDigit(pin.charAt(2)) && isDigit(pin.charAt(3))) {
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean isDigit(Character c) {
		if (c >='0' && c <='9') {
			return true;
		} else {
			return false;
		}
	}
}