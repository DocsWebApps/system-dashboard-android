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
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;

public class DashboardService extends Service{
	private volatile Looper mServiceLooper;
	private volatile ServiceHandler mServiceHandler;
	
	// Factory method to make an Intent used to start this service
	public static Intent makeIntent(Context context, Uri uri, Handler parentHandler) {
		Intent intent=new Intent(context, DashboardService.class);
		intent.setData(uri);
		intent.putExtra("MESSENGER", new Messenger(parentHandler));
		return intent;
	}
	
	// Start a looper thread and create a ServiceHandler passing in the looper reference
	public void onCreate() {
		HandlerThread thread=new HandlerThread("DashboardService");
		thread.start();
		
		mServiceLooper=thread.getLooper();
		mServiceHandler=new ServiceHandler(mServiceLooper);
	}
	
	// Create a message to doSomething and post to the Service Handler's looper
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Message message=mServiceHandler.configureTaskMessage(intent, startId);
    	mServiceHandler.sendMessage(message);
    	return Service.START_NOT_STICKY;
    }
    
    private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		
		// Entry point for messages, triggered by mServiceHandler.sendMessage(message);
		public void handleMessage(Message message) {
			try {
				sendRequestToWebAPI((Intent) message.obj);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			stopSelf(message.arg1);
		}
		
		// Main code here
		private void sendRequestToWebAPI(Intent intent) throws ClientProtocolException, IOException {
			String jsonMessage=null;
			//String restfulRoute=intent.getData().toString();
			
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpGet httpGet=new HttpGet("system-dashboard.herokuapp.com/api/v2/systems");
		    httpGet.addHeader("Authorization","Token token=a7cf047390a68d72b7fc4f2162093f63");
		    httpGet.addHeader("Accept","application/json");
		    HttpResponse response = httpclient.execute(httpGet);
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        jsonMessage = out.toString();
		    } else{
		        response.getEntity().getContent().close();
		        throw new IOException(statusLine.getReasonPhrase());
		    }
			
			Message message = makeReplyMessage(jsonMessage);
			Messenger messenger = (Messenger)intent.getExtras().get("MESSENGER");
            try {
                messenger.send(message);
            } catch (Exception e) { }
		}
		
		// Create a message with the process ID to retrieve data from the web based API
		private Message configureTaskMessage(Intent intent, int startId) {
			Message message=Message.obtain();
			message.arg1=startId;
			message.obj=intent;
			return message;
		}
		
		// Make Reply message
		private Message makeReplyMessage(String replyMessage) {
			Message message=Message.obtain();
			message.arg1= replyMessage==null ? Activity.RESULT_CANCELED : Activity.RESULT_OK;
			Bundle bundle=new Bundle();
			bundle.putString("RETURN",replyMessage);
			message.setData(bundle);
			return message;
		}
	}
	
    // ****************************************************
	// Must have this method even if you don't implement it
    // It is for bound services
	@Override
	public IBinder onBind(Intent arg0) {return null;}
}