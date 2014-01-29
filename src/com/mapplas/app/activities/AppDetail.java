package com.mapplas.app.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.ImageAdapter;
import com.mapplas.app.adapters.detail.DetailMoreAppsArrayAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
import com.mapplas.utils.cache.CacheFolderFactory;
import com.mapplas.utils.cache.ImageFileManager;
import com.mapplas.utils.language.LanguageSetter;
import com.mapplas.utils.network.async_tasks.AppDetailTask;
import com.mapplas.utils.network.async_tasks.LoadImageTask;
import com.mapplas.utils.network.async_tasks.TaskAsyncExecuter;
import com.mapplas.utils.network.requests.BlockRequestThread;
import com.mapplas.utils.network.requests.PinRequestThread;
import com.mapplas.utils.network.requests.ShareRequestThread;
import com.mapplas.utils.static_intents.AppChangedSingleton;
import com.mapplas.utils.static_intents.SuperModelSingleton;
import com.mapplas.utils.utils.NumberUtils;
import com.mapplas.utils.visual.custom_views.RobotoTextView;
import com.mapplas.utils.visual.helpers.AppLaunchHelper;
import com.mapplas.utils.visual.helpers.ListViewInsideScrollHeigthHelper;
import com.mapplas.utils.visual.helpers.PlayStoreLinkCreator;
import com.mapplas.utils.visual.helpers.ShareHelper;

public class AppDetail extends LanguageActivity {

	private App app;

	private User user = null;

	private SuperModel model = null;

	private RotateAnimation flipAnimation;

	private RotateAnimation reverseFlipAnimation;

	private DisplayMetrics metrics = new DisplayMetrics();

	private boolean somethingChanged = false;

	private DetailMoreAppsArrayAdapter moreAppsAdapter;

	private ListView moreAppsList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app);

		this.extractDataFromBundle();
		this.requestApplicationDetailInfo();
		this.initializeAnimations();

		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		this.initializeLayoutComponents();
		this.initializeLaunchButtonAndBehaviour();
		this.initializeActionsLayout();
	}

	@Override
	protected void onPause() {
		if(this.somethingChanged) {
			AppChangedSingleton.somethingChanged = true;
			this.somethingChanged = false;
			AppChangedSingleton.changedList = this.model.appList();
		}
		super.onPause();
	}

	/**
	 * 
	 * Private methods
	 * 
	 */
	private void extractDataFromBundle() {
		// Get the id of the app selected
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			if(extras.containsKey(Constants.MAPPLAS_DETAIL_APP)) {
				this.app = (App)extras.getParcelable(Constants.MAPPLAS_DETAIL_APP);
				this.model = SuperModelSingleton.model;
				this.user = this.model.currentUser();
			}
		}
	}

	private void requestApplicationDetailInfo() {
		new AppDetailTask(this, this.app, this).execute();
	}

	public void detailRequestFinishedOk() {
		// Description layout
		this.initDescriptionLayout();

		// Developer layout
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();
		this.manageDeveloperLayout(normalTypeFace);

		// Cargamos la imágenes en la galería
		Gallery gallery = (Gallery)findViewById(R.id.galleryPhotos);
		ImageAdapter imgAdapter = new ImageAdapter(this, gallery);

		for(String photo : this.app.getAuxPhotos()) {
			imgAdapter.mImages.add(photo);
		}
		gallery.setAdapter(imgAdapter);

		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long position) {
				Intent intent = new Intent(AppDetail.this, GalleryActivity.class);
				intent.putExtra(Constants.APP_IMAGES_GALLERY, app.getAuxPhotos());
				intent.putExtra(Constants.APP_IMAGES_GALLERY_INDEX, (int)position);
				startActivity(intent);
			}
		});

		// Init more apps button
		RelativeLayout moreAppsLayout = (RelativeLayout)this.findViewById(R.id.more_from_developer_plus_layout);
		if(this.app.moreFromDeveloperCount() > Constants.NUMBER_OF_RELATED_APPS_TO_SHOW) {
			moreAppsLayout.setVisibility(View.VISIBLE);
			moreAppsLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(AppDetail.this, MoreFromDeveloperActivity.class);
					intent.putExtra(Constants.MORE_FROM_DEVELOPER_APP, app);
					intent.putExtra(Constants.MORE_FROM_DEVELOPER_COUNTRY_CODE, new LanguageSetter(AppDetail.this).getLanguageConstantFromPhone());
					startActivity(intent);
				}
			});
		}
		else {
			moreAppsLayout.setVisibility(View.GONE);
		}

		// Set correct height to listview to scroll ok
		new ListViewInsideScrollHeigthHelper().setListViewHeightBasedOnChildren(this.moreAppsList, this.moreAppsAdapter);
	}

	public void detailRequestFinishedNok() {
		// Detail loading error. Try again.
		new AppDetailTask(this, this.app, this).execute();
	}

	private void initializeAnimations() {
		this.flipAnimation = new RotateAnimation(0, 90, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.flipAnimation.setInterpolator(new LinearInterpolator());
		this.flipAnimation.setDuration(300);
		this.flipAnimation.setFillAfter(true);

		this.reverseFlipAnimation = new RotateAnimation(90, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.reverseFlipAnimation.setInterpolator(new LinearInterpolator());
		this.reverseFlipAnimation.setDuration(300);
		this.reverseFlipAnimation.setFillAfter(true);
	}

	private void initializeLayoutComponents() {
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();

		// Set stars
		float rating = this.app.rating();
		RatingBar rbRating = (RatingBar)findViewById(R.id.rbRating);
		rbRating.setRating(rating);

		RobotoTextView ratingValue = (RobotoTextView)findViewById(R.id.rbRatingValue);
		ratingValue.setText(String.valueOf(NumberUtils.RoundToHalf(rating)));

		// Download application logo
		ImageView appLogo = (ImageView)findViewById(R.id.imgLogo);
		ImageFileManager imageFileManager = new ImageFileManager();

		String logoUrl = this.app.getAppLogo();

		if(!logoUrl.equals("")) {
			if(imageFileManager.exists(new CacheFolderFactory(this).create(), logoUrl)) {
				appLogo.setImageBitmap(imageFileManager.load(new CacheFolderFactory(this).create(), logoUrl));
			}
			else {
				TaskAsyncExecuter imageRequest = new TaskAsyncExecuter(new LoadImageTask(this, logoUrl, appLogo, imageFileManager));
				imageRequest.execute();
			}
		}

		// App detail header text view
		TextView appNameAboveRatingTextView = (TextView)findViewById(R.id.lblTitle);
		appNameAboveRatingTextView.setText(this.app.getName());
		appNameAboveRatingTextView.setTypeface(((MapplasApplication)this.getApplicationContext()).getTypeFace());

		if(appNameAboveRatingTextView.getMeasuredWidth() < appNameAboveRatingTextView.getPaint().measureText(this.app.getName())) {
			appNameAboveRatingTextView.setLines(2);
		}

		// Back button
		Button backButton = (Button)findViewById(R.id.btnBack);
		backButton.setTypeface(normalTypeFace);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// More apps from developer list
		this.moreAppsList = (ListView)this.findViewById(R.id.list);

		this.moreAppsAdapter = new DetailMoreAppsArrayAdapter(this, R.layout.row_more_apps, this.app.moreFromDev());
		this.moreAppsList.setAdapter(this.moreAppsAdapter);

		// Item click listener
		this.moreAppsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				String playStoreLink = new PlayStoreLinkCreator().createLinkForApp(app.moreFromDev().get(position).id());
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink));
				AppDetail.this.startActivity(browserIntent);
			}
		});

	}

	private void manageDeveloperLayout(Typeface normalTypeFace) {

		if(this.app.moreFromDev().size() > 0) {
			// if(!this.app.appDeveloperEmail().equals("") ||
			// !this.app.appDeveloperWeb().equals("") ||
			// this.app.moreFromDev().size() > 0) {

			// Developer layout
			LinearLayout developerLayout = (LinearLayout)findViewById(R.id.lytDeveloper);
			developerLayout.setVisibility(View.VISIBLE);

			// Developer text view
			TextView developerTextView = (TextView)findViewById(R.id.lblDeveloper);
			developerTextView.setTypeface(normalTypeFace);

			// // Developer email
			// RelativeLayout emailLayout =
			// (RelativeLayout)this.findViewById(R.id.developer_email_layout);
			// if(!this.app.appDeveloperEmail().equals("")) {
			// emailLayout.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// v.setBackgroundResource(R.color.pinned_app_cell_background_color);
			// Intent i = new Intent(Intent.ACTION_SEND);
			// i.setType("text/html"); // use from live device
			// i.putExtra(Intent.EXTRA_EMAIL, new String[] {
			// app.appDeveloperEmail() });
			// i.putExtra(Intent.EXTRA_SUBJECT,
			// getString(R.string.app_developer_email_contact_subject));
			// startActivity(Intent.createChooser(i,
			// "Select email application."));
			// }
			// });
			// }
			// else {
			// emailLayout.setVisibility(View.GONE);
			// }
			//
			// // Developer web
			// RelativeLayout webLayout =
			// (RelativeLayout)this.findViewById(R.id.developer_web_layout);
			// if(!this.app.appDeveloperWeb().equals("")) {
			// webLayout.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// v.setBackgroundResource(R.color.pinned_app_cell_background_color);
			// Intent intent = new Intent(AppDetail.this,
			// WebViewActivity.class);
			// intent.putExtra(Constants.APP_DEV_URL_INTENT_DATA,
			// app.appDeveloperWeb());
			// AppDetail.this.startActivity(intent);
			// }
			// });
			// }
			// else {
			// webLayout.setVisibility(View.GONE);
			// }

		}
	}

	private void initDescriptionLayout() {
		// Set text
		TextView moreInfoTextView = (TextView)findViewById(R.id.lblMoreInfo);
		moreInfoTextView.setText(this.app.getAppDescription());
		moreInfoTextView.setTypeface(((MapplasApplication)this.getApplicationContext()).getItalicTypeFace());

		final ImageView moreLessImage = (ImageView)findViewById(R.id.imgMore);
		if(moreInfoTextView.getMeasuredWidth() * 6 < moreInfoTextView.getPaint().measureText(app.getAppDescription())) {
			moreLessImage.setVisibility(View.VISIBLE);
		}
		else {
			moreLessImage.setVisibility(View.GONE);
		}

		// Init layout
		LinearLayout descriptionLayout = (LinearLayout)findViewById(R.id.lytDescription);
		descriptionLayout.setTag(this.app);
		descriptionLayout.setOnClickListener(new View.OnClickListener() {

			private boolean close = true;

			@Override
			public void onClick(View v) {
				TextView tv = (TextView)findViewById(R.id.lblMoreInfo);
				if(close) {
					tv.setMaxLines(10000);
					moreLessImage.setImageResource(R.drawable.ic_less);
				}
				else {
					tv.setMaxLines(6);
					moreLessImage.setImageResource(R.drawable.ic_more);
				}
				close = !close;
			}
		});
	}

	private void initializeLaunchButtonAndBehaviour() {
		Button buttonStart = (Button)this.findViewById(R.id.btnStart);
		buttonStart.setTypeface(((MapplasApplication)this.getApplicationContext()).getTypeFace());

		new AppLaunchHelper(this, buttonStart, this.app, this.user, this.model.getLocation()).help();
	}

	private void initializeActionsLayout() {
		// Define layouts
		final LinearLayout lytPinup = (LinearLayout)findViewById(R.id.lytPinup);
		final LinearLayout lytRate = (LinearLayout)findViewById(R.id.lytRate);
		final LinearLayout lytBlock = (LinearLayout)findViewById(R.id.lytBlock);
		final LinearLayout lytShare = (LinearLayout)findViewById(R.id.lytShare);

		// If app is not installed, cannot be rated
		if(this.app.getInternalApplicationInfo() != null) {
			this.initRateLayout(lytRate);
			lytRate.setTag(this.app);
		}
		else {
			lytRate.setVisibility(View.GONE);
		}

		lytPinup.setTag(this.app);
		lytBlock.setTag(this.app);
		lytShare.setTag(this.app);

		this.initPinLayout(lytPinup);
		this.initShareLayout(lytShare);
		this.initBlockLayout(lytBlock);
	}

	private void initPinLayout(LinearLayout lytPinup) {
		final ImageView ivPinup = (ImageView)findViewById(R.id.btnPinUp);
		RobotoTextView tvPinup = (RobotoTextView)findViewById(R.id.lblPinUp);
		ivPinup.setTag(this.app);

		if(this.app.isAuxPin() == 0) {
			ivPinup.setImageResource(R.drawable.action_pin_button);
			tvPinup.setText(R.string.pin_up);
		}
		else {
			ivPinup.setImageResource(R.drawable.action_un_pinup_button);
			tvPinup.setText(R.string.un_pin_up);
		}

		lytPinup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
					String auxuid = "0";
					if(user != null) {
						auxuid = user.getId() + "";
					}

					final String uid = auxuid;

					if(anonLoc.isAuxPin() == 1) {
						ivPinup.setImageResource(R.drawable.action_pin_button);
					}
					else {
						ivPinup.setImageResource(R.drawable.action_un_pinup_button);
					}

					String pinUnpinRequestConstant = Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_PIN;
					if(anonLoc.isAuxPin() == 1) {
						pinUnpinRequestConstant = Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_UNPIN;
						anonLoc.setAuxPin(0);
					}
					else {
						anonLoc.setAuxPin(1);
					}

					// Set app object as pinned or unpinned
					// Pin / unpin app
					boolean found = false;
					int i = 0;
					while (!found && i < model.appList().size()) {
						App currentApp = model.appList().get(i);
						if(currentApp.getId().equals(anonLoc.getId())) {

							int pinned = currentApp.isAuxPin();
							if(pinned == 1) {
								currentApp.setAuxPin(0);
							}
							else {
								currentApp.setAuxPin(1);
							}

							found = true;
						}
						i++;
					}

					somethingChanged = true;

					Thread pinRequestThread = new Thread(new PinRequestThread(pinUnpinRequestConstant, anonLoc, uid, model.getLocation(), model.currentDescriptiveGeoLoc()).getThread());
					pinRequestThread.start();
				}
			}
		});
	}

	private void initShareLayout(LinearLayout lytShare) {
		ImageView bimg = (ImageView)findViewById(R.id.btnShare);
		bimg.setTag(this.app);

		lytShare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
					startActivity(Intent.createChooser(new ShareHelper().getSharingIntent(AppDetail.this, anonLoc), getString(R.string.share)));

					Thread shareThread = new Thread(new ShareRequestThread(anonLoc.getId(), user.getId(), model.getLocation()).getThread());
					shareThread.run();
				}
			}
		});
	}

	private void initBlockLayout(LinearLayout lytBlock) {
		lytBlock.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
					if(anonLoc != null) {
						AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(AppDetail.this);
						myAlertDialog.setTitle(R.string.block_title);
						myAlertDialog.setMessage(R.string.block_text);
						myAlertDialog.setPositiveButton(R.string.block_accept, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								// Block and unpin app

								String uid = String.valueOf(user.getId());
								Thread likeRequestThread = new Thread(new BlockRequestThread(Constants.MAPPLAS_ACTIVITY_LIKE_REQUEST_BLOCK, anonLoc, uid).getThread());
								likeRequestThread.start();

								// Si la app esta pineada, la despineamos
								if(anonLoc.isAuxPin() == 1) {
									Thread unPinRequestThread = new Thread(new PinRequestThread(Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_UNPIN, anonLoc, uid, model.getLocation(), model.currentDescriptiveGeoLoc()).getThread());
									unPinRequestThread.start();
								}

								// Set app object as blocked
								boolean found = false;
								int i = 0;
								while (!found && i < model.appList().size()) {
									App currentApp = model.appList().get(i);
									if(currentApp.getId().equals(anonLoc.getId())) {
										model.appList().getAppList().remove(i);
										found = true;
									}
									i++;
								}

								somethingChanged = true;

								AppDetail.this.finish();
							}
						});
						myAlertDialog.setNegativeButton(R.string.block_cancel, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								// do something when the Cancel button is
								// clicked
							}
						});
						myAlertDialog.show();
					}
				}
			}
		});
	}

	private void initRateLayout(LinearLayout lytRate) {
		ImageView bimg = (ImageView)findViewById(R.id.btnRate);
		bimg.setTag(this.app);

		lytRate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
					String strUrl = new PlayStoreLinkCreator().createLinkForApp(app.getId());
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl));
					AppDetail.this.startActivity(browserIntent);
				}
			}
		});

		LinearLayout layout = (LinearLayout)findViewById(R.id.app_detail_app_title_rating_layout);
		layout.setTag(this.app);

		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
					String strUrl = new PlayStoreLinkCreator().createLinkForApp(app.getId());
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl));
					AppDetail.this.startActivity(browserIntent);
				}
			}
		});
	}
}