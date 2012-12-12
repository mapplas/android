package com.mapplas.app;

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

import com.mapplas.model.Constants;
import com.mapplas.model.Localization;
import com.mapplas.model.Photo;
import com.mapplas.utils.NetRequests;
import com.mapplas.utils.NumberUtils;

public class AppDetail extends Activity {
	
	/* Debug Values */
	private static final boolean mDebug = true;
	
	/* Static Members */
	
	/* Properties */
	private Localization mLoc = null;
	public static int mIndex = 0;

	private Uri imageUri;
	private String imagePath;
	
	private SliderListView alvComments = null;
	private ImageView ivArrow = null;
	private RotateAnimation	flipAnimation;
	private RotateAnimation	reverseFlipAnimation;
	
	private Resizer resizer;
	
	
	 DisplayMetrics metrics = new DisplayMetrics();
	 
	/* Methods */
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.app);
        
        // Get the index of the app
        Bundle extras = getIntent().getExtras();
        int index = extras.getInt(Constants.SYNESTH_DETAIL_INDEX);
        AppDetail.mIndex = index;
        
        
        flipAnimation = new RotateAnimation(0, 90, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		flipAnimation.setInterpolator(new LinearInterpolator());
		flipAnimation.setDuration(300);
		flipAnimation.setFillAfter(true);

		reverseFlipAnimation = new RotateAnimation(90, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseFlipAnimation.setInterpolator(new LinearInterpolator());
		reverseFlipAnimation.setDuration(300);
		reverseFlipAnimation.setFillAfter(true);
        
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        // Get Localization
        this.mLoc = MapplasActivity.GetModel().localizations.get(index);
        
        // Configure Data
        RatingBar rbRating = (RatingBar) findViewById(R.id.rbRating);
        rbRating.setRating(this.mLoc.getAuxTotalRate());
        
        this.alvComments = (SliderListView) findViewById(R.id.lvComments);
        this.ivArrow = (ImageView) findViewById(R.id.ivArrow);
        
        CommentAdapter auxComAd = new CommentAdapter(MapplasActivity.GetAppContext(), R.layout.rowcom, this.mLoc.getAuxComments());
        this.alvComments.setAdapter(auxComAd);
        
        TextView lblName = (TextView) findViewById(R.id.lblAppDetail);
        lblName.setText(this.mLoc.getName());
        
        ImageView iv = (ImageView) findViewById(R.id.imgLogo);
        Bitmap bmp = null;
		
        MapplasActivity.getDbd().loadDrawable(this.mLoc.getAppLogo(), iv, MapplasActivity.getAppActivity().getResources().getDrawable(R.drawable.ic_refresh));
		
		Button buttonStart = (Button) this.findViewById(R.id.btnStart);
		buttonStart.setTypeface(MapplasActivity.mTypefaceBold);
		
        if(buttonStart != null)
        {
        	if(this.mLoc.getType().equalsIgnoreCase("application"))
        	{
        		if(this.mLoc.getInternalApplicationInfo() != null)
        		{
        			// Start
        			buttonStart.setBackgroundResource(R.drawable.badge_launch);
        			buttonStart.setText("");
        		}else
        		{
        			// Install
        			if(this.mLoc.getAppPrice() > 0)
        			{
        				buttonStart.setBackgroundResource(R.drawable.badge_price);
        				buttonStart.setText("$" +this.mLoc.getAppPrice());
        				
        				NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        				buttonStart.setText(nf.format(this.mLoc.getAppPrice()));
        				
        			}else
        			{
        				buttonStart.setBackgroundResource(R.drawable.badge_free);
        				buttonStart.setText(R.string.free);
        			}
        		}
        	}else
        	{
        		// Info
        		buttonStart.setBackgroundResource(R.drawable.badge_html5);
        		buttonStart.setText("");
        	}
        	
        	buttonStart.setTag(this.mLoc);
        	buttonStart.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Localization anonLoc = (Localization)(v.getTag());
					if(anonLoc != null)
					{
						String strUrl = anonLoc.getAppUrl();
						if (!(strUrl.startsWith("http://") || strUrl.startsWith("https://") || strUrl.startsWith("market://")))
							strUrl = "http://" + strUrl;
						
						if(anonLoc.getInternalApplicationInfo() != null)
						{
							Intent appIntent = MapplasActivity.GetAppContext().getPackageManager().getLaunchIntentForPackage(anonLoc.getInternalApplicationInfo().packageName);
							MapplasActivity.GetAppContext().startActivity(appIntent);
							try
							{
								NetRequests.ActivityRequest(MapplasActivity.GetModel().currentLocation, "start", anonLoc.getId() + "", MapplasActivity.GetModel().currentUser.getId() + "");
								finish();
							}catch(Exception exc)
							{
								Log.i(getClass().getSimpleName(), "Action Start: " + exc);
							}
						}else
						{		
							Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl));
							MapplasActivity.GetAppContext().startActivity(browserIntent);
							try
							{
								NetRequests.ActivityRequest(MapplasActivity.GetModel().currentLocation, "install", anonLoc.getId() + "", MapplasActivity.GetModel().currentUser.getId() + "");
							}catch(Exception exc)
							{
								Log.i(getClass().getSimpleName(), "Action Install: " + exc);
							}
						}
					}
				}
			});
        }
        
		
		TextView tv = (TextView) findViewById(R.id.lblAppDetail);
		tv.setTypeface(MapplasActivity.mTypefaceItalic);
		
        tv = (TextView) findViewById(R.id.lblTitle);
        tv.setText(this.mLoc.getName());
        tv.setTypeface(MapplasActivity.getTypeFace());
        
        /*
        tv = (TextView) findViewById(R.id.lblDescription);
        tv.setText(this.mLoc.getAppName());
        tv.setTypeface(SynesthActivity.getTypeFace());
        */
        
        
        
        
        
        tv = (TextView) findViewById(R.id.lblMoreInfo);
        tv.setText(this.mLoc.getAppDescription());
        tv.setTypeface(MapplasActivity.mTypefaceItalic);      
        
        LinearLayout lytDescription = (LinearLayout) findViewById(R.id.lytDescription);
        lytDescription.setTag(mLoc);
        
        lytDescription.setOnClickListener(new View.OnClickListener() {
			
        	private boolean close = true;
        	
			@Override
			public void onClick(View v) {
								
				if(close)
				{
					TextView tv = (TextView) findViewById(R.id.lblMoreInfo);
					tv.setMaxLines(10000);
					ImageView iv = (ImageView) findViewById(R.id.imgDots);
					iv.setVisibility(ImageView.INVISIBLE);
				}else
				{
					
					TextView a = (TextView) findViewById(R.id.lblMoreInfo);
					a.setMaxLines(6);
					ImageView iv = (ImageView) findViewById(R.id.imgDots);
					iv.setVisibility(ImageView.VISIBLE);
				}
				close = !close;
			}
		});
        
        
        
        
        
        tv = (TextView) findViewById(R.id.lblDeveloper);
        tv.setTypeface(MapplasActivity.getTypeFace());
        
        tv = (TextView) findViewById(R.id.lblWeb);
        //tv.setText(this.mLoc.getAppUrl());
        tv.setTypeface(MapplasActivity.getTypeFace());
        tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppDetail.this.mLoc.getAppUrl()));
				MapplasActivity.GetAppContext().startActivity(browserIntent);
			}
		});
        
        
        tv = (TextView) findViewById(R.id.lblEMail);
        //tv.setText(this.mLoc.getAppEmail());
        tv.setTypeface(MapplasActivity.getTypeFace());
        tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);  
				//i.setType("text/plain"); //use this line for testing in the emulator  
				i.setType("message/rfc822") ; // use from live device
				i.putExtra(Intent.EXTRA_EMAIL, new String[]{"test@gmail.com"});  
				i.putExtra(Intent.EXTRA_SUBJECT,"subject goes here");  
				i.putExtra(Intent.EXTRA_TEXT,"body goes here");  
				startActivity(Intent.createChooser(i, "Select email application."));
			}
		});
        
        //tv = (TextView) findViewById(R.id.lblSEMail);
        //tv.setTypeface(SynesthActivity.getTypeFace());
        
        //tv = (TextView) findViewById(R.id.lblSWeb);
        //tv.setTypeface(SynesthActivity.getTypeFace());
        
        
        Button btn = (Button) findViewById(R.id.btnBack);
        btn.setTypeface(MapplasActivity.getTypeFace());
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        
        
        btn = (Button) findViewById(R.id.btnReportProblem);
        btn.setTypeface(MapplasActivity.getTypeFace());
        
        TextView tvComments = (TextView) findViewById(R.id.btnComments);
        tvComments.setTypeface(MapplasActivity.getTypeFace());
        tvComments.setTag(mLoc);
        
        TextView lblNumberOfComments = (TextView) findViewById(R.id.lblNumberOfComments);
        lblNumberOfComments.setText("(" + NumberUtils.FormatNumber(mLoc.getAuxTotalComments()) + ")");
        
        LinearLayout lytComments = (LinearLayout) findViewById(R.id.lytComments);
        lytComments.setTag(mLoc);
        
        lytComments.setOnClickListener(new View.OnClickListener() {
			
        	private boolean open = false;
        	
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(AppDetail.this, Comments.class);
				//intent.putExtra(Constants.SYNESTH_DETAIL_INDEX, AppDetail.mIndex);
				//AppDetail.this.startActivity(intent);
				int h = (int) (AppDetail.this.mLoc.getAuxComments().size() * 70 * metrics.density);
				float vel = AppDetail.this.mLoc.getAuxComments().size() * 500 * metrics.density;
				
				if(open)
				{
					AppDetail.this.alvComments.SlideUp(vel);
					AppDetail.this.ivArrow.startAnimation(reverseFlipAnimation);
				}else
				{
					
					AppDetail.this.alvComments.SlideDown(h, vel);
					AppDetail.this.ivArrow.startAnimation(flipAnimation);
				}
				open = !open;
				
				final Localization anonLoc = (Localization)(v.getTag());
				if(anonLoc != null)
				{
					try
					{
						Thread th = new Thread(new Runnable() {							
							@Override
							public void run() {
								try {
									NetRequests.ActivityRequest(MapplasActivity.GetModel().currentLocation, "showcomments", anonLoc.getId() + "", MapplasActivity.GetModel().currentUser.getId() + "");
								} catch (Exception e) {
									Log.i(getClass().getSimpleName(), "Thread Action Start: " + e);
								}
							}
						});
						th.start();
						
					}catch(Exception exc)
					{
						Log.i(getClass().getSimpleName(), "Action Start: " + exc);
					}
				}
			}
		});
        
        
        final LinearLayout lytPinup = (LinearLayout) findViewById(R.id.lytPinup);
        final LinearLayout lytRate = (LinearLayout) findViewById(R.id.lytRate);
        final LinearLayout lytLike = (LinearLayout) findViewById(R.id.lytLike);
        final LinearLayout lytBlock = (LinearLayout) findViewById(R.id.lytBlock);
        final LinearLayout lytShare = (LinearLayout) findViewById(R.id.lytShare);
        final LinearLayout lytPhone = (LinearLayout) findViewById(R.id.lytPhone);
        
        lytPinup.setTag(this.mLoc);
        lytRate.setTag(this.mLoc);
        lytLike.setTag(this.mLoc);
        lytBlock.setTag(this.mLoc);
        lytShare.setTag(this.mLoc);
        lytPhone.setTag(this.mLoc);
        
        
        final ImageView ivFavourite = (ImageView) findViewById(R.id.btnFavourite);
        ivFavourite.setTag(this.mLoc);
        
        if(this.mLoc.isAuxFavourite())
        {
        	ivFavourite.setImageResource(R.drawable.action_like_button_done);
        }
        
        lytLike.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Localization anonLoc = (Localization)(v.getTag());
				if(anonLoc != null)
				{
					String auxuid = "0";
					if(MapplasActivity.GetModel().currentUser != null)
					{
						auxuid = MapplasActivity.GetModel().currentUser.getId() + "";
					}
					
					final String uid = auxuid;

					String auxaction = "p";
					if(anonLoc.isAuxFavourite())
			        {
						auxaction = "pr";
						ivFavourite.setImageResource(R.drawable.action_like_button);
						anonLoc.setAuxFavourite(false);
			        }else
			        {
			        	auxaction = "p";
			        	ivFavourite.setImageResource(R.drawable.action_like_button_done);
			        	anonLoc.setAuxFavourite(true);
			        }
					
					final String action = auxaction;
					
					try
					{
						Thread th = new Thread(new Runnable() {							
							@Override
							public void run() {
								try {
									NetRequests.LikeRequest(action, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, anonLoc.getId() + "", uid);
									NetRequests.ActivityRequest(MapplasActivity.GetModel().currentLocation, "favourite", anonLoc.getId() + "", MapplasActivity.GetModel().currentUser.getId() + "");
								} catch (Exception e) {
									Log.i(getClass().getSimpleName(), "Thread Action Like: " + e);
								}
							}
						});
						th.start();
						
					}catch(Exception exc)
					{
						Log.i(getClass().getSimpleName(), "Action Like: " + exc);
					}
				}
			}
		});
        
        
        
        
        
        
        
        final ImageView ivPinup  = (ImageView) findViewById(R.id.btnPinUp);
        ivPinup.setTag(this.mLoc);
        
        if(!this.mLoc.isAuxPin())
		{
			ivPinup.setImageResource(R.drawable.action_pin_button);
		}else
		{
			ivPinup.setImageResource(R.drawable.action_unpin_button);
		}
        
        lytPinup.setOnClickListener(new View.OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				final Localization anonLoc = (Localization)(v.getTag());
				if(anonLoc != null)
				{
					String auxuid = "0";
					if(MapplasActivity.GetModel().currentUser != null)
					{
						auxuid = MapplasActivity.GetModel().currentUser.getId() + "";
					}
					
					final String uid = auxuid;
					
					if(anonLoc.isAuxPin())
					{
						ivPinup.setImageResource(R.drawable.action_pin_button);
					}else
					{
						ivPinup.setImageResource(R.drawable.action_unpin_button);
					}
					try
					{
						Thread th = new Thread(new Runnable() {							
							@Override
							public void run() {
								try {
									String action = "pin";
									if(anonLoc.isAuxPin())
									{
										action = "unpin";
										anonLoc.setAuxPin(false);
									}else
									{
										action = "pin";
										anonLoc.setAuxPin(true);
									}
									NetRequests.PinRequest(action, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, anonLoc.getId() + "", uid);
									NetRequests.ActivityRequest(MapplasActivity.GetModel().currentLocation, "pin", anonLoc.getId() + "", MapplasActivity.GetModel().currentUser.getId() + "");
								} catch (Exception e) {
									Log.i(getClass().getSimpleName(), "Thread Action PinUp: " + e);
								}
							}
						});
						th.start();
						
					}catch(Exception exc)
					{
						Log.i(getClass().getSimpleName(), "Action PinUp: " + exc);
					}
					
				}
			}
		});
        
        
        
        
        
        
        
        ImageView bimg = (ImageView) findViewById(R.id.btnShare);
        bimg.setTag(this.mLoc);
        
        lytShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Localization anonLoc = (Localization)(v.getTag());
				if(anonLoc != null)
				{
					Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
					// Comprobar que existe y que está instalada la aplicación en el teléfono
					//sharingIntent.setPackage("com.whatsapp");
					sharingIntent.setType("text/plain");
					String shareBody = anonLoc.getAppName() + " sharing via Synesth";
					//sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, anonLoc.getAppName());
					sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
					startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));
					
					
					if(anonLoc != null)
					{
						try
						{
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
						}catch(Exception exc)
						{
							Log.i(getClass().getSimpleName(), "Action Share: " + exc);
						}
					}
				}
			}
		});
        
        
        lytBlock.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Localization anonLoc = (Localization)(v.getTag());
				if(anonLoc != null)
				{
					if(anonLoc != null)
					{
						AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(AppDetail.this);
						 myAlertDialog.setTitle(R.string.block_title);
						 myAlertDialog.setMessage(R.string.block_text);
						 myAlertDialog.setPositiveButton(R.string.block_accept, new DialogInterface.OnClickListener() {

						  public void onClick(DialogInterface arg0, int arg1) {
							  try
								{
								  
									Thread th = new Thread(new Runnable() {							
										@Override
										public void run() {
											try {
												
												String auxuid = "0";
												if(MapplasActivity.GetModel().currentUser != null)
												{
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
									
									AppDetail.this.finish();
									
									// Eliminamos el item de la lista
									MapplasActivity.getLocalizationAdapter().remove(anonLoc);
									MapplasActivity.getLocalizationAdapter().notifyDataSetChanged();
									
									
								}catch(Exception exc)
								{
									Log.i(getClass().getSimpleName(), "Action PinUp: " + exc);
								}
						  }});
						 myAlertDialog.setNegativeButton(R.string.block_cancel, new DialogInterface.OnClickListener() {
						       
						  public void onClick(DialogInterface arg0, int arg1) {
						  // do something when the Cancel button is clicked
						  }});
						 myAlertDialog.show();
					}
				}
			}
		});
        
        
        /*
        btn = (Button) findViewById(R.id.btnCamera);
        btn.setTag(this.mLoc);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
				
				File photo = new File(Environment.getExternalStorageDirectory(), "synesth.jpg");
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
			            Uri.fromFile(photo));
			    imageUri = Uri.fromFile(photo);
			    imagePath = photo.getAbsolutePath();
				
				startActivityForResult(cameraIntent, Constants.SYNESTH_DETAILS_CAMERA_ID);
				
				
				
				final Localization anonLoc = (Localization)(v.getTag());
				if(anonLoc != null)
				{
					try
					{
						Thread th = new Thread(new Runnable() {							
							@Override
							public void run() {
								try {
									NetRequests.ActivityRequest(SynesthActivity.GetModel().currentLocation, "camera", anonLoc.getId() + "", SynesthActivity.GetModel().currentUser.getId() + "");
								} catch (Exception e) {
									Log.i(getClass().getSimpleName(), "Thread Action Camera: " + e);
								}
							}
						});
						th.start();
						
					}catch(Exception exc)
					{
						Log.i(getClass().getSimpleName(), "Action Camera: " + exc);
					}
				}
			}
		});
        */
        
        
        bimg = (ImageView) findViewById(R.id.btnRate);
        
        if(this.mLoc.getAuxRate() > 0)
        {
        	bimg.setImageResource(R.drawable.action_rate_button_done);
        }
        bimg.setTag(this.mLoc);
        
        lytRate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Localization anonLoc = (Localization)(v.getTag());
				if(anonLoc != null)
				{
					RatingDialog myDialog = new RatingDialog(AppDetail.this, "", new OnReadyListener(), anonLoc.getAuxRate(), anonLoc.getAuxComment());
			        myDialog.show();
			        
			        if(anonLoc != null)
					{
						try
						{
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
							
						}catch(Exception exc)
						{
							Log.i(getClass().getSimpleName(), "Action Rate: " + exc);
						}
					}
				}
			}
		});
        
        bimg = (ImageView) findViewById(R.id.btnPhone);
        bimg.setTag(this.mLoc);
        
        lytPhone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Localization anonLoc = (Localization)(v.getTag());
				if(anonLoc != null)
				{
			        String number = "tel:" + anonLoc.getPhone();
			        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(number)); 
			        startActivity(callIntent);
			        
			        if(anonLoc != null)
					{
						try
						{
							Thread th = new Thread(new Runnable() {							
								@Override
								public void run() {
									try {
										NetRequests.ActivityRequest(MapplasActivity.GetModel().currentLocation, "call", anonLoc.getId() + "", MapplasActivity.GetModel().currentUser.getId() + "");
									} catch (Exception e) {
										Log.i(getClass().getSimpleName(), "Thread Action Rate: " + e);
									}
								}
							});
							th.start();
							
						}catch(Exception exc)
						{
							Log.i(getClass().getSimpleName(), "Action Call: " + exc);
						}
					}
				}
			}
		});
        
        if(this.mLoc.getPhone().equals(""))
        {
        	lytPhone.setVisibility(View.GONE);
        }
        
        btn = (Button) findViewById(R.id.btnReportProblem);
        btn.setTag(this.mLoc);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Localization anonLoc = (Localization)(v.getTag());
				if(anonLoc != null)
				{
					ProblemDialog myDialog = new ProblemDialog(AppDetail.this, "", new OnReadyListenerProblem());
			        myDialog.show();
			        
			        if(anonLoc != null)
					{
						try
						{
							Thread th = new Thread(new Runnable() {							
								@Override
								public void run() {
									try {
										NetRequests.ActivityRequest(MapplasActivity.GetModel().currentLocation, "problem", anonLoc.getId() + "", MapplasActivity.GetModel().currentUser.getId() + "");
									} catch (Exception e) {
										Log.i(getClass().getSimpleName(), "Thread Action Rate: " + e);
									}
								}
							});
							th.start();
							
						}catch(Exception exc)
						{
							Log.i(getClass().getSimpleName(), "Action Problem: " + exc);
						}
					}
				}
			}
		});
        
        
        
        
        // Cargamos la imágenes en la galería
        Gallery gal = (Gallery) findViewById(R.id.galleryPhotos);
        ImageAdapter imgAdapter = new ImageAdapter(this, gal);
        
        for(Photo p : this.mLoc.getAuxPhotos())
        {
	        String strUrl = p.getPhoto();

	        imgAdapter.mImages.add(strUrl);
	    }
        
        gal.setAdapter(imgAdapter);
        
        if(imgAdapter.getCount() > 0)
        {
        	//resizer = new Resizer(gal);
        	//resizer.start(480, 500 * metrics.density);
        }
        
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == Constants.SYNESTH_DETAILS_CAMERA_ID) {  
        	if(resultCode == RESULT_OK)
        	{
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
                    
                    if(width > height)
                    {
                    	x = (width - height) / 2;
                    	width = height;
                    }else
                    {
                    	y = (height - width) / 2;
                    	height = width;
                    }
                    
                    Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, 256, 256, true);
                     
                    scaledBitmap.compress(CompressFormat.JPEG, 75, bos);
          			byte[] scaledImageData = bos.toByteArray();
          			
                     /*
                	FileInputStream fis = new FileInputStream(imagePath);
                	
                	byte[] bucket = new byte[32*1024]; 
                	
                	//Use buffering? No. Buffering avoids costly access to disk or network;
                    //buffering to an in-memory stream makes no sense.
                	ByteArrayOutputStream result = new ByteArrayOutputStream(bucket.length);
                    int bytesRead = 0;
                    while(bytesRead != -1){
                      //aInput.read() returns -1, 0, or more :
                      bytesRead = fis.read(bucket);
                      if(bytesRead > 0){
                        result.write(bucket, 0, bytesRead);
                      }
                    }
                    
                    byte[] scaledImageData = result.toByteArray();
                	*/
                   
                    
         			
         			String uid = "0";
                	String id = "0";
                	String resp = "";
                	
                	if(MapplasActivity.GetModel().currentUser != null)
                	{
                		uid = MapplasActivity.GetModel().currentUser.getId() + "";
                	}
         			
         			NetRequests.ImageRequest(scaledImageData, AppDetail.this.mLoc.getId() + "", uid);
         		
                     
                    Toast.makeText(this, selectedImage.toString(),
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                            .show();
                    Log.e("Camera", e.toString());
                }
        	}
        }  
    }  
    

    private class OnReadyListener implements RatingDialog.ReadyListener {
        @Override
        public void ready(String name) {
            
            
            // Guardamos la valoración del usuario en el servidor
            
            if(!name.equals("CANCEL"))
            {
            	// Ponemos en amarillo el botón
            	ImageView img = (ImageView) findViewById(R.id.btnRate);
            	img.setImageResource(R.drawable.action_rate_button_done);
            	
            	
            	// Enviamos la nota por internet
            	String uid = "0";
            	String id = "0";
            	String resp = "";
            	
            	if(MapplasActivity.GetModel().currentUser != null)
            	{
            		uid = MapplasActivity.GetModel().currentUser.getId() + "";
            	}
            	
            	
            	try
            	{
            		
            		String rat = name.substring(0, name.indexOf("|"));
            		String com = name.substring(name.indexOf("|") + 1);
            		
            		resp = NetRequests.RateRequest(rat, com, AppDetail.this.mLoc.getId() + "", uid);
            		Toast.makeText(AppDetail.this, resp, Toast.LENGTH_LONG).show();
            	}catch(Exception exc)
            	{
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
        	
        	if(MapplasActivity.GetModel().currentUser != null)
        	{
        		uid = MapplasActivity.GetModel().currentUser.getId() + "";
        	}
        	
        	
        	try
        	{
        		
        		resp = NetRequests.ProblemRequest(name, AppDetail.this.mLoc.getId() + "", uid);
        		Toast.makeText(AppDetail.this, resp, Toast.LENGTH_LONG).show();
        	}catch(Exception exc)
        	{
        		Toast.makeText(AppDetail.this, exc.toString(), Toast.LENGTH_LONG).show();
        	}
        }
    }
}