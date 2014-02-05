package com.mapplas.app.activities;

import android.app.ListActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.more_apps.MoreFromDeveloperArrayAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.utils.MoreFromDeveloperHelper;
import com.mapplas.utils.language.LanguageSetter;
import com.mapplas.utils.network.async_tasks.MoreFromDeveloperTask;
import com.mapplas.utils.static_intents.SuperModelSingleton;

public class MoreFromDeveloperActivity extends ListActivity {
	
	private App app;
	
	private String app_language;

	private MoreFromDeveloperArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.more_apps_layout);
		
		this.extractDataFromBundle();
		new MoreFromDeveloperTask(this, this.app, this.app_language, this).execute();

		this.initializeLayoutComponents();
	}
	
	// All other apps extends from LanguageActivity to avoid setting those three lines of code on each.
	// Because MoreFromDeveloperActivity is a listActivity code is set here.
	@Override
	protected void onRestart() {
		super.onRestart();
		new LanguageSetter(this).setLanguageToApp(((MapplasApplication)this.getApplicationContext()).getLanguage());
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
				this.app_language = extras.getString(Constants.MORE_FROM_DEVELOPER_COUNTRY_CODE);
			}
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		new MoreFromDeveloperHelper().launchAppDetailFromMoreFromDeveloperApp(this.app.moreFromDev().get(position), this, SuperModelSingleton.model);
	}
	
	/**
	 * Task response
	 *
	 */
	public void requestFinishedNok() {
		new MoreFromDeveloperTask(this, this.app, this.app_language, this).execute();
	}
	
	public void requestFinishedOk() {
		this.adapter.notifyDataSetChanged();
	}
}
