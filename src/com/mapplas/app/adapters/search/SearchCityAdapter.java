package com.mapplas.app.adapters.search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import app.mapplas.com.R;

import com.mapplas.utils.visual.custom_views.RobotoTextView;

public class SearchCityAdapter extends ArrayAdapter<String> {

	public SearchCityAdapter(Context context, int textViewResourceId, String[] objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final String cityName = getItem(position);

		ViewHolder holder;

		if(convertView == null) {
			convertView = View.inflate(getContext(), R.layout.prueba, null);
			holder = new ViewHolder();
			holder.textName = (RobotoTextView)convertView.findViewById(R.id.autoCompleteItem);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}

		holder.textName.setText(cityName);

		return convertView;
	}

	static class ViewHolder {
		RobotoTextView textName;
	}

}
