package com.mapplas.app.adapters.notification;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.activities.AppDetail;
import com.mapplas.app.activities.AppNotifications;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.database.repositories.NotificationRepository;
import com.mapplas.model.database.repositories.RepositoryManager;
import com.mapplas.model.notifications.Notification;
import com.mapplas.utils.DateUtils;
import com.mapplas.utils.DrawableBackgroundDownloader;
import com.mapplas.utils.static_intents.SuperModelSingleton;

public class NotificationAdapter extends ArrayAdapter<Notification> {

	private ArrayList<Notification> items;

	private Context context = null;

	private SuperModel model = null;
	
	private ArrayList<Integer> notificationSet = null;

	public NotificationAdapter(Context context, int textViewResourceId, ArrayList<Notification> items, SuperModel model, ArrayList<Integer> notificationSet) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.context = context;
		this.model = model;
		this.notificationSet = notificationSet;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null) {
			LayoutInflater vi = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.rownot, null);
		}

		final Notification o = items.get(position);
		if(o != null) {
			// TODO: check if can be commented
			// int posList =
			// MapplasActivity.getLocalizationAdapter().getPosition(o.getAuxLocalization());
			// v.setTag(posList);

			Typeface normalTypeface = ((MapplasApplication)getContext().getApplicationContext()).getTypeFace();

			TextView tt = (TextView)v.findViewById(R.id.lblTitle);
			tt.setText(o.getName());
			tt.setTypeface(normalTypeface);

			tt = (TextView)v.findViewById(R.id.lblDescription);
			tt.setTypeface(normalTypeface);
			tt.setText(o.getDescription());

			tt = (TextView)v.findViewById(R.id.lblDate);
			tt.setTypeface(normalTypeface);
			tt.setText(DateUtils.FormatSinceDate(o.getDate(), o.getHour(), this.context));

			ImageView iv = (ImageView)v.findViewById(R.id.imgLogo);
			new DrawableBackgroundDownloader().loadDrawable(o.getAuxApp().getAppLogo(), iv, this.context.getResources().getDrawable(R.drawable.ic_template));

			v.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// Set notification as seen
					NotificationRepository notificationRepository = RepositoryManager.notifications(context);
					notificationRepository.setNotificationAsSeen(o.getId());

					// Intent to app detail activity
					Intent intent = new Intent(context, AppDetail.class);

					int appPosition = o.getAppId();
					intent.putExtra(Constants.MAPPLAS_DETAIL_APP, model.getAppWithIdInList(appPosition));
					SuperModelSingleton.model = model;

					((AppNotifications)context).startActivityForResult(intent, Constants.SYNESTH_DETAILS_ID);
				}
			});
		}
		
		if(this.notificationSet.get(position) == AppNotifications.typeHighlightedItem && this.notificationSet.size() > 0) {
			v.setBackgroundColor(Color.rgb(153, 204, 255));
		}
		else {
			v.setBackgroundColor(Color.TRANSPARENT);
		}
		
		return v;
	}
}
