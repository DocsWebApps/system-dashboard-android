package org.docsapps.mobapps.systemdashboard;

import java.lang.ref.WeakReference;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * @brief Displays a list view and systems and their statuses and any messages
 */
public class SystemListActivity extends ListActivity {
	private ProgressDialog mProgressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getListView().setFooterDividersEnabled(true);
		LayoutInflater inflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView footerView=(TextView) inflater.inflate(R.layout.footer_view, null);
		getListView().addFooterView(footerView);
		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fetchDataFromWebService();
			}
		});
		fetchDataFromWebService();
	}

	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	    	.setMessage("Are you sure you want to exit?")
	    	.setCancelable(false)
	    	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int id) {
	    			SystemListActivity.this.finish();
	    		}
	    	})
	    	.setNegativeButton("No", null)
	    	.show();
	}

	private void fetchDataFromWebService() {
		RestWebServiceHandler handler = new RestWebServiceHandler(this);
		Intent intent=RestWebService.makeIntent(this, handler, restURL(), returnToken());
		startService(intent);
		showProgressDialog();
	}
	
	private String returnToken() {
		//return "Token token="+StorageUtils.readFirstLineFromFile(this, AppSettingsActivity.TOKEN_FILE);
		return "Token token=fbcdbe86dfbf8c24bde83b20c361be6e"; // Local Rails App
        //return "Token token=467a6d763c591c15cb13042a105ca7b0"; // Heroku Online App
	}
	
	private String restURL() {
		//return StorageUtils.readFirstLineFromFile(this, AppSettingsActivity.WEB_SERVICE_FILE)+"/api/v2/systems";
		return "http://10.0.2.2:3000/api/v2/systems"; //AVD External Host IP Address: 10.0.2.2
        //return "http://system-dashboard.herokuapp.com/api/v2/systems"; //Online Heroku Application
	}
	
	private void launchSystemListActivity(String jsonResponse) throws JSONException {
		SystemListAdapter mAdapter = new SystemListAdapter(getApplicationContext());
		mAdapter.parseJsonString(jsonResponse);
		getListView().setAdapter(mAdapter);
	}
	
	private void processWebServiceResponse(Message msg) throws JSONException {
		Bundle bundle=msg.getData();
		String jsonResponse=bundle.getString(RestWebService.JSON_KEY);
		if (jsonResponse.equals("NoData")) {
			onNoDataFound();
		} else {
			launchSystemListActivity(jsonResponse);
		}
	}
	
	private void onNoDataFound() {
	    new AlertDialog.Builder(this) 
	    	.setTitle("No Data Found !")
	    	.setMessage("No data has been retrieved from the external web service for an unknown reason. Please make sure you have an active Internet connection when using this application.")
	    	.setCancelable(false)
	    	.setNeutralButton("Close Application", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int id) {
    			SystemListActivity.this.finish();
    		}
    	})
    	.show();
	}
	
    private void showProgressDialog() {
        mProgressDialog=ProgressDialog.show(this,"Contacting Dashboard Webservice:", "Retrieving data from external Web Service...");
    }
    
    private void dismissProcessDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }
	
	/**
	 * @class RestWebServiceHandler
	 * @brief Uses a weak reference to the outer class and used to handle responses from the RestWebService.
	 */
	static class RestWebServiceHandler extends Handler {
    	WeakReference<SystemListActivity> outerClass;

    	public RestWebServiceHandler(SystemListActivity outer) {
            outerClass = new WeakReference<SystemListActivity>(outer);
    	}

    	@Override
		public void handleMessage(Message msg) {
            final SystemListActivity activity = outerClass.get();
            if (activity != null) {
            	try {
            		activity.dismissProcessDialog();
					activity.processWebServiceResponse(msg);
				} catch (JSONException e) {e.printStackTrace();}
            }
    	}
    }
}