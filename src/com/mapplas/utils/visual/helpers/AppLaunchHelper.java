package com.mapplas.utils.visual.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import app.mapplas.com.R;

import com.mapplas.app.activities.WebViewActivity;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.User;
import com.mapplas.utils.network.async_tasks.UserPlayStoreInteractionTask;

public class AppLaunchHelper {

	private Context context;

	private Button button;

	private App app;

	private User user;

	private Location location;

	public AppLaunchHelper(Context context, Button button, App app, User user, Location location) {
		this.context = context;
		this.button = button;
		this.app = app;
		this.user = user;
		this.location = location;
	}

	public void help() {
		if(this.button != null) {
			
			// APPLICATION TYPE APP
			if(this.app.getAppType().equalsIgnoreCase(Constants.MAPPLAS_APPLICATION_TYPE_ANDROID_APPLICATION)) {
				
				// LAUNCH APP
				if(this.app.getInternalApplicationInfo() != null) {
					this.button.setBackgroundResource(R.drawable.badge_launch);
					this.button.setText("");
				}
				else {
					// INSTALL APP
					if(app.getAppPrice().equals("Free") || app.getAppPrice().equals("Gratis")) {
						this.button.setBackgroundResource(R.drawable.badge_free);
						this.button.setText(R.string.free);
					}
					else {
						this.button.setBackgroundResource(R.drawable.badge_price);
						this.button.setText(app.getAppPrice());
					}
				}
			}
			else {
				// HTML5 TYPE APP
				this.button.setBackgroundResource(R.drawable.badge_html5);
				this.button.setText("");
			}

			this.button.setTag(this.app);
			this.button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					final App anonLoc = (App)(v.getTag());
					if(anonLoc != null) {

						String strUrl = new PlayStoreLinkCreator().createLinkForApp(anonLoc.getId());

						if(anonLoc.getInternalApplicationInfo() != null) {
							ApplicationInfo packageInfo = anonLoc.getInternalApplicationInfo();
							String package_name = packageInfo.packageName;
							if(package_name != null) {
								Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(package_name);
								if(appIntent != null) {
									context.startActivity(appIntent);
								}
								else {
									Toast.makeText(context, context.getString(R.string.problem_launching_app), Toast.LENGTH_SHORT).show();
								}
							}
							else {
								Toast.makeText(context, context.getString(R.string.problem_launching_app), Toast.LENGTH_SHORT).show();
							}
						}
						else {
							// USER-PLAY STORE INTERACTION CONNECTOR
							if(app.getAppType().equals(Constants.MAPPLAS_APPLICATION_TYPE_ANDROID_APPLICATION)) {
								Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl));
								context.startActivity(browserIntent);
								
								new UserPlayStoreInteractionTask(app.getId(), user.getId(), location).execute();
							}
							else {
								Intent webViewIntent = new Intent(context, WebViewActivity.class);
								webViewIntent.putExtra(Constants.APP_DEV_URL_INTENT_DATA, anonLoc.getId());
								context.startActivity(webViewIntent);
							}
						}
					}
				}
			});
		}
	}
}
