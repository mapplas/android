package com.mapplas.model;

public class Constants {

	// Constants
//	public static final String SYNESTH_SERVER = "54.217.233.146";
	public static final String MAPPLAS_SERVER = "54.217.249.103";

	public static final int MAPPLAS_SERVER_PORT = 80;

	public static final String MAPPLAS_SERVER_PATH = "/api/";

	public static final String MAPPLAS_VERSION = "1";

	// Languages
	public static final String ENGLISH = "US";

	public static final String SPANISH = "ES";

	public static final String BASQUE = "EU";

	// Constants of the Activities
	public static final int SYNESTH_LOGIN_ID = 10;

	public static final int SYNESTH_USER_ID = 20;

	public static final int SYNESTH_DETAILS_ID = 30;

	public static final int SYNESTH_DETAILS_CAMERA_ID = 300;

	public static final int SYNESTH_DETAILS_CAMERA_ID2 = 302;

	public static final int SYNESTH_REGISTER_ID = 40;
	
	public static final int MAPPLAS_GOOLE_POSITIONING_SETTINGS_CHANGED = 400;

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

	// public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_FAVOURITE =
	// "favourite";
	// public static final String MAPPLAS_ACTIVITY_REQUEST_ACTION_UNFAVOURITE =
	// "unfavourite";
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

	public static final String MAPPLAS_APPLICATION_TYPE_MOCK = "mock";

	// Number of apps to request
	public static final int MAPPLAS_APPLICATION_APPS_PAGINATION_NUMBER = 20;

	public static final int LOCATION_TIMEOUT_IN_MILLISECONDS = 9000;

	// Intent bundle strings for webview
	public static String APP_DEV_URL_INTENT_DATA = "com.mapplas.activity.bundle.dev_url";

	public static String APP_DEV_APP_NAMEL_INTENT_DATA = "com.mapplas.activity.bundle.dev_url_app_name";

	// Intent bundle more from developer apps
	public static String MORE_FROM_DEVELOPER_APP_ARRAY = "com.mapplas.activity.bundle.more_from_dev_app_array";

	public static String MORE_FROM_DEVELOPER_APP = "com.mapplas.activity.bundle.more_from_dev_app";

	public static String MORE_FROM_DEVELOPER_COUNTRY_CODE = "com.mapplas.activity.bundle.more_from_dev_country_code";

	public static int NUMBER_OF_RELATED_APPS_TO_SHOW = 3;

	public static String LOW_PIXEL_DENSITY = "low";

	public static String MEDIUM_PIXEL_DENSITY = "medium";

	public static String HIGH_PIXEL_DENSITY = "high";

	public static String EXTRA_HIGH_PIXEL_DENSITY = "extra_high";

	// User ident constants
	public static String USER_IDENTIFICATION_SERVER_RESPONSE_ERROR = "server_response_error_user_ident";
	
	public static String USER_IDENTIFICATION_SOCKET_ERROR = "socket_error_user_ident";
	
	public static int NUMBER_OF_REQUEST_RETRIES = 100;
	
	// Intent bundle settings restart app language change
	public static String SETTINGS_LANGUAGE_CHANGE_BUNDLE = "com.mapplas.activity.bundle.settings_language_change";

	public static String MAPPLAS_SHARED_PREFS = "MAPPLAS_PREF";

	public static String MAPPLAS_SHARED_PREFS_LANGUAGE_DIALOG_SHOWN = "LANG_DIALOG_FIRST_SHOWN";

	public static String APP_OBTENTION_ERROR_GENERIC = "ERROR_GENERIC";

	public static String APP_OBTENTION_OK = "OK";

	public static String APP_OBTENTION_ERROR_SOCKET = "ERROR_SOCKET";
	
	

}
