package org.docsapss.mobapps.systemdashboard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DashboardActivity extends Activity{
	
	private static TextView textView;;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		textView=(TextView) findViewById(R.id.textView1);
	}
	
	public void getSystems(View view) {
		new WebService().execute("http://192.168.56.102:3000/api/v2/systems");					// railsdev
		//new WebService().execute("http://system-dashboard.herokuapp.com/api/v2/systems");		// Heroku
	}
	
	static class WebService extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... url) {
			String jsonMessage=null;
			try {
				jsonMessage = getDataFromAPI(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return jsonMessage;
		}
		
		private String getDataFromAPI(String[] url) throws IOException {
			String jsonMessage=null;
			HttpClient httpclient = new DefaultHttpClient();
		    HttpGet httpGet=new HttpGet(url[0]);
		    httpGet.addHeader("Authorization","Token token=1f57183b411b402523893b7717c6e8d1"); 		// railsdev
		    //httpGet.addHeader("Authorization","Token token=a7cf047390a68d72b7fc4f2162093f63");	// Heroku	
		    httpGet.addHeader("Accept","application/json");
		    HttpResponse response = httpclient.execute(httpGet);
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        jsonMessage = out.toString();
		    } else {
		        response.getEntity().getContent().close();
		        throw new IOException(statusLine.getReasonPhrase());
		    }
			return jsonMessage;
			//return "{\"systems\":[{\"name\":\"kirk\",\"status\":\"green\"},{\"name\":\"spock\",\"status\":\"amber\"},{\"name\":\"bones\",\"status\":\"red\"}]}";
		    }
		
		@Override
		protected void onPostExecute(String json) {
			textView.setText(json);
		}
	}
}