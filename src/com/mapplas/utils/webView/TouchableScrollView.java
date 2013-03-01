package com.mapplas.utils.webView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class TouchableScrollView extends ScrollView {

	private RelativeLayout navBar = null;

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
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if(t <= 10) {
			this.navBar.setVisibility(View.VISIBLE);
			this.setTimerToNavigationBar();
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}

	private void setTimerToNavigationBar() {
		this.navBar.postDelayed(new Runnable() {

			@Override
			public void run() {
				navBar.setVisibility(View.GONE);
			}
		}, 3000);
	}

}
