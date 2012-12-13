package com.mapplas.app.application;

import android.app.Application;
import android.graphics.Typeface;


public class MapplasApplication extends Application {
	
	private Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

	private Typeface typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

	private Typeface typefaceItalic = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Italic.ttf");

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
