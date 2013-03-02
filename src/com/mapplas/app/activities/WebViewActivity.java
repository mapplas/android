package com.mapplas.app.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.mapplas.com.R;

import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.App;
import com.mapplas.utils.webView.TouchableScrollView;

public class WebViewActivity extends Activity {

	private App app = null;

	private RelativeLayout navBar = null;

	private TouchableScrollView scroll = null;
	
	private WebView webView = null;
	
	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);

		this.extractDataFromBundle();
		this.setWebViewClientToWebView();

		this.initializeComponents();
		this.initializeTitle();
		this.initializeBackButton();
	}

	private void setWebViewClientToWebView() {
		this.webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				scroll.stopDialogAndStartAnimation();
			}
		});
	}

	private void extractDataFromBundle() {
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			if(extras.containsKey(AppDetail.APP_DEV_URL_INTENT_DATA) && extras.getParcelable(AppDetail.APP_DEV_URL_INTENT_DATA) != null) {
				this.app = (App)extras.getParcelable(AppDetail.APP_DEV_URL_INTENT_DATA);
				if(!this.app.getAppUrl().equals("")) {
					this.webView = (WebView)findViewById(R.id.webView);
					this.webView.loadUrl(this.app.getAppUrl());
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

	private void initializeComponents() {
		this.scroll = (TouchableScrollView)findViewById(R.id.scrollView1);
		
		this.navBar = (RelativeLayout)findViewById(R.id.webview_navbar);
		this.scroll.setNavBar(this.navBar);
		
		this.progressDialog = ProgressDialog.show(this, "", "Please wait...");
		this.scroll.setProgressDialog(this.progressDialog);
	}
}
