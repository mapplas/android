package com.mapplas.app.activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.detail.MoreAppsArrayAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.Constants;
import com.mapplas.model.MoreFromDeveloperApp;

public class MoreFromDeveloperActivity extends ListActivity {

	private ArrayList<MoreFromDeveloperApp> items;

	private MoreAppsArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_apps_layout);

		this.extractDataFromBundle();

		this.adapter = new MoreAppsArrayAdapter(this, R.layout.row_more_apps, this.items);
		this.setListAdapter(this.adapter);
		
		this.initLayoutComponents();
	}

	private void initLayoutComponents() {
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();
		Typeface italicTypeFace = ((MapplasApplication)this.getApplicationContext()).getItalicTypeFace();

		// Back button
		Button backButton = (Button)findViewById(R.id.btnBack);
		backButton.setTypeface(normalTypeFace);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// Header text
		TextView appNameTextView = (TextView)findViewById(R.id.lblAppDetail);
		appNameTextView.setText("TODO MORE FROM DEVELOPER");
		appNameTextView.setTypeface(italicTypeFace);
	}

	private void extractDataFromBundle() {
		// Get the id of the app selected
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			if(extras.containsKey(Constants.MORE_FROM_DEVELOPER_APP_ARRAY)) {
				this.items = extras.getParcelableArrayList(Constants.MORE_FROM_DEVELOPER_APP_ARRAY);
			}
		}
	}
}
