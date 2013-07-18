package com.mapplas.app.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.about_us.AboutUsAdapter;
import com.mapplas.app.application.MapplasApplication;

public class AboutUsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);

		this.initializeLayoutComponents();
	}

	private void initializeLayoutComponents() {
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();
		Typeface italicTypeFace = ((MapplasApplication)this.getApplicationContext()).getItalicTypeFace();

		TextView title = (TextView)this.findViewById(R.id.preferences_screen_title);
		title.setTypeface(italicTypeFace);

		Button backButton = (Button)this.findViewById(R.id.preferences_back_button);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ListView list = (ListView)this.findViewById(R.id.preferences_list);
		list.setAdapter(new AboutUsAdapter(this, normalTypeFace));
	}

}
