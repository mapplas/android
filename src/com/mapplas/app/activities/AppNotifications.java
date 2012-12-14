package com.mapplas.app.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.NotificationAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.AppNotification;
import com.mapplas.model.Constants;

public class AppNotifications extends Activity {

	/* Debug Values */
	// private static final boolean mDebug = true;

	private ListAdapter mListAdapter = null;

	private ArrayList<AppNotification> notificationList = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifications);

		this.getDataFromBundle();

		// Header title
		TextView tv = (TextView)findViewById(R.id.lblTitle);
		tv.setTypeface(((MapplasApplication)getApplicationContext()).getItalicTypeFace());

		// Back button
		Button btn = (Button)findViewById(R.id.btnBack);
		btn.setTypeface(((MapplasApplication)getApplicationContext()).getTypeFace());
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ListView lv = (ListView)findViewById(R.id.lvLista);
		this.mListAdapter = new NotificationAdapter(this, R.layout.rownot, this.notificationList);
		lv.setAdapter(this.mListAdapter);

	}

	@SuppressWarnings("unchecked")
	private void getDataFromBundle() {
		Bundle bundle = this.getIntent().getExtras();
		if(bundle != null) {
			if(bundle.containsKey(Constants.MAPPLAS_NOTIFICATION_LIST)) {
				this.notificationList = (ArrayList<AppNotification>)bundle.getSerializable(Constants.MAPPLAS_NOTIFICATION_LIST);
			}
		}
	}
}