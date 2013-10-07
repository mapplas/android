package com.mapplas.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class NetworkConnectionChecker {

	public boolean isWifiEnabled(Context context) {
		WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		return wifi.isWifiEnabled();
	}

	public boolean isNetworkConnectionEnabled(Context context) {
		ConnectivityManager connectivityMan = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return networkInfo.isConnected();
	}
	
	public boolean isWifiConnected(Context context) {
		ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}

	public boolean isNetworkConnectionConnected(Context context) {
		ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetwork = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return mNetwork.isConnected();
	}
	
	public Toast getNetworkErrorToast(Context context, String stringToShow) {
		return Toast.makeText(context, stringToShow, Toast.LENGTH_LONG);
	}

}
