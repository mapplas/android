//package com.mapplas.model.notifications;
//
//import java.util.Comparator;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//public class NotificationComparator implements Comparator<Notification>, Parcelable {
//
//	// Return options
//	// 1 - Notif1 newer
//	// 2 - Notif2 newer
//	
//	public NotificationComparator() {
//	}
//
//	@Override
//	public int compare(Notification notif1, Notification notif2) {
//		String[] splitedNotif1 = notif1.getDate().split("-");
//		String[] splitedNotif2 = notif2.getDate().split("-");
//
//		int yearNotif1 = Integer.parseInt(splitedNotif1[0]);
//		int monthNotif1 = Integer.parseInt(splitedNotif1[1]);
//		int dayNotif1 = Integer.parseInt(splitedNotif1[2]);
//		int yearNotif2 = Integer.parseInt(splitedNotif2[0]);
//		int monthNotif2 = Integer.parseInt(splitedNotif2[1]);
//		int dayNotif2 = Integer.parseInt(splitedNotif2[2]);
//
//		if(yearNotif1 > yearNotif2) {
//			return -1;
//		}
//		else if(yearNotif1 < yearNotif2) {
//			return 1;
//		}
//		else {
//			if(monthNotif1 > monthNotif2) {
//				return -1;
//			}
//			else if(monthNotif1 < monthNotif2) {
//				return 1;
//			}
//			else {
//				if(dayNotif1 > dayNotif2) {
//					return -1;
//				}
//				else if(dayNotif1 < dayNotif2) {
//					return 1;
//				}
//				else {
//					// Same day!!
//					String[] splitedHour1 = notif1.getHour().split(":");
//					String[] splitedHour2 = notif2.getHour().split(":");
//
//					int hourNotif1 = Integer.parseInt(splitedHour1[0]);
//					int minsNotif1 = Integer.parseInt(splitedHour1[1]);
//					int hourNotif2 = Integer.parseInt(splitedHour2[0]);
//					int minsNotif2 = Integer.parseInt(splitedHour2[1]);
//
//					if(hourNotif1 > hourNotif2) {
//						return -1;
//					}
//					else if(hourNotif1 < hourNotif2) {
//						return 1;
//					}
//					else {
//						if(minsNotif1 > minsNotif2) {
//							return -1;
//						}
//						else if(hourNotif1 < hourNotif2) {
//							return 1;
//						}
//						else {
//							return -1;
//						}
//					}
//				}
//			}
//		}
//	}
//
//	/**
//	 * Parcelable methods
//	 */
//	@Override
//	public int describeContents() {
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//	}
//
//	public NotificationComparator(Parcel parcel) {
//	}
//
//	public static final Parcelable.Creator<NotificationComparator> CREATOR = new Parcelable.Creator<NotificationComparator>() {
//
//		@Override
//		public NotificationComparator createFromParcel(Parcel source) {
//			return new NotificationComparator(source);
//		}
//
//		@Override
//		public NotificationComparator[] newArray(int size) {
//			return new NotificationComparator[size];
//		}
//	};
//}
