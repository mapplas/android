package com.mapplas.utils.network.requests;

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

public class NetRequests {

	/**
	 * Unrate request
	 * 
	 * @param id
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public static String UnrateRequest(String id, String uid) throws Exception {
		return NetRequests.UnrateRequest(Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid);
	}

	public static String UnrateRequest(String serverIpAddress, int serverPort, String serverPath, String id, String uid) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_unrate.php");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
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

	/**
	 * Rate request
	 * 
	 * @param rate
	 * @param comment
	 * @param currentLocation
	 * @param currentDescriptiveGeoLoc
	 * @param id
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public static String RateRequest(String rate, String comment, String currentLocation, String currentDescriptiveGeoLoc, String id, String uid) throws Exception {
		return NetRequests.RateRequest(rate, comment, currentLocation, currentDescriptiveGeoLoc, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid);
	}

	public static String RateRequest(String rate, String comment, String currentLocation, String currentDescriptiveGeoLoc, String serverIpAddress, int serverPort, String serverPath, String id, String uid) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_rate.php");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
		nameValuePairs.add(new BasicNameValuePair("r", rate));
		nameValuePairs.add(new BasicNameValuePair("c", comment));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		nameValuePairs.add(new BasicNameValuePair("l", currentLocation));
		nameValuePairs.add(new BasicNameValuePair("dl", currentDescriptiveGeoLoc));
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
