package org.docsapss.mobapps.systemdashboard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class DashboardActivity extends Activity{
	
	private static TextView textView;
	private ActivityHandler activityHandler=new ActivityHandler();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		textView=(TextView) findViewById(R.id.textView1);
	}

    public void getSystems(View view) {
    	// Send a request to retrieve all the systems information from the V2 API
        Intent intent = DashboardService.makeIntent(this, Uri.parse("http://system-dashboard.herokuapp.com/api/v2/systems"), activityHandler);
        startService(intent);
    }
	
	public class ActivityHandler extends Handler{
		public void handleMessage(Message message) {
			Bundle bundle=message.getData();
			textView.setText((String) bundle.getString("RETURN"));
		}
	}
}