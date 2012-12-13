package com.mapplas.app.handlers;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mapplas.app.AwesomeListView;
import com.mapplas.app.LocalizationAdapter;
import com.mapplas.model.SuperModel;


public class MessageHandlerFactory {
	
	public Handler getMapplasActivityMessageHandler(TextView listViewHeaderStatusMessage, boolean isSplashActive, SuperModel model, LocalizationAdapter listViewAdapter, AwesomeListView listView, List<ApplicationInfo> applicationList) {
		return new MapplasAppActivityMessageHandler(listViewHeaderStatusMessage, isSplashActive, model, listViewAdapter, listView, applicationList).getHandler();	
	}
	
	public Handler getUserFormActivityMessageHandler(ListView list, String currentResponse, Context context, LinearLayout privateFooter) {
		return new UserFormActivityMessageHandler(list, currentResponse, context, privateFooter).getHandler();
	}

}
