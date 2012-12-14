package com.mapplas.app;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.User;
import com.mapplas.utils.DrawableBackgroundDownloader;
import com.mapplas.utils.NetRequests;

public class UserLocalizationAdapter extends ArrayAdapter<App> {

	public static final int BLOCK = 0;

	public static final int FAVOURITE = 1;

	public static final int RATE = 2;

	public static final int PINUP = 3;

	private int mType = 0;

	private ArrayList<App> items;

	private Context context = null;
	
	private User user = null;
	
	private String currentLocation = "";

	private static Semaphore mSemaphore = new Semaphore(1);

	private static App mRateLoc = null;

	private static App mBlockLoc = null;

	final Animation fadeOutAnimation = new AlphaAnimation(1, 0);

	/* Message Handler */
	static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			try {
				UserLocalizationAdapter.mSemaphore.acquire();
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
				UserLocalizationAdapter.mSemaphore.release();
			} catch (Exception e) {
				Log.i(this.getClass().getSimpleName(), "handleMessage: " + e);
			}
		}
	};

	public UserLocalizationAdapter(Context context, int textViewResourceId, ArrayList<App> items, int type, User user, String currentLocation) {
		super(context, textViewResourceId, items);

		this.context = context;
		this.items = items;
		this.mType = type;
		this.user = user;
		this.currentLocation = currentLocation;

		fadeOutAnimation.setInterpolator(new AccelerateInterpolator()); // and
																		// this
		fadeOutAnimation.setStartOffset(0);
		fadeOutAnimation.setDuration(500);
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
				if(UserLocalizationAdapter.mBlockLoc != null) {
					UserLocalizationAdapter.this.remove(UserLocalizationAdapter.mBlockLoc);
				}
			}
		});
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		boolean isNewView = false;
		View v = convertView;

		if(v == null) {
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			switch (this.mType) {
				case BLOCK:
					v = vi.inflate(R.layout.row_blocks, null);
					break;

				case FAVOURITE:
					v = vi.inflate(R.layout.row_likes, null);
					break;

				case PINUP:
					v = vi.inflate(R.layout.row_pinup, null);
					break;

				case RATE:
					v = vi.inflate(R.layout.row_blocks, null);
					break;
			}

			isNewView = true;
		}
		else {
			isNewView = false;
		}

		final View innerView = v;

		final App o = items.get(position);

		if(o != null) {
			v.setTag(position);

			final TextView lblTitle = (TextView)v.findViewById(R.id.lblTitle);
			// final TextView lblDescription = (TextView)
			// v.findViewById(R.id.lblDescription);
			final ImageView btnAction = (ImageView)v.findViewById(R.id.btnAction);

			final ImageView ivLogo = (ImageView)v.findViewById(R.id.imgLogo);

			String strUrl = o.getAppLogo();
			if(strUrl != "") {
				new DrawableBackgroundDownloader().loadDrawable(strUrl, ivLogo, this.context.getResources().getDrawable(R.drawable.ic_refresh));
			}
			else {
				ivLogo.setImageResource(R.drawable.ic_refresh);
			}

			lblTitle.setTypeface(((MapplasApplication)getContext().getApplicationContext()).getTypeFace());
			// lblDescription.setTypeface(SynesthActivity.getTypeFace());

			lblTitle.setText(o.getName());

			btnAction.setTag(o);
			btnAction.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final App anonLoc = (App)v.getTag();
					innerView.startAnimation(UserLocalizationAdapter.this.fadeOutAnimation);

					UserLocalizationAdapter.this.fadeOutAnimation.setAnimationListener(new AnimationListener() {

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
							items.remove(anonLoc);
							UserLocalizationAdapter.this.notifyDataSetChanged();

							final String uid = user.getId() + "";

							switch (UserLocalizationAdapter.this.mType) {
								case BLOCK:
									try {

										Thread th = new Thread(new Runnable() {

											@Override
											public void run() {
												try {
													NetRequests.LikeRequest("mr", Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, anonLoc.getId() + "", uid);
													NetRequests.ActivityRequest(currentLocation, "unblock", String.valueOf(anonLoc.getId()), String.valueOf(user.getId()));
												} catch (Exception e) {
													Log.i(getClass().getSimpleName(), "Thread Action unblock: " + e);
												}
											}
										});
										th.start();

									} catch (Exception exc) {
										Log.i(getClass().getSimpleName(), "Action unblock: " + exc);
									}
									break;

								case FAVOURITE:
									try {
										Thread th = new Thread(new Runnable() {

											@Override
											public void run() {
												try {
													NetRequests.LikeRequest("pr", Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, anonLoc.getId() + "", uid);
													NetRequests.ActivityRequest(currentLocation, "unfavourite", String.valueOf(anonLoc.getId()), String.valueOf(user.getId()));
												} catch (Exception e) {
													Log.i(getClass().getSimpleName(), "Thread Action favourite: " + e);
												}
											}
										});
										th.start();

									} catch (Exception exc) {
										Log.i(getClass().getSimpleName(), "Action Favourite: " + exc);
									}
									break;

								case PINUP:
									try {
										Thread th = new Thread(new Runnable() {

											@Override
											public void run() {
												try {
													NetRequests.PinRequest("unpin", Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, anonLoc.getId() + "", uid);
													NetRequests.ActivityRequest(currentLocation, "unpin", String.valueOf(anonLoc.getId()), String.valueOf(user.getId()));
												} catch (Exception e) {
													Log.i(getClass().getSimpleName(), "Thread Action PinUp: " + e);
												}
											}
										});
										th.start();

									} catch (Exception exc) {
										Log.i(getClass().getSimpleName(), "Action PinUp: " + exc);
									}
									break;

								case RATE:
									try {

										Thread th = new Thread(new Runnable() {

											@Override
											public void run() {
												try {
													NetRequests.UnrateRequest(anonLoc.getId() + "", uid);
													NetRequests.ActivityRequest(currentLocation, "unrate", String.valueOf(anonLoc.getId()), String.valueOf(user.getId()));
												} catch (Exception e) {
													Log.i(getClass().getSimpleName(), "Thread Action unrate: " + e);
												}
											}
										});
										th.start();

									} catch (Exception exc) {
										Log.i(getClass().getSimpleName(), "Action rate: " + exc);
									}
									break;
							}
						}
					});

				}
			});
		}

		return v;
	}

}
