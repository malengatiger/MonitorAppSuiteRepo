package com.com.boha.monitor.library.util;



import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class WebCheck {
	static ConnectivityManager connectivity;
	
	public static void startWirelessSettings(Context ctx) {
		Intent i = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		ctx.startActivity(i);
	}
	public static WebCheckResult checkNetworkAvailability(Context ctx) {
		// ARE WE CONNECTED TO THE NET
		long start = System.currentTimeMillis();
		if (connectivity == null) {
			connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		
	    NetworkInfo wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    NetworkInfo mobileInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    WebCheckResult result = new WebCheckResult();
	    if (wifiInfo.isAvailable()) {
	    	Log.w(TAG, "###### WIFI AVAILABLE on check");
	    	result.setWifiAvailable(true);
	    } else {
	    	Log.e(TAG, "###### WIFI NOT AVAILABLE on check");
	    	result.setWifiAvailable(false);
	    }
	    
	    if (wifiInfo.isConnected()) {	    		
	    	Log.w(TAG, "###### WIFI CONNECTED on check");
    		result.setWifiConnected(true);
    	}else {
    		Log.e(TAG, "@@@ WIFI NOT CONNECTED on check");
    		result.setWifiConnected(false);
    	}
	    //
        if (mobileInfo != null) {
            if (mobileInfo.isAvailable()) {
                Log.w(TAG, "###### MOBILE_NETWORK AVAILABLE on check");
                result.setMobileAvailable(true);
            } else {
                Log.d(TAG, "@@@ MOBILE_NETWORK NOT AVAILABLE on check");
                result.setMobileAvailable(false);
            }
            if (mobileInfo.isConnected()) {
                Log.w(TAG, "###### MOBILE_NETWORK CONNECTED on check");
                result.setMobileConnected(true);
            } else {
                Log.d(TAG, "@@@ MOBILE_NETWORK NOT CONNECTED on check");
                result.setMobileConnected(false);
            }
        } else {
            Log.d(TAG, "@@@ MOBILE_NETWORK NOT AVAILABLE on check");
            result.setMobileAvailable(false);
        }
	    if (!result.isWifiConnected() && !result.isMobileConnected()) {
	    	result.setNetworkUnavailable(true);
	    }
	    long end = System.currentTimeMillis();
	    Log.d(TAG, "###### Network check completed in " + (end-start) + " ms");
	    return result;
	    
	}
	
	public static final int WIFI_AVAILABLE_CONNECTED = 1;
	//
	public static final int MOBILE_NETWORK_AVAILABLE = 2;
	public static final int BOTH_NETWORKS_AVAILABLE = 3;
	//
	public static final int NETWORK_NOT_AVAILABLE = 5;
	public static final String TAG = "WebCheck";
}
