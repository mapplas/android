package com.mapplas.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import app.mapplas.com.R;

import com.mapplas.model.Constants;
import com.mapplas.model.JsonParser;
import com.mapplas.model.Localization;
import com.mapplas.utils.NetRequests;

public class UserForm extends Activity {
	
	final Animation animFlipInNext = AnimationUtils.loadAnimation(MapplasActivity.GetAppContext(), R.anim.flipinnext);
    final Animation animFlipOutNext = AnimationUtils.loadAnimation(MapplasActivity.GetAppContext(), R.anim.flipoutnext);
    final Animation animFlipInPrevious = AnimationUtils.loadAnimation(MapplasActivity.GetAppContext(), R.anim.flipinprevious);
    final Animation animFlipOutPrevious = AnimationUtils.loadAnimation(MapplasActivity.GetAppContext(), R.anim.flipoutprevious);
	
	private RotateAnimation	flipAnimation;
	private RotateAnimation	reverseFlipAnimation;
	
	private RotateAnimation	refreshAnimation;
	
	private Uri mImageCaptureUri;
	
	private static String mCurrentResponse = "";
	
	private static ListView mPrivateList = null;
	private static Context mCurrentContext = null;
	private static View mPrivateFooter = null;
	private static View mPrivateFooterInfo= null;
	private static ImageView mPrivateRefreshIcon = null;

	static Handler mHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	    	
	    	JsonParser jp = new JsonParser();
	    	UserLocalizationAdapter ula2;
	    	ArrayList<Localization> locs;
	    	
	    	mPrivateList.removeFooterView(mPrivateFooter);
	    	
	    	
	      switch(msg.what)
	      {
	      case Constants.SYNESTH_USER_PINUPS_ID:
	    	  	locs = jp.SimpleParseLocalizations(UserForm.mCurrentResponse);
				
				ula2 = new UserLocalizationAdapter(UserForm.mCurrentContext, R.id.lblTitle, locs, UserLocalizationAdapter.PINUP);
				
				UserForm.mPrivateList.setAdapter(ula2);
	    	  break;
	    	  
	      case Constants.SYNESTH_USER_LIKES_ID:
	    	    locs = jp.SimpleParseLocalizations(UserForm.mCurrentResponse);
				
				ula2 = new UserLocalizationAdapter(UserForm.mCurrentContext, R.id.lblTitle, locs, UserLocalizationAdapter.FAVOURITE);
				
				UserForm.mPrivateList.setAdapter(ula2);	  
	    	  break;
	    	  
	      case Constants.SYNESTH_USER_BLOCKS_ID:
	    	    locs = jp.SimpleParseLocalizations(UserForm.mCurrentResponse);
				
				ula2 = new UserLocalizationAdapter(UserForm.mCurrentContext, R.id.lblTitle, locs, UserLocalizationAdapter.BLOCK);
				
				UserForm.mPrivateList.setAdapter(ula2);
	    	  break;
	      }
	    }
	  };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.user);
        
        refreshAnimation = new RotateAnimation(-360, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		refreshAnimation.setInterpolator(new LinearInterpolator());
		refreshAnimation.setDuration(1500);
		refreshAnimation.setFillAfter(true);
		refreshAnimation.setRepeatCount(Animation.INFINITE);
		refreshAnimation.setRepeatMode(Animation.RESTART);
        
        UserForm.mCurrentContext = this;
        
        View header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.profile_header, null);
        mPrivateFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.profile_footer, null);
        mPrivateFooterInfo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.profile_footer_info, null);
        mPrivateRefreshIcon = (ImageView) mPrivateFooter.findViewById(R.id.ivImage);
        
        ListView lvLista = (ListView) findViewById(R.id.lvList);
        lvLista.addHeaderView(header);        
        lvLista.addFooterView(mPrivateFooter);
        lvLista.addFooterView(mPrivateFooterInfo);
        
        mPrivateRefreshIcon.startAnimation(refreshAnimation);
        UserLocalizationAdapter ula = new UserLocalizationAdapter(UserForm.this, R.id.lblTitle, new ArrayList<Localization>(), UserLocalizationAdapter.BLOCK);
		
        lvLista.setAdapter(ula);
        
        // Define the divider color of the listview
        ColorDrawable color= new ColorDrawable(this.getResources().getColor(R.color.user_list_divider));
        lvLista.setDivider(color);
        lvLista.setDividerHeight(1);
        
        UserForm.mPrivateList = lvLista;
        
        try
		{
			Thread th = new Thread(new Runnable() {							
				@Override
				public void run() {
					try
					{
						mCurrentResponse = NetRequests.UserPinUpsRequest(MapplasActivity.GetModel().currentUser.getId() + "");
						Message.obtain(mHandler, Constants.SYNESTH_USER_PINUPS_ID, null).sendToTarget();
						
					}catch (Exception exc)
					{
						Log.d(this.getClass().getSimpleName(), "Get PinUps", exc);
					}
			        
				}
			});
			th.start();
		}catch(Exception exc)
		{
			Log.i(getClass().getSimpleName(), "Action Get PinUps: " + exc);
		}
        
        
        
        TextView lblProfile = (TextView) findViewById(R.id.lblProfile);
        lblProfile.setTypeface(MapplasActivity.mTypefaceItalic);
        
        final Animation animFlipInNext = AnimationUtils.loadAnimation(MapplasActivity.GetAppContext(), R.anim.flipinnext);
        final Animation animFlipOutNext = AnimationUtils.loadAnimation(MapplasActivity.GetAppContext(), R.anim.flipoutnext);
        final Animation animFlipInPrevious = AnimationUtils.loadAnimation(MapplasActivity.GetAppContext(), R.anim.flipinprevious);
        final Animation animFlipOutPrevious = AnimationUtils.loadAnimation(MapplasActivity.GetAppContext(), R.anim.flipoutprevious);
        
        flipAnimation = new RotateAnimation(0, 90, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		flipAnimation.setInterpolator(new LinearInterpolator());
		flipAnimation.setDuration(300);
		flipAnimation.setFillAfter(true);

		reverseFlipAnimation = new RotateAnimation(90, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseFlipAnimation.setInterpolator(new LinearInterpolator());
		reverseFlipAnimation.setDuration(300);
		reverseFlipAnimation.setFillAfter(true);
        
        final ViewFlipper vfRowProfile = (ViewFlipper) findViewById(R.id.vfRowProfile);
        final Button btnEdit = (Button) findViewById(R.id.btnEdit);
        final Button btnSend = (Button) findViewById(R.id.btnSend);
        final Button btnLogout = (Button) findViewById(R.id.btnLogout);
        final Button btnProfileImage = (Button) findViewById(R.id.btnProfileImage);
        final ImageView imgUser = (ImageView) findViewById(R.id.imgUser);
        
        btnEdit.setTypeface(MapplasActivity.getTypeFace());
        btnSend.setTypeface(MapplasActivity.getTypeFace());
        
        final TextView lblName = (TextView) findViewById(R.id.lblName);
        final TextView lblEmail = (TextView) findViewById(R.id.lblEmail);
        
        lblName.setTypeface(MapplasActivity.getTypeFace());
        lblEmail.setTypeface(MapplasActivity.getTypeFace());
        
        if(MapplasActivity.GetModel().currentUser.getName().equals(""))
        {
        	lblName.setText(R.string.name_not_set);
        }else
        {
        	lblName.setText(MapplasActivity.GetModel().currentUser.getName());
        }
        
        if(MapplasActivity.GetModel().currentUser.getEmail().equals(""))
        {
        	lblName.setTag(R.string.email_not_set);
        }else
        {
        	lblEmail.setText(MapplasActivity.GetModel().currentUser.getEmail());
        }
        
        final EditText txtName = (EditText) findViewById(R.id.txtName);
        final EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
        
        txtName.setTypeface(MapplasActivity.getTypeFace());
        txtEmail.setTypeface(MapplasActivity.getTypeFace());
        
        txtName.setText(MapplasActivity.GetModel().currentUser.getName());
        txtEmail.setText(MapplasActivity.GetModel().currentUser.getEmail());
        
        btnEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(vfRowProfile.indexOfChild(vfRowProfile.getCurrentView()) == 0)
				{
					vfRowProfile.setInAnimation(animFlipInNext);
					vfRowProfile.setOutAnimation(animFlipOutNext);
					vfRowProfile.showNext();
				}else
				{
					vfRowProfile.setInAnimation(animFlipInPrevious);
					vfRowProfile.setOutAnimation(animFlipOutPrevious);
					vfRowProfile.showPrevious();
				}
			}
		});
        
        btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Send and store the user data.
				MapplasActivity.GetModel().currentUser.setName(txtName.getText().toString());
				MapplasActivity.GetModel().currentUser.setEmail(txtEmail.getText().toString());
				
				txtName.setText(MapplasActivity.GetModel().currentUser.getName());
		        txtEmail.setText(MapplasActivity.GetModel().currentUser.getEmail());
		        
		        if(MapplasActivity.GetModel().currentUser.getName().equals(""))
		        {
		        	lblName.setText(R.string.name_not_set);
		        }else
		        {
		        	lblName.setText(MapplasActivity.GetModel().currentUser.getName());
		        }
		        
		        if(MapplasActivity.GetModel().currentUser.getEmail().equals(""))
		        {
		        	lblName.setTag(R.string.email_not_set);
		        }else
		        {
		        	lblEmail.setText(MapplasActivity.GetModel().currentUser.getEmail());
		        }
		        
		        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		        imm.hideSoftInputFromWindow(txtName.getWindowToken(), 0);
		        imm.hideSoftInputFromWindow(txtEmail.getWindowToken(), 0);
		        
		        try
				{
					Thread th = new Thread(new Runnable() {							
						@Override
						public void run() {
							try {
								NetRequests.UserEditRequest(MapplasActivity.GetModel().currentUser.getName(), 
										MapplasActivity.GetModel().currentUser.getEmail(), 
										MapplasActivity.GetModel().currentUser.getImei(),
										MapplasActivity.GetModel().currentUser.getId() + "");
							} catch (Exception e) {
								Log.i(getClass().getSimpleName(), "Thread Edit User: " + e);
							}
						}
					});
					th.start();
					
				}catch(Exception exc)
				{
					Log.i(getClass().getSimpleName(), "Edit User: " + exc);
				}
				
				
				if(vfRowProfile.indexOfChild(vfRowProfile.getCurrentView()) == 0)
				{
					vfRowProfile.setInAnimation(animFlipInNext);
					vfRowProfile.setOutAnimation(animFlipOutNext);
					vfRowProfile.showNext();
				}else
				{
					vfRowProfile.setInAnimation(animFlipInPrevious);
					vfRowProfile.setOutAnimation(animFlipOutPrevious);
					vfRowProfile.showPrevious();
				}
			}
		});
        
        btnLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Send and store the user data.
				final String n = MapplasActivity.GetModel().currentUser.getName();
				final String e = MapplasActivity.GetModel().currentUser.getEmail();
				
				new AlertDialog.Builder(UserForm.this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle(R.string.logout)
		        .setMessage(R.string.really_logout)
		        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	MapplasActivity.GetModel().currentUser.setName("");
						MapplasActivity.GetModel().currentUser.setEmail("");
						
						txtName.setText(MapplasActivity.GetModel().currentUser.getName());
				        txtEmail.setText(MapplasActivity.GetModel().currentUser.getEmail());
				        

				        lblName.setText(R.string.name_not_set);
				        lblEmail.setText(R.string.email_not_set);
				        
				        try
						{
							Thread th = new Thread(new Runnable() {							
								@Override
								public void run() {
									try {
										NetRequests.ActivityRequest(MapplasActivity.GetModel().currentLocation,
												"logout (" + n + ":" + e + ")", 
												"0", 
												MapplasActivity.GetModel().currentUser.getId() + "");
										NetRequests.UserEditRequest("", 
												"", 
												MapplasActivity.GetModel().currentUser.getImei(),
												MapplasActivity.GetModel().currentUser.getId() + "");
									} catch (Exception e) {
										Log.i(getClass().getSimpleName(), "Thread Logout User: " + e);
									}
								}
							});
							th.start();
							
						}catch(Exception exc)
						{
							Log.i(getClass().getSimpleName(), "Logout User: " + exc);
						}	
		            }

		        })
		        .setNegativeButton(R.string.no, null)
		        .show();	
			}
		});
        
        
        
        Button btn = (Button) findViewById(R.id.btnBack);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
        
        btn = (Button) findViewById(R.id.btnLogout);
        
        if(MapplasActivity.GetModel().currentUser == null)
        {
        	btn.setBackgroundResource(R.drawable.ic_refresh);
        }
        
        final LinearLayout lytBlocks = (LinearLayout) findViewById(R.id.lytBlocks);
        final LinearLayout lytPinUps = (LinearLayout) findViewById(R.id.lytPinups);
        final LinearLayout lytRates = (LinearLayout) findViewById(R.id.lytRates);
        final LinearLayout lytLikes = (LinearLayout) findViewById(R.id.lytLikes);
        
        final ListView lvList = (ListView) findViewById(R.id.lvList);
        
        
        lytBlocks.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Obtain blocked items data
					UserForm.mPrivateList.removeFooterView(mPrivateFooterInfo);
					lytBlocks.setBackgroundResource(Color.TRANSPARENT);
					lytPinUps.setBackgroundResource(Color.TRANSPARENT);
					lytRates.setBackgroundResource(Color.TRANSPARENT);
					lytLikes.setBackgroundResource(Color.TRANSPARENT);
					
					lytBlocks.setBackgroundResource(R.drawable.bgd_tab_pressed);
				
					UserForm.mPrivateList.addFooterView(mPrivateFooter);
					UserForm.mPrivateList.addFooterView(mPrivateFooterInfo);
			        mPrivateRefreshIcon.startAnimation(refreshAnimation);
			        UserLocalizationAdapter ula = new UserLocalizationAdapter(UserForm.this, R.id.lblTitle, new ArrayList<Localization>(), UserLocalizationAdapter.BLOCK);
					
			        UserForm.mPrivateList.setAdapter(ula);
			        
			        try
					{
						Thread th = new Thread(new Runnable() {							
							@Override
							public void run() {
								try
								{
									mCurrentResponse = NetRequests.UserBlocksRequest(MapplasActivity.GetModel().currentUser.getId() + "");
									Message.obtain(mHandler, Constants.SYNESTH_USER_BLOCKS_ID, null).sendToTarget();
									
								}catch (Exception exc)
								{
									Log.d(this.getClass().getSimpleName(), "Get Blocks", exc);
								}
						        
							}
						});
						th.start();
					}catch(Exception exc)
					{
						Log.i(getClass().getSimpleName(), "Action Get Blocks: " + exc);
					}
				}
		});
        
        lytPinUps.setBackgroundResource(R.drawable.bgd_tab_pressed);
        lytPinUps.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Obtain blocked items data
				UserForm.mPrivateList.removeFooterView(mPrivateFooterInfo);
				lytBlocks.setBackgroundResource(Color.TRANSPARENT);
				lytPinUps.setBackgroundResource(Color.TRANSPARENT);
				lytRates.setBackgroundResource(Color.TRANSPARENT);
				lytLikes.setBackgroundResource(Color.TRANSPARENT);
				
						
				
				lytPinUps.setBackgroundResource(R.drawable.bgd_tab_pressed);
				
					UserForm.mPrivateList.addFooterView(mPrivateFooter);
					UserForm.mPrivateList.addFooterView(mPrivateFooterInfo);
			        mPrivateRefreshIcon.startAnimation(refreshAnimation);
			        UserLocalizationAdapter ula = new UserLocalizationAdapter(UserForm.this, R.id.lblTitle, new ArrayList<Localization>(), UserLocalizationAdapter.BLOCK);
					
			        UserForm.mPrivateList.setAdapter(ula);
			        
			        try
					{
						Thread th = new Thread(new Runnable() {							
							@Override
							public void run() {
								try
								{
									mCurrentResponse = NetRequests.UserPinUpsRequest(MapplasActivity.GetModel().currentUser.getId() + "");
									Message.obtain(mHandler, Constants.SYNESTH_USER_PINUPS_ID, null).sendToTarget();
									
								}catch (Exception exc)
								{
									Log.d(this.getClass().getSimpleName(), "Get PinUps", exc);
								}
						        
							}
						});
						th.start();
					}catch(Exception exc)
					{
						Log.i(getClass().getSimpleName(), "Action Get PinUps: " + exc);
					}
				}
		});
        
        lytLikes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Obtain blocked items data
				UserForm.mPrivateList.removeFooterView(mPrivateFooterInfo);
				lytBlocks.setBackgroundResource(Color.TRANSPARENT);
				lytPinUps.setBackgroundResource(Color.TRANSPARENT);
				lytRates.setBackgroundResource(Color.TRANSPARENT);
				lytLikes.setBackgroundResource(Color.TRANSPARENT);
				
				lytLikes.setBackgroundResource(R.drawable.bgd_tab_pressed);
				
				
				UserForm.mPrivateList.addFooterView(mPrivateFooter);
				UserForm.mPrivateList.addFooterView(mPrivateFooterInfo);
			        mPrivateRefreshIcon.startAnimation(refreshAnimation);
			        UserLocalizationAdapter ula = new UserLocalizationAdapter(UserForm.this, R.id.lblTitle, new ArrayList<Localization>(), UserLocalizationAdapter.BLOCK);
					
			        UserForm.mPrivateList.setAdapter(ula);

			        try
					{
						Thread th = new Thread(new Runnable() {							
							@Override
							public void run() {
								try
								{
									mCurrentResponse = NetRequests.UserLikesRequest(MapplasActivity.GetModel().currentUser.getId() + "");
									Message.obtain(mHandler, Constants.SYNESTH_USER_LIKES_ID, null).sendToTarget();
									
								}catch (Exception exc)
								{
									Log.d(this.getClass().getSimpleName(), "Get Likes", exc);
								}
						        
							}
						});
						th.start();
					}catch(Exception exc)
					{
						Log.i(getClass().getSimpleName(), "Action Get Likes: " + exc);
					}
				}
		});
        
        lytRates.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Obtain blocked items data	
					try
					{
						String response = NetRequests.UserRatesRequest(MapplasActivity.GetModel().currentUser.getId() + "");
						JsonParser jp = new JsonParser();
						
						ArrayList<Localization> locs = jp.SimpleParseLocalizations(response);
						
						UserLocalizationAdapter ula = new UserLocalizationAdapter(UserForm.this, R.id.lblTitle, locs, UserLocalizationAdapter.RATE);
						
						lvList.setAdapter(ula);

						
					}catch (Exception exc)
					{
						Log.d(this.getClass().getSimpleName(), "Get Rates", exc);
					}
			}
		});
        

        btnProfileImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				 
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				 
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.SYNESTH_DETAILS_CAMERA_ID);

			}
		});
        
        // Try to get image
        try
        {
        	SharedPreferences settings = getSharedPreferences("prefs", 0);
            String uri = settings.getString("user_image", "");
            
            if(!uri.equals(""))
            {
            	Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uri));
            	imgUser.setImageBitmap(bmp);
            }
        }catch(Exception exc)
        {
        	imgUser.setImageResource(R.drawable.ic_menu_profile);
        }
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   if (resultCode != RESULT_OK) return;
	   
	   		switch(requestCode)
	   		{
	   		case Constants.SYNESTH_DETAILS_CAMERA_ID:
				this.mImageCaptureUri = data.getData();
				
				//final ImageView imgUser = (ImageView) findViewById(R.id.imgUser);
				//Drawable dw = Drawable.createFromPath(mImageCaptureUri.getPath());
				
				//imgUser.setImageDrawable(dw);
				doCrop();
				break;
				
	   		case Constants.SYNESTH_DETAILS_CAMERA_ID2:
	   			Bundle extras = data.getExtras();
	   		 
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
 
                    final ImageView imgUser = (ImageView) findViewById(R.id.imgUser);
    				//Drawable dw = Drawable.createFromPath(mImageCaptureUri.getPath());
    				
    				imgUser.setImageBitmap(photo);
    				
    				String url = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "synesth", "synesth");
    				
    				SharedPreferences settings = getSharedPreferences("prefs", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("user_image", url);
					
					// Commit the edits!
					editor.commit();
                }
 
                File f = new File(mImageCaptureUri.getPath());
 
                if (f.exists()) f.delete();
	   			break;
	   		}
	}
	

	private void doCrop()
	{
	    Intent intent = new Intent("com.android.camera.action.CROP");
	    intent.setType("image/*");
	    intent.putExtra("outputX", 200);
	    intent.putExtra("outputY", 200);
	    intent.putExtra("aspectX", 1);
	    intent.putExtra("aspectY", 1);
	    intent.putExtra("scale", true);
	    intent.putExtra("return-data", true);
	    
	    List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
	
	    int size = list.size();
	
	    if (size == 0) {
	        Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
	
	        return;
	    } else {
	        intent.setData(mImageCaptureUri);
	
	        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.SYNESTH_DETAILS_CAMERA_ID2);
	    }
	}
}
