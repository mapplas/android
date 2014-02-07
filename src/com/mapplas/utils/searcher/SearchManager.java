package com.mapplas.utils.searcher;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import app.mapplas.com.R;

public class SearchManager {

	private AutoCompleteTextView autoCompleteTextView;

	private Context context;

	public SearchManager(AutoCompleteTextView textView, Context context) {
		this.autoCompleteTextView = textView;
		this.context = context;
	}

	public void initializeSearcher() {
		
		final String[] deal = {"New York", "New Orleans", "Orlando"};
		
		final TextView tv;
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_dropdown_item_1line, deal);
		this.autoCompleteTextView.setAdapter(adapter);
		this.autoCompleteTextView.setThreshold(1);
		this.autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				tv = (TextView)findViewById(R.id.selecteditem_tv);
//				tv.setText(deal[arg2]);
				arg0.getItemAtPosition(arg2);
				Log.i("SELECTED TEXT WAS------->", deal[arg2]);
			}
		});
	}

}
