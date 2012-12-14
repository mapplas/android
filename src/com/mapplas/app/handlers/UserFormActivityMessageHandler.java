package com.mapplas.app.handlers;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.ListView;
import app.mapplas.com.R;

import com.mapplas.app.UserLocalizationAdapter;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.JsonParser;
import com.mapplas.model.User;

public class UserFormActivityMessageHandler {
	
	private ListView list;
	
	private String currentResponse;
	
	private Context context;
	
	private LinearLayout privateFooter;
	
	private User user;
	
	private String currentLocation;
	
	public UserFormActivityMessageHandler(ListView list, String currentResponse, Context context, LinearLayout privateFooter, User user, String currentLocation) {
		this.list = list;
		this.currentResponse = currentResponse;
		this.context = context;
		this.privateFooter = privateFooter;
		this.user = user;
		this.currentLocation = currentLocation;
	}

	@SuppressLint("HandlerLeak")
	public Handler getHandler() {
		Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				JsonParser jp = new JsonParser();
				UserLocalizationAdapter ula2;
				ArrayList<App> locs;

				list.removeFooterView(privateFooter);

				switch (msg.what) {
					case Constants.SYNESTH_USER_PINUPS_ID:
						locs = jp.SimpleParseLocalizations(currentResponse);

						ula2 = new UserLocalizationAdapter(context, R.id.lblTitle, locs, UserLocalizationAdapter.PINUP, user, currentLocation);
						list.setAdapter(ula2);
						break;

					case Constants.SYNESTH_USER_LIKES_ID:
						locs = jp.SimpleParseLocalizations(currentResponse);

						ula2 = new UserLocalizationAdapter(context, R.id.lblTitle, locs, UserLocalizationAdapter.FAVOURITE, user, currentLocation);
						list.setAdapter(ula2);
						break;

					case Constants.SYNESTH_USER_BLOCKS_ID:
						locs = jp.SimpleParseLocalizations(currentResponse);

						ula2 = new UserLocalizationAdapter(context, R.id.lblTitle, locs, UserLocalizationAdapter.BLOCK, user, currentLocation);
						list.setAdapter(ula2);
						break;
				}
			}
		};
		
		return mHandler;
	}

}
