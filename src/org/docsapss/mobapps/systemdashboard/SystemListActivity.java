package org.docsapss.mobapps.systemdashboard;

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
	
	private void fetchDataFromWebService() {
		RestWebServiceHandler handler = new RestWebServiceHandler(this);
		Intent intent=RestWebService.makeIntent(this, handler, restURL());
		startService(intent);
		showDialog();
	}

	private String restURL() {
		return "http://awayday-feedback.herokuapp.com/api/v2/systems";
	}
	
	private void launchSystemListActivity(String jsonResponse) throws JSONException {
		SystemListAdapter mAdapter = new SystemListAdapter(getApplicationContext());
		mAdapter.parseJsonString(jsonResponse);
		getListView().setAdapter(mAdapter);
	}
	
	private void processWebServiceResponse(Message msg) throws JSONException {
		Bundle bundle=msg.getData();
		String jsonResponse=(String) bundle.getString(RestWebService.JSON_KEY);
		if (jsonResponse.equals("NoData")) {
			noDataFound();
		} else {
			launchSystemListActivity(jsonResponse);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void noDataFound() {
		AlertDialog alertDialog=new AlertDialog.Builder(this).create();
		alertDialog.setTitle("No Data Found");
		alertDialog.setMessage("No data has been retrieved from the external web service for an unknown reason. Please make sure you have an active Internet connection when using this application.");
		alertDialog.setButton("Exit Application", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
			}
		});
		alertDialog.show();
	}
	
    private void showDialog() {
        mProgressDialog=ProgressDialog.show(this,"Contacting Dashboard Webservice:", "Retrieving data from external Web Service...");
    }
    
    private void dismissDialog() {
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
            		activity.dismissDialog();
					activity.processWebServiceResponse(msg);
				} catch (JSONException e) {e.printStackTrace();}
            }
    	}
    }
}