package com.mapplas.app.activities;

import com.mapplas.app.NotificationAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import app.mapplas.com.R;

public class AppNotifications extends Activity {
	
	/* Debug Values */
	private static final boolean mDebug = true;
	
	/* Static Members */
	
	
	/* Properties */
	
	private ListAdapter mListAdapter = null;
	
	/* Methods */
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.notifications);
        
        
        // Configure Data
		TextView tv = (TextView) findViewById(R.id.lblTitle);
		tv.setTypeface(MapplasActivity.typefaceItalic);
		
        
        Button btn = (Button) findViewById(R.id.btnBack);
        btn.setTypeface(MapplasActivity.getTypeFace());
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        
    	final ListView lv = (ListView) findViewById(R.id.lvLista);
        
        this.mListAdapter = new NotificationAdapter(this, R.layout.rownot, MapplasActivity.GetModel().notifications);
        lv.setAdapter(this.mListAdapter);
        
        
    }
}