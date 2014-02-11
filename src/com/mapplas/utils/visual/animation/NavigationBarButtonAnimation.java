package com.mapplas.utils.visual.animation;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import app.mapplas.com.R;

import com.mapplas.utils.visual.custom_views.RobotoButton;

public class NavigationBarButtonAnimation {

	private RobotoButton userButton;

	private RobotoButton searchButton;

	public NavigationBarButtonAnimation(RobotoButton userButton, RobotoButton searchButton) {
		this.userButton = userButton;
		this.searchButton = searchButton;
	}

	public void startFadeInAnimation(Activity activity) {
		Animation myFadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
		userButton.setVisibility(View.VISIBLE);
		userButton.startAnimation(myFadeInAnimation);

		searchButton.setVisibility(View.VISIBLE);
		searchButton.startAnimation(myFadeInAnimation);
	}

	public void startFadeOutAnimation(Activity activity) {
		Animation myFadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_out);
		userButton.setVisibility(View.GONE);
		userButton.startAnimation(myFadeInAnimation);

		searchButton.setVisibility(View.GONE);
		searchButton.startAnimation(myFadeInAnimation);
	}

}
