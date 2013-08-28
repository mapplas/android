package com.mapplas.app.application;

import java.util.Locale;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.mapplas.model.Constants;

public class MapplasApplication extends Application {

	private Typeface typeface = null;

	private Typeface typefaceBold = null;

	private Typeface typefaceItalic = null;

	// Typefaces
	public void loadTypefaces() {
		this.typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		this.typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
		this.typefaceItalic = Typeface.createFromAsset(getAssets(), "fonts/Roboto-ThinItalic.ttf");
	}

	public Typeface getTypeFace() {
		return this.typeface;
	}

	public Typeface getBoldTypeFace() {
		return this.typefaceBold;
	}

	public Typeface getItalicTypeFace() {
		return this.typefaceItalic;
	}

	// Language
	public void setDefaultLanguage() {
		SharedPreferences sharedPrefs = getSharedPreferences("MAPPLAS_PREF", MODE_PRIVATE);
		if(Locale.getDefault().getDisplayLanguage().equals("espa–ol")) {
			sharedPrefs.edit().putString("language", Constants.SPANISH).commit();
		}
		else {
			sharedPrefs.edit().putString("language", Constants.ENGLISH).commit();
		}
	}

	public String getLanguage() {
		SharedPreferences sharedPrefs = getSharedPreferences("MAPPLAS_PREF", MODE_PRIVATE);
		return sharedPrefs.getString("language", "");
	}

	public void setLanguage(String lang) {
		SharedPreferences sharedPrefs = getSharedPreferences("MAPPLAS_PREF", MODE_PRIVATE);
		sharedPrefs.edit().putString("language", lang).commit();
	}

}
