package com.mapplas.app.handlers;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mapplas.app.AwesomeListView;
import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.app.adapters.AppAdapter;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;


public class MessageHandlerFactory {
	
	public Handler getMapplasActivityMessageHandler(TextView listViewHeaderStatusMessage, boolean isSplashActive, SuperModel model, AppAdapter listViewAdapter, AwesomeListView listView, List<ApplicationInfo> applicationList, MapplasActivity activity) {
		return new MapplasAppActivityMessageHandler(listViewHeaderStatusMessage, isSplashActive, model, listViewAdapter, listView, applicationList, activity).getHandler();	
	}
	
	public Handler getUserFormActivityMessageHandler(ListView list, String currentResponse, Context context, LinearLayout privateFooter, User user, String currentLocation) {
		return new UserFormActivityMessageHandler(list, currentResponse, context, privateFooter, user, currentLocation).getHandler();
	}

}
