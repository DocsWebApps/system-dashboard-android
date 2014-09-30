package org.docsapss.mobapps.systemdashboard;

import android.content.Context;

/**
 * @class PinCodeUtils
 * @brief Provides utility methods for dealing and validating piCodes and for reading/writing pinCodes to files
 */
public class PinCodeUtils {
	
	private static String fileName="application.pin"; 
	
	public static boolean pinIsNotValid(Context context) {
		String pinCode=readPinFromFile(context);
		if (pinCode.equals("PinCodeError")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getPinCode(Context context) {
		return readPinFromFile(context);
	}
	
	public static boolean writePinToFile(Context context, String pinCode) {
		try {
			if (validatePin(pinCode) && StorageUtils.writeStringToFile(context, fileName, pinCode)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static String readPinFromFile(Context context) {
		String pinCode=null;
		pinCode=StorageUtils.readFirstLineFromFile(context, fileName);
	
		if (validatePin(pinCode)) {
			return pinCode;
		} else {
			return "PinCodeError";
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