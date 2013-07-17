package com.mapplas.utils.network.connectors;

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

import android.location.Location;

import com.mapplas.model.Constants;

public class AppShareConnector {

	public static String request(String app_id, int user_id, Location location) throws Exception {
		String serverResponse = "";

		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + Constants.SYNESTH_SERVER + ":" + Constants.SYNESTH_SERVER_PORT + Constants.SYNESTH_SERVER_PATH + "user/share/");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("app", app_id));
		nameValuePairs.add(new BasicNameValuePair("uid", String.valueOf(user_id)));
		nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(location.getLatitude())));
		nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(location.getLongitude())));
		nameValuePairs.add(new BasicNameValuePair("via", ""));

		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		try {
			HttpResponse rp = hc.execute(post);

			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				serverResponse = EntityUtils.toString(rp.getEntity());
			}

		} catch (Exception exc) {
			throw exc;
		}

		return serverResponse;
	}
}
