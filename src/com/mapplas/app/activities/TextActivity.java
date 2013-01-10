package com.mapplas.app.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.Constants;

public class TextActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.text_layout);
		
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();
		Typeface italicTypeFace = ((MapplasApplication)this.getApplicationContext()).getItalicTypeFace();

		Bundle bundle = this.getIntent().getExtras();
		if(bundle != null) {
			if(bundle.containsKey(Constants.MAPPLAS_TEXT_ACTIVITY_EXTRA_TITLE)) {
				TextView screenTitle = (TextView)this.findViewById(R.id.text_layout_screen_title);
				screenTitle.setTypeface(italicTypeFace);
				screenTitle.setText(bundle.getString(Constants.MAPPLAS_TEXT_ACTIVITY_EXTRA_TITLE));
			}

			if(bundle.containsKey(Constants.MAPPLAS_TEXT_ACTIVITY_EXTRA_MESSAGE)) {
				TextView screenMessage = (TextView)this.findViewById(R.id.text_layout_message);
				screenMessage.setTypeface(normalTypeFace);
				screenMessage.setText(bundle.getString(Constants.MAPPLAS_TEXT_ACTIVITY_EXTRA_MESSAGE));
			}
		}
		
		Button backButton = (Button)this.findViewById(R.id.text_layout_back_button);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
