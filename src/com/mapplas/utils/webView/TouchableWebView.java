package com.mapplas.utils.webView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class TouchableWebView extends WebView {

	private RelativeLayout navBar = null;

	public TouchableWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TouchableWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TouchableWebView(Context context) {
		super(context);
	}

	public void setNavBar(RelativeLayout navigationBar) {
		this.navBar = navigationBar;
		this.setTimerToNavigationBar();
	}

	private void setTimerToNavigationBar() {
		this.navBar.postDelayed(new Runnable() {

			@Override
			public void run() {
				navBar.setVisibility(View.GONE);
			}
		}, 3000);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		switch (event.getAction()) {
//			case (MotionEvent.ACTION_DOWN):
//				this.navBar.setVisibility(View.VISIBLE);
//				this.setTimerToNavigationBar();
//				return true;
//			default:
//				return false;
//		}
//	}
}
