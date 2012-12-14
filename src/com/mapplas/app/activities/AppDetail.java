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
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
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
import com.mapplas.app.RatingDialog;
import com.mapplas.app.Resizer;
import com.mapplas.app.SliderListView;
import com.mapplas.app.adapters.CommentAdapter;
import com.mapplas.app.adapters.ImageAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.Photo;
import com.mapplas.model.User;
import com.mapplas.utils.DrawableBackgroundDownloader;
import com.mapplas.utils.NetRequests;
import com.mapplas.utils.NumberUtils;

public class AppDetail extends Activity {

	/* Debug Values */
//	private static final boolean mDebug = true;

	/* */
	private App app = null; // mLoc

	private User user = null;

	private String currentLocation = "";
	
	private String currentDescriptiveGeoLoc = "";

	private Uri imageUri;

//	private String imagePath;

	private SliderListView commentsListView = null;

	private ImageView commentsArrow = null;

	private RotateAnimation flipAnimation;

	private RotateAnimation reverseFlipAnimation;

//	private Resizer resizer;

	private DisplayMetrics metrics = new DisplayMetrics();

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
					String id = "0";
					String resp = "";

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

	private class OnReadyListener implements RatingDialog.ReadyListener {

		@Override
		public void ready(String name) {

			// Guardamos la valoración del usuario en el servidor

			if(!name.equals("CANCEL")) {
				// Ponemos en amarillo el botón
				ImageView img = (ImageView)findViewById(R.id.btnRate);
				img.setImageResource(R.drawable.action_rate_button_done);

				// Enviamos la nota por internet
				String uid = "0";
				String id = "0";
				String resp = "";

				if(user != null) {
					uid = user.getId() + "";
				}

				try {

					String rat = name.substring(0, name.indexOf("|"));
					String com = name.substring(name.indexOf("|") + 1);

					resp = NetRequests.RateRequest(rat, com, currentLocation, currentDescriptiveGeoLoc, AppDetail.this.app.getId() + "", uid);
					Toast.makeText(AppDetail.this, resp, Toast.LENGTH_LONG).show();
				} catch (Exception exc) {
					Toast.makeText(AppDetail.this, exc.toString(), Toast.LENGTH_LONG).show();
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
				resp = NetRequests.ProblemRequest(name, AppDetail.this.app.getId() + "", uid);
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
				this.app = (App)extras.getSerializable(Constants.MAPPLAS_DETAIL_APP);
			}

			if(extras.containsKey(Constants.MAPPLAS_DETAIL_USER)) {
				this.user = (User)extras.getSerializable(Constants.MAPPLAS_DETAIL_USER);
			}

			if(extras.containsKey(Constants.MAPPLAS_DETAIL_CURRENT_LOCATION)) {
				this.currentLocation = extras.getString(Constants.MAPPLAS_DETAIL_CURRENT_LOCATION);
			}
			
			if(extras.containsKey(Constants.MAPPLAS_DETAIL_CURRENT_DESCRIPT_GEO_LOCATION)) {
				this.currentDescriptiveGeoLoc = extras.getString(Constants.MAPPLAS_DETAIL_CURRENT_DESCRIPT_GEO_LOCATION);
			}
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

		RatingBar rbRating = (RatingBar)findViewById(R.id.rbRating);
		rbRating.setRating(this.app.getAuxTotalRate());

		// Comments list
		this.commentsListView = (SliderListView)findViewById(R.id.lvComments);
		CommentAdapter auxComAd = new CommentAdapter(this, R.layout.rowcom, this.app.getAuxComments());
		this.commentsListView.setAdapter(auxComAd);

		// Comments arrow image
		this.commentsArrow = (ImageView)findViewById(R.id.ivArrow);

		// Comments
		TextView tvComments = (TextView)findViewById(R.id.btnComments);
		tvComments.setTypeface(normalTypeFace);
		tvComments.setTag(this.app);

		// Number of comments
		TextView numberOfComments = (TextView)findViewById(R.id.lblNumberOfComments);
		numberOfComments.setText("(" + NumberUtils.FormatNumber(this.app.getAuxTotalComments()) + ")");

		// Comments layout
		LinearLayout lytComments = (LinearLayout)findViewById(R.id.lytComments);
		lytComments.setTag(this.app);
		lytComments.setOnClickListener(new View.OnClickListener() {

			private boolean open = false;

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(AppDetail.this, Comments.class);
				// intent.putExtra(Constants.SYNESTH_DETAIL_INDEX,
				// AppDetail.mIndex);
				// AppDetail.this.startActivity(intent);
				int h = (int)(AppDetail.this.app.getAuxComments().size() * 70 * metrics.density);
				float vel = AppDetail.this.app.getAuxComments().size() * 500 * metrics.density;

				if(open) {
					commentsListView.SlideUp(vel);
					commentsArrow.startAnimation(reverseFlipAnimation);
				}
				else {
					commentsListView.SlideDown(h, vel);
					commentsArrow.startAnimation(flipAnimation);
				}
				open = !open;

				final App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
					try {
						Thread th = new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									NetRequests.ActivityRequest(currentLocation, "showcomments", anonLoc.getId() + "", user.getId() + "");
								} catch (Exception e) {
									Log.i(getClass().getSimpleName(), "Thread Action Start: " + e);
								}
							}
						});
						th.start();

					} catch (Exception exc) {
						Log.i(getClass().getSimpleName(), "Action Start: " + exc);
					}
				}
			}
		});

		// Download application logo
		ImageView appLogo = (ImageView)findViewById(R.id.imgLogo);
		Bitmap bmp = null;

		new DrawableBackgroundDownloader().loadDrawable(this.app.getAppLogo(), appLogo, this.getResources().getDrawable(R.drawable.ic_refresh));

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

		// Developer text view
		TextView developerTextView = (TextView)findViewById(R.id.lblDeveloper);
		developerTextView.setTypeface(normalTypeFace);

		// Developer mail and web buttons layout
		TextView developerWebMailTextView = (TextView)findViewById(R.id.lblWeb);
		// tv.setText(this.mLoc.getAppUrl());
		developerWebMailTextView.setTypeface(normalTypeFace);
		developerWebMailTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppDetail.this.app.getAppUrl()));
				AppDetail.this.startActivity(browserIntent);
			}
		});

		// Developer mail button
		TextView tv = (TextView)findViewById(R.id.lblEMail);
		// tv.setText(this.mLoc.getAppEmail());
		tv.setTypeface(normalTypeFace);
		tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				// i.setType("text/plain"); //use this line for testing in the
				// emulator
				i.setType("message/rfc822"); // use from live device
				i.putExtra(Intent.EXTRA_EMAIL, new String[] { "test@gmail.com" });
				i.putExtra(Intent.EXTRA_SUBJECT, "subject goes here");
				i.putExtra(Intent.EXTRA_TEXT, "body goes here");
				startActivity(Intent.createChooser(i, "Select email application."));
			}
		});

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

	private void initializeButtonsAndBehaviour() {
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();

		Button buttonStart = (Button)this.findViewById(R.id.btnStart);
		buttonStart.setTypeface(((MapplasApplication)this.getApplicationContext()).getBoldTypeFace());

		if(this.app.getType().equalsIgnoreCase("application")) {
			if(this.app.getInternalApplicationInfo() != null) {
				// Start
				buttonStart.setBackgroundResource(R.drawable.badge_launch);
				buttonStart.setText("");
			}
			else {
				// Install
				if(this.app.getAppPrice() > 0) {
					buttonStart.setBackgroundResource(R.drawable.badge_price);
					buttonStart.setText("$" + this.app.getAppPrice());

					NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
					buttonStart.setText(nf.format(this.app.getAppPrice()));
				}
				else {
					buttonStart.setBackgroundResource(R.drawable.badge_free);
					buttonStart.setText(R.string.free);
				}
			}
		}
		else {
			// Info
			buttonStart.setBackgroundResource(R.drawable.badge_html5);
			buttonStart.setText("");
		}

		buttonStart.setTag(this.app);
		buttonStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
					String strUrl = anonLoc.getAppUrl();
					if(!(strUrl.startsWith("http://") || strUrl.startsWith("https://") || strUrl.startsWith("market://")))
						strUrl = "http://" + strUrl;

					if(anonLoc.getInternalApplicationInfo() != null) {
						Intent appIntent = AppDetail.this.getPackageManager().getLaunchIntentForPackage(anonLoc.getInternalApplicationInfo().packageName);
						AppDetail.this.startActivity(appIntent);
						try {
							NetRequests.ActivityRequest(currentLocation, "start", anonLoc.getId() + "", user.getId() + "");
							finish();
						} catch (Exception exc) {
							Log.i(getClass().getSimpleName(), "Action Start: " + exc);
						}
					}
					else {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl));
						AppDetail.this.startActivity(browserIntent);
						try {
							NetRequests.ActivityRequest(currentLocation, "install", anonLoc.getId() + "", user.getId() + "");
						} catch (Exception exc) {
							Log.i(getClass().getSimpleName(), "Action Install: " + exc);
						}
					}
				}
			}
		});

		// Support button
		Button supportButton = (Button)findViewById(R.id.btnReportProblem);
		supportButton.setTypeface(normalTypeFace);
		supportButton.setTag(this.app);
		supportButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
					ProblemDialog myDialog = new ProblemDialog(AppDetail.this, "", new OnReadyListenerProblem());
					myDialog.show();

					if(anonLoc != null) {
						try {
							Thread th = new Thread(new Runnable() {

								@Override
								public void run() {
									try {
										NetRequests.ActivityRequest(currentLocation, "problem", anonLoc.getId() + "", user.getId() + "");
									} catch (Exception e) {
										Log.i(getClass().getSimpleName(), "Thread Action Rate: " + e);
									}
								}
							});
							th.start();

						} catch (Exception exc) {
							Log.i(getClass().getSimpleName(), "Action Problem: " + exc);
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
		final LinearLayout lytLike = (LinearLayout)findViewById(R.id.lytLike);
		final LinearLayout lytBlock = (LinearLayout)findViewById(R.id.lytBlock);
		final LinearLayout lytShare = (LinearLayout)findViewById(R.id.lytShare);
		final LinearLayout lytPhone = (LinearLayout)findViewById(R.id.lytPhone);

		lytPinup.setTag(this.app);
		lytRate.setTag(this.app);
		lytLike.setTag(this.app);
		lytBlock.setTag(this.app);
		lytShare.setTag(this.app);
		lytPhone.setTag(this.app);

		this.initFavLayout(lytLike);
		this.initPinLayout(lytPinup);
		this.initShareLayout(lytShare);
		this.initBlockLayout(lytBlock);
		this.initRateLayout(lytRate);
		this.initPhoneLayout(lytPhone);
	}

	private void initFavLayout(LinearLayout lytLike) {
		// Favourite
		final ImageView ivFavourite = (ImageView)findViewById(R.id.btnFavourite);
		ivFavourite.setTag(this.app);

		if(this.app.isAuxFavourite()) {
			ivFavourite.setImageResource(R.drawable.action_like_button_done);
		}

		lytLike.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
					String auxuid = "0";
					if(user != null) {
						auxuid = user.getId() + "";
					}

					final String uid = auxuid;

					String auxaction = "p";
					if(anonLoc.isAuxFavourite()) {
						auxaction = "pr";
						ivFavourite.setImageResource(R.drawable.action_like_button);
						anonLoc.setAuxFavourite(false);
					}
					else {
						auxaction = "p";
						ivFavourite.setImageResource(R.drawable.action_like_button_done);
						anonLoc.setAuxFavourite(true);
					}

					final String action = auxaction;

					try {
						Thread th = new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									NetRequests.LikeRequest(action, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, anonLoc.getId() + "", uid);
									NetRequests.ActivityRequest(currentLocation, "favourite", anonLoc.getId() + "", user.getId() + "");
								} catch (Exception e) {
									Log.i(getClass().getSimpleName(), "Thread Action Like: " + e);
								}
							}
						});
						th.start();

					} catch (Exception exc) {
						Log.i(getClass().getSimpleName(), "Action Like: " + exc);
					}
				}
			}
		});
	}

	private void initPinLayout(LinearLayout lytPinup) {
		final ImageView ivPinup = (ImageView)findViewById(R.id.btnPinUp);
		ivPinup.setTag(this.app);

		if(!this.app.isAuxPin()) {
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

					if(anonLoc.isAuxPin()) {
						ivPinup.setImageResource(R.drawable.action_pin_button);
					}
					else {
						ivPinup.setImageResource(R.drawable.action_unpin_button);
					}
					try {
						Thread th = new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									String action = "pin";
									if(anonLoc.isAuxPin()) {
										action = "unpin";
										anonLoc.setAuxPin(false);
									}
									else {
										action = "pin";
										anonLoc.setAuxPin(true);
									}
									NetRequests.PinRequest(action, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, anonLoc.getId() + "", uid);
									NetRequests.ActivityRequest(currentLocation, "pin", anonLoc.getId() + "", user.getId() + "");
								} catch (Exception e) {
									Log.i(getClass().getSimpleName(), "Thread Action PinUp: " + e);
								}
							}
						});
						th.start();

					} catch (Exception exc) {
						Log.i(getClass().getSimpleName(), "Action PinUp: " + exc);
					}
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
					Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
					// Comprobar que existe y que está instalada la aplicación
					// en el teléfono
					sharingIntent.setPackage("com.whatsapp");
					sharingIntent.setType("text/plain");
					String shareBody = anonLoc.getAppName() + " sharing via Synesth";
					// sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					anonLoc.getAppName();
					sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
					startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));

					if(anonLoc != null) {
						try {
							Thread th = new Thread(new Runnable() {

								@Override
								public void run() {
									try {
										NetRequests.ActivityRequest(currentLocation, "share", anonLoc.getId() + "", user.getId() + "");
									} catch (Exception e) {
										Log.i(getClass().getSimpleName(), "Thread Action Share: " + e);
									}
								}
							});
							th.start();
						} catch (Exception exc) {
							Log.i(getClass().getSimpleName(), "Action Share: " + exc);
						}
					}
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
								try {

									Thread th = new Thread(new Runnable() {

										@Override
										public void run() {
											try {

												String auxuid = "0";
												if(user != null) {
													auxuid = user.getId() + "";
												}

												final String uid = auxuid;

												NetRequests.LikeRequest("m", Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, anonLoc.getId() + "", uid);
												NetRequests.ActivityRequest(currentLocation, "block", anonLoc.getId() + "", user.getId() + "");
											} catch (Exception e) {
												Log.i(getClass().getSimpleName(), "Thread Action PinUp: " + e);
											}
										}
									});
									th.start();

									AppDetail.this.finish();

									// Eliminamos el item de la lista
//									TODO: find solution
//									MapplasActivity.getLocalizationAdapter().remove(anonLoc);
//									MapplasActivity.getLocalizationAdapter().notifyDataSetChanged();

								} catch (Exception exc) {
									Log.i(getClass().getSimpleName(), "Action PinUp: " + exc);
								}
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

		if(this.app.getAuxRate() > 0) {
			bimg.setImageResource(R.drawable.action_rate_button_done);
		}
		bimg.setTag(this.app);

		lytRate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
					RatingDialog myDialog = new RatingDialog(AppDetail.this, "", new OnReadyListener(), anonLoc.getAuxRate(), anonLoc.getAuxComment());
					myDialog.show();

					if(anonLoc != null) {
						try {
							Thread th = new Thread(new Runnable() {

								@Override
								public void run() {
									try {
										NetRequests.ActivityRequest(currentLocation, "rate", anonLoc.getId() + "", user.getId() + "");
									} catch (Exception e) {
										Log.i(getClass().getSimpleName(), "Thread Action Rate: " + e);
									}
								}
							});
							th.start();

						} catch (Exception exc) {
							Log.i(getClass().getSimpleName(), "Action Rate: " + exc);
						}
					}
				}
			}
		});
	}

	private void initPhoneLayout(LinearLayout lytPhone) {
		ImageView bimg = (ImageView)findViewById(R.id.btnPhone);
		bimg.setTag(this.app);

		lytPhone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final App anonLoc = (App)(v.getTag());
				if(anonLoc != null) {
					String number = "tel:" + anonLoc.getPhone();
					Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
					startActivity(callIntent);

					if(anonLoc != null) {
						try {
							Thread th = new Thread(new Runnable() {

								@Override
								public void run() {
									try {
										NetRequests.ActivityRequest(currentLocation, "call", anonLoc.getId() + "", user.getId() + "");
									} catch (Exception e) {
										Log.i(getClass().getSimpleName(), "Thread Action Rate: " + e);
									}
								}
							});
							th.start();

						} catch (Exception exc) {
							Log.i(getClass().getSimpleName(), "Action Call: " + exc);
						}
					}
				}
			}
		});

		if(this.app.getPhone().equals("")) {
			lytPhone.setVisibility(View.GONE);
		}
	}
	/*
	 * btn = (Button) findViewById(R.id.btnCamera); btn.setTag(this.mLoc);
	 * btn.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { Intent cameraIntent = new
	 * Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	 * 
	 * File photo = new File(Environment.getExternalStorageDirectory(),
	 * "synesth.jpg"); cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	 * Uri.fromFile(photo)); imageUri = Uri.fromFile(photo); imagePath =
	 * photo.getAbsolutePath();
	 * 
	 * startActivityForResult(cameraIntent,
	 * Constants.SYNESTH_DETAILS_CAMERA_ID);
	 * 
	 * 
	 * 
	 * final Localization anonLoc = (Localization)(v.getTag()); if(anonLoc !=
	 * null) { try { Thread th = new Thread(new Runnable() {
	 * 
	 * @Override public void run() { try {
	 * NetRequests.ActivityRequest(SynesthActivity .GetModel().currentLocation,
	 * "camera", anonLoc.getId() + "",
	 * SynesthActivity.GetModel().currentUser.getId() + ""); } catch (Exception
	 * e) { Log.i(getClass().getSimpleName(), "Thread Action Camera: " + e); } }
	 * }); th.start();
	 * 
	 * }catch(Exception exc) { Log.i(getClass().getSimpleName(),
	 * "Action Camera: " + exc); } } } });
	 */
}