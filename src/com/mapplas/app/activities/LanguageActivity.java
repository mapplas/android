package com.mapplas.app.activities;

import android.app.Activity;

import com.mapplas.app.application.MapplasApplication;
import com.mapplas.utils.language.LanguageSetter;

public class LanguageActivity extends Activity {

	@Override
	protected void onRestart() {
		super.onRestart();
		new LanguageSetter(this).setLanguageToApp(((MapplasApplication)this.getApplicationContext()).getLanguage());
	}

	@Override
	protected void onStart() {
		new LanguageSetter(this).setLanguageToApp(((MapplasApplication)this.getApplicationContext()).getLanguage());
		super.onStart();
	}
}
