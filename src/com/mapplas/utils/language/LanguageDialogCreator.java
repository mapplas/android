package com.mapplas.utils.language;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import app.mapplas.com.R;

import com.mapplas.utils.visual.dialogs.LanguageDialogInterface;


public class LanguageDialogCreator {
	
	private Context context;
	
	private LanguageDialogInterface languageInterface;
	
	public LanguageDialogCreator(Context context, LanguageDialogInterface languageInterface) {
		this.context = context;
		this.languageInterface = languageInterface;
	}
	
	public void createLanguageListDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
		builder.setTitle(R.string.language_change_alert_title);

		builder.setItems(R.array.languages_array, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				languageInterface = (LanguageDialogInterface)context;

				switch (which) {
					case 0:
						languageInterface.onDialogEnglishLanguageClick();
						break;

					case 1:
						languageInterface.onDialogSpanishLanguageClick();
						break;

					case 2:
						languageInterface.onDialogBasqueLanguageClick();
						break;
					default:
						break;
				}
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
