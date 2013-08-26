package com.mapplas.app.application;

import java.util.Locale;

import android.app.Application;
import android.graphics.Typeface;

import com.mapplas.model.Constants;

public class MapplasApplication extends Application {

	private boolean firstLaunch = true;

	private Typeface typeface = null;

	private Typeface typefaceBold = null;

	private Typeface typefaceItalic = null;

	private String language = null;

	// First launch
	public boolean getIsFirstLaunch() {
		return this.firstLaunch;
	}

	public void setFirstLaunchFalse() {
		this.firstLaunch = false;
	}

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
		if(Locale.getDefault().getDisplayLanguage().equals("espa–ol")) {
			this.language = Constants.SPANISH;
		}
		else {
			this.language = Constants.ENGLISH;
		}
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String lang) {
		this.language = lang;
	}

}
