package com.mapplas.utils.network.connectors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.mapplas.model.Constants;


public class NotifyUserConnector {

	public static void request(int userId, String email, double latitude, double longitude) throws Exception {
		
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + Constants.SYNESTH_SERVER + ":" + Constants.SYNESTH_SERVER_PORT + Constants.SYNESTH_SERVER_PATH + "user/notify/" + userId + "/");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("e", email));
		nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(latitude)));
		nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(longitude)));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		try {
			hc.execute(post);
		} catch (IOException e) {
			throw e;
		}
	}
}
