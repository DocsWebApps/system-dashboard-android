package org.docsapps.mobapps.systemdashboard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @class NetworkUtils
 * @brief Checks for various levels of connectivity to either 3G / Wi-Fi networks or indeed the Internet
 */
public class NetworkUtils {
	private Context context=null;
	
	public NetworkUtils(Context context) {
		this.context=context;
	}
	
	public static boolean noConnection(Context context) {
		return !(new NetworkUtils(context).isNetworkAvailable());
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		return isConnected;
	}
}