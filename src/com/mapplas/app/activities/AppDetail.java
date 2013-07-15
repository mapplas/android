package com.mapplas.app.activities;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import app.mapplas.com.R;

import com.mapplas.app.ProblemDialog;
import com.mapplas.app.SliderListView;
import com.mapplas.app.adapters.ImageAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.app.async_tasks.LoadImageTask;
import com.mapplas.app.async_tasks.TaskAsyncExecuter;
import com.mapplas.app.threads.ActivityRequestThread;
import com.mapplas.app.threads.LikeRequestThread;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.Photo;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
import com.mapplas.utils.cache.CacheFolderFactory;
import com.mapplas.utils.cache.ImageFileManager;
import com.mapplas.utils.network.requests.NetRequests;
import com.mapplas.utils.share.ShareHelper;
import com.mapplas.utils.static_intents.AppChangedSingleton;
import com.mapplas.utils.static_intents.SuperModelSingleton;

public class AppDetail extends Activity {

	/* Debug Values */
	// private static final boolean mDebug = true;

	/* */
	public static String APP_DEV_URL_INTENT_DATA = "com.mapplas.activity.bundle.dev_url";
	
	private App app = null; // mLoc

	private User user = null;

	private SuperModel model = null;

	private String currentLocation = "";

	private String currentDescriptiveGeoLoc = "";

	private Uri imageUri;

	// private String imagePath;

	private SliderListView commentsListView = null;

	private ImageView commentsArrow = null;

	private RotateAnimation flipAnimation;

	private RotateAnimation reverseFlipAnimation;

	// private Resizer resizer;

	private DisplayMetrics metrics = new DisplayMetrics();

	private boolean somethingChanged = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app);

		this.extractDataFromBundle();
		this.initializeAnimations();

		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		this.initializeLayoutComponents();
		this.initializeButtonsAndBehaviour();
		this.initializeActionsLayout();

		// Cargamos la imágenes en la galería
		Gallery gal = (Gallery)findViewById(R.id.galleryPhotos);
		ImageAdapter imgAdapter = new ImageAdapter(this, gal);

		for(Photo p : this.app.getAuxPhotos()) {
			String strUrl = p.getPhoto();
			imgAdapter.mImages.add(strUrl);
		}
		gal.setAdapter(imgAdapter);

		if(imgAdapter.getCount() > 0) {
			// resizer = new Resizer(gal);
			// resizer.start(480, 500 * metrics.density);
		}
	}

	@Override
	protected void onPause() {
		if(this.somethingChanged) {
			AppChangedSingleton.somethingChanged = true;
			this.somethingChanged = false;
		}
		super.onPause();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constants.SYNESTH_DETAILS_CAMERA_ID) {
			if(resultCode == RESULT_OK) {
				Uri selectedImage = imageUri;
				getContentResolver().notifyChange(selectedImage, null);
				ContentResolver cr = getContentResolver();
				Bitmap bitmap;
				try {

					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);

					int width = bitmap.getWidth();
					int height = bitmap.getHeight();
					int x = 0;
					int y = 0;

					if(width > height) {
						x = (width - height) / 2;
						width = height;
					}
					else {
						y = (height - width) / 2;
						height = width;
					}

					Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
					Bitmap scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, 256, 256, true);

					scaledBitmap.compress(CompressFormat.JPEG, 75, bos);
					byte[] scaledImageData = bos.toByteArray();

					/*
					 * FileInputStream fis = new FileInputStream(imagePath);
					 * 
					 * byte[] bucket = new byte[32*1024];
					 * 
					 * //Use buffering? No. Buffering avoids costly access to
					 * disk or network; //buffering to an in-memory stream makes
					 * no sense. ByteArrayOutputStream result = new
					 * ByteArrayOutputStream(bucket.length); int bytesRead = 0;
					 * while(bytesRead != -1){ //aInput.read() returns -1, 0, or
					 * more : bytesRead = fis.read(bucket); if(bytesRead > 0){
					 * result.write(bucket, 0, bytesRead); } }
					 * 
					 * byte[] scaledImageData = result.toByteArray();
					 */

					String uid = "0";
					// String id = "0";
					// String resp = "";

					if(this.user != null) {
						uid = this.user.getId() + "";
					}

					NetRequests.ImageRequest(scaledImageData, AppDetail.this.app.getId() + "", uid);

					Toast.makeText(this, selectedImage.toString(), Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
					Log.e("Camera", e.toString());
				}
			}
		}
	}

	private class OnReadyListenerProblem implements ProblemDialog.ReadyListener {

		@Override
		public void ready(String name) {
			Toast.makeText(AppDetail.this, name, Toast.LENGTH_LONG).show();

			// Guardamos la valoración del usuario en el servidor
			// Enviamos la nota por internet
			String uid = "0";
			String id = "0";
			String resp = "";

			if(user != null) {
				uid = user.getId() + "";
			}
			try {
				resp = NetRequests.ProblemRequest(name, String.valueOf(AppDetail.this.app.getId()), uid);
				Toast.makeText(AppDetail.this, resp, Toast.LENGTH_LONG).show();
			} catch (Exception exc) {
				Toast.makeText(AppDetail.this, exc.toString(), Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 
	 * Private methods
	 * 
	 */
	private void extractDataFromBundle() {
		// Get the app pressed
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			if(extras.containsKey(Constants.MAPPLAS_DETAIL_APP)) {
				this.app = (App)extras.getParcelable(Constants.MAPPLAS_DETAIL_APP);
			}

			this.model = SuperModelSingleton.model;
			this.user = this.model.currentUser();
			this.currentLocation = this.model.currentLocation();
			this.currentDescriptiveGeoLoc = this.model.currentDescriptiveGeoLoc();
		}
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
//		rbRating.setRating(this.app.getAuxTotalRate());

		// When tapping between app image and price, rating dialog is shown. (In
		// app cathegory or static stars).
//		LinearLayout layout = (LinearLayout)findViewById(R.id.app_detail_app_title_rating_layout);
//		layout.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				RatingDialog myDialog = new RatingDialog(AppDetail.this, "", new OnReadyListener(user, AppDetail.this, model, app), app.getAuxRate(), app.getAuxComment());
//				myDialog.show();
//			}
//		});

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

		// More info text view
		TextView moreInfoTextView = (TextView)findViewById(R.id.lblMoreInfo);
		moreInfoTextView.setText(this.app.getAppDescription());
		moreInfoTextView.setTypeface(((MapplasApplication)this.getApplicationContext()).getItalicTypeFace());

		// Description layout - content of screen except header
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

		this.manageDeveloperLayout(normalTypeFace);

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
//		if(this.app.getAppUrl().equals("") && this.app.getDeveloperMail.equals("")) {
		if(this.app.getId().equals("")) {
			findViewById(R.id.lytDeveloper).setVisibility(View.INVISIBLE);
		}
		else {
			// Developer text view
			TextView developerTextView = (TextView)findViewById(R.id.lblDeveloper);
			developerTextView.setTypeface(normalTypeFace);

			// Developer mail and web buttons layout
			TextView developerWebMailTextView = (TextView)findViewById(R.id.lblWeb);

			if(this.app.getId().equals("")) {
				developerWebMailTextView.setVisibility(View.GONE);
			} else {
				developerWebMailTextView.setTypeface(normalTypeFace);
				developerWebMailTextView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {						
						Intent intent = new Intent(AppDetail.this, WebViewActivity.class);
						intent.putExtra(AppDetail.APP_DEV_URL_INTENT_DATA, app);
						AppDetail.this.startActivity(intent);
					}
				});
			}

			// Developer mail button
			TextView tv = (TextView)findViewById(R.id.lblEMail);
			
//			if(this.app.getDeveloperMail().equals("")) {
//				tv.setVisibility(View.GONE);
//			} else {
				tv.setTypeface(normalTypeFace);
				tv.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(Intent.ACTION_SEND);
						i.setType("text/html"); // use from live device
						i.putExtra(Intent.EXTRA_EMAIL, new String[] { "contact@mapplas.com" });
						i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_developer_email_contact_subject));
						startActivity(Intent.createChooser(i, "Select email application."));
					}
				});
//			}
		}
	}

	private void initializeButtonsAndBehaviour() {
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();

		Button buttonStart = (Button)this.findViewById(R.id.btnStart);
		buttonStart.setTypeface(((MapplasApplication)this.getApplicationContext()).getTypeFace());

//		if(this.app.getType().equalsIgnoreCase("application")) {
			if(this.app.getInternalApplicationInfo() != null) {
				// Start
				buttonStart.setBackgroundResource(R.drawable.badge_launch);
				buttonStart.setText("");
			}
			else {
				// Install
//				if(this.app.getAppPrice() > 0) {
					buttonStart.setBackgroundResource(R.drawable.badge_price);
					buttonStart.setText(this.app.getAppPrice());

					NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
					buttonStart.setText(nf.format(this.app.getAppPrice()));
				}
//				else {
//					buttonStart.setBackgroundResource(R.drawable.badge_free);
//					buttonStart.setText(R.string.free);
//				}
//			}
//		}
//		else {
//			// Info
//			buttonStart.setBackgroundResource(R.drawable.badge_html5);
//			buttonStart.setText("");
//		}

		buttonStart.setTag(this.app);
		buttonStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
					String strUrl = anonLoc.getId();
					if(!(strUrl.startsWith("http://") || strUrl.startsWith("https://") || strUrl.startsWith("market://")))
						strUrl = "http://" + strUrl;

					if(anonLoc.getInternalApplicationInfo() != null) {
						Intent appIntent = AppDetail.this.getPackageManager().getLaunchIntentForPackage(anonLoc.getInternalApplicationInfo().packageName);
						AppDetail.this.startActivity(appIntent);
						try {
							NetRequests.ActivityRequest(currentLocation, Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_START, String.valueOf(anonLoc.getId()), String.valueOf(user.getId()));
							finish();
						} catch (Exception exc) {
							Log.i(getClass().getSimpleName(), "Action Start: " + exc);
						}
					}
					else {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl));
						AppDetail.this.startActivity(browserIntent);
						try {
							NetRequests.ActivityRequest(currentLocation, Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_INSTALL, String.valueOf(anonLoc.getId()), String.valueOf(user.getId()));
						} catch (Exception exc) {
							Log.i(getClass().getSimpleName(), "Action Install: " + exc);
						}
					}
				}
			}
		});
	}

	private void initializeActionsLayout() {
		// Define layouts
		final LinearLayout lytPinup = (LinearLayout)findViewById(R.id.lytPinup);
		final LinearLayout lytRate = (LinearLayout)findViewById(R.id.lytRate);
		// final LinearLayout lytLike =
		// (LinearLayout)findViewById(R.id.lytLike);
		final LinearLayout lytBlock = (LinearLayout)findViewById(R.id.lytBlock);
		final LinearLayout lytShare = (LinearLayout)findViewById(R.id.lytShare);
		final LinearLayout lytPhone = (LinearLayout)findViewById(R.id.lytPhone);

		lytPinup.setTag(this.app);
		lytRate.setTag(this.app);
		// lytLike.setTag(this.app);
		lytBlock.setTag(this.app);
		lytShare.setTag(this.app);
		lytPhone.setTag(this.app);

		// this.initFavLayout(lytLike);
		this.initPinLayout(lytPinup);
		this.initShareLayout(lytShare);
		this.initBlockLayout(lytBlock);
		this.initRateLayout(lytRate);
	}

	private void initPinLayout(LinearLayout lytPinup) {
		final ImageView ivPinup = (ImageView)findViewById(R.id.btnPinUp);
		ivPinup.setTag(this.app);

//		if(!this.app.isAuxPin()) {
//			ivPinup.setImageResource(R.drawable.action_pin_button);
//		}
//		else {
//			ivPinup.setImageResource(R.drawable.action_unpin_button);
//		}

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

//					if(anonLoc.isAuxPin()) {
						ivPinup.setImageResource(R.drawable.action_pin_button);
//					}
//					else {
//						ivPinup.setImageResource(R.drawable.action_unpin_button);
//					}

//					String pinUnpinRequestConstant = Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_PIN;
//					String activityRequestConstant = Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_PIN;
//					if(anonLoc.isAuxPin()) {
//						pinUnpinRequestConstant = Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_UNPIN;
//						activityRequestConstant = Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_UNPIN;
//						anonLoc.setAuxPin(false);
//					}
//					else {
//						anonLoc.setAuxPin(true);
//					}

					// Set app object as pinned or unpinned
					// Pin/unpin app
					boolean found = false;
					int i = 0;
//					while (!found && i < model.appList().size()) {
//						App currentApp = model.appList().get(i);
//						if(currentApp.getId() == anonLoc.getId()) {
//							currentApp.setAuxPin(!currentApp.isAuxPin());
//
//							// Add or not 1 element to total pins count
//							if(currentApp.isAuxPin()) {
//								currentApp.setAuxTotalPins(currentApp.getAuxTotalPins() + 1);
//							}
//							else {
//								currentApp.setAuxTotalPins(currentApp.getAuxTotalPins() - 1);
//							}
//
//							found = true;
//						}
//						i++;
//					}

					somethingChanged = true;

//					Thread pinRequestThread = new Thread(new PinRequestThread(pinUnpinRequestConstant, anonLoc, uid, currentLocation).getThread());
//					pinRequestThread.start();
//
//					Thread activityRequestThread = new Thread(new ActivityRequestThread(currentLocation, anonLoc, user, activityRequestConstant).getThread());
//					activityRequestThread.start();
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

					Thread activityRequestThread = new Thread(new ActivityRequestThread(currentLocation, anonLoc, user, Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_SHARE).getThread());
					activityRequestThread.start();
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
								Thread likeRequestThread = new Thread(new LikeRequestThread(Constants.MAPPLAS_ACTIVITY_LIKE_REQUEST_BLOCK, anonLoc, uid).getThread());
								likeRequestThread.start();

								// Si la app esta pineada, la despineamos
//								if(anonLoc.isAuxPin()) {
//									Thread unPinRequestThread = new Thread(new PinRequestThread(Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_UNPIN, anonLoc, uid, currentLocation).getThread());
//									unPinRequestThread.start();
//								}

								Thread activityRequestThread = new Thread(new ActivityRequestThread(uid, anonLoc, user, Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_BLOCK).getThread());
								activityRequestThread.start();

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

//		if(this.app.getAuxRate() > 0) {
//			bimg.setImageResource(R.drawable.action_rate_button_done);
//		}
		bimg.setTag(this.app);

		lytRate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
//					RatingDialog myDialog = new RatingDialog(AppDetail.this, "", new OnReadyListener(user, AppDetail.this, model, app), anonLoc.getAuxRate(), anonLoc.getAuxComment());
//					myDialog.show();

					Thread activityRequestThread = new Thread(new ActivityRequestThread(currentLocation, anonLoc, user, Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_RATE).getThread());
					activityRequestThread.start();
				}
			}
		});
	}
}