package org.docsapss.mobapps.systemdashboard;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @class NoInternetConnectionActivity
 * @brief Facility to set a new pin code to login with
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
