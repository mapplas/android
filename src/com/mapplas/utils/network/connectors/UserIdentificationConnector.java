package com.mapplas.utils.network.connectors;

import java.io.IOException;
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


public class UserIdentificationConnector {

	/**
	 * UserI request
	 * 
	 * @param location
	 * @param ii
	 * @return
	 * @throws Exception
	 */
	public static String request(String ii) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + Constants.SYNESTH_SERVER + ":" + Constants.SYNESTH_SERVER_PORT + Constants.SYNESTH_SERVER_PATH + "user/add/");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("imei", ii));
		nameValuePairs.add(new BasicNameValuePair("tel", android.os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE));
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
