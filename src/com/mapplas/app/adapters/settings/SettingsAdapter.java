package com.mapplas.app.adapters.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.activities.HtmlTextActivity;
import com.mapplas.app.activities.SettingsActivity;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.Constants;
import com.mapplas.utils.visual.dialogs.LanguageDialogInterface;

public class SettingsAdapter extends BaseAdapter {

	private Context context;

	private LayoutInflater inflater;

	private Typeface normalTypeface;

	private LanguageDialogInterface languageInterface;

	public SettingsAdapter(Context context, Typeface normalTypeface) {
		this.context = context;
		this.normalTypeface = normalTypeface;

		this.inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return 4;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public String getItem(int position) {
		return "";
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;

		view = this.inflater.inflate(R.layout.preferences_cell_layout, null);
		TextView text = (TextView)view.findViewById(android.R.id.text1);
		text.setTypeface(this.normalTypeface);
		text.setTextSize(16);

		view.setOnClickListener(this.createClickListenerForListElements(position));

		switch (position) {
			case 0:
				String constantLanguage = ((MapplasApplication)this.context.getApplicationContext()).getLanguage();
				
				String language = this.context.getString(R.string.language_english);
				if(constantLanguage.equals(Constants.SPANISH)) {
					language = this.context.getString(R.string.language_spanish);
				}
				else if(constantLanguage.equals(Constants.BASQUE)) {
					language = this.context.getString(R.string.language_basque);
				}
				
				text.setText(this.context.getString(R.string.language_change_list_item) + " " + language);
				break;
			case 1:
				text.setText(this.context.getString(R.string.terms_of_use_title));
				break;

			case 2:
				text.setText(this.context.getString(R.string.privacy_policy_title));
				break;
			case 3:
				text.setText(this.context.getString(R.string.conf_screen_contact_title));
				break;

			default:
				break;
		}
		return view;
	}

	private OnClickListener createClickListenerForListElements(final int position) {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent((SettingsActivity)context, HtmlTextActivity.class);

				String message = "";

				switch (position) {
					case 1:
						message = context.getString(R.string.terms_of_use_message);
						break;

					case 2:
						message = context.getString(R.string.privacy_policy_message);
						break;

					default:
						break;
				}

				switch (position) {
					case 0:
						createLanguageListDialog();
						break;

					case 1:
						intent.putExtra(Constants.MAPPLAS_TEXT_ACTIVITY_EXTRA_MESSAGE, message);
						context.startActivity(intent);
						break;

					case 2:
						intent.putExtra(Constants.MAPPLAS_TEXT_ACTIVITY_EXTRA_MESSAGE, message);
						context.startActivity(intent);
						break;

					case 3:
						Intent emailIntent = new Intent(Intent.ACTION_SEND);
						emailIntent.setType("text/html");
						emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { context.getString(R.string.config_contact_us_email) });
						emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.config_contact_us_email_subject));
						context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
						break;

					default:
						break;
				}
			}
		};
	}

	private void createLanguageListDialog() {
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
