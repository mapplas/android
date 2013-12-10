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
	public static String request(String ii, Context context) {
		
		HttpClient hc = new DefaultHttpClient();
		HttpParams params = hc.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 8000);
		HttpConnectionParams.setSoTimeout(params, 8000);

		HttpPost post = new HttpPost("http://" + Constants.MAPPLAS_SERVER + ":" + Constants.MAPPLAS_SERVER_PORT + Constants.MAPPLAS_SERVER_PATH + "user/add/");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("imei", ii));
		nameValuePairs.add(new BasicNameValuePair("tel", android.os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE));

		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse rp = hc.execute(post);

			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(rp.getEntity());
			}
			else {
				return Constants.USER_IDENTIFICATION_SERVER_RESPONSE_ERROR;
			}
		} catch (SocketTimeoutException se) {
			return Constants.USER_IDENTIFICATION_SOCKET_ERROR;
		} catch (SocketException se) {
			return Constants.USER_IDENTIFICATION_SOCKET_ERROR;
		} catch (UnsupportedEncodingException ee) {
			return Constants.USER_IDENTIFICATION_SERVER_RESPONSE_ERROR;
		} catch (IOException e) {
			return Constants.USER_IDENTIFICATION_SERVER_RESPONSE_ERROR;
		}
	}
}
