package com.synesth.app;

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
import app.synesth.com.R;

import com.synesth.model.Constants;
import com.synesth.model.Localization;
import com.synesth.utils.NetRequests;

public class UserLocalizationAdapter extends ArrayAdapter<Localization> {

	public static final int BLOCK = 0;
	public static final int FAVOURITE = 1;
	public static final int RATE = 2;
	public static final int PINUP = 3;
	
	private int mType = 0;
	
    private ArrayList<Localization> items;
    private Context mContext = null;

    private static Semaphore mSemaphore = new Semaphore(1);
    
    private static Localization mRateLoc = null;
    private static Localization mBlockLoc = null;

    final Animation fadeOutAnimation = new AlphaAnimation(1, 0);

    
    /* Message Handler */
	static Handler mHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	    	
	      try{
		      UserLocalizationAdapter.mSemaphore.acquire();
		      switch(msg.what)
		      {
		    	  
		      case Constants.SYNESTH_ROWLOC_IMAGE_ID:
		    	  	
		    	  
		    	    String strUrl = (String) ((Object[])msg.obj)[0];
		            ImageView iv = (ImageView) ((Object[])msg.obj)[1];
		            Localization o = (Localization) ((Object[])msg.obj)[2];
		            Bitmap bmp = (Bitmap) ((Object[])msg.obj)[3];
		            
		            
		            if(bmp != null && iv != null)
		            {
		            	iv.setImageBitmap(bmp);
		            	iv.invalidate();
		            }
		            
		    	  break;
		      }
		      UserLocalizationAdapter.mSemaphore.release();
	      	}catch(Exception e)
		    {
		    	Log.i(this.getClass().getSimpleName(), "handleMessage: " + e);
		    }
	    }
	  };
    
    public UserLocalizationAdapter(Context context, int textViewResourceId, ArrayList<Localization> items, int type) {
            super(context, textViewResourceId, items);
            
            this.mType = type;
            
            this.items = items;
            
            this.mContext = context;
            
            fadeOutAnimation.setInterpolator(new AccelerateInterpolator()); //and this
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
					if(UserLocalizationAdapter.mBlockLoc != null)
					{
						UserLocalizationAdapter.this.remove(UserLocalizationAdapter.mBlockLoc);
					}
				}
			});
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    	boolean isNewView = false;
        View v = convertView;
            
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch(this.mType)
            {
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
        }else
        {
        	isNewView = false;
        }
        
        
        final View innerView = v;
        
        final Localization o = items.get(position);
        
        if (o != null) {
        	v.setTag(position);
        	
        	final TextView lblTitle = (TextView) v.findViewById(R.id.lblTitle);
        	//final TextView lblDescription = (TextView) v.findViewById(R.id.lblDescription);
        	final ImageView btnAction = (ImageView) v.findViewById(R.id.btnAction);
        	
        	final ImageView ivLogo = (ImageView) v.findViewById(R.id.imgLogo);
            
            String strUrl = o.getAppLogo();
    		if(strUrl != "")
    		{
    			SynesthActivity.getDbd().loadDrawable(strUrl, ivLogo, SynesthActivity.getAppActivity().getResources().getDrawable(R.drawable.ic_refresh));
    		}else
    		{
    			ivLogo.setImageResource(R.drawable.ic_refresh);
    		}
        	
        	lblTitle.setTypeface(SynesthActivity.getTypeFace());
        	//lblDescription.setTypeface(SynesthActivity.getTypeFace());
        	
        	lblTitle.setText(o.getName());
        	
        	btnAction.setTag(o);
        	btnAction.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final Localization anonLoc = (Localization) v.getTag();
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
							
							final String uid = SynesthActivity.GetModel().currentUser.getId() + "";
							
							switch(UserLocalizationAdapter.this.mType)
							{
							case BLOCK:
								try
								{
								  
									Thread th = new Thread(new Runnable() {							
										@Override
										public void run() {
											try {
												NetRequests.LikeRequest("mr", Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, anonLoc.getId() + "", uid);
												NetRequests.ActivityRequest(SynesthActivity.GetModel().currentLocation, "unblock", anonLoc.getId() + "", SynesthActivity.GetModel().currentUser.getId() + "");
											} catch (Exception e) {
												Log.i(getClass().getSimpleName(), "Thread Action unblock: " + e);
											}
										}
									});
									th.start();
																		
								}catch(Exception exc)
								{
									Log.i(getClass().getSimpleName(), "Action unblock: " + exc);
								}
				            	break;
				            	
				        	case FAVOURITE:
				        		try
								{
									Thread th = new Thread(new Runnable() {							
										@Override
										public void run() {
											try {
												NetRequests.LikeRequest("pr", Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, anonLoc.getId() + "", uid);
												NetRequests.ActivityRequest(SynesthActivity.GetModel().currentLocation, "unfavourite", anonLoc.getId() + "", SynesthActivity.GetModel().currentUser.getId() + "");
											} catch (Exception e) {
												Log.i(getClass().getSimpleName(), "Thread Action favourite: " + e);
											}
										}
									});
									th.start();
									
								}catch(Exception exc)
								{
									Log.i(getClass().getSimpleName(), "Action Favourite: " + exc);
								}
				            	break;
				            	
				        	case PINUP:
				        		try
								{
									Thread th = new Thread(new Runnable() {							
										@Override
										public void run() {
											try {
												NetRequests.PinRequest("unpin", Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, anonLoc.getId() + "", uid);
												NetRequests.ActivityRequest(SynesthActivity.GetModel().currentLocation, "unpin", anonLoc.getId() + "", SynesthActivity.GetModel().currentUser.getId() + "");
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
				        		break;
				        		
				        	case RATE:
				        		try
								{
								  
									Thread th = new Thread(new Runnable() {							
										@Override
										public void run() {
											try {
												NetRequests.UnrateRequest(anonLoc.getId() + "", uid);
												NetRequests.ActivityRequest(SynesthActivity.GetModel().currentLocation, "unrate", anonLoc.getId() + "", SynesthActivity.GetModel().currentUser.getId() + "");
											} catch (Exception e) {
												Log.i(getClass().getSimpleName(), "Thread Action unrate: " + e);
											}
										}
									});
									th.start();
																		
								}catch(Exception exc)
								{
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
