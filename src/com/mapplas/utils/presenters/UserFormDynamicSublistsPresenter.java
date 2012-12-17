package com.mapplas.utils.presenters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ListView;
import app.mapplas.com.R;

import com.mapplas.app.UserLocalizationAdapter;
import com.mapplas.app.threads.BlockRequestThread;
import com.mapplas.app.threads.UserLikesRequestThread;
import com.mapplas.app.threads.UserPinUpsRequestThread;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
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
				list.removeFooterView(layoutComponents.footerInfoLayout());
				layoutComponents.blocksLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.pinUpsLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.ratesLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.likesLayout().setBackgroundResource(Color.TRANSPARENT);

				layoutComponents.blocksLayout().setBackgroundResource(R.drawable.bgd_tab_pressed);

				list.addFooterView(layoutComponents.footerLayout());
				list.addFooterView(layoutComponents.footerInfoLayout());
				layoutComponents.refreshIcon().startAnimation(refreshAnimation);
				UserLocalizationAdapter ula = new UserLocalizationAdapter(context, R.id.lblTitle, new ArrayList<App>(), UserLocalizationAdapter.BLOCK, user, currentLocation);

				list.setAdapter(ula);

				// Block request
				Thread blockRequestThread = new Thread(new BlockRequestThread(user.getId()).getThread());
				Message.obtain(messageHandler, Constants.SYNESTH_USER_BLOCKS_ID, null).sendToTarget();
				blockRequestThread.start();
			}
		});
	}

	private void pinUpsLayoutBehaviour() {
		this.layoutComponents.pinUpsLayout().setBackgroundResource(R.drawable.bgd_tab_pressed);

		this.layoutComponents.pinUpsLayout().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Obtain blocked items data
				list.removeFooterView(layoutComponents.footerInfoLayout());
				layoutComponents.blocksLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.pinUpsLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.ratesLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.likesLayout().setBackgroundResource(Color.TRANSPARENT);

				layoutComponents.pinUpsLayout().setBackgroundResource(R.drawable.bgd_tab_pressed);

				list.addFooterView(layoutComponents.footerLayout());
				list.addFooterView(layoutComponents.footerInfoLayout());
				layoutComponents.refreshIcon().startAnimation(refreshAnimation);

				UserLocalizationAdapter ula = new UserLocalizationAdapter(context, R.id.lblTitle, new ArrayList<App>(), UserLocalizationAdapter.BLOCK, user, currentLocation);
				list.setAdapter(ula);
				
				// User pin-ups request thread
				Thread userPinUpsRequestThread = new Thread(new UserPinUpsRequestThread(messageHandler, user.getId()).getThread());
				userPinUpsRequestThread.start();
			}
		});
	}

	private void likesLayoutBehaviour() {
		this.layoutComponents.likesLayout().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Obtain blocked items data
				list.removeFooterView(layoutComponents.footerInfoLayout());
				layoutComponents.blocksLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.pinUpsLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.ratesLayout().setBackgroundResource(Color.TRANSPARENT);
				layoutComponents.likesLayout().setBackgroundResource(Color.TRANSPARENT);

				layoutComponents.likesLayout().setBackgroundResource(R.drawable.bgd_tab_pressed);

				list.addFooterView(layoutComponents.footerLayout());
				list.addFooterView(layoutComponents.footerInfoLayout());
				layoutComponents.refreshIcon().startAnimation(refreshAnimation);

				UserLocalizationAdapter ula = new UserLocalizationAdapter(context, R.id.lblTitle, new ArrayList<App>(), UserLocalizationAdapter.BLOCK, user, currentLocation);
				list.setAdapter(ula);

				// User-likes request
				Thread userLikesRequestThread = new Thread(new UserLikesRequestThread(user.getId()).getThread());
				Message.obtain(messageHandler, Constants.SYNESTH_USER_LIKES_ID, null).sendToTarget();
				userLikesRequestThread.start();
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
					JsonParser jp = new JsonParser();

					ArrayList<App> locs = jp.SimpleParseLocalizations(response);

					UserLocalizationAdapter ula = new UserLocalizationAdapter(context, R.id.lblTitle, locs, UserLocalizationAdapter.RATE, user, currentLocation);

					list.setAdapter(ula);

				} catch (Exception exc) {
					Log.d(this.getClass().getSimpleName(), "Get Rates", exc);
				}
			}
		});

	}
}
