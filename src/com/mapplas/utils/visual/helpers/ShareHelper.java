package com.mapplas.utils.visual.helpers;

import android.content.Context;
import android.content.Intent;
import app.mapplas.com.R;

import com.mapplas.model.App;

public class ShareHelper {

	public Intent getSharingIntent(Context context, App app) {
		
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
				
		String shareBody = context.getString(R.string.share_message_part1) + " " + new PlayStoreLinkCreator().createPlayStoreExternalLinkForApp(app.getId()) + " " + context.getString(R.string.share_message_part2);
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

		return sharingIntent;
	}

}
