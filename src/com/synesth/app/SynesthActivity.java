package com.synesth.app;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import app.synesth.com.R;

import com.synesth.model.Constants;
import com.synesth.model.JsonParser;
import com.synesth.model.Localization;
import com.synesth.model.SuperModel;
import com.synesth.model.User;
import com.synesth.utils.DrawableBackgroundDownloader;
import com.synesth.utils.NetRequests;

public class SynesthActivity extends Activity {
	
	/* Debug Values */
	private static final boolean mDebug = false;
	
	/* Static Members */
	private static Context mContext = null;
	private static SynesthActivity mActivity = null;
	
	private static boolean mIsFilter = false;
	private static LocationManager mLocationManager = null;
	private static LocationListener mLocationListener = null;
	
	private static Typeface mTypeface = null;
	public static Typeface mTypefaceBold = null;
	public static Typeface mTypefaceItalic = null;
	
	private static TextView mStatus = null;
	private static ImageView mMap = null;
	
	public static List<ApplicationInfo> applicationList = null;
	
	public static RelativeLayout mLayoutMain = null;
	public static AwesomeListView mPullToRefreshListView = null;
	public static LinearLayout mPullToRefreshHeader = null;
	
	public static ViewFlipper mFlipperSplash = null;
	public static LinearLayout mLayoutSplash = null;
	public static LinearLayout mLayoutContent = null;
	
	
	private static SharedPreferences mPreferences = null;
	
	private static DrawableBackgroundDownloader mDbd = new DrawableBackgroundDownloader();
	
	public static ApplicationInfo FindApplicationInfo(String id)
	{
		ApplicationInfo ret = null;
		
		for(int i = 0; i < SynesthActivity.applicationList.size(); i++)
		{
			ApplicationInfo ai = SynesthActivity.applicationList.get(i);
			if(id.contentEquals(ai.packageName))
			{
				ret = SynesthActivity.applicationList.get(i); 
			}
		}
		
		return ret;
	}
	
	public static Context GetAppContext()
	{
		return SynesthActivity.mContext;
	}
	
	public static SynesthActivity getAppActivity()
	{
		return SynesthActivity.mActivity;
	}
	
	public static Typeface getTypeFace()
	{
		return SynesthActivity.mTypeface;
	}
	
	public static SharedPreferences getAppPreferences()
	{
		return SynesthActivity.mPreferences;
	}
	
	public static LocalizationAdapter getLocalizationAdapter()
	{
		return SynesthActivity.mListAdapter;
	}
	
	public static DrawableBackgroundDownloader getDbd()
	{
		return SynesthActivity.mDbd;
	}
	
	/* Properties */
	private static final SuperModel model = new SuperModel();
	
	private static LocalizationAdapter mListAdapter = null;
	private static Button mButtonNotifications = null;
	
	private long mLastRequest = 0;
	private SplashScreen mSplashScreen = null;
	

	private static boolean mSplashActive = true;
	
	/* Message Handler */
	static Handler mHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	      switch(msg.what)
	      {
	      case Constants.SYNESTH_MAIN_STATUS_ID:
	    	  if(SynesthActivity.mStatus != null)
	    	  {
	    		  SynesthActivity.mStatus.setText((String)msg.obj);
	    	  }
	    	  break;
	    	  
	      case Constants.SYNESTH_MAIN_LIST_ID:
	    	  
	    	  if(SynesthActivity.mSplashActive)
	    	  {
	    		  final Animation fadeOutAnimation = new AlphaAnimation(1, 0);
	    		  fadeOutAnimation.setInterpolator(new AccelerateInterpolator()); //and this
	              fadeOutAnimation.setStartOffset(0);
	              fadeOutAnimation.setDuration(500);
	              
	              final Animation fadeInAnimation = new AlphaAnimation(0, 1);
	              fadeInAnimation.setInterpolator(new AccelerateInterpolator()); //and this
	              fadeInAnimation.setStartOffset(0);
	              fadeInAnimation.setDuration(500);
	              
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
	  					SynesthActivity.mLayoutMain.removeView(SynesthActivity.mLayoutSplash);
	  					SynesthActivity.mLayoutContent.startAnimation(fadeInAnimation);
	  				}
	  			});
	    		  
	              SynesthActivity.mLayoutMain.startAnimation(fadeOutAnimation);
	              SynesthActivity.mSplashActive = false;
	    	  }
	    	  
	    	  if(SynesthActivity.mListAdapter != null)
	    	  {
	    		  SynesthActivity.mListAdapter.clear();
	    		  
    			  ((SynesthActivity)SynesthActivity.getAppActivity()).RefreshLocalizations();
					SynesthActivity.mListAdapter.notifyDataSetChanged();
		        	
		        	for(int i = 0; i < model.localizations.size(); i++)
		        	{
		        		ApplicationInfo ai = SynesthActivity.FindApplicationInfo(model.localizations.get(i).getAppName());
		        		if(ai != null)
		        		{
		        			model.localizations.get(i).setInternalApplicationInfo(ai);
		        		}
		        	}
		        	
		        	
		        	
		        	if(model.notifications.size() > 0)
		        	{
		        		SynesthActivity.mButtonNotifications.setText(model.notifications.size() + "");
		        		SynesthActivity.mButtonNotifications.setBackgroundResource(R.drawable.menu_notifications_number_button);
		        	}else
		        	{
		        		SynesthActivity.mButtonNotifications.setText("");
		        		SynesthActivity.mButtonNotifications.setBackgroundResource(R.drawable.menu_notifications_button);
		        	}
					
					//((SynesthActivity)SynesthActivity.getAppActivity()).RefreshLocalizations();
		        	
		        	for(int i = 0; i < model.localizations.size(); i++)
		        	{
		        		ApplicationInfo ai = SynesthActivity.FindApplicationInfo(model.localizations.get(i).getAppName());
		        		if(ai != null)
		        		{
		        			model.localizations.get(i).setInternalApplicationInfo(ai);
		        		}
		        	}
		        	
		        	SynesthActivity.mListAdapter.notifyDataSetChanged();
		        	
		        	SynesthActivity.mPullToRefreshListView.FinishRefreshing();
		        	//(SynesthActivity.mPullToRefreshListView).onRefreshComplete();
	    	  }
	    	  break;
	      }
	    }
	  };
	
	/* Methods */
	public static Button GetButtonNotifications()
	{
		return mButtonNotifications;
	}
	
	public static SuperModel GetModel()
	{
		return model;
	}
	
	public static Localization GetLocalizationById(long id)
	{
		if(model != null)
		{
			for(int i = 0; i < model.localizations.size(); i++)
			{
				if(model.localizations.get(i).getId() == id)
				{
					return model.localizations.get(i);
				}
			}
		}
		
		return null;
	}
	
	public void RefreshLocalizations()
	{
		
		final ListView lv = (ListView) findViewById(R.id.lvLista);
        
		//SynesthActivity.mListAdapter = new LocalizationAdapter(this, R.layout.rowloc, model.localizations);
		//lv.setAdapter(SynesthActivity.mListAdapter);

		for(int i = 0; i < model.localizations.size(); i++)
		{
			SynesthActivity.mListAdapter.add(model.localizations.get(i));
		}
		
		
	}
	
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SynesthActivity.mContext = this;
        SynesthActivity.mActivity = this;
       
        
        // Obtenemos el IMEI como identificador (ANDROID_ID da problemas)
        TelephonyManager manager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE); 
        SynesthActivity.model.currentIMEI  = manager.getDeviceId(); 
        
        mPreferences = getApplicationContext().getSharedPreferences("synesth", Context.MODE_PRIVATE);
                
        // Identificamos contra el servidor
        
        try
        {
        	Thread th = new Thread(new Runnable() {							
				@Override
				public void run() {
					try {
			        	String response = NetRequests.UserIRequest(SynesthActivity.model.currentLocation, SynesthActivity.model.currentIMEI);
			        	JsonParser jp = new JsonParser();
			        	
			        	SynesthActivity.model.currentUser = jp.ParseUser(response);
			        	
			        }catch (Exception exc2)
			        {
			        	SynesthActivity.model.currentUser = null;
			        	Log.d(getClass().getSimpleName(), "Login: " + exc2);
			        }
				}
        	});
        	
        	th.run();
        }catch(Exception exc)
        {
        	SynesthActivity.model.currentUser = null;
        	Log.d(getClass().getSimpleName(), "Login: " + exc);
        }
        
        final PackageManager pm = getPackageManager();
        SynesthActivity.applicationList = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        
        setContentView(R.layout.main);
        
        SynesthActivity.mTypeface = Typeface.createFromAsset(getAssets(),
	            "fonts/OpenSans-Regular.ttf");
        
        SynesthActivity.mTypefaceBold = Typeface.createFromAsset(getAssets(),
	            "fonts/OpenSans-Bold.ttf");
        
        SynesthActivity.mTypefaceItalic = Typeface.createFromAsset(getAssets(),
	            "fonts/OpenSans-Italic.ttf");
        
        
        this.mSplashActive = true;
        SynesthActivity.mLayoutContent = (LinearLayout) findViewById(R.id.lytContent);
        this.mLayoutSplash = (LinearLayout) findViewById(R.id.id_splash);
        
        Button button = (Button) findViewById(R.id.btnProfile);
        button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SynesthActivity.this, UserForm.class);
				
				User usr = model.currentUser;
			
				if(usr != null)
				{
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTID_ID, usr.getId());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTNAME_ID, usr.getName());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTLASTNAME_ID, usr.getLastname());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTGENDER_ID, usr.getGender());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTBIRTHDATE_ID, usr.getBirthdate());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTLOGIN_ID, usr.getLogin());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTPASS_ID, usr.getPassword());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTEMAIL_ID, usr.getEmail());
				}
				
				SynesthActivity.this.startActivityForResult(intent, Constants.SYNESTH_USER_ID);
			}
		});
        
        button = (Button) findViewById(R.id.btnNotifications);
        SynesthActivity.mButtonNotifications = button;
        button.setTypeface(mTypeface);
        button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SynesthActivity.this, AppNotifications.class);
				
				SynesthActivity.this.startActivity(intent);
			}
		});
        
        SynesthActivity.mLayoutMain = (RelativeLayout) findViewById(R.id.layoutMain);
        SynesthActivity.mPullToRefreshListView = (AwesomeListView) findViewById(R.id.lvLista);

        SynesthActivity.mPullToRefreshHeader = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ptr_header, null);		
        
        RelativeLayout auxLL = (RelativeLayout) SynesthActivity.mPullToRefreshHeader.findViewById(R.id.llInnerPtr);
        TextView auxTV = (TextView) SynesthActivity.mPullToRefreshHeader.findViewById(R.id.lblAction);
        ImageView auxIV = (ImageView) SynesthActivity.mPullToRefreshHeader.findViewById(R.id.ivImage);
        
        View status = SynesthActivity.mPullToRefreshHeader.findViewById(R.id.lytStatus);
        LinearLayout mainContainer = (LinearLayout) findViewById(R.id.lytMainContainer);

        ViewGroup.LayoutParams paramsStatus = status.getLayoutParams();

        SynesthActivity.mPullToRefreshListView.InsertHeader(SynesthActivity.mPullToRefreshHeader, 
        		auxLL,
        		auxTV, 
        		auxIV, 
        		getResources().getDrawable(R.drawable.ic_pulltorefresh_arrow), 
        		getResources().getDrawable(R.drawable.ic_refresh_photo),
        		getResources().getString(R.string.ptr_pull_to_refresh), 
        		getResources().getString(R.string.ptr_pull_to_refresh), 
        		getResources().getString(R.string.ptr_release_to_refresh), 
        		getResources().getString(R.string.ptr_refreshing));
        
        SynesthActivity.mPullToRefreshListView.setOnReleasehHeaderListener(new AwesomeListView.OnRelease() {
			
			@Override
			public void onRelease() {
				try
        		{
        			mLocationManager.requestLocationUpdates(
        	  	          LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        	
        	  	}catch(Exception e1)
        	  	{
        	  		Log.i(getClass().getSimpleName(), "SynesthActivity.onCreate: " + e1);
        	  	}
			}
		});
        
        
        SynesthActivity.mListAdapter = new LocalizationAdapter(this, R.layout.rowloc, model.localizations);
        SynesthActivity.mPullToRefreshListView.setAdapter(SynesthActivity.mListAdapter);
        
        if(SynesthActivity.mPullToRefreshListView != null)
        {
	        
	        SynesthActivity.mPullToRefreshListView.setOnScrollListener(new AbsListView.OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					
					
					SynesthActivity.mPullToRefreshListView.mScrollState = scrollState;
					
					for(int i = 0; i < SynesthActivity.mPullToRefreshListView.getCount(); i++)
					{
						View v = SynesthActivity.mPullToRefreshListView.getChildAt(i);
						if(v != null)
						{
							ViewFlipper vf = (ViewFlipper) v.findViewById(R.id.vfRowLoc);
							if(vf != null)
							{
								vf.setInAnimation(null);
						        vf.setOutAnimation(null);
								vf.setDisplayedChild(0);
							}
						}
					}	
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					
				}
			});
	    }

        
        
        SynesthActivity.mStatus = (TextView) findViewById(R.id.lblStatus);
        SynesthActivity.mStatus.setTypeface(SynesthActivity.mTypeface);
        SynesthActivity.mStatus.setText(R.string.location_searching);
        
        //SynesthActivity.mPullToRefreshListView.mDebugText = SynesthActivity.mStatus;
        
        SynesthActivity.mMap = (ImageView) findViewById(R.id.imgMap);
        SynesthActivity.mMap.setBackgroundResource(R.drawable.icon_map);
        
        //AnimationDrawable frameAnimation = (AnimationDrawable) SynesthActivity.mMap.getBackground();
        
        //frameAnimation.setOneShot(false);
        //frameAnimation.start();
        
        try
    	{
    		SynesthActivity.mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	      if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
    	        
    	        SynesthActivity.mLocationListener = new LocationListener() {
    				private String textoToast = "";
    	        	
    				@Override
    				public void onStatusChanged(String provider, int status, Bundle extras) {
    					if(SynesthActivity.mDebug)
    					{
        					textoToast = "onStatusChanged: " + provider + ", " + status + ", " + extras;
        					Toast.makeText(getBaseContext(),
        					          textoToast,
        					          Toast.LENGTH_LONG).show();
    					}
    				}
    				
    				@Override
    				public void onProviderEnabled(String provider) {
    					if(SynesthActivity.mDebug)
    					{
        					textoToast = "onProviderEnabled: " + provider;
        					Toast.makeText(getBaseContext(),
        					          textoToast,
        					          Toast.LENGTH_LONG).show();
    					}      
    				}
    				
    				@Override
    				public void onProviderDisabled(String provider) {
    					if(SynesthActivity.mDebug)
    					{
        					textoToast = "onProviderDisabled: " + provider;
        					Toast.makeText(getBaseContext(),
        					          textoToast,
        					          Toast.LENGTH_LONG).show();
    					}	          
    				}
    				
    				@Override
    				public void onLocationChanged(Location location) {

    					SynesthActivity.mStatus.setText(R.string.location_done);
    					SynesthActivity.mMap.setBackgroundResource(R.drawable.icon_map);
    					
    					SynesthActivity.model.currentLocation = location.getLatitude() + "," + location.getLongitude();
    					
						try
						{
							SynesthActivity.mLocationManager.removeUpdates(SynesthActivity.mLocationListener);
							
							SynesthActivity.mMap.setBackgroundResource(R.drawable.icon_map);
							SynesthActivity.mStatus.setText(R.string.location_searching);
							
							
							(new ObtainLocalizationsTask(SynesthActivity.this)).execute(new Location[] {location});
					        (new ReverseGeocodingTask(SynesthActivity.this)).execute(new Location[] {location});
					        
					        
				        }catch(Exception e){
				        	Log.i(getClass().getSimpleName(), "LocationListener.onLocationChanged: " + e);
				        }
    				}
    			};
    			
    			try
        		{
        			mLocationManager.requestLocationUpdates(
        	  	          LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        	
        	  	}catch(Exception e1)
        	  	{
        	  		Log.i(getClass().getSimpleName(), "SynesthActivity.onCreate: " + e1);
        	  	}
            	

    	      } else {	
    	    	  SynesthActivity.mStatus.setText(R.string.location_error);
    	        Toast.makeText(getBaseContext(),
    	          R.string.location_error,
    	          Toast.LENGTH_LONG).show();
    	      }
    	}catch(Exception e1)
    	{
    		SynesthActivity.mStatus.setText(R.string.location_needed);
    		Toast.makeText(getBaseContext(),
      	          R.string.location_needed,
      	          Toast.LENGTH_LONG).show();
    		e1.printStackTrace();
    	}
        
        
    }
    
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected void onPostExecute(String[] result) {
            //mListItems.addFirst("Added after refresh...");
            // Call onRefreshComplete when the list has been refreshed.
        	
        	try
    		{
    			mLocationManager.requestLocationUpdates(
    	  	          LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
    	
    	  	}catch(Exception e1)
    	  	{
    	  		Log.i(getClass().getSimpleName(), "GetDataTask.onPostExecute: " + e1);
    	  	}
        	
            super.onPostExecute(result);
        }

		@Override
		protected String[] doInBackground(Void... arg0) {
			
			return null;
		}
    }
    
    private class ReverseGeocodingTask extends AsyncTask<Location, Void, Void> {
        Context mContext;

        public ReverseGeocodingTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected Void doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

            Location loc = params[0];
            List<Address> addresses = null;
            try {
                // Call the synchronous getFromLocation() method by passing in the lat/long values.
                addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            } catch (IOException e) {
            	try {
                    // Call the synchronous getFromLocation() method by passing in the lat/long values.
                    addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                } catch (IOException e2) {
                    e.printStackTrace();
                    // Update UI field with the exception.
                    Message.obtain(mHandler, Constants.SYNESTH_MAIN_STATUS_ID, getString(R.string.unable_geoloc)).sendToTarget();
                    
                    SynesthActivity.GetModel().currentDescriptiveGeoLoc = "";
                }
            }
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                // Format the first line of address (if available), city, and country name.
                String addressText = String.format("%s, %s.",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality());
                
                
                
                SynesthActivity.GetModel().currentDescriptiveGeoLoc = addressText;
                
                // Update the UI via a message handler.
                Message.obtain(mHandler, Constants.SYNESTH_MAIN_STATUS_ID, addressText).sendToTarget();
            }
            return null;
        }
    }
    
    
    private static class ObtainLocalizationsTask extends AsyncTask<Location, Void, Void> {
        Context mContext;

        private static Semaphore semaphore = new Semaphore(1);
        private static boolean occupied = false;
        
        public ObtainLocalizationsTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected Void doInBackground(Location... params) {

        	try
        	{
	        	semaphore.acquire();
	        	if(occupied)
	        	{
	        		semaphore.release();
	        		return null;
	        	}
	        	
	        	occupied = true;
	        	semaphore.release();
        	}catch(Exception exc)
        	{
        		Log.d(this.getClass().getSimpleName(), "doInBackground", exc);
        		return null;
        	}
        	
            Location location = params[0];
            
            String uid = "0";
            String serverResponse = "";
			
			if(SynesthActivity.GetModel().currentUser != null)
			{
				uid = SynesthActivity.GetModel().currentUser.getId() + "";
			}
			
			String strLastNotifications = SynesthActivity.mPreferences.getString(Constants.SYNESTH_LAST_NOTIFICATIONS, "");
			
			HttpClient hc = new DefaultHttpClient();
	        HttpPost post = new HttpPost("http://" + Constants.SYNESTH_SERVER + ":" + Constants.SYNESTH_SERVER_PORT + Constants.SYNESTH_SERVER_PATH + "ipc_locations.php?l=" + location.getLatitude() + "," + location.getLongitude() + "&uid=" + uid + "&ln=" + strLastNotifications + "&v=" + Constants.SYNESTH_VERSION);
	        
	        try
	        {
	        	HttpResponse rp = hc.execute(post);

		        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		        {
		        	serverResponse = EntityUtils.toString(rp.getEntity());
		        	
		        	// Comprobamos que el parser funciona correctamente
		        	JsonParser jp = new JsonParser();
		        	jp.Init();
		        	jp.ParseLocalizations(serverResponse, model, false);
		        	jp.Delete();
		        		        					        
		        }else
		        {
		        	if(SynesthActivity.mDebug)
					{
			        	String textoToast = "ESTADO: " + rp.getStatusLine().getStatusCode();
    					Toast.makeText(SynesthActivity.GetAppContext(),
    					          textoToast,
    					          Toast.LENGTH_LONG).show();
					}
		        }
            
		        Message.obtain(mHandler, Constants.SYNESTH_MAIN_LIST_ID, null).sendToTarget();
            
	        }catch(Exception exc)
	        {
	        	Log.i(getClass().getSimpleName(), "ObtainLocalizationTask.doInBackGround: " + exc);
	        }
            
	        try
	        {
		        semaphore.acquire();
	        	occupied = false;
	        	semaphore.release();
	        }catch(Exception exc)
        	{
        		Log.d(this.getClass().getSimpleName(), "doInBackground", exc);
        		return null;
        	}
	        
            return null;
        }
    }
    
    
}

