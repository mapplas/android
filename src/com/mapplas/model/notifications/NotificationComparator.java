package com.mapplas.model.notifications;

import java.io.Serializable;
import java.util.Comparator;

public class NotificationComparator implements Comparator<Notification>, Serializable {

	// Return options
	// 1 - Notif1 newer
	// 2 - Notif2 newer

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(Notification notif1, Notification notif2) {
		String[] splitedNotif1 = notif1.getDate().split("-");
		String[] splitedNotif2 = notif2.getDate().split("-");

		int yearNotif1 = Integer.parseInt(splitedNotif1[0]);
		int monthNotif1 = Integer.parseInt(splitedNotif1[1]);
		int dayNotif1 = Integer.parseInt(splitedNotif1[2]);
		int yearNotif2 = Integer.parseInt(splitedNotif2[0]);
		int monthNotif2 = Integer.parseInt(splitedNotif2[1]);
		int dayNotif2 = Integer.parseInt(splitedNotif2[2]);

		if(yearNotif1 > yearNotif2) {
			return -1;
		}
		else if(yearNotif1 < yearNotif2) {
			return 1;
		}
		else {
			if(monthNotif1 > monthNotif2) {
				return -1;
			}
			else if(monthNotif1 < monthNotif2) {
				return 1;
			}
			else {
				if(dayNotif1 > dayNotif2) {
					return -1;
				}
				else if(dayNotif1 < dayNotif2) {
					return 1;
				}
				else {
					// Same day!!
					String[] splitedHour1 = notif1.getHour().split(":");
					String[] splitedHour2 = notif2.getHour().split(":");

					int hourNotif1 = Integer.parseInt(splitedHour1[0]);
					int minsNotif1 = Integer.parseInt(splitedHour1[1]);
					int hourNotif2 = Integer.parseInt(splitedHour2[0]);
					int minsNotif2 = Integer.parseInt(splitedHour2[1]);

					if(hourNotif1 > hourNotif2) {
						return -1;
					}
					else if(hourNotif1 < hourNotif2) {
						return 1;
					}
					else {
						if(minsNotif1 > minsNotif2) {
							return -1;
						}
						else if(hourNotif1 < hourNotif2) {
							return 1;
						}
						else {
							return -1;
						}
					}
				}
			}
		}
	}

}
