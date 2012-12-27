package com.mapplas.utils.presenters;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ListView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.UserAppAdapter;
import com.mapplas.app.async_tasks.UserBlocksTask;
import com.mapplas.app.async_tasks.UserLikesTask;
import com.mapplas.app.async_tasks.UserPinUpsTask;
import com.mapplas.model.App;
import com.mapplas.model.JsonParser;
import com.mapplas.model.User;
import com.mapplas.model.UserFormLayoutComponents;
import com.mapplas.utils.NetRequests;

public class UserFormDynamicSublistsPresenter {

	private UserFormLayoutComponents layoutComponents;

	private ListView list;

	private Context context;

	private User user;

	private Handler messageHandler;

	private Animation refreshAnimation;

	private String currentLocation;

	public UserFormDynamicSublistsPresenter(UserFormLayoutComponents layoutComponents, ListView list, Context context, User user, Handler messageHandler, Animation refreshAnimation, String currentLocation) {
		this.layoutComponents = layoutComponents;
		this.list = list;
		this.context = context;
		this.user = user;
		this.messageHandler = messageHandler;
		this.refreshAnimation = refreshAnimation;
		this.currentLocation = currentLocation;
	}

	public void present() {
		this.setBlocksLayoutBehaviour();
		this.pinUpsLayoutBehaviour();
		this.likesLayoutBehaviour();
		this.ratesLayoutBehaviour();
	}

	private void setBlocksLayoutBehaviour() {
		this.layoutComponents.blocksLayout().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Obtain blocked items data
				list.removeFooterView(layoutComponents.footerButtonsLayout());

				layoutComponents.pinUpsLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.ratesLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.likesLayout().setBackgroundResource(Color.TRANSPARENT);

				layoutComponents.blocksLayout().setBackgroundResource(R.drawable.bgd_tab_pressed);

				list.addFooterView(layoutComponents.footerListRefreshLayout());
				SharedPreferences settings = context.getSharedPreferences("prefs", 0);
				if(settings.getBoolean("user_logged", false)) {
					list.addFooterView(layoutComponents.footerButtonsLayout());
				}
				layoutComponents.refreshIcon().startAnimation(refreshAnimation);

				UserAppAdapter ula = new UserAppAdapter(context, R.id.lblTitle, new ArrayList<App>(), UserAppAdapter.BLOCK, user, currentLocation);
				list.setAdapter(ula);

				// Block request
				new UserBlocksTask(messageHandler, user.getId()).execute();
			}
		});
	}

	private void pinUpsLayoutBehaviour() {
		this.layoutComponents.pinUpsLayout().setBackgroundResource(R.drawable.bgd_tab_pressed);

		this.layoutComponents.pinUpsLayout().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Obtain blocked items data
				list.removeFooterView(layoutComponents.footerButtonsLayout());

				layoutComponents.blocksLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.ratesLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.likesLayout().setBackgroundResource(Color.TRANSPARENT);

				layoutComponents.pinUpsLayout().setBackgroundResource(R.drawable.bgd_tab_pressed);

				list.addFooterView(layoutComponents.footerListRefreshLayout());
				SharedPreferences settings = context.getSharedPreferences("prefs", 0);
				if(settings.getBoolean("user_logged", false)) {
					list.addFooterView(layoutComponents.footerButtonsLayout());
				}
				layoutComponents.refreshIcon().startAnimation(refreshAnimation);

				UserAppAdapter ula = new UserAppAdapter(context, R.id.lblTitle, new ArrayList<App>(), UserAppAdapter.BLOCK, user, currentLocation);
				list.setAdapter(ula);

				// User pin-ups request task
				new UserPinUpsTask(messageHandler, user.getId()).execute();
			}
		});
	}

	private void likesLayoutBehaviour() {
		this.layoutComponents.likesLayout().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Obtain blocked items data
				list.removeFooterView(layoutComponents.footerButtonsLayout());

				layoutComponents.blocksLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.pinUpsLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.ratesLayout().setBackgroundResource(Color.TRANSPARENT);

				layoutComponents.likesLayout().setBackgroundResource(R.drawable.bgd_tab_pressed);

				list.addFooterView(layoutComponents.footerListRefreshLayout());
				SharedPreferences settings = context.getSharedPreferences("prefs", 0);
				if(settings.getBoolean("user_logged", false)) {
					list.addFooterView(layoutComponents.footerButtonsLayout());
				}
				layoutComponents.refreshIcon().startAnimation(refreshAnimation);

				UserAppAdapter ula = new UserAppAdapter(context, R.id.lblTitle, new ArrayList<App>(), UserAppAdapter.BLOCK, user, currentLocation);
				list.setAdapter(ula);

				// User-likes request
				new UserLikesTask(messageHandler, user.getId()).execute();
			}
		});
	}

	private void ratesLayoutBehaviour() {
		layoutComponents.ratesLayout().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Obtain blocked items data
				try {
					String response = NetRequests.UserRatesRequest(user.getId() + "");
					JsonParser jp = new JsonParser(context);

					ArrayList<App> locs = jp.SimpleParseLocalizations(response);

					UserAppAdapter ula = new UserAppAdapter(context, R.id.lblTitle, locs, UserAppAdapter.RATE, user, currentLocation);

					list.setAdapter(ula);

				} catch (Exception exc) {
					Log.d(this.getClass().getSimpleName(), "Get Rates", exc);
				}
			}
		});

	}
}
