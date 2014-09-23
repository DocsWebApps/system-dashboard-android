package org.docsapss.mobapps.systemdashboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @class PinCheckActivity
 * @brief Checks and validates the users pin before launching the LoginActivity.
 * 		  If the user hasn't set a pin then it will direct you to an Activity to enter and save your pin, then launch the Login Activity.
 */
public class PinCheckActivity extends Activity {
	
	public static String PIN_CODE="PIN_CODE";
	
	private String PIN_FILE="application.pin";
	private String returnPinCode=null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkForExistingPin();
	}
	
	private void checkForExistingPin() {
		if (checkPinExists()) {
			launchLoginActivity();
		} else {
			enterNewPin();
		}
	}
	
	private void enterNewPin() {
		setContentView(R.layout.activity_pinentry);
		Button butNewPinSave=(Button) findViewById(R.id.new_pin_save);
		butNewPinSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView newPinEntry=(TextView) findViewById(R.id.new_pin_entry);
				String pinCode=newPinEntry.getText().toString().trim();
				if (validatePin(pinCode)) {
					writePinToFile(pinCode);
					launchLoginActivity();
				} else {
					pinErrorMessage();
				};
			}
		});
	}
	
	private void writePinToFile(String pinCode) {
		try {
			OutputStreamWriter osw=new OutputStreamWriter(openFileOutput(PIN_FILE, Context.MODE_PRIVATE));
			osw.write(pinCode);
			osw.flush();
			osw.close();
			setReturnPinCode(pinCode);
		} catch (Exception e) {e.printStackTrace();}	
	}
	
	private void pinErrorMessage() {
		Toast.makeText(this,"Incorrect pin! There is something wrong with the pin entered, please re-enter a 4 digit pin only.", Toast.LENGTH_LONG).show();
	}
	
	private void setReturnPinCode(String pinCode) {
		returnPinCode=pinCode;
	}

	private void launchLoginActivity() {
		Intent loginIntent=new Intent(this, LoginActivity.class);
		loginIntent.putExtra(PIN_CODE, returnPinCode);
		startActivity(loginIntent);
		finish();
	}
	
	private boolean checkPinExists() {
		return validatePin(readPinFromFile());	
	}
	
	private boolean checkFileExists() {
		File file=getBaseContext().getFileStreamPath(PIN_FILE);
		return file.exists();
	}
	
	private String readPinFromFile() {
		String pinCode=null;
		if (checkFileExists()) {
			try {
				FileInputStream fis=openFileInput(PIN_FILE);
				InputStreamReader isr= new InputStreamReader(fis);
				BufferedReader br=new BufferedReader(isr);
				pinCode=br.readLine().toString();
			} catch (Exception e) {e.printStackTrace();}
			setReturnPinCode(pinCode);
			return pinCode;
		} else {
			return "FileNotFound";
		}
	}
	
	private boolean validatePin(String pin) {
		if 	(pin.length()==4 && isDigit(pin.charAt(0)) && isDigit(pin.charAt(1)) && isDigit(pin.charAt(2)) && isDigit(pin.charAt(3))) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isDigit(Character c) {
		if (c >='0' && c <='9') {
			return true;
		} else {
			return false;
		}
	}
}