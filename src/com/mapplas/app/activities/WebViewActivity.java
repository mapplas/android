package com.mapplas.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.mapplas.com.R;

import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.App;
import com.mapplas.utils.webView.TouchableWebView;

public class WebViewActivity extends Activity {

	private App app = null;
	
	private RelativeLayout navBar = null;
	
	private LinearLayout webLayout = null;
	
	private TouchableWebView gestureWebview = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		
		this.initializeNavigationBarAnimation();
		this.extractDataFromBundle();

		this.initializeTitle();
		this.initializeBackButton();
	}

	private void extractDataFromBundle() {
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			if(extras.containsKey(AppDetail.APP_DEV_URL_INTENT_DATA) && extras.getParcelable(AppDetail.APP_DEV_URL_INTENT_DATA) != null) {
				this.app = (App)extras.getParcelable(AppDetail.APP_DEV_URL_INTENT_DATA);
				if(!this.app.getAppUrl().equals("")) {
					this.gestureWebview.loadUrl(this.app.getAppUrl());
//					this.gestureWebview.loadUrl("www.google.es");

				}
				else {
					Toast errorToast = Toast.makeText(this, getString(R.string.app_detail_app_web_dev_url_error), Toast.LENGTH_LONG);
					errorToast.show();
				}
			}
		}
	}

	private void initializeTitle() {
		TextView titleText = (TextView)findViewById(R.id.lblAppDetail);
		titleText.setTypeface(((MapplasApplication)this.getApplicationContext()).getItalicTypeFace());
		
		if(this.app == null) {
			titleText.setText(getString(R.string.error));
		}
		else {
			titleText.setText(this.app.getName());
		}
	}

	private void initializeBackButton() {
		Button backButton = (Button)findViewById(R.id.btnBack);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void initializeNavigationBarAnimation() {
		this.webLayout = (LinearLayout)findViewById(R.id.webview_layout);
		this.navBar = (RelativeLayout)findViewById(R.id.webview_navbar);
		
		this.gestureWebview = new TouchableWebView(this);
		this.gestureWebview.setNavBar(this.navBar);
		this.webLayout.addView(gestureWebview);
	}
}
