package com.mapplas.utils.network.connectors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.mapplas.model.Constants;

public class GeofenceConnector {

	public static String request() throws Exception {
		String serverResponse = "";

		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + Constants.MAPPLAS_SERVER + ":" + Constants.MAPPLAS_SERVER_PORT + Constants.MAPPLAS_SERVER_PATH + "geofence-list/");

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
