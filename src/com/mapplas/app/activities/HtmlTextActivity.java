package com.mapplas.app.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import app.mapplas.com.R;

import com.mapplas.model.Constants;

public class HtmlTextActivity extends LanguageActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.text_layout);

		Bundle bundle = this.getIntent().getExtras();
		if(bundle != null) {

			if(bundle.containsKey(Constants.MAPPLAS_TEXT_ACTIVITY_EXTRA_MESSAGE)) {
				WebView screenMessage = (WebView)this.findViewById(R.id.web_layout_message);

				String html = bundle.getString(Constants.MAPPLAS_TEXT_ACTIVITY_EXTRA_MESSAGE);
				String mime = "text/html";
				String encoding = "utf-8";

				screenMessage.getSettings().setJavaScriptEnabled(true);
				screenMessage.loadDataWithBaseURL(null, html, mime, encoding, null);
			}
		}

		Button backButton = (Button)this.findViewById(R.id.nav_bar_back_button);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
