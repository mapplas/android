package com.mapplas.utils;

import android.app.Activity;
import android.content.Intent;

import com.mapplas.app.activities.AppDetail;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.MoreFromDeveloperApp;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.static_intents.SuperModelSingleton;

public class MoreFromDeveloperHelper {

	public void launchAppDetailFromMoreFromDeveloperApp(MoreFromDeveloperApp moreFromDeveloperApp, Activity activity, SuperModel model) {
		App selected_app = new App();
		selected_app.setId(moreFromDeveloperApp.id());
		selected_app.setAppLogo(moreFromDeveloperApp.logo());
		selected_app.setName(moreFromDeveloperApp.name());
		selected_app.setAppPrice(moreFromDeveloperApp.price());
		
		Intent intent = new Intent(activity, AppDetail.class);
		intent.putExtra(Constants.MAPPLAS_DETAIL_APP, selected_app);
		SuperModelSingleton.model = model;
		activity.startActivity(intent);				
	}

}
