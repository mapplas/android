package com.mapplas.app;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Semaphore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.Toast;
import android.widget.ViewFlipper;
import app.mapplas.com.R;

import com.mapplas.app.activities.AppDetail;
import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.Constants;
import com.mapplas.model.App;
import com.mapplas.utils.NetRequests;
import com.mapplas.utils.NumberUtils;

public class LocalizationAdapter extends ArrayAdapter<App> {

	private ArrayList<App> items;

	private Context context = null;

	private static Semaphore mSemaphore = new Semaphore(1);

	private static App mRateLoc = null;

	private static App mBlockLoc = null;

	final Animation animFlipInNext = AnimationUtils.loadAnimation(this.context, R.anim.flipinnext);

	final Animation animFlipOutNext = AnimationUtils.loadAnimation(this.context, R.anim.flipoutnext);

	final Animation animFlipInPrevious = AnimationUtils.loadAnimation(this.context, R.anim.flipinprevious);

	final Animation animFlipOutPrevious = AnimationUtils.loadAnimation(this.context, R.anim.flipoutprevious);

	final Animation fadeOutAnimation = new AlphaAnimation(1, 0);

	// final Animation fadeOutAnimation = new ScaleAnimation(100.0f, 100.0f,
	// 100.0f, 0.0f);

	/* Message Handler */
	static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			try {
				LocalizationAdapter.mSemaphore.acquire();
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
				LocalizationAdapter.mSemaphore.release();
			} catch (Exception e) {
				Log.i(this.getClass().getSimpleName(), "handleMessage: " + e);
			}
		}
	};

	public LocalizationAdapter(Context context, int textViewResourceId, ArrayList<App> items) {
		super(context, textViewResourceId, items);
		this.items = items;

		this.context = context;

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
				if(LocalizationAdapter.mBlockLoc != null) {
					LocalizationAdapter.this.remove(LocalizationAdapter.mBlockLoc);
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
			v = vi.inflate(R.layout.rowloc, null);
			isNewView = true;
		}
		else {
			isNewView = false;
		}

		final View vinner = v;

		final App o = items.get(position);

		if(o != null) {
			v.setTag(position);

			TextView tt = (TextView)v.findViewById(R.id.lblTitle);
			final TextView ttPinUps = (TextView)v.findViewById(R.id.lblPinUps);

			final TextView ttPinUp = (TextView)v.findViewById(R.id.lblPinUp);

			tt.setText(o.getName());
			if(o.getAuxTotalPins() == 1) {
				ttPinUps.setText(NumberUtils.FormatNumber(o.getAuxTotalPins()) + " " + this.context.getString(R.string.pin_up));
			}
			else {
				ttPinUps.setText(NumberUtils.FormatNumber(o.getAuxTotalPins()) + " " + this.context.getString(R.string.pin_ups));
			}

			if(isNewView) {
				Typeface normalTypeface = ((MapplasApplication)getContext().getApplicationContext()).getTypeFace();
				
				tt.setTypeface(normalTypeface);
				ttPinUps.setTypeface(normalTypeface);

				tt = (TextView)v.findViewById(R.id.lblPinUp);
				tt.setTypeface(normalTypeface);
				tt = (TextView)v.findViewById(R.id.lblRate);
				tt.setTypeface(normalTypeface);
				tt = (TextView)v.findViewById(R.id.lblBlock);
				tt.setTypeface(normalTypeface);
				tt = (TextView)v.findViewById(R.id.lblShare);
				tt.setTypeface(normalTypeface);
			}

			Button buttonStart = (Button)v.findViewById(R.id.btnStart);
			buttonStart.setTypeface(((MapplasApplication)getContext().getApplicationContext()).getBoldTypeFace());

			if(buttonStart != null) {
				if(o.getType().equalsIgnoreCase("application")) {
					if(o.getInternalApplicationInfo() != null) {
						// Start
						buttonStart.setBackgroundResource(R.drawable.badge_launch);
						buttonStart.setText("");
					}
					else {
						// Install
						if(o.getAppPrice() > 0) {
							buttonStart.setBackgroundResource(R.drawable.badge_price);
							// buttonStart.setText("$" + o.getAppPrice());
							// String country = o.getCountry();
							NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
							buttonStart.setText(nf.format(o.getAppPrice()));

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

				buttonStart.setTag(o);

				if(isNewView) {

					buttonStart.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							final App anonLoc = (App)(v.getTag());
							if(anonLoc != null) {
								String strUrl = anonLoc.getAppUrl();
								if(!(strUrl.startsWith("http://") || strUrl.startsWith("https://") || strUrl.startsWith("market://")))
									strUrl = "http://" + strUrl;

								if(anonLoc.getInternalApplicationInfo() != null) {
									Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(anonLoc.getInternalApplicationInfo().packageName);
									context.startActivity(appIntent);
								}
								else {
									Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl));
									context.startActivity(browserIntent);
								}
							}
						}
					});
				}
			}

			Button buttonNavigation = (Button)v.findViewById(R.id.btnNav);
			buttonNavigation.setTag(o);
			if(isNewView) {
				buttonNavigation.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						App anonLoc = (App)v.getTag();
						Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + MapplasActivity.GetModel().currentLocation + "&daddr=" + anonLoc.getLatitude() + "," + anonLoc.getLongitude() + "&dirflg=w"));
						context.startActivity(navigation);
					}
				});
			}

			final ViewFlipper vf = (ViewFlipper)v.findViewById(R.id.vfRowLoc);
			vf.setInAnimation(null);
			vf.setOutAnimation(null);
			vf.setDisplayedChild(0);

			final ImageView iv = (ImageView)v.findViewById(R.id.imgLogo);
			iv.setTag(vf);
			iv.setImageResource(R.drawable.ic_refresh);

			if(isNewView) {
				iv.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
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
					}
				});

			}

			String strUrl = o.getAppLogo();
			if(strUrl != "") {
				MapplasActivity.getDbd().loadDrawable(strUrl, iv, this.context.getResources().getDrawable(R.drawable.ic_refresh));
			}
			else {
				iv.setImageResource(R.drawable.ic_refresh);
			}

			LinearLayout auxll = (LinearLayout)v.findViewById(R.id.id_rowloc_unpressed);
			auxll.setTag(position);
			auxll.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(context, AppDetail.class);
					intent.putExtra(Constants.MAPPLAS_DETAIL_APP, (int)((Integer)v.getTag()));
					((MapplasActivity)context).startActivityForResult(intent, Constants.SYNESTH_DETAILS_ID);

				}
			});

			final ImageView ivrc = (ImageView)v.findViewById(R.id.imgRonundC);
			if(o.isAuxPin()) {
				ivrc.setBackgroundResource(R.drawable.roundc_pinup_selector);
				// setImageResource(R.drawable.roundc_pinup_selector);
			}
			else {
				ivrc.setBackgroundResource(R.drawable.roundc_btn_selector);
				// setImageResource(R.drawable.roundc_btn_selector);
			}

			final ImageView ivAppRate = (ImageView)v.findViewById(R.id.btnRate);
			final ImageView ivAppPinUp = (ImageView)v.findViewById(R.id.btnPinUp);
			final ImageView ivAppShare = (ImageView)v.findViewById(R.id.btnShare);
			final ImageView ivAppBlock = (ImageView)v.findViewById(R.id.btnBlock);

			final LinearLayout lytPinUp = (LinearLayout)v.findViewById(R.id.lytPinup);
			final LinearLayout lytRate = (LinearLayout)v.findViewById(R.id.lytRate);
			final LinearLayout lytShare = (LinearLayout)v.findViewById(R.id.lytShare);
			final LinearLayout lytBlock = (LinearLayout)v.findViewById(R.id.lytBlock);

			final RatingBar rbRating = (RatingBar)v.findViewById(R.id.rbRating);

			final TextView lblRating = (TextView)v.findViewById(R.id.lblRating);
			lblRating.setTypeface(((MapplasApplication)getContext().getApplicationContext()).getTypeFace());

			rbRating.setRating(o.getAuxTotalRate());

			if(o.getAuxTotalRate() == 0) {
				lblRating.setText(R.string.unRated);
			}
			else {
				int auxCase = (int)Math.ceil(o.getAuxTotalRate());
				switch (auxCase) {
					case 1:
						lblRating.setText(R.string.poor);
						break;

					case 2:
						lblRating.setText(R.string.belowAvg);
						break;

					case 3:
						lblRating.setText(R.string.average);
						break;

					case 4:
						lblRating.setText(R.string.aboveAvg);
						break;

					case 5:
						lblRating.setText(R.string.excellent);
						break;
				}
			}

			ivAppRate.setTag(o);
			ivAppPinUp.setTag(o);
			ivAppShare.setTag(o);
			ivAppBlock.setTag(o);

			lytPinUp.setTag(o);
			lytRate.setTag(o);
			lytShare.setTag(o);
			lytBlock.setTag(o);

			lytRate.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final App anonLoc = (App)(v.getTag());
					LocalizationAdapter.mRateLoc = anonLoc;
					if(anonLoc != null) {
						RatingDialog myDialog = new RatingDialog(context, "", new OnReadyListener(), anonLoc.getAuxRate(), anonLoc.getAuxComment());
						myDialog.show();

						if(anonLoc != null) {
							try {
								Thread th = new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											NetRequests.ActivityRequest(MapplasActivity.GetModel().currentLocation, "rate", anonLoc.getId() + "", MapplasActivity.GetModel().currentUser.getId() + "");
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

			if(o.isAuxPin()) {
				ivAppPinUp.setImageResource(R.drawable.action_unpin_button);
				ttPinUp.setText(R.string.un_pin_up);
			}
			else {
				ivAppPinUp.setImageResource(R.drawable.action_pin_button);
				ttPinUp.setText(R.string.pin_up);
			}

			lytPinUp.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					final App anonLoc = (App)(v.getTag());
					if(anonLoc != null) {
						String auxuid = "0";
						if(MapplasActivity.GetModel().currentUser != null) {
							auxuid = MapplasActivity.GetModel().currentUser.getId() + "";
						}

						final String uid = auxuid;

						if(anonLoc.isAuxPin()) {
							ivAppPinUp.setImageResource(R.drawable.action_pin_button);
							ivrc.setBackgroundResource(R.drawable.roundc_btn_selector);
							// setImageResource(R.drawable.roundc_btn_selector);
							ttPinUp.setText(R.string.pin_up);
						}
						else {
							ivAppPinUp.setImageResource(R.drawable.action_unpin_button);
							ivrc.setBackgroundResource(R.drawable.roundc_pinup_selector);
							// setImageResource(R.drawable.roundc_pinup_selector);
							ttPinUp.setText(R.string.un_pin_up);
						}

						ivrc.invalidate();
						v.invalidate();
						MapplasActivity.listView.invalidate();

						try {
							Thread th = new Thread(new Runnable() {

								private final App privateLocalization = anonLoc;

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
										NetRequests.ActivityRequest(MapplasActivity.GetModel().currentLocation, "pin", privateLocalization.getId() + "", MapplasActivity.GetModel().currentUser.getId() + "");
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

			lytShare.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					final App anonLoc = (App)(v.getTag());
					if(anonLoc != null) {
						Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
						// Comprobar que existe y que está instalada la
						// aplicación en el teléfono
						// sharingIntent.setPackage("com.whatsapp");
						sharingIntent.setType("text/plain");
						String shareBody = anonLoc.getAppName() + " sharing via Synesth";
						// sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						// anonLoc.getAppName());
						sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
						context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share)));

						if(anonLoc != null) {
							try {
								Thread th = new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											NetRequests.ActivityRequest(MapplasActivity.GetModel().currentLocation, "share", anonLoc.getId() + "", MapplasActivity.GetModel().currentUser.getId() + "");
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

			lytBlock.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final App anonLoc = (App)(v.getTag());
					LocalizationAdapter.mBlockLoc = anonLoc;

					AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
					myAlertDialog.setTitle(R.string.block_title);
					myAlertDialog.setMessage(R.string.block_text);
					myAlertDialog.setPositiveButton(R.string.block_accept, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							try {
								// Eliminamos el item de la lista
								// vinner.setAnimation(fadeOutAnimation);
								vinner.startAnimation(fadeOutAnimation);

								// LocalizationAdapter.this.remove(object)

								Thread th = new Thread(new Runnable() {

									@Override
									public void run() {
										try {

											String auxuid = "0";
											if(MapplasActivity.GetModel().currentUser != null) {
												auxuid = MapplasActivity.GetModel().currentUser.getId() + "";
											}

											final String uid = auxuid;

											NetRequests.LikeRequest("m", Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, anonLoc.getId() + "", uid);
											NetRequests.ActivityRequest(MapplasActivity.GetModel().currentLocation, "block", anonLoc.getId() + "", MapplasActivity.GetModel().currentUser.getId() + "");
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

		return v;
	}

	private class OnReadyListener implements RatingDialog.ReadyListener {

		@Override
		public void ready(String name) {

			// Guardamos la valoración del usuario en el servidor

			if(!name.equals("CANCEL")) {
				// Enviamos la nota por internet
				String uid = "0";
				String id = "0";
				String resp = "";

				if(MapplasActivity.GetModel().currentUser != null) {
					uid = MapplasActivity.GetModel().currentUser.getId() + "";
				}

				try {
					String rat = name.substring(0, name.indexOf("|"));
					String com = name.substring(name.indexOf("|") + 1);

					resp = NetRequests.RateRequest(rat, com, LocalizationAdapter.mRateLoc.getId() + "", uid);
					Toast.makeText(context, resp, Toast.LENGTH_LONG).show();
				} catch (Exception exc) {
					Toast.makeText(context, exc.toString(), Toast.LENGTH_LONG).show();
				}
			}

		}
	}

}
