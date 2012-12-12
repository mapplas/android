package com.mapplas.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Gallery;
import android.widget.ImageView;

public class DrawableBackgroundDownloader {    

private final Map<String, SoftReference<Drawable>> mCache = new HashMap<String, SoftReference<Drawable>>();   
private final LinkedList <Drawable> mChacheController = new LinkedList <Drawable> ();
private ExecutorService mThreadPool;  
private final Map<ImageView, String> mImageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());  

private RotateAnimation	rotateAnimation;


public static int MAX_CACHE_SIZE = 80; 
public int THREAD_POOL_SIZE = 1;

/**
 * Constructor
 */
public DrawableBackgroundDownloader() {  
    mThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);  
    
    rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
    rotateAnimation.setInterpolator(new LinearInterpolator());
    rotateAnimation.setDuration(1000);
    rotateAnimation.setFillAfter(true);
    rotateAnimation.setRepeatCount(10);
    rotateAnimation.setRepeatMode(RotateAnimation.RESTART);
}  


/**
 * Clears all instance data and stops running threads
 */
public void Reset() {
    ExecutorService oldThreadPool = mThreadPool;
    mThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    oldThreadPool.shutdownNow();

    mChacheController.clear();
    mCache.clear();
    mImageViews.clear();
}  



public void loadDrawable(final String url, final ImageView imageView,Drawable placeholder)
{
	this.loadDrawable(url, imageView, placeholder, false, 0);
}

public void loadDrawable(final String url, final ImageView imageView,Drawable placeholder, boolean keepRatio, int constantw) {  
    mImageViews.put(imageView, url);  
    Drawable drawable = getDrawableFromCache(url);  

    // check in UI thread, so no concurrency issues  
    if (drawable != null) {  
        //Log.d(null, "Item loaded from mCache: " + url);  
    	
    	imageView.clearAnimation();
    	imageView.setImageDrawable(drawable);
    	
    } else {  

    	imageView.startAnimation(rotateAnimation);
    	imageView.setImageDrawable(placeholder);  

        queueJob(url, imageView, placeholder, keepRatio, constantw);  
    }  
} 


private Drawable getDrawableFromCache(String url) {  
    if (mCache.containsKey(url)) {  
        return mCache.get(url).get();  
    }  

    return null;  
}

private synchronized void putDrawableInCache(String url,Drawable drawable) {  
    int chacheControllerSize = mChacheController.size();
    if (chacheControllerSize > MAX_CACHE_SIZE) 
        mChacheController.subList(0, MAX_CACHE_SIZE/2).clear();

    mChacheController.addLast(drawable);
    mCache.put(url, new SoftReference<Drawable>(drawable));

}  

private void queueJob(final String url, final ImageView imageView,final Drawable placeholder)
{
	this.queueJob(url, imageView, placeholder, false, 0);
}

private void queueJob(final String url, final ImageView imageView,final Drawable placeholder, final boolean keepRatio, final int constantw) {  
    /* Create handler in UI thread. */  
    final Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
        	String tag = mImageViews.get(imageView);  
        	
        	
            if (tag != null && tag.equals(url)) {
                if (imageView.isShown())
                    if (msg.obj != null) {
                    	imageView.clearAnimation();
                         
                    	Drawable dw = (Drawable) msg.obj;
                    	imageView.setImageDrawable((Drawable) msg.obj); 
                        if(keepRatio)
                        {
                    		float ratio = (float)dw.getIntrinsicWidth() / (float)dw.getIntrinsicHeight();
                    		int w = dw.getIntrinsicWidth();
                    		int h = dw.getIntrinsicHeight();
                    		
                    		w = constantw;
                    		h = constantw;
                    		
                    		if(dw.getIntrinsicWidth() > dw.getIntrinsicHeight())
                    		{
                    			h = (int) (constantw / ratio);
                    		}else
                    		{
                    			w = (int) (constantw * ratio);
                    		}
                    		
                    		//imageView.setLayoutParams(new Gallery.LayoutParams(w, h + ((480 - h) / 2)));
                    		imageView.setLayoutParams(new Gallery.LayoutParams(w, h));
                    		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    		
                    		//imageView.setPadding(0, (480 - h) / 2, 0, 0);
                        }
                        
                        imageView.invalidate();
                        
                        
                    } else {  
                        imageView.setImageDrawable(placeholder);  
                        //Log.d(null, "fail " + url);  
                    }
        	}
            
        }  
    };  

    mThreadPool.submit(new Runnable() {  
        @Override  
        public void run() {  
            final Drawable bmp = downloadDrawable(url);
            // if the view is not visible anymore, the image will be ready for next time in cache
            if (imageView.isShown())
            {
                Message message = Message.obtain();  
                message.obj = bmp;
                //Log.d(null, "Item downloaded: " + url);  

                handler.sendMessage(message);
            }
        	
        }  
    });  
}  



private Drawable downloadDrawable(String url) {  
    try {  
        InputStream is = getInputStream(url);

        Drawable drawable = Drawable.createFromStream(is, url);
        putDrawableInCache(url,drawable);  
        return drawable;  

    } catch (MalformedURLException e) {  
        e.printStackTrace();  
    } catch (IOException e) {  
        e.printStackTrace();  
    }  

    return null;  
}  


private InputStream getInputStream(String urlString) throws MalformedURLException, IOException {
    URL url = new URL(urlString);
    URLConnection connection;
    connection = url.openConnection();
    connection.setUseCaches(true); 
    connection.connect();
    InputStream response = connection.getInputStream();

    return response;
}

}
