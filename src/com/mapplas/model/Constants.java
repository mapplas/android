package com.mapplas.model;

public class Constants {
	
	// Constants
	public static final String SYNESTH_SERVER = "54.217.233.146";
	public static final int SYNESTH_SERVER_PORT = 80;
	public static final String SYNESTH_SERVER_PATH = "/api/";
	
	public static final String SYNESTH_VERSION = "1";
	
	// Constants of the Activities
	public static final int SYNESTH_LOGIN_ID = 10;
	public static final int SYNESTH_USER_ID = 20;
	public static final int SYNESTH_DETAILS_ID = 30;
	public static final int SYNESTH_DETAILS_CAMERA_ID = 300;
	public static final int SYNESTH_DETAILS_CAMERA_ID2 = 302;
	public static final int SYNESTH_REGISTER_ID = 40;
	
	// Constant of the message values of the activities
	public static final int SYNESTH_MAIN_STATUS_ID = 10;
	public static final int SYNESTH_MAIN_LIST_ID = 20;
	public static final int SYNESTH_MAIN_IMAGE_ID = 30;
	
	public static final int SYNESTH_USER_PINUPS_ID = 10;
	public static final int SYNESTH_USER_LIKES_ID = 20;
	public static final int SYNESTH_USER_BLOCKS_ID = 30;
	
	public static final int SYNESTH_ROWLOC_IMAGE_ID = 40;
	
	// Constant of the return values of the activities
	// Login Activity
	public static final String MAPPLAS_LOGIN_USER = "com.mapplas.model.login.user";
	public static final String MAPPLAS_LOGIN_USER_ID = "com.mapplas.model.login.user.id";
	public static final String MAPPLAS_LOGIN_LOCATION = "com.mapplas.model.login.location";
	public static final String MAPPLAS_LOGIN_APP_LIST = "com.mapplas.model.login.app.list";

	public static final String MAPPLAS_LOGIN_LOCATION_ID = "com.mapplas.model.login.location";

	
	public static final String SYNESTH_LAST_NOTIFICATIONS = "com.synesth.model.lastNotifications";
	
	// App Detail Activity
	public static final String MAPPLAS_DETAIL_APP = "com.mapplas.model.detail.app";
	
	
	// Notification Activity
	public static final String MAPPLAS_NOTIFICATION_MODEL = "com.mapplas.model.notification.model";
	
	// Activity request actions
	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_START = "start";
	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_INSTALL = "install";
	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_PROBLEM = "problem";
	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_LOGOUT = "logout";

//	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_FAVOURITE = "favourite";
//	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_UNFAVOURITE = "unfavourite";
	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_PIN = "pin";
	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_UNPIN = "unpin";
	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_SHARE = "share";
	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_BLOCK = "block";
	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_UNBLOCK = "unblock";
	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_RATE = "rate";
	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_CALL = "call";
	public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_SHOW_COMMENTS = "showcomments";
	
	// Pin-request constants
	public static final String MAPPLAS_ACTIVITY_PIN_REQUEST_PIN = "pin";
	public static final String MAPPLAS_ACTIVITY_PIN_REQUEST_UNPIN = "unpin";
	
	// Block-request constants
	public static final String MAPPLAS_ACTIVITY_LIKE_REQUEST_BLOCK = "block";
	public static final String MAPPLAS_ACTIVITY_LIKE_REQUEST_UNBLOCK = "unblock";
	
	// Images
	public static final String MAPPLAS_EXTRA_BITMAP = "extra_bitmap";
	public static final String MAPPLAS_EXTRA_BITMAP_POSITION = "extra_bitmap.position";
	
	public static final String MAPPLAS_ACTION_BROADCAST_IMAGE_APP_ADAPTER = "com.mapplas.action.image.app_adapter";
	
	// TextActivity
	public static final String MAPPLAS_TEXT_ACTIVITY_EXTRA_TITLE = "text_activity.extra_title";
	public static final String MAPPLAS_TEXT_ACTIVITY_EXTRA_MESSAGE = "text_activity.extra_message";

	// App types
	public static final String MAPPLAS_APPLICATION_TYPE_HTML5 = "html_5";
	public static final String MAPPLAS_APPLICATION_TYPE_ANDROID_APPLICATION = "application";
	
	// Number of apps to request
	public static final int MAPPLAS_APPLICATION_APPS_PAGINATION_NUMBER = 20;
	
	public static final int LOCATION_TIMEOUT_IN_MILLISECONDS = 9000;

}
