package com.mapplas.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import app.mapplas.com.R;

import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.App;

public class WebViewActivity extends Activity {

	private App app = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);

		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			if(extras.containsKey(AppDetail.APP_DEV_URL_INTENT_DATA) && extras.getParcelable(AppDetail.APP_DEV_URL_INTENT_DATA) != null) {
				this.app = (App)extras.getParcelable(AppDetail.APP_DEV_URL_INTENT_DATA);
				if(!this.app.getAppUrl().equals("")) {
					WebView myWebView = (WebView)findViewById(R.id.webview);
					myWebView.loadUrl(this.app.getAppUrl());
				}
				else {
					Toast errorToast = Toast.makeText(this, getString(R.string.app_detail_app_web_dev_url_error), Toast.LENGTH_LONG);
					errorToast.show();
				}
			}
		}

		this.initializeTitle();
		this.initializeBackButton();
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

}
