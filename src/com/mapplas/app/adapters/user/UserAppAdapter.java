package com.mapplas.app.adapters.user;

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

import com.mapplas.app.activities.UserForm;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.app.async_tasks.LoadImageTask;
import com.mapplas.app.async_tasks.TaskAsyncExecuter;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.User;
import com.mapplas.utils.cache.CacheFolderFactory;
import com.mapplas.utils.cache.ImageFileManager;
import com.mapplas.utils.network.requests.BlockRequestThread;
import com.mapplas.utils.network.requests.PinRequestThread;

public class UserAppAdapter extends ArrayAdapter<App> {

	public static final int BLOCK = 0;

	public static final int FAVOURITE = 1;

	public static final int RATE = 2;

	public static final int PINUP = 3;

	private int mType = 0;

	private ArrayList<App> items;

	private Context context = null;

	private User user = null;

	private boolean showEmptyMessage;

	private static Semaphore mSemaphore = new Semaphore(1);

	private static App mBlockLoc = null;

	final Animation fadeOutAnimation = new AlphaAnimation(1, 0);

	/* Message Handler */
	static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			try {
				UserAppAdapter.mSemaphore.acquire();
				switch (msg.what) {

					case Constants.SYNESTH_ROWLOC_IMAGE_ID:

						// String strUrl = (String)((Object[])msg.obj)[0];
						ImageView iv = (ImageView)((Object[])msg.obj)[1];
						// App o = (App)((Object[])msg.obj)[2];
						Bitmap bmp = (Bitmap)((Object[])msg.obj)[3];

						if(bmp != null && iv != null) {
							iv.setImageBitmap(bmp);
							iv.invalidate();
						}

						break;
				}
				UserAppAdapter.mSemaphore.release();
			} catch (Exception e) {
				Log.i(this.getClass().getSimpleName(), "handleMessage: " + e);
			}
		}
	};

	public UserAppAdapter(Context context, int textViewResourceId, ArrayList<App> items, int type, User user, boolean showEmptyMessage) {
		super(context, textViewResourceId, items);

		this.context = context;
		this.items = items;
		this.mType = type;
		this.user = user;
		this.showEmptyMessage = showEmptyMessage;

		this.fadeOutAnimation.setInterpolator(new AccelerateInterpolator()); // and
		// this
		this.fadeOutAnimation.setStartOffset(0);
		this.fadeOutAnimation.setDuration(500);
		this.fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(UserAppAdapter.mBlockLoc != null) {
					UserAppAdapter.this.remove(UserAppAdapter.mBlockLoc);
				}
			}
		});
	}

	@Override
	public int getCount() {
		int count = 1;
		if(this.items.size() > 0) {
			count = this.items.size();
		}
		return count;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(this.items.size() == 0) {
			view = inflater.inflate(R.layout.profile_empty_list_header, null);
			view.setVisibility(View.VISIBLE);
			TextView text = (TextView)view.findViewById(R.id.user_form_empty_list_text);
			switch (this.mType) {
				case BLOCK:
					text.setText(R.string.profile_empty_block_list);
					break;

				case PINUP:
					text.setText(R.string.profile_empty_pinup_list);
					break;
			}
			if(!this.showEmptyMessage) {
				view.setVisibility(View.GONE);
			}
		}
		else {
			if(view == null) {
				switch (this.mType) {
					case BLOCK:
						view = inflater.inflate(R.layout.row_blocks, null);
						break;

					case PINUP:
						view = inflater.inflate(R.layout.row_pinup, null);
						
						final TextView pinnedLocation = (TextView)view.findViewById(R.id.lblLocation);
						pinnedLocation.setText(items.get(position).getAddress());
						
						break;
				}
			}

			final View innerView = view;

			final App app = items.get(position);

			if(app != null) {
				view.setTag(position);

				final TextView lblTitle = (TextView)view.findViewById(R.id.lblTitle);
				final ImageView btnAction = (ImageView)view.findViewById(R.id.btnAction);
				final ImageView ivLogo = (ImageView)view.findViewById(R.id.imgLogo);

				// Set app logo
				ImageFileManager imageFileManager = new ImageFileManager();
				String logoUrl = app.getAppLogo();
				if(!logoUrl.equals("")) {
					if(imageFileManager.exists(new CacheFolderFactory(this.context).create(), logoUrl)) {
						ivLogo.setImageBitmap(imageFileManager.load(new CacheFolderFactory(this.context).create(), logoUrl));
					}
					else {
						TaskAsyncExecuter imageRequest = new TaskAsyncExecuter(new LoadImageTask(this.context, logoUrl, ivLogo, imageFileManager));
						imageRequest.execute();
					}
				}
				else {
					ivLogo.setImageResource(R.drawable.ic_template);
				}

				lblTitle.setTypeface(((MapplasApplication)getContext().getApplicationContext()).getTypeFace());
				lblTitle.setText(app.getName());
				
				btnAction.setTag(app);
				btnAction.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						final App anonLoc = (App)v.getTag();
						innerView.startAnimation(UserAppAdapter.this.fadeOutAnimation);

						UserAppAdapter.this.fadeOutAnimation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								items.remove(anonLoc);
								UserAppAdapter.this.notifyDataSetChanged();

								String uid = String.valueOf(user.getId());
								Thread activityRequestThread = null;

								switch (UserAppAdapter.this.mType) {
									case BLOCK:
//										activityRequestThread = new Thread(new ActivityRequestThread(currentLocation, anonLoc, user, Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_UNBLOCK).getThread());
//										activityRequestThread.start();

										Thread blockRequestThread = new Thread(new BlockRequestThread(Constants.MAPPLAS_ACTIVITY_LIKE_REQUEST_UNBLOCK, anonLoc, uid).getThread());
										blockRequestThread.start();

										// Remove blocked app from blocked list
										user.blockedApps().remove(anonLoc);
										
										// Add unblocked object to model list
										UserForm.appOrderedList.add(anonLoc);
										
										// Set something changed to true
										UserForm.somethingChanged = true;

										break;
										
									case PINUP:
//										activityRequestThread = new Thread(new ActivityRequestThread(currentLocation, anonLoc, user, Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_UNPIN).getThread());
//										activityRequestThread.start();

										Thread pinRequestThread = new Thread(new PinRequestThread(Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_UNPIN, anonLoc, uid, null, "").getThread());
										pinRequestThread.start();

										// Remove pinned app from pinned list
										user.pinnedApps().remove(anonLoc);
										
										// Set app object as pinned or unpinned
										// Pin/unpin app
										boolean found = false;
										int i = 0;
										while (!found && i < UserForm.appOrderedList.size()) {
											App currentApp = UserForm.appOrderedList.get(i);
											if(currentApp.getId() == anonLoc.getId()) {
//												currentApp.setAuxPin(!currentApp.isAuxPin());
												found = true;
											}
											i++;
										}
										
										// Set something changed to true
										UserForm.somethingChanged = true;

										break;
								}
							}
						});

					}

				});
			}
		}

		return view;
	}
}
