package com.mapplas.utils.network.connectors;

import java.io.IOException;
import java.net.URLEncoder;
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
import org.apache.http.util.EntityUtils;

import com.mapplas.model.Constants;

public class PinRequestConnector {

	public static String request(String action, String app_id, String uid, double currentLongitude, double currentLatitude, String reverseGeocodedLocation) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + Constants.MAPPLAS_SERVER + ":" + Constants.MAPPLAS_SERVER_PORT + Constants.MAPPLAS_SERVER_PATH + "user/pin/");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("s", action));
		nameValuePairs.add(new BasicNameValuePair("app", app_id));
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		
		if(action == Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_PIN) {
			nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(currentLatitude)));
			nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(currentLongitude)));
			nameValuePairs.add(new BasicNameValuePair("a", URLEncoder.encode(reverseGeocodedLocation)));
		}
		
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		try {
			HttpResponse rp = hc.execute(post);

			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				serverResponse = EntityUtils.toString(rp.getEntity());
			}
			else {
				throw new Exception("HttpStatus != SC_OK");
			}
		} catch (IOException e) {
			throw e;
		}

		return serverResponse;
	}
}
