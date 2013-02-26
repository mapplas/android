package com.mapplas.utils.rating_dialog;

import android.content.Context;
import android.widget.Toast;
import app.mapplas.com.R;

import com.mapplas.app.RatingDialog;
import com.mapplas.model.App;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
import com.mapplas.utils.NetRequests;


public class OnReadyListener implements RatingDialog.ReadyListener {
	
	private SuperModel model;
	
	private Context context;
	
	private User user;
	
	private App app;

	public OnReadyListener(User user, Context context, SuperModel model, App app) {
		this.model = model;
		this.context = context;
		this.user = user;
		this.app = app;
	}

	@Override
	public void ready(String name) {

		// Guardamos la valoración del usuario en el servidor
		if(!name.equals("CANCEL")) {
			// Enviamos la nota por internet
			String uid = "0";

			if(this.user != null) {
				uid = String.valueOf(this.user.getId());
			}

			try {
				String rat = name.substring(0, name.indexOf("|"));
				String com = name.substring(name.indexOf("|") + 1);

				NetRequests.RateRequest(rat, com, this.model.currentLocation(), this.model.currentDescriptiveGeoLoc(), String.valueOf(this.app.getId()), uid);
				Toast.makeText(context, R.string.toast_after_rate_ok, Toast.LENGTH_LONG).show();
			} catch (Exception exc) {
				Toast.makeText(context, R.string.toast_after_rate_nok, Toast.LENGTH_LONG).show();
			}
		}
	}
}
