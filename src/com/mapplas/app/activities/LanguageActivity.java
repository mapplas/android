package com.mapplas.app.activities;

import com.mapplas.app.application.MapplasApplication;
import com.mapplas.utils.language.LanguageSetter;

import android.app.Activity;

public class LanguageActivity extends Activity {

	@Override
	protected void onRestart() {
		super.onRestart();
		new LanguageSetter(this).setLanguageToApp(((MapplasApplication)this.getApplicationContext()).getLanguage());
	}

}
