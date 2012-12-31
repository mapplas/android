package com.mapplas.app.adapters.notification;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.activities.AppDetail;
import com.mapplas.app.activities.AppNotifications;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.app.async_tasks.LoadImageTask;
import com.mapplas.app.async_tasks.TaskAsyncExecuter;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.database.repositories.NotificationRepository;
import com.mapplas.model.database.repositories.RepositoryManager;
import com.mapplas.model.notifications.Notification;
import com.mapplas.utils.DateUtils;
import com.mapplas.utils.cache.CacheFolderFactory;
import com.mapplas.utils.cache.ImageFileManager;

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

	/**
	 * ViewHolders for eficient listViews
	 * 
	 */
	public static class NotificationCellHolder {

		ImageView logo;

		ImageView logoRoundCorner;

		TextView title;

		TextView date;

		TextView description;
	}

	public static class NotificationHeaderHolder {

		TextView location;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param model
	 */
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
					ArrayList<Notification> value = entry.getValue();
					for(Notification currentNotification : value) {
						if(myCounter == position) {
							return currentNotification;
						}
						myCounter += 1;
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
		NotificationCellHolder cellHolder;
		NotificationHeaderHolder headerHolder;

		int type = this.getItemViewType(position);
		Notification notification = this.getItem(position);

		if(convertView == null) {
			switch (type) {
				case TYPE_ITEM:

					cellHolder = new NotificationCellHolder();
					convertView = this.inflater.inflate(R.layout.rownot, null);

					cellHolder.logo = (ImageView)convertView.findViewById(R.id.imgLogo);
					cellHolder.logoRoundCorner = (ImageView)convertView.findViewById(R.id.imgRonundC);
					cellHolder.title = (TextView)convertView.findViewById(R.id.lblTitle);
					cellHolder.date = (TextView)convertView.findViewById(R.id.lblDate);
					cellHolder.description = (TextView)convertView.findViewById(R.id.lblDescription);

					this.initializeNotificationCell(cellHolder, notification);
					this.setClickListenerToView(convertView, notification);

					convertView.setTag(cellHolder);

					break;

				case TYPE_SEPARATOR:

					headerHolder = new NotificationHeaderHolder();
					convertView = this.inflater.inflate(R.layout.rownotheader, null);

					headerHolder.location = (TextView)convertView.findViewById(R.id.rownote_header_title);

					this.initializeNotificationHeaderCell(headerHolder, notification);

					convertView.setTag(headerHolder);

					break;
			}
		}
		else {
			switch (type) {
				case TYPE_ITEM:
					cellHolder = (NotificationCellHolder)convertView.getTag();

					this.initializeNotificationCell(cellHolder, notification);
					this.setClickListenerToView(convertView, notification);

					break;

				case TYPE_SEPARATOR:
					headerHolder = (NotificationHeaderHolder)convertView.getTag();

					this.initializeNotificationHeaderCell(headerHolder, notification);

					break;
			}
		}
		return convertView;
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

	private void initializeNotificationCell(NotificationCellHolder cellHolder, Notification notification) {
		Typeface normalTypeface = ((MapplasApplication)this.context.getApplicationContext()).getTypeFace();

		cellHolder.title.setText(notification.getName());
		cellHolder.title.setTypeface(normalTypeface);

		cellHolder.description.setTypeface(normalTypeface);
		cellHolder.description.setText(notification.getDescription());

		cellHolder.date.setTypeface(normalTypeface);
		cellHolder.date.setText(DateUtils.FormatSinceDate(notification.getDate(), notification.getHour(), this.context));

		// Load notification app logo
		ImageFileManager imageFileManager = new ImageFileManager();
		String logoUrl = notification.getAuxApp().getAppLogo();
		if(!logoUrl.equals("")) {
			if(imageFileManager.exists(new CacheFolderFactory(this.context).create(), logoUrl)) {
				cellHolder.logo.setImageBitmap(imageFileManager.load(new CacheFolderFactory(this.context).create(), logoUrl));
			}
			else {
				TaskAsyncExecuter imageRequest = new TaskAsyncExecuter(new LoadImageTask(this.context, logoUrl, cellHolder.logo, imageFileManager));
				imageRequest.execute();
			}
		}
	}

	private void initializeNotificationHeaderCell(NotificationHeaderHolder headerHolder, Notification notification) {
		Typeface normalTypeface = ((MapplasApplication)this.context.getApplicationContext()).getTypeFace();

		if(notification != null) {
			headerHolder.location.setText(notification.currentLocation());
			headerHolder.location.setTypeface(normalTypeface);
		}
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

	private void setClickListenerToView(View view, final Notification notification) {
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Set notification as seen
				NotificationRepository notificationRepository = RepositoryManager.notifications(context);
				notificationRepository.setNotificationAsSeen(notification.getId());

				// Intent to app detail activity
				Intent intent = new Intent(context, AppDetail.class);

				int appPosition = notification.getIdLocalization();
				intent.putExtra(Constants.MAPPLAS_DETAIL_APP, model.getAppWithIdInList(appPosition));
				intent.putExtra(Constants.MAPPLAS_DETAIL_USER, model.currentUser());
				intent.putExtra(Constants.MAPPLAS_DETAIL_CURRENT_LOCATION, model.currentLocation());
				intent.putExtra(Constants.MAPPLAS_DETAIL_CURRENT_DESCRIPT_GEO_LOCATION, model.currentDescriptiveGeoLoc());

				((AppNotifications)context).startActivityForResult(intent, Constants.SYNESTH_DETAILS_ID);
			}
		});

		// TODO: Carga asincrona de las imágenes en un hilo
	}

}
