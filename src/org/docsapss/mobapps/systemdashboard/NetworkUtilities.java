package org.docsapss.mobapps.systemdashboard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtilities {
	private Context context=null;
	
	public NetworkUtilities(Context context) {
		this.context=context;
	}
	
	public static boolean noConnection(Context context) {
		NetworkUtilities networkUtility=new NetworkUtilities(context);
		return !(networkUtility.isNetworkAvailable());
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		return isConnected;
	}
}
