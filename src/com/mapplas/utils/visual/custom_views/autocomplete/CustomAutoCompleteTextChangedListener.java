package com.mapplas.utils.visual.custom_views.autocomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import app.mapplas.com.R;

import com.mapplas.app.adapters.search.SearchCityAdapter;
import com.mapplas.utils.searcher.SearchManager;

public class CustomAutoCompleteTextChangedListener implements TextWatcher {

	private SearchManager searchManager;

	private Context context;
	
	private int charactersBefore = 0;
	
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
		
		int charactersNow = userInput.length();
		
		if(this.charactersBefore == 0 && charactersNow == 1 || this.charactersBefore == 2 && charactersNow == 1) {
			// SHow Nothing
			String[] searchValues = {};
			Arrays.fill(searchValues, "");
			
			searchManager.myAdapter.notifyDataSetChanged();
			searchManager.myAdapter = new SearchCityAdapter(context, R.layout.prueba, searchValues, null);
			searchManager.autoCompleteTextView.setAdapter(searchManager.myAdapter);
		}
		else {
			try {

				HashMap<Integer, ArrayList<List>> dict = searchManager.db.read(userInput.toString());

				String[] myObjs = new String[dict.size()];
				int j = 0;
				for (Map.Entry<Integer, ArrayList<List>> entry : dict.entrySet() ) {
				    ArrayList<List> value = entry.getValue();
					ArrayList<Integer> entity_id = (ArrayList<Integer>)value.get(1);
					int id = entity_id.get(0);
					myObjs[j] = String.valueOf(id);
				    j++;
				}
				
				searchManager.myAdapter.notifyDataSetChanged();
				searchManager.myAdapter = new SearchCityAdapter(context, R.layout.prueba, myObjs, dict);
				//Avoids crash
				String sdfa = searchManager.myAdapter.getItem(0);
				searchManager.autoCompleteTextView.setAdapter(searchManager.myAdapter);

			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.charactersBefore = userInput.length();
	}

}