package com.mapplas.utils.searcher;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import app.mapplas.com.R;

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.app.adapters.search.SearchCityAdapter;
import com.mapplas.model.Constants;
import com.mapplas.model.database.MySQLiteHelper;
import com.mapplas.utils.visual.custom_views.RobotoTextView;
import com.mapplas.utils.visual.custom_views.autocomplete.CustomAutoCompleteTextChangedListener;
import com.mapplas.utils.visual.custom_views.autocomplete.CustomAutoCompleteView;

public class SearchManager {

	public static int APP_REQUEST_TYPE_BEING_DONE = -1;

	public static int APP_REQUEST_ENTITY_BEING_DONE = -1;

	public CustomAutoCompleteView autoCompleteTextView;

	private RelativeLayout searchLayout;

	private Context context;

	private MapplasActivity activity;

	private ListView listView;
		
	public SearchCityAdapter myAdapter;
	
	public MySQLiteHelper db;

	public SearchManager(CustomAutoCompleteView textView, RelativeLayout searchLayout, Context context, MapplasActivity activity, ListView listView) {
		this.autoCompleteTextView = textView;
		this.searchLayout = searchLayout;
		this.context = context;
		this.activity = activity;
		this.listView = listView;
	}

	public void initializeSearcher() {

		this.db = new MySQLiteHelper(this.context);
		final String[] searchValues = db.getSearchValues();
		
		this.myAdapter = new SearchCityAdapter(this.context, R.layout.prueba, searchValues);
		this.autoCompleteTextView.setAdapter(this.myAdapter);
		
		this.autoCompleteTextView.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this, this.context));
		this.autoCompleteTextView.setDropDownBackgroundResource(R.color.drop_down_background);
		
		this.autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int id = db.getIdFromName(arg0.getItemAtPosition(0).toString());
				activity.requestAppsForEntity(id, autoCompleteTextView.getText().toString());

				SearchManager.APP_REQUEST_TYPE_BEING_DONE = Constants.APP_REQUEST_TYPE_ENTITY_ID;
				SearchManager.APP_REQUEST_ENTITY_BEING_DONE = id;

				listView.setSelection(0);
				searchLayout.setVisibility(View.GONE);
				autoCompleteTextView.setText("");
				InputMethodManager in = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(autoCompleteTextView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
	}

}
