package com.mapplas.app.async_tasks;

import java.util.concurrent.Semaphore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.model.Constants;
import com.mapplas.model.JsonParser;
import com.mapplas.model.SuperModel;

public class AppGetterTask extends AsyncTask<Location, Void, Void> {

	private Context context;
	
	private SuperModel model;
	
	private Handler handler;

	private static Semaphore semaphore = new Semaphore(1);

	private static boolean occupied = false;

	public AppGetterTask(Context context, SuperModel model, Handler messageHandler) {
		super();
		this.context = context;
		this.model = model;
		this.handler = messageHandler;
	}

	@Override
	protected Void doInBackground(Location... params) {

		try {
			semaphore.acquire();
			if(occupied) {
				semaphore.release();
				return null;
			}

			occupied = true;
			semaphore.release();
		} catch (Exception exc) {
			Log.d(this.getClass().getSimpleName(), "doInBackground", exc);
			return null;
		}

		Location location = params[0];

		String uid = "0";
		String serverResponse = "";

		if(this.model.currentUser() != null) {
			uid = this.model.currentUser().getId() + "";
		}

		SharedPreferences sharedPreferences = this.context.getSharedPreferences("synesth", Context.MODE_PRIVATE);
		String strLastNotifications = sharedPreferences.getString(Constants.SYNESTH_LAST_NOTIFICATIONS, "");

		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + Constants.SYNESTH_SERVER + ":" + Constants.SYNESTH_SERVER_PORT + Constants.SYNESTH_SERVER_PATH + "ipc_locations.php?l=" + location.getLatitude() + "," + location.getLongitude() + "&uid=" + uid + "&ln=" + strLastNotifications + "&v=" + Constants.SYNESTH_VERSION);

		try {
			HttpResponse rp = hc.execute(post);

			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				serverResponse = EntityUtils.toString(rp.getEntity());

//				TODO: uncomment for emulator use
//				serverResponse = "[{'IDLocalization':'21','Name':'Revienta burbujas','Latitude':'42.01','Longitude':'-4.5','Address':'','ZipCode':''," +
//						"'State':'','City':'','Country':'','Type':'application','IDCompany':'1','OfferID':'0','OfferName':'','OfferLogo':'','OfferLogoMini':''," +
//						"'OfferURL':'','OfferText':'','URLID':'0','URLName':'','URLLogo':'','URLLogoMini':'','URLValue':'','URLText':'','AppID':'0','AppName':'App 7'," +
//						"'AppLogo':'http://lh4.ggpht.com/4ek8rG7i-KP-m5sAmm2c9Msj5G9wVHDqou0F25iiGjSoXVpx_dUV2qfRcyfLeJBeUg=w72-h72','AppLogoMini':''," +
//						"'AppURL':'http://www.organic-software.com','AppDescription':'Descripcion 7','AppType':'android','UserAlarmID':'0','UserAlarmName':'','UserURLID':'0'," +
//						"'UserURLValue':'','UserURLDescription':'','UserURLTags':'','UserURLComment':'','UserURLPhoto':'','IDUser':'0','Radius':'20'," +
//						"'CreationDate':'0000-00-00','Phone':'','Wifi':'V','Bluetooth':'V','Location':'V','AppPrice':'0.99','AppIDGpcategory':'0','AppIDGpsubcategory':'0'," +
//						"'AppCategoryText':'','AppIDGpapp':'0','IndexFilter':'0','MatchRate':'0','AuxPlus':0,'AuxMinus':0,'AuxFavourite':false,'AuxPin':true,'AuxTotalPins':2," +
//						"'AuxRate':0,'AuxComment':'','AuxTotalRate':3.5,'AuxTotalComments':1," +
//						"" +
//						"" +
//						"'AuxNews':[{'IDNewsfeed':'3','IDCompany':'1','IDLocalization':'21','Title':'App 7 rated (3.5)'," +
//						"'Body':' rated App 7 at 20018 Donostia-San Sebasti\u00e1n, Donostia-San Sebasti\u00e1n, Espa\u00f1a','Date':'2012-08-30'," +
//						"'Hour':'13:55','Latitude':'0','Longitude':'0','DescriptiveGeoLoc':''}," +
//						
//						"{'IDNewsfeed':'4','IDCompany':'1','IDLocalization':'21','Title':'App 7 rated (3.5)'," +
//						"'Body':' rated App 7 at 20018 Donostia-San Sebasti\u00e1n, Donostia-San Sebasti\u00e1n, Espa\u00f1a','Date':'2012-07-25'," +
//						"'Hour':'13:05','Latitude':'0','Longitude':'0','DescriptiveGeoLoc':''}," +
//						
//						"{'IDNewsfeed':'5','IDCompany':'1','IDLocalization':'21','Title':'App 7 rated (3.5)'," +
//						"'Body':' rated App 7 at 20018 Donostia-San Sebasti\u00e1n, Donostia-San Sebasti\u00e1n, Espa\u00f1a','Date':'2012-10-28'," +
//						"'Hour':'13:05','Latitude':'0','Longitude':'0','DescriptiveGeoLoc':''}," +
//						
//						"{'IDNewsfeed':'6','IDCompany':'1','IDLocalization':'21','Title':'App 7 rated (3.5)'," +
//						"'Body':' rated App 7 at 20018 Donostia-San Sebasti\u00e1n, Donostia-San Sebasti\u00e1n, Espa\u00f1a','Date':'2012-12-10'," +
//						"'Hour':'13:05','Latitude':'0','Longitude':'0','DescriptiveGeoLoc':''}," +
//						
//						"{'IDNewsfeed':'7','IDCompany':'1','IDLocalization':'21','Title':'App 7 rated (3.5)'," +
//						"'Body':' rated App 7 at 20018 Donostia-San Sebasti\u00e1n, Donostia-San Sebasti\u00e1n, Espa\u00f1a','Date':'2012-12-6'," +
//						"'Hour':'13:05','Latitude':'0','Longitude':'0','DescriptiveGeoLoc':''}]," +
//						"" +
//						"'AuxComments':[{'IDComment':'18','IDLocalization':'21','Comment':'hhjb'," +
//						"'Rate':'3.5','Date':'2012-08-25','IDUser':'3','Hour':'','Latitude':'43.2944','Longitude':'-1.99626','DescriptiveGeoLoc':null}],'AuxPhotos':[]}]";

				// Comprobamos que el parser funciona correctamente
				JsonParser jp = new JsonParser(this.context);
				jp.parseApps(serverResponse, model, false);

			}
			else {
				if(MapplasActivity.mDebug) {
					String textoToast = "ESTADO: " + rp.getStatusLine().getStatusCode();
					Toast.makeText(context, textoToast, Toast.LENGTH_LONG).show();
				}
			}

			Message.obtain(handler, Constants.SYNESTH_MAIN_LIST_ID, null).sendToTarget();

		} catch (Exception exc) {
			Log.i(getClass().getSimpleName(), "ObtainLocalizationTask.doInBackGround: " + exc);
		}

		try {
			semaphore.acquire();
			occupied = false;
			semaphore.release();
		} catch (Exception exc) {
			Log.d(this.getClass().getSimpleName(), "doInBackground", exc);
			return null;
		}

		return null;
	}
}
