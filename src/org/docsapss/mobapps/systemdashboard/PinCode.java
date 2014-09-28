package org.docsapss.mobapps.systemdashboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;

public class PinCode {
	
	private static String fileName="application.pin"; 
	
	public static boolean pinIsNotValid(Context context) {
		String pinCode=readPinFromFile(context);
		if (pinCode.equals("PinCodeNotSet")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getPinCode(Context context) {
		return readPinFromFile(context);
	}
	
	private static String readPinFromFile(Context context) {
		String pinCode="PinCodeNotSet";
		if (checkFileExists(context)) {
			try {
				FileInputStream fis=context.openFileInput(fileName);
				InputStreamReader isr= new InputStreamReader(fis);
				BufferedReader br=new BufferedReader(isr);
				pinCode=br.readLine().toString();
			} catch (Exception e) {
				e.printStackTrace();
				return pinCode;
			}
		}
		
		if (validatePin(pinCode)) {
			return pinCode;
		} else {
			return "PinCodeNotSet";
		}
	}
	
	public static boolean writePinToFile(Context context, String pinCode) {
		try {
			if (validatePin(pinCode)) {
				OutputStreamWriter osw=new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
				osw.write(pinCode);
				osw.flush();
				osw.close();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
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
	
	private static boolean checkFileExists(Context context) {
		File file=context.getFileStreamPath(fileName);
		return file.exists();
	}
}