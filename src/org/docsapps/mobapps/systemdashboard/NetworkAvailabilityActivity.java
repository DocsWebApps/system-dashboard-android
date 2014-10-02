package org.docsapps.mobapps.systemdashboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @class NetworkAvailabilityActivity
 * @brief Displays the screen indicating there is no network connection and allows the user to exit
 */
public class NetworkAvailabilityActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nointernetconnection_view);
		Button butNoInternetExit=(Button) findViewById(R.id.nointernet_button);
		butNoInternetExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}