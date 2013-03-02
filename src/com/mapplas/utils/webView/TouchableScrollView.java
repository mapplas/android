package com.mapplas.utils.webView;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class TouchableScrollView extends ScrollView {

	private RelativeLayout navBar;
	
	private ProgressDialog progressDialog;

	private Animation translateOutAnimation;

	private Animation translateInAnimation;

	public TouchableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TouchableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TouchableScrollView(Context context) {
		super(context);
	}

	public void setNavBar(RelativeLayout navigationBar) {
		this.navBar = navigationBar;
		this.initAnimations();
	}
	
	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}
	
	public void stopDialogAndStartAnimation() {
		this.progressDialog.hide();
		this.setTimerToNavigationBar();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if(t <= 10) {
			// this.navBar.startAnimation(this.translateInAnimation);
			this.setTimerToNavigationBar();
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}

	private void setTimerToNavigationBar() {
		this.navBar.postDelayed(new Runnable() {

			@Override
			public void run() {
				navBar.startAnimation(translateOutAnimation);
			}
		}, 9000);
	}

	private void initAnimations() {
		this.translateOutAnimation = new AlphaAnimation(1, 0);
		this.translateOutAnimation.setInterpolator(new AccelerateInterpolator()); // and
		this.translateOutAnimation.setStartOffset(0);
		this.translateOutAnimation.setDuration(500);
		this.translateOutAnimation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				navBar.setVisibility(View.GONE);
			}
		});

		// this.translateInAnimation = new TranslateAnimation(0, 0, -100, 0);
		// this.translateInAnimation.setInterpolator(new
		// AccelerateInterpolator());
		// this.translateInAnimation.setStartOffset(0);
		// this.translateInAnimation.setDuration(700);
		// this.translateOutAnimation.setAnimationListener(new
		// Animation.AnimationListener() {
		//
		// @Override
		// public void onAnimationStart(Animation animation) {
		// navBar.setVisibility(View.VISIBLE);
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation animation) {
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation) {
		// }
		// });
	}

}
