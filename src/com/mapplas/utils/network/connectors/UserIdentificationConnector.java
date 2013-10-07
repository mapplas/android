package com.mapplas.utils.network.connectors;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.model.Constants;
import com.mapplas.utils.network.NetworkConnectionChecker;

public class UserIdentificationConnector {

	/**
	 * UserI request
	 * 
	 * @param location
	 * @param ii
	 * @return
	 * @throws Exception
	 */
	public static String request(String ii, Context context) {
		String serverResponse = "";

		HttpClient hc = new DefaultHttpClient();
		HttpParams params = hc.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 8000);
		HttpConnectionParams.setSoTimeout(params, 8000);

		HttpPost post = new HttpPost("http://" + Constants.SYNESTH_SERVER + ":" + Constants.SYNESTH_SERVER_PORT + Constants.SYNESTH_SERVER_PATH + "user/add/");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("imei", ii));
		nameValuePairs.add(new BasicNameValuePair("tel", android.os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE));

		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse rp = hc.execute(post);

			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				serverResponse = EntityUtils.toString(rp.getEntity());
			}
			else {
				serverResponse = Constants.SERVER_RESPONSE_ERROR_USER_IDENTIFICATION;
			}
		} catch (SocketTimeoutException se) {
			showNetworkConnectionToastAndQuit(context);
		} catch (SocketException se) {
			showNetworkConnectionToastAndQuit(context);
		} catch (UnsupportedEncodingException ee) {
			showNetworkConnectionToastAndQuit(context);
		} catch (IOException e) {
			showNetworkConnectionToastAndQuit(context);
		}

		return serverResponse;
	}
	
	private static void showNetworkConnectionToastAndQuit(Context context) {
		NetworkConnectionChecker networkConnChecker = new NetworkConnectionChecker();
		networkConnChecker.getNetworkErrorToast(context, "USER REGISTERING CONNECTION ERROR").show();
		((MapplasActivity)context).finish();
	}
}
