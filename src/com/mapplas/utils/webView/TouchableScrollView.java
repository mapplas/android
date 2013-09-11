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

	private Animation fadeOutAnimation;

	private Animation fadeInAnimation;

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
			this.navBar.startAnimation(this.fadeInAnimation);
			this.setTimerToNavigationBar();
		}

		View view = (View)getChildAt(getChildCount() - 1);
		int diff = (view.getBottom() - (getHeight() + getScrollY()));
		if(diff == 0) {
			this.navBar.startAnimation(this.fadeInAnimation);
			this.setTimerToNavigationBar();
		}

		super.onScrollChanged(l, t, oldl, oldt);
	}

	private void setTimerToNavigationBar() {
		this.navBar.postDelayed(new Runnable() {

			@Override
			public void run() {
				navBar.startAnimation(fadeOutAnimation);
			}
		}, 3000);
	}

	private void initAnimations() {
		this.fadeOutAnimation = new AlphaAnimation(1, 0);
		this.fadeOutAnimation.setInterpolator(new AccelerateInterpolator()); // and
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
				navBar.setVisibility(View.GONE);
			}
		});

		this.fadeInAnimation = new AlphaAnimation(0, 1);
		this.fadeInAnimation.setInterpolator(new AccelerateInterpolator()); // and
		this.fadeInAnimation.setStartOffset(0);
		this.fadeInAnimation.setDuration(500);
		this.fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				navBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});

	}

}
