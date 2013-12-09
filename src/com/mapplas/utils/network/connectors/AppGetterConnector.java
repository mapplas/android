package com.mapplas.utils.network.connectors;

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
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.network.mappers.JsonToAppReponseMapper;

public class AppGetterConnector {

	public static String request(Location location, SuperModel model, boolean resetPagination, Context context, String app_language) {
		String serverResponse = "";

		int page = checkPageToRequest(resetPagination, model);

		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + Constants.MAPPLAS_SERVER + ":" + Constants.MAPPLAS_SERVER_PORT + Constants.MAPPLAS_SERVER_PATH + "apps/" + page + "/");

		// HttpParams params = hc.getParams();
		// HttpConnectionParams.setConnectionTimeout(params, 10000);
		// HttpConnectionParams.setSoTimeout(params, 10000);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(location.getLatitude())));
		nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(location.getLongitude())));
		nameValuePairs.add(new BasicNameValuePair("p", String.valueOf(location.getAccuracy())));
		nameValuePairs.add(new BasicNameValuePair("uid", String.valueOf(model.currentUser().getId())));
		nameValuePairs.add(new BasicNameValuePair("cc", app_language));
		nameValuePairs.add(new BasicNameValuePair("dcc", model.deviceCountry()));

		// Sent to server if has to decode user position to show dialog language
		SharedPreferences sharedPrefs = context.getSharedPreferences(Constants.MAPPLAS_SHARED_PREFS, Context.MODE_PRIVATE);
		boolean firstboot = sharedPrefs.getBoolean(Constants.MAPPLAS_SHARED_PREFS_LANGUAGE_DIALOG_SHOWN, true);
		nameValuePairs.add(new BasicNameValuePair("fb", String.valueOf(firstboot)));

		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse rp = hc.execute(post);

			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				serverResponse = EntityUtils.toString(rp.getEntity());

				JsonToAppReponseMapper mapper = new JsonToAppReponseMapper();
				mapper.setResetPagination(resetPagination, context);
				// Iterative mapper
				mapper.map(new JSONObject(serverResponse), model);
			}
			else {
				JsonToAppReponseMapper mapper = new JsonToAppReponseMapper();
				mapper.setMockedAppToList(model);
			}
		} catch (SocketTimeoutException se) {
//			Log.e("app", "SocketTimeoutException");
			return Constants.APP_OBTENTION_ERROR_SOCKET;
		} catch (SocketException se) {
//			Log.e("app", "SocketException");
			return Constants.APP_OBTENTION_ERROR_SOCKET;
		} catch (Exception e) {
			JsonToAppReponseMapper mapper = new JsonToAppReponseMapper();
			mapper.setMockedAppToList(model);
			return Constants.APP_OBTENTION_OK;
		}

		return Constants.APP_OBTENTION_OK;
	}

	private static int checkPageToRequest(boolean resetPagination, SuperModel model) {
		int page = 0;

		if(!resetPagination) {
			int loadedApps = model.appList().size();
			page = loadedApps / Constants.MAPPLAS_APPLICATION_APPS_PAGINATION_NUMBER;
		}

		return page;
	}
}