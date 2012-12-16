package com.mapplas.app.handlers.adapters;

import java.util.concurrent.Semaphore;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.mapplas.model.App;
import com.mapplas.model.Constants;

public class AppAdapterMessageHandler {

	private static Semaphore mSemaphore = new Semaphore(1);

	@SuppressLint("HandlerLeak")
	public Handler getHandler() {
		return new Handler() {

			@Override
			public void handleMessage(Message msg) {

				try {
					mSemaphore.acquire();
					switch (msg.what) {

						case Constants.SYNESTH_ROWLOC_IMAGE_ID:

							String strUrl = (String)((Object[])msg.obj)[0];
							ImageView iv = (ImageView)((Object[])msg.obj)[1];
							App o = (App)((Object[])msg.obj)[2];
							Bitmap bmp = (Bitmap)((Object[])msg.obj)[3];

							if(bmp != null && iv != null) {
								iv.setImageBitmap(bmp);
								iv.invalidate();
							}

							break;
					}
					mSemaphore.release();
				} catch (Exception e) {
					Log.i(this.getClass().getSimpleName(), "handleMessage: " + e);
				}
			}
		};
	}
}
