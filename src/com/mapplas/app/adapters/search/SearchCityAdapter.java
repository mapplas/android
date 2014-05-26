package com.mapplas.app.adapters.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import app.mapplas.com.R;

import com.mapplas.utils.visual.custom_views.RobotoTextView;

public class SearchCityAdapter extends ArrayAdapter<String> {
	
	private HashMap<Integer, ArrayList<List>> dict;

	public SearchCityAdapter(Context context, int textViewResourceId, String[] objects, HashMap<Integer, ArrayList<List>> dict) {
		super(context, textViewResourceId, objects);
		
		this.dict = dict;
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
		
		// Get country name from dictionary
		if(dict != null) {
			ArrayList<List> entity = dict.get(position);
			ArrayList<String> name_country = (ArrayList<String>)entity.get(0);

			holder.textName.setText(cityName + " (" + name_country.get(1) + ")");			
		}

		return convertView;
	}

	static class ViewHolder {
		RobotoTextView textName;
	}

}
