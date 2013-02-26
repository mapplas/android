package com.mapplas.app.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.notification.NotificationAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.SuperModel;
import com.mapplas.model.database.repositories.NotificationRepository;
import com.mapplas.model.database.repositories.RepositoryManager;
import com.mapplas.model.notifications.Notification;
import com.mapplas.utils.static_intents.SuperModelSingleton;

public class AppNotifications extends Activity {
	
	public static Integer typeHighlightedItem = 0;
	
	public static Integer typeNormalItem = 1;

	/* Debug Values */
	// private static final boolean mDebug = true;

	private ListAdapter mListAdapter = null;
	
	private SuperModel model = null;
	
	private LinkedHashMap<Long, ArrayList<Notification>> hashData = null;
	
	private ArrayList<Long> orderedDataKeys = new ArrayList<Long>();
	
	private ArrayList<Integer> notificationSet = new ArrayList<Integer>();

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

		NotificationRepository notificationRepository = RepositoryManager.notifications(this);
		this.hashData = notificationRepository.getNotificationsSeparatedByLocation();
		this.orderTableData();
		this.initializeNotificationSet();
		
		this.mListAdapter = new NotificationAdapter(this, R.layout.rownot, this.model.notificationList().getList(), this.model, this.notificationSet);
		ListView lv = (ListView)findViewById(R.id.lvLista);
		lv.setAdapter(this.mListAdapter);

		// Set notifications as shown
		notificationRepository.setNotificationsAsShown();
	}

	private void getDataFromBundle() {
		this.model = SuperModelSingleton.model;
	}
	
	private void orderTableData() {
		Set<Long> keySet = this.hashData.keySet();
		for(Long currentArrivalTimestamp : keySet) {
			this.orderedDataKeys.add(currentArrivalTimestamp);
		}
	
		Collections.sort(this.orderedDataKeys);
	}
	
	private void initializeNotificationSet() {
		boolean highlighted = false;
		
		for(Long currentArrivalTimestamp : this.orderedDataKeys) {
			highlighted = !highlighted;
			ArrayList<Notification> notifs = this.hashData.get(currentArrivalTimestamp);
			for(Notification notification : notifs) {
				if(highlighted) {
					notificationSet.add(AppNotifications.typeHighlightedItem);
				}
				else {
					this.notificationSet.add(AppNotifications.typeNormalItem);
				}
			}
			
		}
	}
}