package com.mapplas.utils.network.async_tasks;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.AsyncTask;

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.model.GeoFence;
import com.mapplas.utils.network.connectors.GeofenceConnector;
import com.mapplas.utils.network.mappers.JsonToGeoFenceListMapper;
import com.mapplas.utils.network.mappers.JsonToGeoFenceMapper;

public class GeofenceRequesterTask extends AsyncTask<Void, Void, String> {

	private MapplasActivity activity;

	public GeofenceRequesterTask(MapplasActivity activity) {
		this.activity = activity;
	}

	@Override
	protected String doInBackground(Void... params) {
		String server_response = "";
		try {
			server_response = GeofenceConnector.request();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return server_response;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		ArrayList<GeoFence> geoFences = new ArrayList<GeoFence>();

		// Parse response
		try {
			geoFences = new JsonToGeoFenceListMapper(new JsonToGeoFenceMapper()).map(new JSONArray(result));
		} catch (Exception e) {
			// this.activity.geofenceRequestWentNok();
		}

		this.activity.geofenceRequestWentOk(geoFences);
	}

}
