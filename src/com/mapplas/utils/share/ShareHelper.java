package com.mapplas.utils.share;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import app.mapplas.com.R;

import com.mapplas.model.App;

public class ShareHelper {

	public Intent getSharingIntent(Context context, App app) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		// Comprobar que existe y que está instalada la aplicación
		// en el teléfono
		if(this.isAppInstalled("com.whatsapp", context)) {
			sharingIntent.setPackage("com.whatsapp");
		}
		sharingIntent.setType("text/plain");
		
		String shareBody = context.getString(R.string.share_message_part1) + " " + app.getAppUrl() + " " + context.getString(R.string.share_message_part2);
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

		return sharingIntent;
	}

	private boolean isAppInstalled(String uri, Context context) {
		PackageManager pm = context.getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

}
