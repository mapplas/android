package com.mapplas.utils.searcher;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.model.Constants;
import com.mapplas.model.database.MySQLiteHelper;

public class SearchManager {

	public static int APP_REQUEST_TYPE_BEING_DONE = -1;

	public static int APP_REQUEST_ENTITY_BEING_DONE = -1;

	private AutoCompleteTextView autoCompleteTextView;

	private RelativeLayout searchLayout;

	private Context context;

	private MapplasActivity activity;

	public SearchManager(AutoCompleteTextView textView, RelativeLayout searchLayout, Context context, MapplasActivity activity) {
		this.autoCompleteTextView = textView;
		this.searchLayout = searchLayout;
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
				activity.requestAppsForEntity(id, autoCompleteTextView.getText().toString());

				SearchManager.APP_REQUEST_TYPE_BEING_DONE = Constants.APP_REQUEST_TYPE_ENTITY_ID;
				SearchManager.APP_REQUEST_ENTITY_BEING_DONE = id;

				searchLayout.setVisibility(View.GONE);
				autoCompleteTextView.setText("");
				InputMethodManager in = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(autoCompleteTextView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
	}

}
