package com.mapplas.utils.searcher;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.model.database.MySQLiteHelper;

public class SearchManager {

	private AutoCompleteTextView autoCompleteTextView;

	private Context context;
	
	private MapplasActivity activity;

	public SearchManager(AutoCompleteTextView textView, Context context, MapplasActivity activity) {
		this.autoCompleteTextView = textView;
		this.context = context;
		this.activity = activity;
	}

	public void initializeSearcher() {

		final MySQLiteHelper db = new MySQLiteHelper(this.context);
		final String[] searchValues = db.getSearchValues();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_dropdown_item_1line, searchValues);
		this.autoCompleteTextView.setAdapter(adapter);
		this.autoCompleteTextView.setThreshold(1);
		this.autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int id = db.getIdFromName(arg0.getItemAtPosition(arg2).toString());
				activity.requestAppsForEntity(id);
			}
		});
	}

}
