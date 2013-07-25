package com.mapplas.app.activities;

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
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.utils.network.async_tasks.MoreFromDeveloperTask;
import com.mapplas.utils.visual.helpers.PlayStoreLinkCreator;

public class MoreFromDeveloperActivity extends ListActivity {
	
	private App app;
	
	private String country_code;

	private MoreFromDeveloperArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.more_apps_layout);
		
		this.extractDataFromBundle();
		new MoreFromDeveloperTask(this, this.app, this.country_code).execute();

		this.initializeLayoutComponents();
	}

	private void initializeLayoutComponents() {
		this.adapter = new MoreFromDeveloperArrayAdapter(this, R.id.row_more_apps, this.app.moreFromDev());
		this.setListAdapter(this.adapter);

		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();

		// Back button
		Button backButton = (Button)findViewById(R.id.btnBack);
		backButton.setTypeface(normalTypeFace);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void extractDataFromBundle() {
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			if(extras.containsKey(Constants.MORE_FROM_DEVELOPER_APP) ) {
				this.app = (App)extras.getParcelable(Constants.MORE_FROM_DEVELOPER_APP);
			}
			if(extras.containsKey(Constants.MORE_FROM_DEVELOPER_COUNTRY_CODE)) {
				this.country_code = extras.getString(Constants.MORE_FROM_DEVELOPER_COUNTRY_CODE);
			}
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String playStoreLink = new PlayStoreLinkCreator().createLinkForApp(this.app.moreFromDev().get(position).id());
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink));
		MoreFromDeveloperActivity.this.startActivity(browserIntent);
	}
	
	/**
	 * Task response
	 *
	 */
	public void requestFinishedNok() {
		new MoreFromDeveloperTask(this, this.app, this.country_code).execute();
	}
	
	public void requestFinishedOk() {
		this.adapter.notifyDataSetChanged();
		
		Typeface italicTypeFace = ((MapplasApplication)this.getApplicationContext()).getItalicTypeFace();

		// Screen title
		TextView appNameTextView = (TextView)findViewById(R.id.lblAppDetail);
		appNameTextView.setText(this.app.developerName());
		appNameTextView.setTypeface(italicTypeFace);
	}
}
