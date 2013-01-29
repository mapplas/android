package com.mapplas.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
	 * User blocks request
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public static String UserBlocksRequest(String uid) throws Exception {
		return NetRequests.UserBlocksRequest(Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, uid);
	}

	public static String UserBlocksRequest(String serverIpAddress, int serverPort, String serverPath, String uid) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_userBlocks.php");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
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
	 * User pin ups request
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public static String UserPinUpsRequest(String uid) throws Exception {
		return NetRequests.UserPinUpsRequest(Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, uid);
	}

	public static String UserPinUpsRequest(String serverIpAddress, int serverPort, String serverPath, String uid) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_userPinups.php");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
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
	 * User edit request
	 * 
	 * @param name
	 * @param email
	 * @param imei
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public static String UserEditRequest(String name, String email, String imei, String uid) throws Exception {
		return NetRequests.UserEditRequest(name, email, imei, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, uid);
	}

	public static String UserEditRequest(String name, String email, String imei, String serverIpAddress, int serverPort, String serverPath, String uid) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_userEdit.php");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
		nameValuePairs.add(new BasicNameValuePair("n", name));
		nameValuePairs.add(new BasicNameValuePair("e", email));
		nameValuePairs.add(new BasicNameValuePair("ii", imei));
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		nameValuePairs.add(new BasicNameValuePair("l", Locale.getDefault().getLanguage()));
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
	 * Image request
	 * 
	 * @param image
	 * @param id
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public static String ImageRequest(byte[] image, String id, String uid) throws Exception {
		return NetRequests.ImageRequest(image, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid);
	}

	public static String ImageRequest(byte[] image, String serverIpAddress, int serverPort, String serverPath, String id, String uid) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_image.php");

		// ByteArrayEntity bae = new ByteArrayEntity(image);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		nameValuePairs.add(new BasicNameValuePair("image", Base64.encodeToString(image, false)));
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
	 * Problem request
	 * 
	 * @param problem
	 * @param id
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public static String ProblemRequest(String problem, String id, String uid) throws Exception {
		return NetRequests.ProblemRequest(problem, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid);
	}

	public static String ProblemRequest(String problem, String serverIpAddress, int serverPort, String serverPath, String id, String uid) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_problem.php");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
		nameValuePairs.add(new BasicNameValuePair("p", problem));
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

	/**
	 * Pin request
	 * 
	 * @param state
	 * @param id
	 * @param uid
	 * @param currentLatitude
	 * @param currentLongitude
	 * @return
	 * @throws Exception
	 */
	public static String PinRequest(String state, String id, String uid, String currentLongitude, String currentLatitude) throws Exception {
		return NetRequests.PinRequest(state, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid, currentLongitude, currentLatitude);
	}

	public static String PinRequest(String state, String serverIpAddress, int serverPort, String serverPath, String id, String uid, String currentLongitude, String currentLatitude) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_pin.php");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
		nameValuePairs.add(new BasicNameValuePair("s", state));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("uid", uid));
		nameValuePairs.add(new BasicNameValuePair("la", currentLatitude));
		nameValuePairs.add(new BasicNameValuePair("lo", currentLongitude));
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
	 * Like request
	 * 
	 * @param action
	 * @param id
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public static String LikeRequest(String action, String id, String uid) throws Exception {
		return NetRequests.LikeRequest(action, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid);
	}

	public static String LikeRequest(String action, String serverIpAddress, int serverPort, String serverPath, String id, String uid) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_like.php");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
		nameValuePairs.add(new BasicNameValuePair("t", action));
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
	 * Activity request
	 * 
	 * @param location
	 * @param action
	 * @param id
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public static String ActivityRequest(String location, String action, String id, String uid) throws Exception {
		return NetRequests.ActivityRequest(location, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, action, id, uid);
	}

	public static String ActivityRequest(String location, String serverIpAddress, int serverPort, String serverPath, String action, String id, String uid) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_activity.php");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
		nameValuePairs.add(new BasicNameValuePair("l", location));
		nameValuePairs.add(new BasicNameValuePair("a", action));
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
	 * UserI request
	 * 
	 * @param location
	 * @param ii
	 * @return
	 * @throws Exception
	 */
	public static String UserIRequest(String location, String ii) throws Exception {
		return NetRequests.UserIRequest(location, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, ii);
	}

	public static String UserIRequest(String location, String serverIpAddress, int serverPort, String serverPath, String ii) throws Exception {
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_ii.php");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
		nameValuePairs.add(new BasicNameValuePair("l", location));
		nameValuePairs.add(new BasicNameValuePair("ii", ii));
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

	// /**
	// * Notifications request
	// *
	// * @param location
	// * @param ii
	// * @return
	// * @throws Exception
	// */
	// public static String NotificationsRequest(String location, String ii)
	// throws Exception {
	// return NetRequests.NotificationsRequest(location,
	// Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT,
	// Constants.SYNESTH_SERVER_PATH, ii);
	// }
	//
	// public static String NotificationsRequest(String location, String
	// serverIpAddress, int serverPort, String serverPath, String ii) throws
	// Exception {
	// String serverResponse = "";
	// HttpClient hc = new DefaultHttpClient();
	// HttpPost post = new HttpPost("http://" + serverIpAddress + ":" +
	// serverPort + serverPath + "ipc_notifications.php");
	//
	// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	// nameValuePairs.add(new BasicNameValuePair("v",
	// Constants.SYNESTH_VERSION));
	// nameValuePairs.add(new BasicNameValuePair("l", location));
	// nameValuePairs.add(new BasicNameValuePair("ii", ii));
	// post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	//
	// try {
	// HttpResponse rp = hc.execute(post);
	//
	// if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	// serverResponse = EntityUtils.toString(rp.getEntity());
	//
	// }
	// else {
	// throw new Exception("HttpStatus != SC_OK");
	// }
	// } catch (IOException e) {
	// throw e;
	// }
	//
	// return serverResponse;
	// }

}
