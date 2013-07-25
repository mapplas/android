package com.mapplas.app.activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.more_apps.MoreFromDeveloperArrayAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.Constants;
import com.mapplas.model.MoreFromDeveloperApp;
import com.mapplas.utils.visual.helpers.PlayStoreLinkCreator;

public class MoreFromDeveloperActivity extends ListActivity {

	private ArrayList<MoreFromDeveloperApp> items;

	private MoreFromDeveloperArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.more_apps_layout);

		this.extractDataFromBundle();
		this.initializeLayoutComponents();
	}

	private void initializeLayoutComponents() {
		this.adapter = new MoreFromDeveloperArrayAdapter(this, R.id.row_more_apps, this.items);
		this.setListAdapter(this.adapter);

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

		// Screen title
		TextView appNameTextView = (TextView)findViewById(R.id.lblAppDetail);
		appNameTextView.setText("TODO DEVELOPER NAME");
		appNameTextView.setTypeface(italicTypeFace);
	}

	private void extractDataFromBundle() {
		Intent intent = getIntent();
		if(intent != null) {
			this.items = intent.getParcelableArrayListExtra(Constants.MORE_FROM_DEVELOPER_APP_ARRAY);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String playStoreLink = new PlayStoreLinkCreator().createLinkForApp(this.items.get(position).id());
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink));
		MoreFromDeveloperActivity.this.startActivity(browserIntent);
	}
}
