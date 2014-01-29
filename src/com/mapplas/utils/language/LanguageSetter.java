package com.mapplas.utils.language;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;

import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.Constants;

public class LanguageSetter {

	private Context context;

	public LanguageSetter(Context context) {
		this.context = context;
	}

	public void setLanguageToApp(String language) {

		Locale locale = null;
		if(language.equals(Constants.ENGLISH)) {
			locale = new Locale("en");
		}
		else if(language.equals(Constants.SPANISH)) {
			locale = new Locale("es");
		}
		else if(language.equals(Constants.BASQUE)) {
			locale = new Locale("eu");
		}
		else if(Locale.getDefault().getDisplayLanguage().equals("espa–ol")) {
			locale = new Locale("es");
		}
		else if(Locale.getDefault().getDisplayLanguage().equals("euskera")) {
			locale = new Locale("eu");
		}
		else {
			locale = new Locale("en");
		}

		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		this.context.getResources().updateConfiguration(config, this.context.getResources().getDisplayMetrics());
	}

	public String getLanguageConstantFromPhone() {
		
		String constantLanguage = ((MapplasApplication)this.context.getApplicationContext()).getResources().getConfiguration().locale.getDisplayName();
		
		if(constantLanguage.equals("espa–ol")) {
			return Constants.SPANISH;
		}
		else if(constantLanguage.equals("euskera") | constantLanguage.equals("Basque")) {
			return Constants.BASQUE;
		}
		else {
			return Constants.ENGLISH;
		}
	}

}
