package com.mapplas.app.adapters.app;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import app.mapplas.com.R;

import com.mapplas.app.activities.AppDetail;
import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
import com.mapplas.utils.network.async_tasks.NotifyUserTask;
import com.mapplas.utils.network.requests.BlockRequestThread;
import com.mapplas.utils.network.requests.PinRequestThread;
import com.mapplas.utils.network.requests.ShareRequestThread;
import com.mapplas.utils.static_intents.SuperModelSingleton;
import com.mapplas.utils.third_party.RefreshableListView;
import com.mapplas.utils.utils.LogoHelper;
import com.mapplas.utils.view_holder.AppViewHolder;
import com.mapplas.utils.visual.custom_views.RobotoButton;
import com.mapplas.utils.visual.custom_views.RobotoTextView;
import com.mapplas.utils.visual.helpers.AppLaunchHelper;
import com.mapplas.utils.visual.helpers.PlayStoreLinkCreator;
import com.mapplas.utils.visual.helpers.ShareHelper;

public class AppArrayAdapter extends ArrayAdapter<App> {

	public Context context;

	private ArrayList<App> items;

	private Animation animFlipInNext = null;

	private Animation animFlipOutNext = null;

	private Animation animFlipInPrevious = null;

	private Animation animFlipOutPrevious = null;

	private RefreshableListView list = null;

	protected SuperModel model = null;

	public User user = null;

	private static App mBlockApp = null;

	private Animation fadeOutAnimation = null;

	private ViewFlipper currentViewFlipped = null;

	public AppArrayAdapter(Context context, int layout, int textViewResourceId, ArrayList<App> items, RefreshableListView list, SuperModel model) {
		super(context, layout, textViewResourceId, items);

		this.context = context;
		this.items = items;
		this.list = list;
		this.model = model;

		this.user = this.model.currentUser();

		this.initializeAnimations();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppViewHolder cellHolder;
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(this.items.size() == 1 && this.items.get(0).getAppType().equals(Constants.MAPPLAS_APPLICATION_TYPE_MOCK)) {
			View commingSoonCell = inflater.inflate(R.layout.empty_apps, null);

			RobotoButton notifyUserButton = (RobotoButton)commingSoonCell.findViewById(R.id.comming_soon_notify_user_button);
			notifyUserButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
					final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
					dialog.setContentView(R.layout.dialog_two_buttons_edittext);
					dialog.setCanceledOnTouchOutside(true);
					dialog.show();

					RobotoTextView title = (RobotoTextView)dialog.findViewById(R.id.dialog_title);
					title.setText(R.string.notify_user_dialog_title);
					RobotoTextView message = (RobotoTextView)dialog.findViewById(R.id.dialog_message);
					message.setText(R.string.notify_user_dialog_message);
					
					final EditText userEmail = (EditText)dialog.findViewById(R.id.dialog_edittext);
					
					RobotoButton positiveButton = (RobotoButton)dialog.findViewById(R.id.dialog_positive_button);
					positiveButton.setText(R.string.cancel);
					positiveButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					
					RobotoButton negativeButton = (RobotoButton)dialog.findViewById(R.id.dialog_negative_button);
					negativeButton.setText(R.string.send);
					negativeButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							new NotifyUserTask(user.getId(), userEmail.getText().toString(), model.getLocation().getLatitude(), model.getLocation().getLongitude()).execute();
							dialog.dismiss();
						}
					});
				}
			});

			return commingSoonCell;
		}
		else {
			App app = this.items.get(position);

			if(convertView == null || !(convertView.getTag() instanceof AppViewHolder)) {
				cellHolder = new AppViewHolder();

				convertView = inflater.inflate(R.layout.rowloc, null);

				cellHolder.title = (TextView)convertView.findViewById(R.id.lblTitle);
				cellHolder.shortDescription = (TextView)convertView.findViewById(R.id.lblShortDescription);

				cellHolder.pinUp = (TextView)convertView.findViewById(R.id.lblPinUp);
				cellHolder.rate = (TextView)convertView.findViewById(R.id.lblRate);
				cellHolder.share = (TextView)convertView.findViewById(R.id.lblShare);
				cellHolder.block = (TextView)convertView.findViewById(R.id.lblBlock);

				cellHolder.pinUpImg = (ImageView)convertView.findViewById(R.id.btnPinUp);
				cellHolder.rateImg = (ImageView)convertView.findViewById(R.id.btnRate);
				cellHolder.shareImg = (ImageView)convertView.findViewById(R.id.btnShare);
				cellHolder.blockImg = (ImageView)convertView.findViewById(R.id.btnBlock);

				cellHolder.pinUpLayout = (LinearLayout)convertView.findViewById(R.id.lytPinup);
				cellHolder.rateLayout = (LinearLayout)convertView.findViewById(R.id.lytRate);
				cellHolder.shareLayout = (LinearLayout)convertView.findViewById(R.id.lytShare);
				cellHolder.blockLayout = (LinearLayout)convertView.findViewById(R.id.lytBlock);

				cellHolder.buttonStart = (Button)convertView.findViewById(R.id.btnStart);

				cellHolder.viewFlipper = (ViewFlipper)convertView.findViewById(R.id.vfRowLoc);
				cellHolder.logo = (ImageView)convertView.findViewById(R.id.imgLogo);
				cellHolder.logoRoundCorner = (ImageView)convertView.findViewById(R.id.imgRonundC);

				cellHolder.rowUnpressed = (LinearLayout)convertView.findViewById(R.id.id_rowloc_unpressed);

				this.initializeCellHolder(app, cellHolder);

				this.initializeStartButton(app, cellHolder.buttonStart);

				this.initializeLogo(app, cellHolder.logo);
				this.initializeRowUnpressed(app, cellHolder.rowUnpressed, position, cellHolder.viewFlipper);
				this.initializeLogoBackgroundPinImage(app, cellHolder.logoRoundCorner);

				this.initializeActionLayouts(app, cellHolder, convertView);

				convertView.setTag(cellHolder);
			}
			else {
				cellHolder = (AppViewHolder)convertView.getTag();

				this.initializeCellHolder(app, cellHolder);

				this.initializeStartButton(app, cellHolder.buttonStart);

				this.initializeLogo(app, cellHolder.logo);
				this.initializeRowUnpressed(app, cellHolder.rowUnpressed, position, cellHolder.viewFlipper);
				this.initializeLogoBackgroundPinImage(app, cellHolder.logoRoundCorner);

				this.initializeActionLayouts(app, cellHolder, convertView);
			}

			// Cell background
			if(app.isAuxPin() == 0) {
				convertView.setBackgroundResource(R.color.White);
			}
			else {
				convertView.setBackgroundResource(R.color.pinned_app_cell_background_color);
			}

			return convertView;
		}
	}

	private void initializeAnimations() {
		this.fadeOutAnimation = new AlphaAnimation(1, 0);
		this.fadeOutAnimation.setInterpolator(new AccelerateInterpolator()); // and
																				// this
		this.fadeOutAnimation.setStartOffset(0);
		this.fadeOutAnimation.setDuration(500);
		this.fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(mBlockApp != null) {
					remove(mBlockApp);
				}
			}
		});

		this.animFlipInNext = AnimationUtils.loadAnimation(this.context, R.anim.flipinnext);
		this.animFlipOutNext = AnimationUtils.loadAnimation(this.context, R.anim.flipoutnext);
		this.animFlipInPrevious = AnimationUtils.loadAnimation(this.context, R.anim.flipinprevious);
		this.animFlipOutPrevious = AnimationUtils.loadAnimation(this.context, R.anim.flipoutprevious);
	}

	private void initializeCellHolder(App app, AppViewHolder cellHolder) {
		// Set typefaces
		Typeface normalTypeface = ((MapplasApplication)this.context.getApplicationContext()).getTypeFace();
		cellHolder.title.setTypeface(normalTypeface);
		cellHolder.title.setText(app.getName());

		if(app.getName() != null && app.getAppShortDescription() != null) {

			if(!app.getName().equals("") && !app.getAppShortDescription().equals("")) {
				// Check 1-2 lines for app name. 2-3 lines for app short
				// description.
				if(cellHolder.title.getMeasuredWidth() != 0 && cellHolder.title.getMeasuredWidth() < cellHolder.title.getPaint().measureText(app.getName())) {
					cellHolder.title.setSingleLine(false);
					cellHolder.title.setLines(2);
					// cellHolder.title.setBackgroundColor(Color.RED);
					cellHolder.shortDescription.setLines(2);
					// cellHolder.shortDescription.setBackgroundColor(Color.BLUE);
				}
				else {
					cellHolder.title.setSingleLine(true);
					cellHolder.title.setLines(1);
					// cellHolder.title.setBackgroundColor(Color.RED);
					cellHolder.shortDescription.setLines(3);
					// cellHolder.shortDescription.setBackgroundColor(Color.BLUE);
				}
			}
			else if(!app.getName().equals("") && app.getAppShortDescription().equals("")) {
				cellHolder.title.setSingleLine(false);
				cellHolder.title.setLines(3);
				// cellHolder.title.setBackgroundColor(Color.YELLOW);
				cellHolder.shortDescription.setLines(0);
				// cellHolder.shortDescription.setBackgroundColor(Color.GRAY);
			}
			else if(app.getName().equals("") && !app.getAppShortDescription().equals("")) {
				cellHolder.shortDescription.setSingleLine(false);
				cellHolder.shortDescription.setLines(3);
				// cellHolder.shortDescription.setBackgroundColor(Color.GREEN);
				cellHolder.title.setLines(0);
				// cellHolder.title.setBackgroundColor(Color.MAGENTA);
			}

		}

		cellHolder.shortDescription.setTypeface(normalTypeface);
		cellHolder.shortDescription.setText(app.getAppShortDescription());

		cellHolder.pinUp.setTypeface(normalTypeface);
		cellHolder.rate.setTypeface(normalTypeface);
		cellHolder.share.setTypeface(normalTypeface);
		cellHolder.block.setTypeface(normalTypeface);

		cellHolder.buttonStart.setTypeface(normalTypeface);

		// Initialize viewFlipper
		cellHolder.viewFlipper.setInAnimation(null);
		cellHolder.viewFlipper.setOutAnimation(null);
		cellHolder.viewFlipper.setDisplayedChild(0);
	}

	private void initializeActionLayouts(App app, AppViewHolder cellHolder, View convertView) {

		this.initializePinUpLayout(app, cellHolder);
		this.initializeBlockLayout(app, cellHolder, convertView);
		this.initializeShareLayout(app, cellHolder);

		if(app.getInternalApplicationInfo() != null) {
			cellHolder.rateLayout.setVisibility(View.VISIBLE);
			this.initializeRateLayout(app, cellHolder);
		}
		else {
			cellHolder.rateLayout.setVisibility(View.GONE);
		}
	}

	private void initializeRowUnpressed(final App app, LinearLayout layout, int position, ViewFlipper viewFlipper) {

		layout.setTag(viewFlipper);
		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(app.getAppType().equalsIgnoreCase(Constants.MAPPLAS_APPLICATION_TYPE_ANDROID_APPLICATION)) {
					Intent intent = new Intent(context, AppDetail.class);
					intent.putExtra(Constants.MAPPLAS_DETAIL_APP, app);
					SuperModelSingleton.model = model;
					((MapplasActivity)context).startActivity(intent);
				}
				else {
					final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(app.getId()));
					context.startActivity(intent);
				}
			}
		});

		layout.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if(currentViewFlipped != null) {
					closeFlippedView();
				}

				currentViewFlipped = (ViewFlipper)v.getTag();

				if(currentViewFlipped != null) {
					if(currentViewFlipped.indexOfChild(currentViewFlipped.getCurrentView()) == 0) {
						openFlippedView();
					}
				}
				return true;
			}
		});

		this.list.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
					case MotionEvent.ACTION_MOVE:
						if(currentViewFlipped != null) {
							closeFlippedView();
						}
						break;

					default:
						break;
				}
				return false;
			}
		});
	}

	private void initializeStartButton(final App app, Button buttonStart) {
		new AppLaunchHelper(this.context, buttonStart, app, this.user, this.model.getLocation()).help();
	}

	private void initializeLogoBackgroundPinImage(App app, ImageView image) {
		if(app.isAuxPin() == 1) {
			image.setBackgroundResource(R.drawable.roundc_pinup);
		}
		else {
			image.setBackgroundResource(R.drawable.roundc);
		}
	}

	private void initializeLogo(App app, ImageView logo) {
		logo.setImageResource(R.drawable.ic_template);

		// Load app logo
		new LogoHelper(this.context).setLogoToApp(app, logo);
	}

	private void initializePinUpLayout(final App app, final AppViewHolder cellHolder) {

		if(app.isAuxPin() == 1) {
			cellHolder.pinUpImg.setImageResource(R.drawable.action_un_pinup_button);
			cellHolder.logoRoundCorner.setBackgroundResource(R.drawable.roundc_pinup);
			cellHolder.pinUp.setText(R.string.un_pin_up);
		}
		else {
			cellHolder.pinUpImg.setImageResource(R.drawable.action_pin_button);
			cellHolder.logoRoundCorner.setBackgroundResource(R.drawable.roundc);
			cellHolder.pinUp.setText(R.string.pin_up);
		}

		cellHolder.pinUpLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(app != null) {
					String auxuid = "0";
					if(user != null) {
						auxuid = String.valueOf(user.getId());
					}

					final String uid = auxuid;

					if(app.isAuxPin() == 1) {
						cellHolder.pinUpImg.setImageResource(R.drawable.action_pin_button);
						cellHolder.logoRoundCorner.setBackgroundResource(R.drawable.roundc);
						cellHolder.pinUp.setText(R.string.pin_up);
					}
					else {
						cellHolder.pinUpImg.setImageResource(R.drawable.action_un_pinup_button);
						cellHolder.logoRoundCorner.setBackgroundResource(R.drawable.roundc_pinup);
						cellHolder.pinUp.setText(R.string.un_pin_up);
					}

					cellHolder.logoRoundCorner.invalidate();
					v.invalidate();
					list.invalidate();

					String pinRequestConstant = Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_PIN;
					if(app.isAuxPin() == 1) {
						pinRequestConstant = Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_UNPIN;
					}

					// Pin/unpin request
					Thread pinRequestThread = new Thread(new PinRequestThread(pinRequestConstant, app, uid, model.getLocation(), model.currentDescriptiveGeoLoc()).getThread());
					pinRequestThread.start();

					// Set app object as pinned or unpinned
					// Pin/unpin app
					boolean found = false;
					int i = 0;
					while (!found && i < model.appList().size()) {
						if(model.appList().get(i).equals(app)) {

							if(app.isAuxPin() == 1) {
								model.appList().get(i).setAuxPin(0);
							}
							else {
								model.appList().get(i).setAuxPin(1);
							}

							found = true;
						}
						i++;
					}

					list.updateAdapter(context, model, new ArrayList<ApplicationInfo>());
					currentViewFlipped = null;
				}

			}
		});
	}

	private void initializeBlockLayout(final App app, AppViewHolder cellHolder, final View convertView) {
		cellHolder.blockLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBlockApp = app;

				final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
				dialog.setContentView(R.layout.dialog_two_buttons);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();

				RobotoTextView title = (RobotoTextView)dialog.findViewById(R.id.dialog_title);
				title.setText(R.string.block_title);
				RobotoTextView message = (RobotoTextView)dialog.findViewById(R.id.dialog_message);
				message.setText(R.string.block_text);

				RobotoButton positiveButton = (RobotoButton)dialog.findViewById(R.id.dialog_positive_button);
				positiveButton.setText(R.string.block_accept);
				positiveButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						convertView.startAnimation(fadeOutAnimation);
						currentViewFlipped = null;

						String uid = "0";
						if(user != null) {
							uid = String.valueOf(user.getId());
						}

						// Block request thread
						Thread likeRequestThread = new Thread(new BlockRequestThread(Constants.MAPPLAS_ACTIVITY_LIKE_REQUEST_BLOCK, app, uid).getThread());
						likeRequestThread.start();

						// Do unpin also
						if(mBlockApp.isAuxPin() == 1) {
							Thread unpinRequestThread = new Thread(new PinRequestThread(Constants.MAPPLAS_ACTIVITY_PIN_REQUEST_UNPIN, app, uid, model.getLocation(), model.currentDescriptiveGeoLoc()).getThread());
							unpinRequestThread.start();

						}
						dialog.dismiss();
					}
				});

				RobotoButton negativeButton = (RobotoButton)dialog.findViewById(R.id.dialog_negative_button);
				negativeButton.setText(R.string.block_cancel);
				negativeButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						currentViewFlipped = null;
						dialog.dismiss();
					}
				});
			}
		});
	}

	private void initializeRateLayout(final App app, AppViewHolder cellHolder) {
		cellHolder.rateLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				currentViewFlipped = null;

				if(app != null) {
					String strUrl = new PlayStoreLinkCreator().createLinkForApp(app.getId());
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl));
					context.startActivity(browserIntent);
				}
			}
		});
	}

	private void initializeShareLayout(final App app, AppViewHolder cellHolder) {
		cellHolder.shareLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				currentViewFlipped = null;

				if(app != null) {
					context.startActivity(Intent.createChooser(new ShareHelper().getSharingIntent(context, app), context.getString(R.string.share)));

					Thread shareThread = new Thread(new ShareRequestThread(app.getId(), user.getId(), model.getLocation()).getThread());
					shareThread.run();
				}

			}
		});
	}

	private void closeFlippedView() {
		currentViewFlipped.setInAnimation(animFlipInPrevious);
		currentViewFlipped.setOutAnimation(animFlipOutPrevious);
		currentViewFlipped.showPrevious();
		currentViewFlipped = null;
	}

	private void openFlippedView() {
		currentViewFlipped.setInAnimation(animFlipInNext);
		currentViewFlipped.setOutAnimation(animFlipOutNext);
		currentViewFlipped.showNext();
	}

}
