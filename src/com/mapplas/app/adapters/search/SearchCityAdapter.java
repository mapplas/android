package com.mapplas.app.adapters.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import app.mapplas.com.R;

import com.mapplas.model.database.MySQLiteHelper;
import com.mapplas.utils.visual.custom_views.RobotoTextView;

public class SearchCityAdapter extends ArrayAdapter<String> {
	
	private HashMap<Integer, ArrayList<List>> dict;
	
	private MySQLiteHelper dbHelper;

	public SearchCityAdapter(Context context, int textViewResourceId, String[] objects, HashMap<Integer, ArrayList<List>> dict) {
		super(context, textViewResourceId, objects);
		
		this.dict = dict;
		this.dbHelper = new MySQLiteHelper(context);
	}
	
	public HashMap<Integer, ArrayList<List>> getDict() {
		return this.dict;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int entity_id = Integer.parseInt(getItem(position));

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
			ArrayList<String> entity_namecountry = this.dbHelper.getNameAndCountryFromId(entity_id);

			holder.textName.setText(entity_namecountry.get(0) + " (" + entity_namecountry.get(1) + ")");			
		}

		return convertView;
	}

	static class ViewHolder {
		RobotoTextView textName;
	}

}
