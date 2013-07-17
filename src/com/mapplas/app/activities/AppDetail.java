package com.mapplas.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.ImageAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
import com.mapplas.utils.cache.CacheFolderFactory;
import com.mapplas.utils.cache.ImageFileManager;
import com.mapplas.utils.network.async_tasks.AppDetailTask;
import com.mapplas.utils.network.async_tasks.LoadImageTask;
import com.mapplas.utils.network.async_tasks.TaskAsyncExecuter;
import com.mapplas.utils.network.requests.BlockRequestThread;
import com.mapplas.utils.network.requests.PinRequestThread;
import com.mapplas.utils.network.requests.ShareRequestThread;
import com.mapplas.utils.static_intents.AppChangedSingleton;
import com.mapplas.utils.static_intents.SuperModelSingleton;
import com.mapplas.utils.visual.helpers.AppLaunchHelper;
import com.mapplas.utils.visual.helpers.PlayStoreLinkCreator;
import com.mapplas.utils.visual.helpers.ShareHelper;

public class AppDetail extends Activity {

	private App app;

	private User user = null;

	private SuperModel model = null;

	private RotateAnimation flipAnimation;

	private RotateAnimation reverseFlipAnimation;

	private DisplayMetrics metrics = new DisplayMetrics();

	private boolean somethingChanged = false;

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
		new AppDetailTask(this, this.app).execute();
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

		if(imgAdapter.getCount() > 0) {
			// resizer = new Resizer(gal);
			// resizer.start(480, 500 * metrics.density);
		}
	}

	public void detailRequestFinishedNok() {
		// Detail loading error. Try again.
		new AppDetailTask(this, this.app).execute();
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
		Typeface italicTypeFace = ((MapplasApplication)this.getApplicationContext()).getItalicTypeFace();

		// Set stars
		RatingBar rbRating = (RatingBar)findViewById(R.id.rbRating);
		rbRating.setRating(this.app.rating());

		// When tapping between app image and price, rating dialog is shown. (In
		// app cathegory or static stars).
		LinearLayout layout = (LinearLayout)findViewById(R.id.app_detail_app_title_rating_layout);
		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: sent user to play store
			}
		});

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
		TextView appNameTextView = (TextView)findViewById(R.id.lblAppDetail);
		appNameTextView.setText(this.app.getName());
		appNameTextView.setTypeface(italicTypeFace);

		TextView appNameAboveRatingTextView = (TextView)findViewById(R.id.lblTitle);
		appNameAboveRatingTextView.setText(this.app.getName());
		appNameAboveRatingTextView.setTypeface(((MapplasApplication)this.getApplicationContext()).getTypeFace());

		// Back button
		Button backButton = (Button)findViewById(R.id.btnBack);
		backButton.setTypeface(normalTypeFace);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void manageDeveloperLayout(Typeface normalTypeFace) {

		if(!this.app.appDeveloperEmail().equals("") || !this.app.appDeveloperWeb().equals("")) {

			// Developer layout
			LinearLayout developerLayout = (LinearLayout)findViewById(R.id.lytDeveloper);
			developerLayout.setVisibility(View.VISIBLE);

			// Developer text view
			TextView developerTextView = (TextView)findViewById(R.id.lblDeveloper);
			developerTextView.setTypeface(normalTypeFace);

			// Developer web buttons layout
			Button developerAppWebTextView = (Button)findViewById(R.id.lblWeb);

			if(this.app.appDeveloperWeb().equals("")) {
				developerAppWebTextView.setVisibility(View.GONE);
			}
			else {
				developerAppWebTextView.setTypeface(normalTypeFace);
				developerAppWebTextView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(AppDetail.this, WebViewActivity.class);
						intent.putExtra(Constants.APP_DEV_URL_INTENT_DATA, app.appDeveloperWeb());
						intent.putExtra(Constants.APP_DEV_APP_NAMEL_INTENT_DATA, app.getName());
						AppDetail.this.startActivity(intent);
					}
				});
			}

			// Developer mail button
			Button developerSupportTextView = (Button)findViewById(R.id.lblEMail);

			if(this.app.appDeveloperEmail().equals("")) {
				developerSupportTextView.setVisibility(View.GONE);
			}
			else {
				developerSupportTextView.setTypeface(normalTypeFace);
				developerSupportTextView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(Intent.ACTION_SEND);
						i.setType("text/html"); // use from live device
						i.putExtra(Intent.EXTRA_EMAIL, new String[] { app.appDeveloperEmail() });
						i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_developer_email_contact_subject));
						startActivity(Intent.createChooser(i, "Select email application."));
					}
				});
			}
		}
	}

	private void initDescriptionLayout() {
		// Set text
		TextView moreInfoTextView = (TextView)findViewById(R.id.lblMoreInfo);
		moreInfoTextView.setText(this.app.getAppDescription());
		moreInfoTextView.setTypeface(((MapplasApplication)this.getApplicationContext()).getItalicTypeFace());

		// Init layout
		LinearLayout descriptionLayout = (LinearLayout)findViewById(R.id.lytDescription);
		descriptionLayout.setTag(this.app);
		descriptionLayout.setOnClickListener(new View.OnClickListener() {

			private boolean close = true;

			@Override
			public void onClick(View v) {
				TextView tv = (TextView)findViewById(R.id.lblMoreInfo);
				ImageView iv = (ImageView)findViewById(R.id.imgDots);

				if(close) {
					tv.setMaxLines(10000);
					iv.setVisibility(ImageView.INVISIBLE);
				}
				else {
					tv.setMaxLines(6);
					iv.setVisibility(ImageView.VISIBLE);
				}
				close = !close;
			}
		});
	}

	private void initializeLaunchButtonAndBehaviour() {
		Button buttonStart = (Button)this.findViewById(R.id.btnStart);
		buttonStart.setTypeface(((MapplasApplication)this.getApplicationContext()).getTypeFace());

		new AppLaunchHelper(this, buttonStart, this.app).help();
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
		ivPinup.setTag(this.app);

		if(this.app.isAuxPin() == 0) {
			ivPinup.setImageResource(R.drawable.action_pin_button);
		}
		else {
			ivPinup.setImageResource(R.drawable.action_unpin_button);
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
						ivPinup.setImageResource(R.drawable.action_unpin_button);
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
						if(currentApp.getId() == anonLoc.getId()) {

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
					model.appList().sort();

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
									if(currentApp.getId() == anonLoc.getId()) {
										model.appList().getAppList().remove(i);
										found = true;
									}
									i++;
								}

								somethingChanged = true;
								model.appList().sort();

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
	}
}