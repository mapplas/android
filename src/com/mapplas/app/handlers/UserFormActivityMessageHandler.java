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
import com.mapplas.model.Constants;
import com.mapplas.model.JsonParser;
import com.mapplas.model.App;

public class UserFormActivityMessageHandler {
	
	private ListView list;
	
	private String currentResponse;
	
	private Context context;
	
	private LinearLayout privateFooter;
	
	public UserFormActivityMessageHandler(ListView list, String currentResponse, Context context, LinearLayout privateFooter) {
		this.list = list;
		this.currentResponse = currentResponse;
		this.context = context;
		this.privateFooter = privateFooter;
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

						ula2 = new UserLocalizationAdapter(context, R.id.lblTitle, locs, UserLocalizationAdapter.PINUP);
						list.setAdapter(ula2);
						break;

					case Constants.SYNESTH_USER_LIKES_ID:
						locs = jp.SimpleParseLocalizations(currentResponse);

						ula2 = new UserLocalizationAdapter(context, R.id.lblTitle, locs, UserLocalizationAdapter.FAVOURITE);
						list.setAdapter(ula2);
						break;

					case Constants.SYNESTH_USER_BLOCKS_ID:
						locs = jp.SimpleParseLocalizations(currentResponse);

						ula2 = new UserLocalizationAdapter(context, R.id.lblTitle, locs, UserLocalizationAdapter.BLOCK);
						list.setAdapter(ula2);
						break;
				}
			}
		};
		
		return mHandler;
	}

}
