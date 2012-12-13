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
import com.mapplas.model.Constants;
import com.mapplas.model.JsonParser;
import com.mapplas.model.Localization;
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

	public UserFormDynamicSublistsPresenter(UserFormLayoutComponents layoutComponents, ListView list, Context context, User user, Handler messageHandler, Animation refreshAnimation) {
		this.layoutComponents = layoutComponents;
		this.list = list;
		this.context = context;
		this.user = user;
		this.messageHandler = messageHandler;
		this.refreshAnimation = refreshAnimation;
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
				UserLocalizationAdapter ula = new UserLocalizationAdapter(context, R.id.lblTitle, new ArrayList<Localization>(), UserLocalizationAdapter.BLOCK);

				list.setAdapter(ula);

				try {
					Thread th = new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								NetRequests.UserBlocksRequest(String.valueOf(user.getId()));
								// mCurrentResponse =
								// NetRequests.UserBlocksRequest(String.valueOf(user.getId()));
								Message.obtain(messageHandler, Constants.SYNESTH_USER_BLOCKS_ID, null).sendToTarget();

							} catch (Exception exc) {
								Log.d(this.getClass().getSimpleName(), "Get Blocks", exc);
							}

						}
					});
					th.start();
				} catch (Exception exc) {
					Log.i(getClass().getSimpleName(), "Action Get Blocks: " + exc);
				}
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

				UserLocalizationAdapter ula = new UserLocalizationAdapter(context, R.id.lblTitle, new ArrayList<Localization>(), UserLocalizationAdapter.BLOCK);
				list.setAdapter(ula);

				try {
					Thread th = new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								NetRequests.UserPinUpsRequest(user.getId() + "");
								// mCurrentResponse =
								// NetRequests.UserPinUpsRequest(MapplasActivity.GetModel().currentUser.getId()
								// + "");
								Message.obtain(messageHandler, Constants.SYNESTH_USER_PINUPS_ID, null).sendToTarget();

							} catch (Exception exc) {
								Log.d(this.getClass().getSimpleName(), "Get PinUps", exc);
							}
						}
					});
					th.start();
				} catch (Exception exc) {
					Log.i(getClass().getSimpleName(), "Action Get PinUps: " + exc);
				}
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

				UserLocalizationAdapter ula = new UserLocalizationAdapter(context, R.id.lblTitle, new ArrayList<Localization>(), UserLocalizationAdapter.BLOCK);
				list.setAdapter(ula);

				try {
					Thread th = new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								// mCurrentResponse =
								// NetRequests.UserLikesRequest(user.getId() +
								// "");
								NetRequests.UserLikesRequest(user.getId() + "");
								Message.obtain(messageHandler, Constants.SYNESTH_USER_LIKES_ID, null).sendToTarget();

							} catch (Exception exc) {
								Log.d(this.getClass().getSimpleName(), "Get Likes", exc);
							}

						}
					});
					th.start();
				} catch (Exception exc) {
					Log.i(getClass().getSimpleName(), "Action Get Likes: " + exc);
				}
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

					ArrayList<Localization> locs = jp.SimpleParseLocalizations(response);

					UserLocalizationAdapter ula = new UserLocalizationAdapter(context, R.id.lblTitle, locs, UserLocalizationAdapter.RATE);

					list.setAdapter(ula);

				} catch (Exception exc) {
					Log.d(this.getClass().getSimpleName(), "Get Rates", exc);
				}
			}
		});

	}
}
