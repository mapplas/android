package com.mapplas.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

class Resizer implements Runnable {

	public static final float DEFAULT_VELOCITY = 10;
	public static final int DEFAUT_HEIGHT = 100;
	
	private int height = DEFAUT_HEIGHT;
    private float velocity = DEFAULT_VELOCITY;
	
	private long lastMilis = 0;
	
    private boolean bigger = false;
    
    private float totalHeight;
    
    private View objective;
    
    Resizer(View v) {
        this.objective = v;
    }

    void start() {
    	height = DEFAUT_HEIGHT;
    	velocity = DEFAULT_VELOCITY;
    	
    	this.lastMilis = System.currentTimeMillis();
    	
    	ViewGroup.LayoutParams params = this.objective.getLayoutParams();
    	this.totalHeight = params.height;
    	
    	if(this.totalHeight <= height)
    	{
    		this.bigger = true;
    	}else
    	{
    		this.bigger = false;
    	}
    	
    	this.objective.post(this);
        
        Log.i("Resizer", "Default Start!");
    }
    
    void start(int h, float v) {
    	height = h;
    	velocity = v;
    	
    	this.lastMilis = System.currentTimeMillis();
    	
    	ViewGroup.LayoutParams params = this.objective.getLayoutParams();
    	this.totalHeight = params.height;
    	
    	if(this.totalHeight <= height)
    	{
    		this.bigger = true;
    	}else
    	{
    		this.bigger = false;
    	}
    	
    	this.objective.post(this);
        
        Log.i("Resizer", "Custom Start! h=" + h + " v=" + v);
    }

    public void run() {

    	Log.i("Resizer", "Run");
    	long milis = System.currentTimeMillis();
    	float delta = ((milis - this.lastMilis) / 1000.0f) * this.velocity;
    	
    	
    	
    	ViewGroup.LayoutParams params = this.objective.getLayoutParams();
    	
    	if(bigger)
    	{
    		this.totalHeight += delta;
    		
    		if(totalHeight <= this.height)
    		{
    			params.height = (int) this.totalHeight;
    			this.objective.post(this);
    		}else
    		{
    			params.height = this.height;
    		}
    	}else
    	{
    		this.totalHeight -= delta;
    		
    		if(totalHeight >= this.height)
    		{
    			params.height = (int) this.totalHeight;
    			this.objective.post(this);
    		}else
    		{
    			params.height = this.height;
    		}
    	}
    	
    	this.objective.setLayoutParams(params);
    	this.objective.invalidate();
        
        this.lastMilis = milis;
    }
    
}