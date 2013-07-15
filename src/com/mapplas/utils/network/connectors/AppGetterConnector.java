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
import org.json.JSONObject;

import android.location.Location;

import com.mapplas.model.Constants;
import com.mapplas.model.User;
import com.mapplas.utils.network.mappers.JsonToAppMapper;


public class AppGetterConnector {
	
	public static String request(Location location, User user) throws Exception {
		String serverResponse = "";
		
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + Constants.SYNESTH_SERVER + ":" + Constants.SYNESTH_SERVER_PORT + Constants.SYNESTH_SERVER_PATH + "apps/0");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(location.getLatitude())));
		nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(location.getLongitude())));
		nameValuePairs.add(new BasicNameValuePair("p", String.valueOf(location.getAccuracy())));
		nameValuePairs.add(new BasicNameValuePair("uid", String.valueOf(user.getId())));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		try {
			HttpResponse rp = hc.execute(post);

			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				serverResponse = EntityUtils.toString(rp.getEntity());

				JsonToAppMapper mapper = new JsonToAppMapper();
				// Iterative mapper
				mapper.map(new JSONObject(serverResponse));
			}

		} catch (Exception exc) {
			throw exc;
		}
		
		return serverResponse;
	}
}