package com.mapplas.app.activities;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import app.mapplas.com.R;

import com.mapplas.app.adapters.user.UserAppAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.app.async_tasks.user_form.UserBlocksTask;
import com.mapplas.app.async_tasks.user_form.UserPinUpsTask;
import com.mapplas.app.threads.ActivityRequestThread;
import com.mapplas.app.threads.UserEditRequestThread;
import com.mapplas.model.Constants;
import com.mapplas.model.JsonParser;
import com.mapplas.model.User;
import com.mapplas.model.UserFormLayoutComponents;
import com.mapplas.model.database.repositories.RepositoryManager;
import com.mapplas.model.database.repositories.UserRepository;
import com.mapplas.utils.presenters.UserFormDynamicSublistsPresenter;

public class UserForm extends Activity {

	private static final int USER_SIGN_IN = 0;

	private static final int USER_LOG_IN = 1;

	private static final int USER_LOGGED_IN = 2;

	private RotateAnimation flipAnimation;

	private RotateAnimation reverseFlipAnimation;

	private RotateAnimation refreshAnimation;

	private Animation animFlipInNext;

	private Animation animFlipOutNext;

	private Animation animFlipInPrevious;

	private Animation animFlipOutPrevious;

	private Uri mImageCaptureUri;

	public static String currentResponse = "";

	private ListView listView = null;

	private User user = null;

	private String currentLocation = "";

	private LinearLayout refreshListBackgroundFooter = null;

	private LinearLayout buttonsFooter = null;

	private View headerLayout = null;

	private Button actionButton = null;
	
	private int userId = 0;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.user);

		this.getDataFromBundle();
		this.getUserFromDB();

		this.initializeAnimations();
		this.initLayoutComponents();
		this.initializeButtonsAndItsBehaviour();

		// Request user app preferences
		JsonParser parser = new JsonParser(this);
		new UserPinUpsTask(this.user, parser, this.listView, this, R.id.lblTitle, this.currentLocation, this.refreshListBackgroundFooter).execute();
		new UserBlocksTask(this.user, parser).execute();

		// Load presenter
		LinearLayout blocksLayout = (LinearLayout)findViewById(R.id.lytBlocks);
		LinearLayout pinUpsLayout = (LinearLayout)findViewById(R.id.lytPinups);
		ImageView mPrivateRefreshIcon = (ImageView)findViewById(R.id.ivImage);
		UserFormLayoutComponents layoutComponents = new UserFormLayoutComponents(blocksLayout, pinUpsLayout, this.refreshListBackgroundFooter, this.buttonsFooter, mPrivateRefreshIcon);

		new UserFormDynamicSublistsPresenter(layoutComponents, this.listView, this, this.user, this.currentLocation).present();

		// Try to get image
		ImageView imgUser = (ImageView)findViewById(R.id.imgUser);
		try {
			SharedPreferences settings = getSharedPreferences("prefs", 0);
			String uri = settings.getString("user_image", "");

			if(!uri.equals("")) {
				Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uri));
				imgUser.setImageBitmap(bmp);
			}
		} catch (Exception exc) {
			imgUser.setImageResource(R.drawable.ic_menu_profile);
		}
	}

	@Override
	protected void onPause() {
		SharedPreferences settings = getSharedPreferences("prefs", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("user_id", this.userId);
		editor.commit();
		super.onPause();
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
			if(bundle.containsKey(Constants.MAPPLAS_LOGIN_LOCATION_ID)) {
				this.currentLocation = bundle.getString(Constants.MAPPLAS_LOGIN_LOCATION_ID);
			}
			
			if(bundle.containsKey(Constants.MAPPLAS_LOGIN_USER_ID)) {
				this.userId = bundle.getInt(Constants.MAPPLAS_LOGIN_USER_ID);
			} else {
				SharedPreferences settings = getSharedPreferences("prefs", 0);
				this.userId = settings.getInt("user_id", 0);
			}
		}
	}

	private void initializeAnimations() {
		this.refreshAnimation = new RotateAnimation(-360, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.refreshAnimation.setInterpolator(new LinearInterpolator());
		this.refreshAnimation.setDuration(1500);
		this.refreshAnimation.setFillAfter(true);
		this.refreshAnimation.setRepeatCount(Animation.INFINITE);
		this.refreshAnimation.setRepeatMode(Animation.RESTART);

		this.flipAnimation = new RotateAnimation(0, 90, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.flipAnimation.setInterpolator(new LinearInterpolator());
		this.flipAnimation.setDuration(300);
		this.flipAnimation.setFillAfter(true);

		this.reverseFlipAnimation = new RotateAnimation(90, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.reverseFlipAnimation.setInterpolator(new LinearInterpolator());
		this.reverseFlipAnimation.setDuration(300);
		this.reverseFlipAnimation.setFillAfter(true);

		this.animFlipInNext = AnimationUtils.loadAnimation(this, R.anim.flipinnext);
		this.animFlipOutNext = AnimationUtils.loadAnimation(this, R.anim.flipoutnext);
		this.animFlipInPrevious = AnimationUtils.loadAnimation(this, R.anim.flipinprevious);
		this.animFlipOutPrevious = AnimationUtils.loadAnimation(this, R.anim.flipoutprevious);
	}

	private void initLayoutComponents() {
		// Action button
		this.actionButton = (Button)findViewById(R.id.btnAction);

		this.headerLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_header, null);
		this.buttonsFooter = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_footer_info, null);
		this.refreshListBackgroundFooter = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_footer, null);
		ImageView mPrivateRefreshIcon = (ImageView)this.refreshListBackgroundFooter.findViewById(R.id.ivImage);
		mPrivateRefreshIcon.startAnimation(refreshAnimation);

		this.listView = (ListView)findViewById(R.id.lvList);
		this.listView.addHeaderView(this.headerLayout);
		this.listView.addFooterView(this.refreshListBackgroundFooter);

		// Set list adapter
		UserAppAdapter ula = new UserAppAdapter(this, R.id.lblTitle, this.user.pinnedApps(), UserAppAdapter.PINUP, this.user, this.currentLocation);
		this.listView.setAdapter(ula);

		// Define the divider color of the listview
		ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.user_list_divider));
		this.listView.setDivider(color);
		this.listView.setDividerHeight(1);

		// Init typeface
		Typeface italicTypeface = ((MapplasApplication)this.getApplicationContext()).getItalicTypeFace();

		// Initialize profile textView
		TextView profileTextView = (TextView)findViewById(R.id.lblProfile);
		profileTextView.setTypeface(italicTypeface);

		// Init layout other components
		this.changeLayoutComponents(this.checkUserState());
	}

	private void initializeButtonsAndItsBehaviour() {
		ViewFlipper profileRow = (ViewFlipper)findViewById(R.id.vfRowProfile);
		this.initializeFormButton(profileRow);
		this.initializeActionButton(profileRow);
		this.initializeBackButton();
		this.initializeProfileImageButton();
		this.initializeSignOutButton();
	}

	private void initializeFormButton(final ViewFlipper profileRow) {
		Button btnSend = (Button)findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Send and store the user data.
				EditText txtName = (EditText)findViewById(R.id.txtName);
				user.setName(txtName.getText().toString());
				txtName.setText(user.getName());

				EditText txtEmail = (EditText)findViewById(R.id.txtEmail);
				user.setEmail(txtEmail.getText().toString());
				txtEmail.setText(user.getEmail());

				TextView lblName = (TextView)findViewById(R.id.lblName);
				lblName.setText(user.getName());
				if(user.getName().equals("")) {
					lblName.setText(R.string.name_not_set);
				}

				TextView lblEmail = (TextView)findViewById(R.id.lblEmail);
				lblEmail.setText(user.getEmail());
				if(user.getEmail().equals("")) {
					lblEmail.setTag(R.string.email_not_set);
				}

				// Hide keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(txtName.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(txtEmail.getWindowToken(), 0);

				// Send user edit request
				try {
					Thread th = new Thread(new UserEditRequestThread(user, currentResponse).getThread());
					th.start();

					SharedPreferences settings = getSharedPreferences("prefs", 0);
					Editor editor = settings.edit();
					editor.putBoolean("user_logged", true);
					editor.commit();

					// Change layout components
					changeLayoutComponents(checkUserState());

				} catch (Exception exc) {
					// Change layout components

					SharedPreferences settings = getSharedPreferences("prefs", 0);
					Editor editor = settings.edit();
					editor.putBoolean("user_logged", false);
					editor.commit();

					changeLayoutComponents(checkUserState());

					Log.i(getClass().getSimpleName(), "Edit User: " + exc);
				}

				if(profileRow.indexOfChild(profileRow.getCurrentView()) == 0) {
					profileRow.setInAnimation(animFlipInNext);
					profileRow.setOutAnimation(animFlipOutNext);
					profileRow.showNext();
				}
				else {
					profileRow.setInAnimation(animFlipInPrevious);
					profileRow.setOutAnimation(animFlipOutPrevious);
					profileRow.showPrevious();
				}
			}
		});
	}

	private void initializeActionButton(final ViewFlipper profileRow) {
		this.actionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(profileRow.indexOfChild(profileRow.getCurrentView()) == 0) {
					profileRow.setInAnimation(animFlipInNext);
					profileRow.setOutAnimation(animFlipOutNext);
					profileRow.showNext();
				}
				else {
					profileRow.setInAnimation(animFlipInPrevious);
					profileRow.setOutAnimation(animFlipOutPrevious);
					profileRow.showPrevious();
				}
			}
		});
	}

	private void initializeBackButton() {
		Button btn = (Button)findViewById(R.id.btnBack);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				user.pinnedApps().clear();
				user.blockedApps().clear();
				finish();
			}
		});
	}

	private void initializeProfileImageButton() {
		Button btnProfileImage = (Button)findViewById(R.id.btnProfileImage);
		btnProfileImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();

				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);

				startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.SYNESTH_DETAILS_CAMERA_ID);
			}
		});
	}

	private void initializeSignOutButton() {
		Button logoutButton = (Button)this.buttonsFooter.findViewById(R.id.lblSignout);
		logoutButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Send and store the user data.
				final String name = user.getName();
				final String email = user.getEmail();

				new AlertDialog.Builder(UserForm.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.signout).setMessage(R.string.really_logout).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						user.setName("");
						user.setEmail("");

						SharedPreferences settings = getSharedPreferences("prefs", 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putBoolean("user_logged", false);
						editor.commit();

						changeLayoutComponents(checkUserState());

						// Logout request
						String message = Constants.MAPPLAS_ACTIVITY_REQUEST_ACTION_LOGOUT + " (" + name + ":" + email + ")";
						Thread activityRequestThread = new Thread(new ActivityRequestThread(currentLocation, null, user, message).getThread());
						activityRequestThread.start();

						// User edit request
						Thread userEditRequestThread = new Thread(new UserEditRequestThread(user, currentResponse).getThread());
						userEditRequestThread.start();
					}

				}).setNegativeButton(R.string.no, null).show();
			}
		});
	}

	/**
	 * 
	 * USER LOG-IN, SIGN-IN... ACTIONS
	 * 
	 */
	private void changeLayoutComponents(int userState) {
		Typeface normalTypeface = ((MapplasApplication)this.getApplicationContext()).getTypeFace();

		this.setUserNameAndEmailFields(normalTypeface);

		TextView actionText = (TextView)this.headerLayout.findViewById(R.id.lblLogin);

		switch (userState) {
			case UserForm.USER_SIGN_IN:
				this.actionButton.setText(R.string.signin);
				this.actionButton.setVisibility(View.VISIBLE);

				actionText.setText(R.string.newAccount);
				actionText.setVisibility(View.VISIBLE);

				this.listView.removeFooterView(this.buttonsFooter);
				break;

			case UserForm.USER_LOG_IN:
				this.actionButton.setText(R.string.login);
				this.actionButton.setVisibility(View.VISIBLE);

				actionText.setText(R.string.newAccountSignedIn);
				actionText.setVisibility(View.VISIBLE);

				this.listView.removeFooterView(this.buttonsFooter);
				break;

			case UserForm.USER_LOGGED_IN:
				this.actionButton.setVisibility(View.INVISIBLE);
				actionText.setVisibility(View.INVISIBLE);

				this.listView.addFooterView(this.buttonsFooter);
				break;
		}
	}

	private void setUserNameAndEmailFields(Typeface normalTypeface) {

		// Initialize name text view field
		TextView lblName = (TextView)findViewById(R.id.lblName);
		lblName.setTypeface(normalTypeface);
		lblName.setText(this.user.getName());

		if(this.user.getName().equals("")) {
			lblName.setText(R.string.name_not_set);
		}

		// Initialize email text view field
		TextView lblEmail = (TextView)findViewById(R.id.lblEmail);
		lblEmail.setTypeface(normalTypeface);
		lblEmail.setText(this.user.getEmail());

		if(this.user.getEmail().equals("")) {
			lblEmail.setText(R.string.email_not_set);
		}

		// Initialize name edit text field
		EditText txtName = (EditText)findViewById(R.id.txtName);
		txtName.setTypeface(normalTypeface);
		txtName.setText(this.user.getName());

		// Initialize email edit text field
		EditText txtEmail = (EditText)findViewById(R.id.txtEmail);
		txtEmail.setTypeface(normalTypeface);
		txtEmail.setText(this.user.getEmail());
	}

	private int checkUserState() {
		SharedPreferences settings = UserForm.this.getSharedPreferences("prefs", 0);

		if(!this.user.getEmail().equals("")) {
			if(settings.getBoolean("user_logged", false)) {
				return UserForm.USER_LOGGED_IN;
			}
			else {
				return UserForm.USER_LOG_IN;
			}
		}
		else {
			return UserForm.USER_SIGN_IN;
		}
	}

	/**
	 * 
	 * IMAGES
	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != RESULT_OK)
			return;

		switch (requestCode) {
			case Constants.SYNESTH_DETAILS_CAMERA_ID:
				this.mImageCaptureUri = data.getData();

				// final ImageView imgUser = (ImageView)
				// findViewById(R.id.imgUser);
				// Drawable dw =
				// Drawable.createFromPath(mImageCaptureUri.getPath());

				// imgUser.setImageDrawable(dw);
				doCrop();
				break;

			case Constants.SYNESTH_DETAILS_CAMERA_ID2:
				Bundle extras = data.getExtras();

				if(extras != null) {
					Bitmap photo = extras.getParcelable("data");

					final ImageView imgUser = (ImageView)findViewById(R.id.imgUser);
					// Drawable dw =
					// Drawable.createFromPath(mImageCaptureUri.getPath());

					imgUser.setImageBitmap(photo);

					String url = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "synesth", "synesth");

					SharedPreferences settings = getSharedPreferences("prefs", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("user_image", url);

					// Commit the edits!
					editor.commit();
				}

				File f = new File(mImageCaptureUri.getPath());

				if(f.exists())
					f.delete();
				break;
		}
	}

	private void doCrop() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);

		List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

		int size = list.size();

		if(size == 0) {
			Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

			return;
		}
		else {
			intent.setData(mImageCaptureUri);

			startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.SYNESTH_DETAILS_CAMERA_ID2);
		}
	}
}
