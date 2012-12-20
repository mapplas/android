package com.mapplas.app.adapters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.App;
import com.mapplas.model.SuperModel;
import com.mapplas.model.database.repositories.NotificationRepository;
import com.mapplas.model.database.repositories.RepositoryManager;
import com.mapplas.model.notifications.Notification;
import com.mapplas.utils.DateUtils;
import com.mapplas.utils.DrawableBackgroundDownloader;

public class NotificationWithHeaderAdapter extends BaseAdapter {

	private static final int TYPE_ITEM = 0;

	private static final int TYPE_SEPARATOR = 1;

	private static final int TYPE_MAX_COUNT = 2;

	private Context context;
	
	private SuperModel model;

	private LayoutInflater inflater;

	private LinkedHashMap<Long, ArrayList<Notification>> mData = new LinkedHashMap<Long, ArrayList<Notification>>();

	// 0 if it is not separator / 1 if it is separator
	private ArrayList<Integer> separatorSet = new ArrayList<Integer>();

	public NotificationWithHeaderAdapter(Context context, SuperModel model) {
		this.context = context;
		this.model = model;
		this.inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		NotificationRepository notificationRepository = RepositoryManager.notifications(this.context);
		this.mData = notificationRepository.getNotificationsSeparatedByLocation();
		this.initializeSeparatorSet();
		this.setAuxAppToNotifications();
	}

	@Override
	public int getCount() {
		int size = 0;
		for(Map.Entry<Long, ArrayList<Notification>> entry : this.mData.entrySet()) {
			size += entry.getValue().size() + 1;
		}
		return size;
	}

	@Override
	public Notification getItem(int position) {
		Notification notification = null;
		
		int itemType = this.separatorSet.get(position);
		int myCounter = 0;

		switch (itemType) {
			case TYPE_SEPARATOR:
				for(Map.Entry<Long, ArrayList<Notification>> entry : this.mData.entrySet()) {
					if(myCounter == position) {
						return entry.getValue().get(0);
					}
					myCounter += 1;
				}
				break;

			case TYPE_ITEM:
				for(Map.Entry<Long, ArrayList<Notification>> entry : this.mData.entrySet()) {
					myCounter += 1;
					ArrayList<Notification> value = entry.getValue();
					for(Notification currentNotification : value) {
						if(myCounter == position) {
							return currentNotification;
						}
						myCounter += 1;
					}
				}
				break;
		}

		return notification;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		int type = getItemViewType(position);

		Notification notification = this.getItem(position);

		if(convertView == null) {
			switch (type) {
				case TYPE_ITEM:
					view = this.inflater.inflate(R.layout.rownot, null);
					this.initializeNotificationCell(view, notification);
					break;

				case TYPE_SEPARATOR:
					view = this.inflater.inflate(R.layout.rownotheader, null);
					this.initializeNotificationHeaderCell(view, notification);
					break;
			}
			view.setTag(convertView);
		}
		else {
			view = (View)convertView.getTag();
		}

		return view;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		return this.separatorSet.get(position);
	}

	/**
	 * 
	 * Private methods
	 * 
	 */

	@SuppressWarnings("unused")
	private void initializeSeparatorSet() {
		for(Map.Entry<Long, ArrayList<Notification>> entry : this.mData.entrySet()) {
			this.separatorSet.add(TYPE_SEPARATOR);

			ArrayList<Notification> value = entry.getValue();
			for(Notification currentNotification : value) {
				this.separatorSet.add(TYPE_ITEM);
			}
		}
	}

	private void initializeNotificationCell(View view, Notification notification) {
		Typeface normalTypeface = ((MapplasApplication)this.context.getApplicationContext()).getTypeFace();

		TextView tt = (TextView)view.findViewById(R.id.lblTitle);
		tt.setText(notification.getName());
		tt.setTypeface(normalTypeface);

		tt = (TextView)view.findViewById(R.id.lblDescription);
		tt.setTypeface(normalTypeface);
		tt.setText(notification.getDescription());

		tt = (TextView)view.findViewById(R.id.lblDate);
		tt.setTypeface(normalTypeface);
		tt.setText(DateUtils.FormatSinceDate(notification.getDate(), notification.getHour(), this.context));

		ImageView iv = (ImageView)view.findViewById(R.id.imgLogo);
		new DrawableBackgroundDownloader().loadDrawable(notification.getAuxApp().getAppLogo(), iv, this.context.getResources().getDrawable(R.drawable.ic_template));
	}

	private void initializeNotificationHeaderCell(View view, Notification notification) {
		Typeface normalTypeface = ((MapplasApplication)this.context.getApplicationContext()).getTypeFace();

		TextView tt = (TextView)view.findViewById(R.id.rownote_header_title);
		tt.setText(notification.currentLocation());
		tt.setTypeface(normalTypeface);
	}
	
	private void setAuxAppToNotifications() {
		for(Map.Entry<Long, ArrayList<Notification>> entry : this.mData.entrySet()) {
			ArrayList<Notification> value = entry.getValue();
			for(Notification currentNotification : value) {
				int appId = currentNotification.getIdLocalization();
				currentNotification.setAuxApp(this.searchAppWithId(appId));
			}
		}
	}

	private App searchAppWithId(int appId) {		
		ArrayList<App> appList = this.model.appList();
		for(App currentApp : appList) {
			if(currentApp.getId() == appId) {
				return currentApp;
			}
		}
		return null;
	}

}
