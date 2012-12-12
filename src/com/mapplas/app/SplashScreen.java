package com.mapplas.app;

import android.app.Activity;
import android.os.Bundle;
import app.mapplas.com.R;

public class SplashScreen extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.launch);
	}
}