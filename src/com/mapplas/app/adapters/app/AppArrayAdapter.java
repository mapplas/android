package com.mapplas.app.adapters.app;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;
import app.mapplas.com.R;

import com.mapplas.app.activities.AppDetail;
import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.app.activities.WebViewActivity;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.app.async_tasks.LoadImageTask;
import com.mapplas.app.async_tasks.TaskAsyncExecuter;
import com.mapplas.app.threads.ActivityRequestThread;
import com.mapplas.app.threads.LikeRequestThread;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
import com.mapplas.utils.cache.CacheFolderFactory;
import com.mapplas.utils.cache.ImageFileManager;
import com.mapplas.utils.network.requests.PinRequestThread;
import com.mapplas.utils.share.ShareHelper;
import com.mapplas.utils.static_intents.SuperModelSingleton;
import com.mapplas.utils.third_party.RefreshableListView;
import com.mapplas.utils.view_holder.AppViewHolder;

public class AppArrayAdapter extends ArrayAdapter<App> {

	public Context context;

	private ArrayList<App> items;

	private Animation animFlipInNext = null;

	private Animation animFlipOutNext = null;

	private Animation animFlipInPrevious = null;

	private Animation animFlipOutPrevious = null;

	private RefreshableListView list = null;

	protected SuperModel model = null;

	public User user = null;

	private static App mBlockApp = null;

	private Animation fadeOutAnimation = null;

	public AppArrayAdapter(Context context, int layout, int textViewResourceId, ArrayList<App> items, RefreshableListView list, SuperModel model) {
		super(context, layout, textViewResourceId, items);

		this.context = context;
		this.items = items;
		this.list = list;
		this.model = model;

		this.user = this.model.currentUser();

		this.initializeAnimations();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppViewHolder cellHolder;

		App app = this.items.get(position);

		if(convertView == null) {
			cellHolder = new AppViewHolder();

			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.rowloc, null);

			cellHolder.title = (TextView)convertView.findViewById(R.id.lblTitle);

			cellHolder.pinUp = (TextView)convertView.findViewById(R.id.lblPinUp);
			cellHolder.rate = (TextView)convertView.findViewById(R.id.lblRate);
			cellHolder.share = (TextView)convertView.findViewById(R.id.lblShare);
			cellHolder.block = (TextView)convertView.findViewById(R.id.lblBlock);

			cellHolder.pinUpImg = (ImageView)convertView.findViewById(R.id.btnPinUp);
			cellHolder.rateImg = (ImageView)convertView.findViewById(R.id.btnRate);
			cellHolder.shareImg = (ImageView)convertView.findViewById(R.id.btnShare);
			cellHolder.blockImg = (ImageView)convertView.findViewById(R.id.btnBlock);

			cellHolder.pinUpLayout = (LinearLayout)convertView.findViewById(R.id.lytPinup);
			cellHolder.rateLayout = (LinearLayout)convertView.findViewById(R.id.lytRate);
			cellHolder.shareLayout = (LinearLayout)convertView.findViewById(R.id.lytShare);
			cellHolder.blockLayout = (LinearLayout)convertView.findViewById(R.id.lytBlock);

			cellHolder.buttonStart = (Button)convertView.findViewById(R.id.btnStart);

			cellHolder.viewFlipper = (ViewFlipper)convertView.findViewById(R.id.vfRowLoc);
			cellHolder.logo = (ImageView)convertView.findViewById(R.id.imgLogo);
			cellHolder.logoRoundCorner = (ImageView)convertView.findViewById(R.id.imgRonundC);

			cellHolder.rowUnpressed = (LinearLayout)convertView.findViewById(R.id.id_rowloc_unpressed);

			cellHolder.ratingBar = (RatingBar)convertView.findViewById(R.id.rbRating);
			cellHolder.ratingText = (TextView)convertView.findViewById(R.id.lblRating);

			this.initializeCellHolder(app, cellHolder);

			this.initializeStartButton(app, cellHolder.buttonStart);

			this.initializeLogo(app, cellHolder.logo, cellHolder.viewFlipper);
			this.initializeRowUnpressed(app, cellHolder.rowUnpressed, position);
			this.initializeLogoBackgroundPinImage(app, cellHolder.logoRoundCorner);
			this.initializeRating(app, cellHolder);

			this.initializeActionLayouts(app, cellHolder, convertView);

			convertView.setTag(cellHolder);
		}
		else {
			cellHolder = (AppViewHolder)convertView.getTag();

			this.initializeCellHolder(app, cellHolder);

			this.initializeStartButton(app, cellHolder.buttonStart);

			this.initializeLogo(app, cellHolder.logo, cellHolder.viewFlipper);
			this.initializeRowUnpressed(app, cellHolder.rowUnpressed, position);
			this.initializeLogoBackgroundPinImage(app, cellHolder.logoRoundCorner);
			this.initializeRating(app, cellHolder);

			this.initializeActionLayouts(app, cellHolder, convertView);
		}

		return convertView;
	}

	private void initializeAnimations() {
		this.fadeOutAnimation = new AlphaAnimation(1, 0);
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
				if(mBlockApp != null) {
					remove(mBlockApp);
				}
			}
		});

		this.animFlipInNext = AnimationUtils.loadAnimation(this.context, R.anim.flipinnext);
		this.animFlipOutNext = AnimationUtils.loadAnimation(this.context, R.anim.flipoutnext);
		this.animFlipInPrevious = AnimationUtils.loadAnimation(this.context, R.anim.flipinprevious);
		this.animFlipOutPrevious = AnimationUtils.loadAnimation(this.context, R.anim.flipoutprevious);
	}

	private void initializeCellHolder(App app, AppViewHolder cellHolder) {
		// Set typefaces
		Typeface normalTypeface = ((MapplasApplication)this.context.getApplicationContext()).getTypeFace();
		cellHolder.title.setTypeface(normalTypeface);
		cellHolder.title.setText(app.getName());
		cellHolder.pinUp.setTypeface(normalTypeface);
		cellHolder.rate.setTypeface(normalTypeface);
		cellHolder.share.setTypeface(normalTypeface);
		cellHolder.block.setTypeface(normalTypeface);

		cellHolder.buttonStart.setTypeface(normalTypeface);
		cellHolder.ratingText.setTypeface(normalTypeface);

		// Initialize viewFlipper
		cellHolder.viewFlipper.setInAnimation(null);
		cellHolder.viewFlipper.setOutAnimation(null);
		cellHolder.viewFlipper.setDisplayedChild(0);
	}

	private void initializeActionLayouts(App app, AppViewHolder cellHolder, View convertView) {
		this.initializePinUpLayout(app, cellHolder);
		this.initializeRateLayout(app, cellHolder);
		this.initializeBlockLayout(app, cellHolder, convertView);
		this.initializeShareLayout(app, cellHolder);
	}

	private void initializeRowUnpressed(final App app, LinearLayout layout, int position) {
		layout.setTag(position);
		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, AppDetail.class);
				intent.putExtra(Constants.MAPPLAS_DETAIL_APP, app);
				SuperModelSingleton.model = model;
				((MapplasActivity)context).startActivityForResult(intent, Constants.SYNESTH_DETAILS_ID);
			}
		});
	}

	private void initializeRating(App app, AppViewHolder cellHolder) {
		// cellHolder.ratingBar.setRating(app.getAuxTotalRate());

		// if(app.getAuxTotalRate() == 0) {
		// cellHolder.ratingText.setText(R.string.unRated);
		// }
		// else {
		// int auxCase = (int)Math.ceil(app.getAuxTotalRate());
		// switch (auxCase) {
		// case 1:
		// cellHolder.ratingText.setText(R.string.poor);
		// break;
		//
		// case 2:
		// cellHolder.ratingText.setText(R.string.belowAvg);
		// break;
		//
		// case 3:
		// cellHolder.ratingText.setText(R.string.average);
		// break;
		//
		// case 4:
		// cellHolder.ratingText.setText(R.string.aboveAvg);
		// break;
		//
		// case 5:
		// cellHolder.ratingText.setText(R.string.excellent);
		// break;
		// }
		// }
	}

	private void initializeStartButton(final App app, Button buttonStart) {
		if(buttonStart != null) {
			if(app.getAppType().equalsIgnoreCase(Constants.MAPPLAS_APPLICATION_TYPE_ANDROID_APPLICATION)) {
				if(app.getInternalApplicationInfo() != null) {
					// Start
					buttonStart.setBackgroundResource(R.drawable.badge_launch);
					buttonStart.setText("");
				}
				else {
					// Install
					if(app.getAppPrice().equals("Free") || app.getAppPrice().equals("Gratis")) {
						buttonStart.setBackgroundResource(R.drawable.badge_free);
						buttonStart.setText(R.string.free);
					}
					else {
						buttonStart.setBackgroundResource(R.drawable.badge_price);
						buttonStart.setText(app.getAppPrice());
					}
				}
			}
			else {
				// Info
				buttonStart.setBackgroundResource(R.drawable.badge_html5);
				buttonStart.setText("");
			}

			buttonStart.setTag(app);
			buttonStart.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					final App anonLoc = (App)(v.getTag());
					if(anonLoc != null) {

						String strUrl = "market://details?id=" + anonLoc.getId();

						if(anonLoc.getInternalApplicationInfo() != null) {
							ApplicationInfo packageInfo = anonLoc.getInternalApplicationInfo();
							String package_name = packageInfo.packageName;
							Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(package_name);
							context.startActivity(appIntent);
						}
						else {
							if(app.getAppType().equals(Constants.MAPPLAS_APPLICATION_TYPE_ANDROID_APPLICATION)) {
								Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl));
								context.startActivity(browserIntent);
							}
							else {
								Intent webViewIntent = new Intent(context, WebViewActivity.class);
								webViewIntent.putExtra(AppDetail.APP_DEV_URL_INTENT_DATA, anonLoc);
								context.startActivity(webViewIntent);
							}
						}
					}
				}
			});
		}
	}

	private void initializeLogoBackgroundPinImage(App app, ImageView image) {
		if(app.isAuxPin() == 1) {
			image.setBackgroundResource(R.drawable.roundc_pinup_selector);
		}
		else {
			image.setBackgroundResource(R.drawable.roundc_btn_selector);
		}
	}

	private void initializeLogo(App app, ImageView logo, ViewFlipper viewFlipper) {
		logo.setTag(viewFlipper);
		logo.setImageResource(R.drawable.ic_template);

		logo.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ViewFlipper vf = (ViewFlipper)v.getTag();

				if(vf != null) {
					if(vf.indexOfChild(vf.getCurrentView()) == 0) {
						vf.setInAnimation(animFlipInNext);
						vf.setOutAnimation(animFlipOutNext);
						vf.showNext();
					}
					else {
						vf.setInAnimation(animFlipInPrevious);
						vf.setOutAnimation(animFlipOutPrevious);
						vf.showPrevious();
					}
				}
				return false;
			}
		});

		// Load app logo
		ImageFileManager imageFileManager = new ImageFileManager();
		String logoUrl = app.getAppLogo();
		if(!logoUrl.equals("")) {
			if(imageFileManager.exists(new CacheFolderFactory(this.context).create(), logoUrl)) {
				logo.setImageBitmap(imageFileManager.load(new CacheFolderFactory(this.context).create(), logoUrl));
			}
			else {
				TaskAsyncExecuter imageRequest = new TaskAsyncExecuter(new LoadImageTask(this.context, logoUrl, logo, imageFileManager));
				imageRequest.execute();
			}
		}
	}

	private void initializePinUpLayout(final App app, final AppViewHolder cellHolder) {

		cellHolder.pinUpLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(app != null) {
					String auxuid = "0";
					if(user != null) {
						auxuid = String.valueOf(user.getId());
					}

					final String uid = auxuid;

					if(app.isAuxPin() == 1) {
						cellHolder.pinUpImg.setImageResource(R.drawable.action_pin_button);
						cellHolder.logoRoundCorner.setBackgroundResource(R.drawable.roundc_btn_selector);
						cellHolder.pinUp.setText(R.string.pin_up);
					}
					else {
						cellHolder.pinUpImg.setImageResource(R.drawable.action_unpin_button);
						cellHolder.logoRoundCorner.setBackgroundResource(R.drawable.roundc_pinup_selector);
						cellHolder.pinUp.setText(R.string.un_pin_up);
					}

					cellHolder.logoRoundCorner.invalidate();
					v.invalidate();
					list.invalidate();

					String pinRequestConstant = Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_PIN;
					String actionRequestConstant = Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_PIN;
					if(app.isAuxPin() == 1) {
						pinRequestConstant = Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_UNPIN;
						actionRequestConstant = Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_UNPIN;
					}

					// Activity request thread
					Thread activityRequestThread = new Thread(new ActivityRequestThread(model.currentLocation(), app, user, actionRequestConstant).getThread());
					activityRequestThread.start();

					// Pin/unpin request
					Thread pinRequestThread = new Thread(new PinRequestThread(pinRequestConstant, app, uid, model.getLocation(), model.currentDescriptiveGeoLoc()).getThread());
					pinRequestThread.start();

					// Set app object as pinned or unpinned
					// Pin/unpin app
					boolean found = false;
					int i = 0;
					while (!found && i < model.appList().size()) {
						if(model.appList().get(i).equals(app)) {

							if(app.isAuxPin() == 1) {
								model.appList().get(i).setAuxPin(0);
							}
							else {
								model.appList().get(i).setAuxPin(1);
							}

							found = true;
						}
						i++;
					}

					// Update app adapter
					// list.updateAdapter(context, model, new
					// InfiniteScrollManager().getFirstXNumberOfApps(model));
				}

			}
		});
	}

	private void initializeBlockLayout(final App app, AppViewHolder cellHolder, final View convertView) {
		cellHolder.blockLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBlockApp = app;

				AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
				myAlertDialog.setTitle(R.string.block_title);
				myAlertDialog.setMessage(R.string.block_text);
				myAlertDialog.setPositiveButton(R.string.block_accept, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {

						convertView.startAnimation(fadeOutAnimation);

						// Activity request thread
						Thread activityRequestThread = new Thread(new ActivityRequestThread(model.currentLocation(), app, user, Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_BLOCK).getThread());
						activityRequestThread.start();

						String uid = "0";
						if(user != null) {
							uid = String.valueOf(user.getId());
						}

						// Block request thread
						Thread likeRequestThread = new Thread(new LikeRequestThread(Constants.MAPPLAS_ACTIVITY_LIKE_REQUEST_BLOCK, app, uid).getThread());
						likeRequestThread.start();

						// Do unpin also
						if(mBlockApp.isAuxPin() == 1) {
							Thread unpinRequestThread = new Thread(new PinRequestThread(Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_UNPIN, app, uid, model.getLocation(), model.currentDescriptiveGeoLoc()).getThread());
							unpinRequestThread.start();
						}
					}
				});
				myAlertDialog.setNegativeButton(R.string.block_cancel, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						// do something when the Cancel button is clicked
					}
				});

				myAlertDialog.show();
			}
		});
	}

	private void initializeRateLayout(final App app, AppViewHolder cellHolder) {
		cellHolder.rateLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(app != null) {
					// RatingDialog myDialog = new RatingDialog(context, "", new
					// OnReadyListener(user, context, model, app),
					// app.getAuxRate());
					// myDialog.show();

					// Activity request action
					Thread activityRequestThread = new Thread(new ActivityRequestThread(model.currentLocation(), app, user, Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_RATE).getThread());
					activityRequestThread.start();
				}
			}
		});

		if(app.isAuxPin() == 1) {
			cellHolder.pinUpImg.setImageResource(R.drawable.action_unpin_button);
			cellHolder.pinUp.setText(R.string.un_pin_up);
		}
		else {
			cellHolder.pinUpImg.setImageResource(R.drawable.action_pin_button);
			cellHolder.pinUp.setText(R.string.pin_up);
		}
	}

	private void initializeShareLayout(final App app, AppViewHolder cellHolder) {
		cellHolder.shareLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(app != null) {
					context.startActivity(Intent.createChooser(new ShareHelper().getSharingIntent(context, app), context.getString(R.string.share)));

					Thread activityRequestThread = new Thread(new ActivityRequestThread(model.currentLocation(), app, user, Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_SHARE).getThread());
					activityRequestThread.start();
				}

			}
		});
	}

}
