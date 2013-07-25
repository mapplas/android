package com.mapplas.app.application;

import android.app.Application;
import android.graphics.Typeface;

public class MapplasApplication extends Application {

	private Typeface typeface = null;

	private Typeface typefaceBold = null;

	private Typeface typefaceItalic = null;

	public void loadTypefaces() {
		this.typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		this.typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		this.typefaceItalic = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Italic.ttf");
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

}
