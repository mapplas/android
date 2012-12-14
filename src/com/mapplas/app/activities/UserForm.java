package com.mapplas.app.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
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

import com.mapplas.app.UserLocalizationAdapter;
import com.mapplas.app.handlers.MessageHandlerFactory;
import com.mapplas.app.threads.UserEditRequestThread;
import com.mapplas.app.threads.UserPinUpsRequestThread;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.User;
import com.mapplas.model.UserFormLayoutComponents;
import com.mapplas.utils.NetRequests;
import com.mapplas.utils.presenters.UserFormDynamicSublistsPresenter;
import com.mapplas.utils.presenters.UserFormPresenter;

public class UserForm extends Activity {

	private RotateAnimation flipAnimation;

	private RotateAnimation reverseFlipAnimation;

	private RotateAnimation refreshAnimation;

	private Animation animFlipInNext;

	private Animation animFlipOutNext;

	private Animation animFlipInPrevious;

	private Animation animFlipOutPrevious;

	private Uri mImageCaptureUri;

	private String currentResponse = "";

	private ListView listView = null;

	private User user = null;

	private String currentLocation = "";

	private Handler messageHandler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.user);

		this.getDataFromBundle();

		this.initializeAnimations();
		this.initLayoutComponents();

		LinearLayout privateFooter = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_footer, null);
		this.messageHandler = new MessageHandlerFactory().getUserFormActivityMessageHandler(this.listView, this.currentResponse, this, privateFooter, this.user, this.currentLocation);

		// Request user pin-up's
		try {
			Thread th = new Thread(new UserPinUpsRequestThread(this.messageHandler, this.user.getId()).getThread());
			th.start();
		} catch (Exception exc) {
			Log.i(this.getClass().getSimpleName(), "Action Get PinUps: " + exc);
		}

		this.initializeButtonsAndItsBehaviour();

		//
		LinearLayout blocksLayout = (LinearLayout)findViewById(R.id.lytBlocks);
		LinearLayout pinUpsLayout = (LinearLayout)findViewById(R.id.lytPinups);
		LinearLayout ratesLayout = (LinearLayout)findViewById(R.id.lytRates);
		LinearLayout likesLayout = (LinearLayout)findViewById(R.id.lytLikes);
		LinearLayout privateFooterInfo = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_footer_info, null);
		ImageView mPrivateRefreshIcon = (ImageView)findViewById(R.id.ivImage);
		UserFormLayoutComponents layoutComponents = new UserFormLayoutComponents(blocksLayout, pinUpsLayout, ratesLayout, likesLayout, privateFooter, privateFooterInfo, mPrivateRefreshIcon);

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
				this.user = (User)bundle.getSerializable(Constants.MAPPLAS_LOGIN_USER_ID);
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

		// Unused?
		// Animation animFlipInNext = AnimationUtils.loadAnimation(this,
		// R.anim.flipinnext);
		// Animation animFlipOutNext = AnimationUtils.loadAnimation(this,
		// R.anim.flipoutnext);
		// Animation animFlipInPrevious = AnimationUtils.loadAnimation(this,
		// R.anim.flipinprevious);
		// Animation animFlipOutPrevious = AnimationUtils.loadAnimation(this,
		// R.anim.flipoutprevious);

		this.flipAnimation = new RotateAnimation(0, 90, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.flipAnimation.setInterpolator(new LinearInterpolator());
		this.flipAnimation.setDuration(300);
		this.flipAnimation.setFillAfter(true);

		this.reverseFlipAnimation = new RotateAnimation(90, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.reverseFlipAnimation.setInterpolator(new LinearInterpolator());
		this.reverseFlipAnimation.setDuration(300);
		this.reverseFlipAnimation.setFillAfter(true);

		animFlipInNext = AnimationUtils.loadAnimation(this, R.anim.flipinnext);
		animFlipOutNext = AnimationUtils.loadAnimation(this, R.anim.flipoutnext);
		animFlipInPrevious = AnimationUtils.loadAnimation(this, R.anim.flipinprevious);
		animFlipOutPrevious = AnimationUtils.loadAnimation(this, R.anim.flipoutprevious);
	}

	private void initLayoutComponents() {
		View header = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_header, null);
		LinearLayout mPrivateFooter = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_footer, null);
		LinearLayout mPrivateFooterInfo = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.profile_footer_info, null);
		ImageView mPrivateRefreshIcon = (ImageView)mPrivateFooter.findViewById(R.id.ivImage);
		mPrivateRefreshIcon.startAnimation(refreshAnimation);

		this.listView = (ListView)findViewById(R.id.lvList);
		this.listView.addHeaderView(header);
		this.listView.addFooterView(mPrivateFooter);
		this.listView.addFooterView(mPrivateFooterInfo);

		// Set list adapter
		UserLocalizationAdapter ula = new UserLocalizationAdapter(UserForm.this, R.id.lblTitle, new ArrayList<App>(), UserLocalizationAdapter.BLOCK, this.user, this.currentLocation);
		this.listView.setAdapter(ula);

		// Define the divider color of the listview
		ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.user_list_divider));
		this.listView.setDivider(color);
		this.listView.setDividerHeight(1);

		// Init layout other components
		new UserFormPresenter(this, this.user).present();
	}

	private void initializeButtonsAndItsBehaviour() {
		ViewFlipper profileRow = (ViewFlipper)findViewById(R.id.vfRowProfile);
		this.initializeEditButton(profileRow);
		this.initializeSendButton(profileRow);
		this.initializeLogoutButton();
		this.initializeBackButton();
		this.initializeProfileImageButton();
	}

	private void initializeEditButton(final ViewFlipper profileRow) {
		Button btnEdit = (Button)findViewById(R.id.btnEdit);
		btnEdit.setOnClickListener(new View.OnClickListener() {

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

	private void initializeSendButton(final ViewFlipper profileRow) {
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
					lblName.setTag(R.string.email_not_set);
				}

				// Hide keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(txtName.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(txtEmail.getWindowToken(), 0);

				// Send user edit request
				try {
					Thread th = new Thread(new UserEditRequestThread(user).getThread());
					th.start();
				} catch (Exception exc) {
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

	private void initializeLogoutButton() {
		Button btnLogout = (Button)findViewById(R.id.btnLogout);

		if(user == null) {
			btnLogout.setBackgroundResource(R.drawable.ic_refresh);
		}

		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Send and store the user data.
				final String name = user.getName();
				final String email = user.getEmail();

				new AlertDialog.Builder(UserForm.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.logout).setMessage(R.string.really_logout).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						user.setName("");
						user.setEmail("");

						EditText txtName = (EditText)findViewById(R.id.txtName);
						txtName.setText(user.getName());

						EditText txtEmail = (EditText)findViewById(R.id.txtEmail);
						txtEmail.setText(user.getEmail());

						TextView lblName = (TextView)findViewById(R.id.lblName);
						lblName.setText(R.string.name_not_set);

						TextView lblEmail = (TextView)findViewById(R.id.lblEmail);
						lblEmail.setText(R.string.email_not_set);

						try {
							Thread th = new Thread(new Runnable() {

								@Override
								public void run() {
									try {
										NetRequests.ActivityRequest(currentLocation, "logout (" + name + ":" + email + ")", "0", user.getId() + "");
										NetRequests.UserEditRequest("", "", user.getImei(), user.getId() + "");
									} catch (Exception e) {
										Log.i(getClass().getSimpleName(), "Thread Logout User: " + e);
									}
								}
							});
							th.start();

						} catch (Exception exc) {
							Log.i(getClass().getSimpleName(), "Logout User: " + exc);
						}
					}

				}).setNegativeButton(R.string.no, null).show();
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
