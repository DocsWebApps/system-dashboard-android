package org.docsapss.mobapps.systemdashboard;

import java.lang.ref.WeakReference;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class DashboardActivity extends Activity{
	
	private TextView textView=null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		textView=(TextView) findViewById(R.id.textView1);
	}
	
	// Triggered by button on display
	public void getSystems(View view) {
		RestWebServiceHandler handler = new RestWebServiceHandler(this);
		Intent intent=RestWebService.makeIntent(this, handler, restURL());
		startService(intent);
	}
	
	// Return the restful URL as a String
	private String restURL() {
		//return "http://localhost:3000/api/v2/systems";					// Railsdev
		return "http://system-dashboard.herokuapp.com/api/v2/systems";		// Heroku
	}
	
	/**
	 * @class RestWebServiceHandler
	 * @brief Uses a weak reference to the outer class and used to handle responses from the RestWebService.
	 */
	static class RestWebServiceHandler extends Handler {
    	WeakReference<DashboardActivity> outerClass;

    	// Set up weak reference to outer class
    	public RestWebServiceHandler(DashboardActivity outer) {
            outerClass = new WeakReference<DashboardActivity>(outer);
    	}
    	
    	// Handle any messages that get sent to this Handler
    	@Override
		public void handleMessage(Message msg) {
            final DashboardActivity activity = outerClass.get();
    		
            if (activity != null) {
    			Bundle bundle=msg.getData();
    			activity.textView.setText((String) bundle.getString(RestWebService.JSON_KEY));
            }
    	}
    }
	/**
	 * End of RestWebServiceHandler Class
	 */
}