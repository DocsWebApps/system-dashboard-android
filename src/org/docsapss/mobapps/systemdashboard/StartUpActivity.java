package org.docsapss.mobapps.systemdashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartUpActivity extends Activity {
	Intent intent=null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		
		if (NetworkUtilities.noConnection(this)) {
			intent=new Intent(this, NetworkAvailabilityActivity.class);
			startActivity(intent);
		} else if  (PinCode.pinIsNotValid(this)) {
			intent=new Intent(this, EnterNewPinActivity.class);
			startActivity(intent);
			this.finish();
		} else {
			Intent loginIntent=new Intent(this, LoginActivity.class);
			startActivity(loginIntent);
		}
		this.finish(); 
	}
}