package com.mapplas.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import app.mapplas.com.R;

import com.mapplas.model.AppOrderedList;
import com.mapplas.model.Constants;
import com.mapplas.model.User;
import com.mapplas.model.UserFormLayoutComponents;
import com.mapplas.model.database.MySQLiteHelper;
import com.mapplas.utils.network.async_tasks.UserPinBlocksTask;
import com.mapplas.utils.static_intents.AppChangedSingleton;
import com.mapplas.utils.visual.custom_views.RobotoButton;
import com.mapplas.utils.visual.presenters.UserFormDynamicSublistsPresenter;

public class UserForm extends LanguageActivity {

	private RotateAnimation flipAnimation;

	private RotateAnimation reverseFlipAnimation;

	public static String currentResponse = "";

	private ListView listView = null;

	private User user = null;

	private String currentLocation = "";

	public AppOrderedList appOrderedList;

	private LinearLayout refreshListBackgroundFooter = null;

	private LinearLayout buttonsFooter = null;

	private int userId = 0;

	public static boolean somethingChanged = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.user);

		this.getDataFromBundle();

		this.initializeAnimations();
		this.initLayoutComponents();
		this.initializeBackButton();
		this.initializeSettingsButton();

		// Request user app preferences
		new UserPinBlocksTask(this.user, this.listView, this, R.id.lblTitle, this.refreshListBackgroundFooter, this.appOrderedList).execute();

		// Load presenter
		RobotoButton blocksButton = (RobotoButton)findViewById(R.id.blocked_btn);
		RobotoButton pinsButton = (RobotoButton)findViewById(R.id.pinned_btn);
		ImageView mPrivateRefreshIcon = (ImageView)findViewById(R.id.ivImage);
		UserFormLayoutComponents layoutComponents = new UserFormLayoutComponents(blocksButton, pinsButton, this.refreshListBackgroundFooter, this.buttonsFooter, mPrivateRefreshIcon);

		new UserFormDynamicSublistsPresenter(layoutComponents, this.listView, this, this.user, this.currentLocation, this.appOrderedList).present();
	}

	@Override
	protected void onPause() {
		SharedPreferences settings = getSharedPreferences("prefs", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("user_id", this.userId);
		editor.commit();

		if(UserForm.somethingChanged) {
			AppChangedSingleton.somethingChanged = true;
			AppChangedSingleton.changedList = this.appOrderedList;
			UserForm.somethingChanged = false;
		}
		super.onPause();
	}

	/**
	 * 
	 * Private methods
	 * 
	 */
	private void getUserFromDB() {
		MySQLiteHelper db = new MySQLiteHelper(this);
		this.user = db.getUser(this.userId);
	}

	private void getDataFromBundle() {

		Bundle bundle = this.getIntent().getExtras();
		if(bundle != null) {
			if(bundle.containsKey(Constants.MAPPLAS_LOGIN_USER_ID)) {
				this.userId = bundle.getInt(Constants.MAPPLAS_LOGIN_USER_ID);
			}

			if(bundle.containsKey(Constants.MAPPLAS_LOGIN_LOCATION)) {
				this.currentLocation = bundle.getString(Constants.MAPPLAS_LOGIN_LOCATION);
			}

			if(bundle.containsKey(Constants.MAPPLAS_LOGIN_APP_LIST)) {
				this.appOrderedList = bundle.getParcelable(Constants.MAPPLAS_LOGIN_APP_LIST);
			}

			if(bundle.containsKey(Constants.MAPPLAS_LOGIN_USER)) {
				this.user = bundle.getParcelable(Constants.MAPPLAS_LOGIN_USER);
			}
			else {
				SharedPreferences settings = getSharedPreferences("prefs", 0);
				this.userId = settings.getInt("user_id", 0);
				this.getUserFromDB();
			}
		}
		else {
			SharedPreferences settings = getSharedPreferences("prefs", 0);
			this.userId = settings.getInt("user_id", 0);
			this.getUserFromDB();
		}
	}

	private void initializeAnimations() {
		this.flipAnimation = new RotateAnimation(0, 90, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.flipAnimation.setInterpolator(new LinearInterpolator());
		this.flipAnimation.setDuration(300);
		this.flipAnimation.setFillAfter(true);

		this.reverseFlipAnimation = new RotateAnimation(90, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.reverseFlipAnimation.setInterpolator(new LinearInterpolator());
		this.reverseFlipAnimation.setDuration(300);
		this.reverseFlipAnimation.setFillAfter(true);
	}

	private void initLayoutComponents() {
		this.refreshListBackgroundFooter = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_footer, null);

		this.listView = (ListView)findViewById(R.id.lvList);
		this.listView.addFooterView(this.refreshListBackgroundFooter);

		// Define the divider color of the listview
		ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.user_list_divider));
		this.listView.setDivider(color);
		this.listView.setDividerHeight(1);
	}

	private void initializeBackButton() {
		Button backBtn = (Button)findViewById(R.id.btnBack);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				user.pinnedApps().clear();
				user.blockedApps().clear();

				Intent intent = new Intent();
				intent.putExtra(Constants.MAPPLAS_LOGIN_USER, user);
				setResult(Constants.SYNESTH_USER_ID, intent);

				finish();
			}
		});
	}

	private void initializeSettingsButton() {
		Button settinsBtn = (Button)this.findViewById(R.id.btnSettings);
		settinsBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserForm.this, SettingsActivity.class);
				startActivity(intent);
			}
		});

	}
}