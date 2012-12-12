package com.mapplas.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.mapplas.app.MapplasActivity;
import com.mapplas.model.Constants;

public class NetRequests {
	
	public static String UnrateRequest(String id, String uid) throws Exception
	{
		return NetRequests.UnrateRequest(Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid);
	}
	
	public static String UnrateRequest(String serverIpAddress, int serverPort, String serverPath, String id, String uid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_unrate.php");
        
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("id", id));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
        
        return serverResponse;
	}
	
	
	public static String UserBlocksRequest(String uid) throws Exception
	{
		return NetRequests.UserBlocksRequest(Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, uid);
	}
	
	public static String UserBlocksRequest(String serverIpAddress, int serverPort, String serverPath, String uid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_userBlocks.php");
        
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
        
        return serverResponse;
	}
	
	
	public static String UserLikesRequest(String uid) throws Exception
	{
		return NetRequests.UserLikesRequest(Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, uid);
	}
	
	public static String UserLikesRequest(String serverIpAddress, int serverPort, String serverPath, String uid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_userLikes.php");
        
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
        
        return serverResponse;
	}
	
	public static String UserRatesRequest(String uid) throws Exception
	{
		return NetRequests.UserRatesRequest(Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, uid);
	}
	
	public static String UserRatesRequest(String serverIpAddress, int serverPort, String serverPath, String uid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_userRates.php");
        
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
        
        return serverResponse;
	}
	
	
	public static String UserPinUpsRequest(String uid) throws Exception
	{
		return NetRequests.UserPinUpsRequest(Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, uid);
	}
	
	public static String UserPinUpsRequest(String serverIpAddress, int serverPort, String serverPath, String uid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_userPinups.php");
        
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
        
        return serverResponse;
	}
	
	
	public static String UserEditRequest(String name, String email, String imei, String uid) throws Exception
	{
		return NetRequests.UserEditRequest(name, email, imei, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, uid);
	}
	
	public static String UserEditRequest(String name, String email, String imei, String serverIpAddress, int serverPort, String serverPath, String uid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_userEdit.php");
        
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("n", name));
        nameValuePairs.add(new BasicNameValuePair("e", email));
        nameValuePairs.add(new BasicNameValuePair("ii", imei));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
        
        return serverResponse;
	}
	
	
	public static String ImageRequest(byte[] image, String id, String uid) throws Exception
	{
		return NetRequests.ImageRequest(image, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid);
	}
	
	public static String ImageRequest(byte[] image, String serverIpAddress, int serverPort, String serverPath, String id, String uid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_image.php");
        
        ByteArrayEntity bae = new ByteArrayEntity(image);
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("id", id));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        nameValuePairs.add(new BasicNameValuePair("image", Base64.encodeToString(image, false)));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
        
        return serverResponse;
	}
	
	
	
	public static String ProblemRequest(String problem, String id, String uid) throws Exception
	{
		return NetRequests.ProblemRequest(problem, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid);
	}
	
	public static String ProblemRequest(String problem, String serverIpAddress, int serverPort, String serverPath, String id, String uid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_problem.php");
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("p", problem));
        nameValuePairs.add(new BasicNameValuePair("id", id));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
        
        return serverResponse;
	}
	
	public static String RateRequest(String rate, String comment, String id, String uid) throws Exception
	{
		return NetRequests.RateRequest(rate, comment, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid);
	}
	
	public static String RateRequest(String rate, String comment, String serverIpAddress, int serverPort, String serverPath, String id, String uid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_rate.php");
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("r", rate));
        nameValuePairs.add(new BasicNameValuePair("c", comment));
        nameValuePairs.add(new BasicNameValuePair("id", id));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        nameValuePairs.add(new BasicNameValuePair("l", MapplasActivity.GetModel().currentLocation));
        nameValuePairs.add(new BasicNameValuePair("dl", MapplasActivity.GetModel().currentDescriptiveGeoLoc));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
        
        return serverResponse;
	}
	
	public static String PinRequest(String state, String id, String uid) throws Exception
	{
		return NetRequests.PinRequest(state, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid);
	}
	
	public static String PinRequest(String state, String serverIpAddress, int serverPort, String serverPath, String id, String uid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_pin.php");
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("s", state));
        nameValuePairs.add(new BasicNameValuePair("id", id));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
        
        return serverResponse;
	}
	
	public static String LikeRequest(String action, String id, String uid) throws Exception
	{
		return NetRequests.LikeRequest(action, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid);
	}
	
	public static String LikeRequest(String action, String serverIpAddress, int serverPort, String serverPath, String id, String uid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_like.php");
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("t", action));
        nameValuePairs.add(new BasicNameValuePair("id", id));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
        
        return serverResponse;
	}
	
	
	public static String LocationsRequest(String location, String uid, String radius) throws Exception
	{
		return NetRequests.LocationsRequest(location, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, uid, radius);
	}
	
	public static String LocationsRequest(String location, String serverIpAddress, int serverPort, String serverPath, String uid, String radius) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_locations.php");
	    
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("l", location));
        nameValuePairs.add(new BasicNameValuePair("radius", radius));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
	    try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
	    
	    return serverResponse;
	}
	
	
	public static String LocationIdRequest(String location, String uid, String lid) throws Exception
	{
		return NetRequests.LocationIdRequest(location, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, uid, lid);
	}
	
	public static String LocationIdRequest(String location, String serverIpAddress, int serverPort, String serverPath, String uid, String lid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_location.php");
	    
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("l", location));
        nameValuePairs.add(new BasicNameValuePair("lid", lid));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
	    try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
	    
	    return serverResponse;
	}
	
	
	public static String HitRequest(String location, String id, String uid) throws Exception
	{
		return NetRequests.HitRequest(location, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, id, uid);
	}
	
	public static String HitRequest(String location, String serverIpAddress, int serverPort, String serverPath, String id, String uid) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_hit.php");
	    
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("l", location));
        nameValuePairs.add(new BasicNameValuePair("id", id));
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
	    try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
	    
	    return serverResponse;
	}
	
	
	public static String ActivityRequest(String location, String action, String id, String uid) throws Exception
	{
		return NetRequests.ActivityRequest(location, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, action, id, uid);
	}
	
	public static String ActivityRequest(String location, String serverIpAddress, int serverPort, String serverPath, String action, String id, String uid) throws Exception
	{
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
		
	    try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
	    
	    return serverResponse;
	}
	
	public static String UserIRequest(String location, String ii) throws Exception
	{
		return NetRequests.UserIRequest(location, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, ii);
	}
	
	public static String UserIRequest(String location, String serverIpAddress, int serverPort, String serverPath, String ii) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_ii.php");
	    
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("l", location));
        nameValuePairs.add(new BasicNameValuePair("ii", ii));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
	    try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
	    
	    return serverResponse;
	}
	
	public static String NotificationsRequest(String location, String ii) throws Exception
	{
		return NetRequests.NotificationsRequest(location, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, ii);
	}
	
	public static String NotificationsRequest(String location, String serverIpAddress, int serverPort, String serverPath, String ii) throws Exception
	{
		String serverResponse = "";
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + serverIpAddress + ":" + serverPort + serverPath + "ipc_notifications.php");
	    
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("v", Constants.SYNESTH_VERSION));
        nameValuePairs.add(new BasicNameValuePair("l", location));
        nameValuePairs.add(new BasicNameValuePair("ii", ii));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
	    try
	    {
	    	HttpResponse rp = hc.execute(post);
	
	        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	        {
	        	serverResponse = EntityUtils.toString(rp.getEntity());
	        	
	        }else
	        {
	        	throw new Exception("HttpStatus != SC_OK");
	        }
	    }catch(IOException e){
	    	throw e;
	    }
	    
	    return serverResponse;
	}
	
}
