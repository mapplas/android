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

import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;

public class AppDetailConnector {

	public static String request(String app_id, String app_language, SuperModel model) throws Exception {
		String serverResponse = "";

		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + Constants.MAPPLAS_SERVER + ":" + Constants.MAPPLAS_SERVER_PORT + Constants.MAPPLAS_SERVER_PATH + "app-detail/" + app_id + "/");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("cc", app_language));
		
		String[] latitudelongitude = model.currentLocation().split(",");
		nameValuePairs.add(new BasicNameValuePair("lat", latitudelongitude[0]));
		nameValuePairs.add(new BasicNameValuePair("lon", latitudelongitude[1]));
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