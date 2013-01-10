package com.mapplas.app.adapters;

import com.mapplas.app.activities.PreferencesActivity;
import com.mapplas.app.activities.TextActivity;
import com.mapplas.model.Constants;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import app.mapplas.com.R;

public class PreferencesAdapter extends BaseAdapter {

	private Context context;

	private LayoutInflater inflater;
	
	private Typeface normalTypeface;

	public PreferencesAdapter(Context context, Typeface normalTypeface) {
		this.context = context;
		this.normalTypeface = normalTypeface;

		this.inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return 2;
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
				text.setText("Terms of use");
				break;

			case 1:
				text.setText("Privacy policy");
				break;
		}

		return view;
	}
	
	private OnClickListener createClickListenerForListElements(final int position) {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent((PreferencesActivity)context, TextActivity.class);
				
				String title = "";
				String message = "";
				if(position == 0) {
					title = context.getString(R.string.terms_of_use_title);
					message = context.getString(R.string.terms_of_use_message);
				}
				else {
					title = context.getString(R.string.privacy_policy_title);
					message = context.getString(R.string.privacy_policy_message);
				}
					
				intent.putExtra(Constants.MAPPLAS_TEXT_ACTIVITY_EXTRA_TITLE, title);
				intent.putExtra(Constants.MAPPLAS_TEXT_ACTIVITY_EXTRA_MESSAGE, message);
				context.startActivity(intent);
			}
		};
	}

}
