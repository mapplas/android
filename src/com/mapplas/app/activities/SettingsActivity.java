package com.mapplas.app.activities;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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
		backButton.setOnClickListener(new View.OnClickListener() {

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
		this.showAppRestartDialog();
	}

	@Override
	public void onDialogSpanishLanguageClick() {
		((MapplasApplication)this.getApplicationContext()).setLanguage(Constants.SPANISH);
		updateLanguage(((MapplasApplication)this.getApplicationContext()).getLanguage());
		this.showAppRestartDialog();
	}

	@Override
	public void onDialogBasqueLanguageClick() {
		((MapplasApplication)this.getApplicationContext()).setLanguage(Constants.BASQUE);
		updateLanguage(((MapplasApplication)this.getApplicationContext()).getLanguage());
		this.showAppRestartDialog();
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
	}

	private void showAppRestartDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.language_change_restart_app_message);
		builder.setPositiveButton(R.string.accept, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(SettingsActivity.this, MapplasActivity.class);
				intent.putExtra(Constants.SETTINGS_LANGUAGE_CHANGE_BUNDLE, true);
				startActivity(intent);
				finish();
			}
		});

		AlertDialog dialog = builder.create();
		
		dialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return true;
			}
		});
		
		dialog.show();
	}
}
