package com.hackathon.smartlogger.gateway.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionUtils {

	/**
	 * Checks if network is available in the device or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(final Context context) {

		if (context == null){
			return false;
		}
		final ConnectivityManager mConnManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnManager != null) {
			final NetworkInfo[] netInfo = mConnManager.getAllNetworkInfo();
			for (NetworkInfo mNetInfo : netInfo) {
				if ((mNetInfo.getTypeName().equalsIgnoreCase("WIFI") || mNetInfo.getTypeName().equalsIgnoreCase(
						"MOBILE"))
						&& mNetInfo.isConnected()) {
					Log.d("", "========== ||  CONNECTION AVAILABLE  || =========");
					return true;
				}
			}
		}
		return false;
	}
}
