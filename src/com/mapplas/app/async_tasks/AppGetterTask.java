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

				// Comprobamos que el parser funciona correctamente
				JsonParser jp = new JsonParser();
				jp.Init();
				jp.ParseLocalizations(serverResponse, model, false);
				jp.Delete();

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
