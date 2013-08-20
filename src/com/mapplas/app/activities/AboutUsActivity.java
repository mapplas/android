package com.mapplas.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.about_us.AboutUsAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.utils.visual.dialogs.LanguageDialogInterface;

public class AboutUsActivity extends Activity implements LanguageDialogInterface {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);

		this.initializeLayoutComponents();
	}

	private void initializeLayoutComponents() {
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();

		Button backButton = (Button)this.findViewById(R.id.preferences_back_button);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ListView list = (ListView)this.findViewById(R.id.preferences_list);
		list.setAdapter(new AboutUsAdapter(this, normalTypeFace, language));
	}

	// Language dialog interface methods
	
	@Override
	public void onDialogEnglishLanguageClick() {
		Log.d("", "ENGLISH");
	}

	@Override
	public void onDialogSpanishLanguageClick() {
		Log.d("", "SPANISH");
	}

	@Override
	public void onDialogBasqueLanguageClick() {
		Log.d("", "BASQUE");
	}

}
