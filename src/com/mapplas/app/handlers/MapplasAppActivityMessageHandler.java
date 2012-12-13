package com.mapplas.app.handlers;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.AwesomeListView;
import com.mapplas.app.LocalizationAdapter;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;

public class MapplasAppActivityMessageHandler {

	private TextView listViewHeaderStatusMessage;

	private boolean isSplashActive;

	private SuperModel model;

	private LocalizationAdapter listViewAdapter;

	private AwesomeListView listView;
	
	private List<ApplicationInfo> applicationList;

	public MapplasAppActivityMessageHandler(TextView listViewHeaderStatusMessage, boolean isSplashActive, SuperModel model, LocalizationAdapter listViewAdapter, AwesomeListView listView, List<ApplicationInfo> applicationList) {
		this.listViewHeaderStatusMessage = listViewHeaderStatusMessage;
		this.isSplashActive = isSplashActive;
		this.model = model;
		this.listViewAdapter = listViewAdapter;
		this.listView = listView;
		this.applicationList = applicationList;
	}

	@SuppressLint("HandlerLeak")
	public Handler getHandler() {

		final RelativeLayout mainLayout = (RelativeLayout)listViewHeaderStatusMessage.findViewById(R.id.layoutMain);
		final LinearLayout mainScreenContentLayout = (LinearLayout)listViewHeaderStatusMessage.findViewById(R.id.lytContent);
		final LinearLayout splashLayout = (LinearLayout)listViewHeaderStatusMessage.findViewById(R.id.id_splash);
		final Button notificationsButton = (Button)listViewHeaderStatusMessage.findViewById(R.id.btnNotifications);

		return new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case Constants.SYNESTH_MAIN_STATUS_ID:
						if(listViewHeaderStatusMessage != null) {
							listViewHeaderStatusMessage.setText((String)msg.obj);
						}
						break;

					case Constants.SYNESTH_MAIN_LIST_ID:

						if(isSplashActive) {
							final Animation fadeOutAnimation = new AlphaAnimation(1, 0);
							fadeOutAnimation.setInterpolator(new AccelerateInterpolator()); // and
							// this
							fadeOutAnimation.setStartOffset(0);
							fadeOutAnimation.setDuration(500);

							final Animation fadeInAnimation = new AlphaAnimation(0, 1);
							fadeInAnimation.setInterpolator(new AccelerateInterpolator()); // and
							// this
							fadeInAnimation.setStartOffset(0);
							fadeInAnimation.setDuration(500);

							fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationRepeat(Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									mainLayout.removeView(splashLayout);
									mainScreenContentLayout.startAnimation(fadeInAnimation);
								}
							});

							mainLayout.startAnimation(fadeOutAnimation);
							isSplashActive = false;
						}

						if(listViewAdapter != null) {
							listViewAdapter.clear();

							refreshLocalizations();
							listViewAdapter.notifyDataSetChanged();

							for(int i = 0; i < model.localizations.size(); i++) {
								ApplicationInfo ai = findApplicationInfo(model.localizations.get(i).getAppName());
								if(ai != null) {
									model.localizations.get(i).setInternalApplicationInfo(ai);
								}
							}

							if(model.notifications.size() > 0) {
								notificationsButton.setText(model.notifications.size() + "");
								notificationsButton.setBackgroundResource(R.drawable.menu_notifications_number_button);
							}
							else {
								notificationsButton.setText("");
								notificationsButton.setBackgroundResource(R.drawable.menu_notifications_button);
							}

							//
							refreshLocalizations();

							for(int i = 0; i < model.localizations.size(); i++) {
								ApplicationInfo appInfo = findApplicationInfo(model.localizations.get(i).getAppName());
								if(appInfo != null) {
									model.localizations.get(i).setInternalApplicationInfo(appInfo);
								}
							}

							listViewAdapter.notifyDataSetChanged();

							listView.FinishRefreshing();
							// (SynesthActivity.mPullToRefreshListView).onRefreshComplete();
						}
						break;
				}
			}
		};
	}

	private void refreshLocalizations() {
		for(int i = 0; i < this.model.localizations.size(); i++) {
			this.listViewAdapter.add(this.model.localizations.get(i));
		}
	}
	
	private ApplicationInfo findApplicationInfo(String id) {
		ApplicationInfo ret = null;

		for(int i = 0; i < this.applicationList.size(); i++) {
			ApplicationInfo ai = this.applicationList.get(i);
			if(id.contentEquals(ai.packageName)) {
				ret = this.applicationList.get(i);
			}
		}
		return ret;
	}

}
