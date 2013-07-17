package com.mapplas.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.user.UserAppAdapter;
import com.mapplas.model.AppOrderedList;
import com.mapplas.model.Constants;
import com.mapplas.model.User;
import com.mapplas.model.UserFormLayoutComponents;
import com.mapplas.model.database.repositories.RepositoryManager;
import com.mapplas.model.database.repositories.UserRepository;
import com.mapplas.utils.network.async_tasks.UserPinBlocksTask;
import com.mapplas.utils.static_intents.AppChangedSingleton;
import com.mapplas.utils.visual.presenters.UserFormDynamicSublistsPresenter;

public class UserForm extends Activity {

	private RotateAnimation flipAnimation;

	private RotateAnimation reverseFlipAnimation;

	public static String currentResponse = "";

	private ListView listView = null;

	private User user = null;

	private String currentLocation = "";
	
	public static AppOrderedList appOrderedList = null;

	private LinearLayout refreshListBackgroundFooter = null;

	private LinearLayout buttonsFooter = null;

	private View headerLayout = null;

	private int userId = 0;

	public static boolean somethingChanged = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.user);

		this.getDataFromBundle();

		this.initializeAnimations();
		this.initLayoutComponents();
		this.initializeButtonsAndItsBehaviour();

		// Request user app preferences
		new UserPinBlocksTask(this.user, this.listView, this, R.id.lblTitle, this.refreshListBackgroundFooter).execute();

		// Load presenter
		LinearLayout blocksLayout = (LinearLayout)findViewById(R.id.lytBlocks);
		LinearLayout pinUpsLayout = (LinearLayout)findViewById(R.id.lytPinups);
		ImageView mPrivateRefreshIcon = (ImageView)findViewById(R.id.ivImage);
		UserFormLayoutComponents layoutComponents = new UserFormLayoutComponents(blocksLayout, pinUpsLayout, this.refreshListBackgroundFooter, this.buttonsFooter, mPrivateRefreshIcon);

		new UserFormDynamicSublistsPresenter(layoutComponents, this.listView, this, this.user, this.currentLocation).present();
	}

	@Override
	protected void onPause() {
		SharedPreferences settings = getSharedPreferences("prefs", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("user_id", this.userId);
		editor.commit();

		if(UserForm.somethingChanged) {
			AppChangedSingleton.somethingChanged = true;
			AppChangedSingleton.changedList = UserForm.appOrderedList;
			UserForm.somethingChanged = false;
		}
		super.onPause();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = this.getMenuInflater();
		menuInflater.inflate(R.layout.config_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, AboutUsActivity.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 * Private methods
	 * 
	 */
	private void getUserFromDB() {
		UserRepository userRepo = RepositoryManager.users(this);
		this.user = (User)userRepo.findFromId(this.userId);
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
				UserForm.appOrderedList = bundle.getParcelable(Constants.MAPPLAS_LOGIN_APP_LIST);
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
//		this.refreshAnimation = new RotateAnimation(-360, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//		this.refreshAnimation.setInterpolator(new LinearInterpolator());
//		this.refreshAnimation.setDuration(1500);
//		this.refreshAnimation.setFillAfter(true);
//		this.refreshAnimation.setRepeatCount(Animation.INFINITE);
//		this.refreshAnimation.setRepeatMode(Animation.RESTART);

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
		this.headerLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_header, null);
		this.refreshListBackgroundFooter = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_footer, null);

		this.listView = (ListView)findViewById(R.id.lvList);
		this.listView.addHeaderView(this.headerLayout);
		this.listView.addFooterView(this.refreshListBackgroundFooter);

		// Set list adapter
		UserAppAdapter ula = new UserAppAdapter(this, R.id.lblTitle, this.user.pinnedApps(), UserAppAdapter.PINUP, this.user, false);
		this.listView.setAdapter(ula);

		// Define the divider color of the listview
		ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.user_list_divider));
		this.listView.setDivider(color);
		this.listView.setDividerHeight(1);
	}

	private void initializeButtonsAndItsBehaviour() {		
		this.initializeBackButton();
	}

	private void initializeBackButton() {
		Button btn = (Button)findViewById(R.id.btnBack);
		btn.setOnClickListener(new View.OnClickListener() {

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
}