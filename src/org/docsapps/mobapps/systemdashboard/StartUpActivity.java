package org.docsapps.mobapps.systemdashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @class StartUpActivity
 * @brief Handles the various startup checks and, if all is well, then hands over to the login process 
 */
public class StartUpActivity extends Activity {
	Intent intent=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		
		if (NetworkUtils.noConnection(this)) {
			intent=new Intent(this, NetworkAvailabilityActivity.class);
			startActivity(intent);
		} else if (PinCodeUtils.pinIsNotValid(this)) {
			intent=new Intent(this, EnterNewPinActivity.class);
			startActivity(intent);
		} else {
			Intent loginIntent=new Intent(this, LoginActivity.class);
			startActivity(loginIntent);
		}
		this.finish(); 
	}
}