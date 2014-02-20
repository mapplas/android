package com.mapplas.utils.visual.custom_views.autocomplete;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import app.mapplas.com.R;

import com.mapplas.app.adapters.search.SearchCityAdapter;
import com.mapplas.utils.searcher.SearchManager;

public class CustomAutoCompleteTextChangedListener implements TextWatcher {

	private SearchManager searchManager;

	private Context context;

	public CustomAutoCompleteTextChangedListener(SearchManager searchManager, Context context) {
		this.searchManager = searchManager;
		this.context = context;
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence userInput, int start, int before, int count) {

		try {
			searchManager.myAdapter.notifyDataSetChanged();

			String[] myObjs = searchManager.db.read(userInput.toString());

			searchManager.myAdapter = new SearchCityAdapter(context, R.layout.prueba, myObjs);
			searchManager.autoCompleteTextView.setAdapter(searchManager.myAdapter);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
