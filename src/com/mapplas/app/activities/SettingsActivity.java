package com.mapplas.app.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
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
import com.mapplas.utils.language.LanguageSetter;
import com.mapplas.utils.visual.custom_views.RobotoButton;
import com.mapplas.utils.visual.custom_views.RobotoTextView;
import com.mapplas.utils.visual.dialogs.LanguageDialogInterface;

public class SettingsActivity extends LanguageActivity implements LanguageDialogInterface {

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
		new LanguageSetter(this).setLanguageToApp(language);
	}

	private void showAppRestartDialog() {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.message_dialog);
		dialog.show();
		
		RobotoTextView title = (RobotoTextView)dialog.findViewById(R.id.dialog_title);
		title.setText(R.string.language_change_restart_app_title);
		RobotoTextView message = (RobotoTextView)dialog.findViewById(R.id.dialog_message);
		message.setText(R.string.language_change_restart_app_message);
		
		RobotoButton positiveButton = (RobotoButton)dialog.findViewById(R.id.dialog_positive_button);
		positiveButton.setText(R.string.accept);
		positiveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(SettingsActivity.this, MapplasActivity.class);
				intent.putExtra(Constants.SETTINGS_LANGUAGE_CHANGE_BUNDLE, true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();	
			}
		});
		
		dialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return true;
			}
		});
	}
}
