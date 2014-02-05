package com.mapplas.utils.language;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import app.mapplas.com.R;

import com.mapplas.utils.network.async_tasks.AppGetterTask;
import com.mapplas.utils.visual.custom_views.RobotoTextView;
import com.mapplas.utils.visual.dialogs.LanguageDialogInterface;

public class LanguageDialogCreator {

	private Context context;

	private LanguageDialogInterface languageInterface;

	private AppGetterTask theClass;

	public LanguageDialogCreator(Context context, LanguageDialogInterface languageInterface, AppGetterTask theClass) {
		this.context = context;
		this.languageInterface = languageInterface;
		this.theClass = theClass;
	}

	public void createLanguageListDialog() {
		final Dialog dialog = new Dialog(this.context, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.dialog_listview);

		RobotoTextView title = (RobotoTextView)dialog.findViewById(R.id.dialog_title);
		title.setText(R.string.language_change_alert_title);

		// Prepare ListView in dialog
		ListView list = (ListView)dialog.findViewById(R.id.dialog_list);
		String[] listContent = this.context.getResources().getStringArray(R.array.languages_array);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.dialog_listview_item, listContent);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(languageInterface instanceof Activity) {
					languageInterface = (LanguageDialogInterface)context;
				}
				else {
					languageInterface = (LanguageDialogInterface)theClass;
				}

				switch (position) {
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
				dialog.dismiss();
			}
		});

		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

}
