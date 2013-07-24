package com.mapplas.app.adapters.detail;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.model.MoreFromDeveloperApp;

public class MoreAppsArrayAdapter extends ArrayAdapter<MoreFromDeveloperApp> {

	private ArrayList<MoreFromDeveloperApp> items;
	
	private Context context;

	public MoreAppsArrayAdapter(Context context, int textViewResourceId, ArrayList<MoreFromDeveloperApp> objects) {
		super(context, textViewResourceId, objects);
		
		this.items = objects;
		this.context = context;
	}

	@Override
	public int getCount() {
		return this.items.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(view == null) {
			view = inflater.inflate(R.layout.row_more_apps, null);
			this.initLayout(view, position);
		} else {
			this.initLayout(view, position);
		}
	
		return view;
	}

	private void initLayout(View view, int position) {
		TextView title = (TextView)view.findViewById(R.id.title);
		title.setText(this.items.get(position).name());
	}

}
