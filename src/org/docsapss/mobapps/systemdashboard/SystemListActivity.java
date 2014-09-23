package org.docsapss.mobapps.systemdashboard;

import java.lang.ref.WeakReference;

import org.json.JSONException;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @class SystemActivity
 * @brief Displays a list view and systems and their statuses and any messages.
 */
public class SystemListActivity extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getListView().setFooterDividersEnabled(true);
		TextView footerView = null;
		LayoutInflater inflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerView=(TextView) inflater.inflate(R.layout.footer_view, null);
		getListView().addFooterView(footerView);
		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fetchDataFromWebService();
			}
		});
		fetchDataFromWebService();
	}
	
	// Fetch data from the external Web Service via an API
	private void fetchDataFromWebService() {
		RestWebServiceHandler handler = new RestWebServiceHandler(this);
		Intent intent=RestWebService.makeIntent(this, handler, restURL());
		startService(intent);
	}
	
	// Return the restful URL as a String
	private String restURL() {
		//return "http://localhost:3000/api/v2/systems";					// Railsdev
		return "http://awayday-feedback.herokuapp.com/api/v2/systems";		// Heroku
	}
	
	// Launch the SystemListActivity
	private void launchSystemListActivity(Message msg) throws JSONException {
		Bundle bundle=msg.getData();
		SystemListAdapter mAdapter = new SystemListAdapter(getApplicationContext());
		mAdapter.parseJsonString((String) bundle.getString(RestWebService.JSON_KEY));
		getListView().setAdapter(mAdapter);
	}
	
	/**
	 * @class RestWebServiceHandler
	 * @brief Uses a weak reference to the outer class and used to handle responses from the RestWebService.
	 */
	static class RestWebServiceHandler extends Handler {
    	WeakReference<SystemListActivity> outerClass;

    	// Set up weak reference to outer class
    	public RestWebServiceHandler(SystemListActivity outer) {
            outerClass = new WeakReference<SystemListActivity>(outer);
    	}
    	
    	// Handle any messages that get sent to this Handler
    	@Override
		public void handleMessage(Message msg) {
            final SystemListActivity activity = outerClass.get();
            if (activity != null) {
            	try {
					activity.launchSystemListActivity(msg);
				} catch (JSONException e) {e.printStackTrace();}
            }
    	}
    }
	/**
	 * End of RestWebServiceHandler Class
	 */
}