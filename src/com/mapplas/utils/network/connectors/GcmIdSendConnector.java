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

public class GcmIdSendConnector {

	public static String request(int user_id, String gcm_id, String app_language) throws Exception {
		String serverResponse = "";

		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + Constants.MAPPLAS_SERVER + ":" + Constants.MAPPLAS_SERVER_PORT + Constants.MAPPLAS_SERVER_PATH + "gcm_register/");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("uid", String.valueOf(user_id)));
		nameValuePairs.add(new BasicNameValuePair("gcmid", gcm_id));
		nameValuePairs.add(new BasicNameValuePair("cc", app_language));
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
