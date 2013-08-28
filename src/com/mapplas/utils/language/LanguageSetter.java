package com.mapplas.utils.language;

import java.util.Locale;

import com.mapplas.model.Constants;

import android.app.Activity;
import android.content.res.Configuration;

public class LanguageSetter {

	private Activity activity;

	public LanguageSetter(Activity activity) {
		this.activity = activity;
	}

	public void setLanguageToApp(String language) {

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
		this.activity.getBaseContext().getResources().updateConfiguration(config, this.activity.getBaseContext().getResources().getDisplayMetrics());
	}

}
