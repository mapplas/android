package com.mapplas.app.activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.more_apps.MoreFromDeveloperArrayAdapter;
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
	}

	private void extractDataFromBundle() {
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			if(extras.containsKey(Constants.MORE_FROM_DEVELOPER_APP_ARRAY)) {
				this.items = extras.getParcelable(Constants.MORE_FROM_DEVELOPER_APP_ARRAY);
			}
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String playStoreLink = new PlayStoreLinkCreator().createLinkForApp(this.items.get(position).id());
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink));
		MoreFromDeveloperActivity.this.startActivity(browserIntent);
	}
}
