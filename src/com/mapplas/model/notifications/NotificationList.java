package com.mapplas.model.notifications;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;

public class NotificationList extends AbstractList<Notification> implements Serializable {

	private static final long serialVersionUID = 1L;

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

}
