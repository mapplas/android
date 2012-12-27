package com.mapplas.app.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.mapplas.app.adapters.UserAppAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.app.async_tasks.UserPinUpsTask;
import com.mapplas.app.handlers.MessageHandlerFactory;
import com.mapplas.app.threads.UserEditRequestThread;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.User;
import com.mapplas.model.UserFormLayoutComponents;
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

	private Handler messageHandler = null;

	private LinearLayout privateFooter = null;

	private LinearLayout privateFooterInfo = null;

	private View headerLayout = null;
	
	private Button actionButton = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.user);

		this.getDataFromBundle();

		this.initializeAnimations();
		this.initLayoutComponents();

		this.messageHandler = new MessageHandlerFactory().getUserFormActivityMessageHandler(this.listView, this, privateFooter, this.user, this.currentLocation);

		// Request user pin-up's
		new UserPinUpsTask(this.messageHandler, this.user.getId()).execute();

		this.initializeButtonsAndItsBehaviour();

		//
		LinearLayout blocksLayout = (LinearLayout)findViewById(R.id.lytBlocks);
		LinearLayout pinUpsLayout = (LinearLayout)findViewById(R.id.lytPinups);
		LinearLayout ratesLayout = (LinearLayout)findViewById(R.id.lytRates);
		LinearLayout likesLayout = (LinearLayout)findViewById(R.id.lytLikes);
		ImageView mPrivateRefreshIcon = (ImageView)findViewById(R.id.ivImage);
		UserFormLayoutComponents layoutComponents = new UserFormLayoutComponents(blocksLayout, pinUpsLayout, ratesLayout, likesLayout, this.privateFooter, this.privateFooterInfo, mPrivateRefreshIcon);

		new UserFormDynamicSublistsPresenter(layoutComponents, this.listView, this, this.user, this.messageHandler, this.refreshAnimation, this.currentLocation).present();

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

	/**
	 * 
	 * Private methods
	 * 
	 */
	private void getDataFromBundle() {
		Bundle bundle = this.getIntent().getExtras();
		if(bundle != null) {
			if(bundle.containsKey(Constants.MAPPLAS_LOGIN_USER_ID)) {
				this.user = (User)bundle.getParcelable(Constants.MAPPLAS_LOGIN_USER_ID);
			}
			if(bundle.containsKey(Constants.MAPPLAS_LOGIN_LOCATION_ID)) {
				this.currentLocation = bundle.getString(Constants.MAPPLAS_LOGIN_LOCATION_ID);
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
		this.privateFooter = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_footer, null);
		this.privateFooterInfo = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_footer_info, null);
		ImageView mPrivateRefreshIcon = (ImageView)this.privateFooter.findViewById(R.id.ivImage);
		mPrivateRefreshIcon.startAnimation(refreshAnimation);

		this.listView = (ListView)findViewById(R.id.lvList);
		this.listView.addHeaderView(this.headerLayout);
		this.listView.addFooterView(this.privateFooter);
		this.listView.addFooterView(this.privateFooterInfo);

		// Set list adapter
		UserAppAdapter ula = new UserAppAdapter(this, R.id.lblTitle, new ArrayList<App>(), UserAppAdapter.BLOCK, this.user, this.currentLocation);
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
					
					// Change layout components
					changeLayoutComponents(checkUserState());

				} catch (Exception exc) {
					// Change layout components
					user.setLoggedIn(false);
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
				actionText.setText(R.string.newAccount);
				this.actionButton.setText(R.string.signin);
				break;

			case UserForm.USER_LOG_IN:
				this.actionButton.setText(R.string.login);
				actionText.setText(R.string.newAccountSignedIn);
				break;

			case UserForm.USER_LOGGED_IN:
				this.actionButton.setVisibility(View.INVISIBLE);
				actionText.setVisibility(View.INVISIBLE);
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
		if(!this.user.getEmail().equals("")) {
			if(this.user.loggedIn()) {
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
