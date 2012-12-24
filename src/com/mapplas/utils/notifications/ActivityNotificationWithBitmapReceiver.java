package com.mapplas.utils.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mapplas.model.Constants;

public class ActivityNotificationWithBitmapReceiver extends BroadcastReceiver {

	private ActivityNotificationWithBitmapObserver listener;

	public ActivityNotificationWithBitmapReceiver(ActivityNotificationWithBitmapObserver listener) {
		this.listener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String intentExtra;
		if(intent.hasExtra(Constants.MAPPLAS_EXTRA_BITMAP)) {
			intentExtra = intent.getStringExtra(Constants.MAPPLAS_EXTRA_BITMAP);
		}
		else {
			intentExtra = "";
		}
		
		int intentExtraPosition = -1;
		if(intent.hasExtra(Constants.MAPPLAS_EXTRA_BITMAP_POSITION)) {
			intentExtraPosition = intent.getIntExtra(Constants.MAPPLAS_EXTRA_BITMAP_POSITION, intentExtraPosition);
		}

		this.listener.update(intentExtra, intentExtraPosition);
	}

}
