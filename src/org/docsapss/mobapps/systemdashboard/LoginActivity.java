package org.docsapss.mobapps.systemdashboard;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @class LoginActivity
 * @brief Handles the initial login activity
 */
public class LoginActivity extends Activity {
	
	private  String myPin=null;
	final Context loginView=this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent=getIntent();
		myPin=intent.getStringExtra(PinCheckActivity.PIN_CODE);
		
		setContentView(R.layout.activity_login);
		
		final EditText pinCode1=(EditText) findViewById(R.id.pin1);
		final EditText pinCode2=(EditText) findViewById(R.id.pin2);
		final EditText pinCode3=(EditText) findViewById(R.id.pin3);
		final EditText pinCode4=(EditText) findViewById(R.id.pin4);
		
		setOnFocusChangeListenerFor(pinCode1);
		setOnFocusChangeListenerFor(pinCode2);
		setOnFocusChangeListenerFor(pinCode3);
		setOnFocusChangeListenerFor(pinCode4);
		
		setOnTextChangedListenerFor(pinCode1,pinCode2);
		setOnTextChangedListenerFor(pinCode2,pinCode3);
		setOnTextChangedListenerFor(pinCode3,pinCode4);
				
		pinCode4.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if (pinCode4.getText().toString().length()<1) return;
				if (myPin.equals(enteredPin(pinCode1, pinCode2, pinCode3, pinCode4))) {
					launchSystemListActivity();
					finish();
				} else {
					resetLoginActivity(pinCode1, pinCode2, pinCode3, pinCode4);
				}
			}
		});
	}
	
	private String enteredPin(EditText pin1, EditText pin2, EditText pin3, EditText pin4) {
		return pin1.getText().toString()+pin2.getText().toString()+pin3.getText().toString()+pin4.getText().toString();
	}
	
	private void launchSystemListActivity() {
		Intent dashboardIntent=new Intent(loginView, SystemListActivity.class);
		startActivity(dashboardIntent);
		finish();
	}
	
	private void resetLoginActivity(EditText pin1, EditText pin2, EditText pin3, EditText pin4) {
		Toast.makeText(this,"Incorrect pin, please re-enter", Toast.LENGTH_SHORT).show();
		pin2.requestFocus();
		pin3.requestFocus();
		pin4.requestFocus();
		pin1.requestFocus();
	}
		
	private void setOnTextChangedListenerFor(final EditText et1, final EditText et2) {
		et1.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if (et1.getText().toString().length()<1) return;
				et2.requestFocus();	
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
				int arg2, int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {}
		});
	}
	
	private void setOnFocusChangeListenerFor(EditText et) {
		et.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					((EditText) v).setText("");
				}
			}
		});
	}
}