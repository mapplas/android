package com.mapplas.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.CommentAdapter;
import com.mapplas.model.Constants;
import com.mapplas.model.Localization;

public class Comments extends Activity {
	
	/* Debug Values */
	private static final boolean mDebug = true;
	
	/* Static Members */
	
	
	/* Properties */
	private Localization mLoc = null;
	private ListAdapter mListAdapter = null;
	
	/* Methods */
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.comments);
        
        // Get the index of the app
        Bundle extras = getIntent().getExtras();
        int index = extras.getInt(Constants.SYNESTH_DETAIL_INDEX);
        
        // Get Localization
        this.mLoc = MapplasActivity.GetModel().localizations.get(index);
        
        // Configure Data
		TextView tv = (TextView) findViewById(R.id.lblTitle);
		tv.setTypeface(MapplasActivity.getTypeFace());
		
        
        Button btn = (Button) findViewById(R.id.btnBack);
        btn.setTypeface(MapplasActivity.getTypeFace());
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        
    	final ListView lv = (ListView) findViewById(R.id.lvLista);
        
        this.mListAdapter = new CommentAdapter(this, R.layout.rowcom, this.mLoc.getAuxComments());
        lv.setAdapter(this.mListAdapter);
        
        
        
    }
}