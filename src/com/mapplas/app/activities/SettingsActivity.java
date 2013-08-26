package com.mapplas.app.activities;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.settings.SettingsAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.Constants;
import com.mapplas.utils.visual.dialogs.LanguageDialogInterface;

public class SettingsActivity extends Activity implements LanguageDialogInterface {

	private SettingsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		
		this.initializeLayoutComponents();
	}

	private void initializeLayoutComponents() {
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();

		Button backButton = (Button)this.findViewById(R.id.preferences_back_button);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ListView list = (ListView)this.findViewById(R.id.preferences_list);
		this.adapter = new SettingsAdapter(this, normalTypeFace);
		list.setAdapter(this.adapter);
	}

	// Language dialog interface methods
	@Override
	public void onDialogEnglishLanguageClick() {
		((MapplasApplication)this.getApplicationContext()).setLanguage(Constants.ENGLISH);
		updateLanguage(((MapplasApplication)this.getApplicationContext()).getLanguage());
	}

	@Override
	public void onDialogSpanishLanguageClick() {
		((MapplasApplication)this.getApplicationContext()).setLanguage(Constants.SPANISH);
		updateLanguage(((MapplasApplication)this.getApplicationContext()).getLanguage());
	}

	@Override
	public void onDialogBasqueLanguageClick() {
		((MapplasApplication)this.getApplicationContext()).setLanguage(Constants.BASQUE);
		updateLanguage(((MapplasApplication)this.getApplicationContext()).getLanguage());
	}

	private void updateLanguage(String language) {
		this.adapter.notifyDataSetChanged();
		
		Locale locale = null;
		if(language.equals(Constants.ENGLISH)) {
			locale = new Locale("en");
		}
		else if(language.equals(Constants.SPANISH)) {
			locale = new Locale("es");
		}
		else {
			locale = new Locale("eu");
		}

		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

		Intent intent = new Intent(this, MapplasActivity.class);
		intent.putExtra(Constants.SETTINGS_LANGUAGE_CHANGE_BUNDLE, true);
		this.startActivity(intent);
		this.finish();
	}

}
