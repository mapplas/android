package com.mapplas.model.notifications;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationList extends AbstractList<Notification> implements Parcelable {

	private ArrayList<Notification> list = null;

	private NotificationComparator notificationComparator = null;

	public NotificationList() {
		this.list = new ArrayList<Notification>();
		this.notificationComparator = new NotificationComparator();
	}

	@Override
	public void add(int position, Notification e) {
		this.list.add(e);
		Collections.sort(this.list, this.notificationComparator);
	}

	@Override
	public Notification get(int location) {
		return this.list.get(location);
	}

	@Override
	public int size() {
		return this.list.size();
	}

	public ArrayList<Notification> getList() {
		return this.list;
	}
	
	/**
	 * Parcelable methods
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(this.list);
		dest.writeParcelable(this.notificationComparator, flags);
	}

	public NotificationList(Parcel parcel) {
		this.list = new ArrayList<Notification>();
		this.notificationComparator = new NotificationComparator();
		
	    parcel.readTypedList(this.list, Notification.CREATOR);
		this.notificationComparator = parcel.readParcelable(null);
	}

	public static final Parcelable.Creator<NotificationList> CREATOR = new Parcelable.Creator<NotificationList>() {

		@Override
		public NotificationList createFromParcel(Parcel source) {
			return new NotificationList(source);
		}

		@Override
		public NotificationList[] newArray(int size) {
			return new NotificationList[size];
		}
	};

}
