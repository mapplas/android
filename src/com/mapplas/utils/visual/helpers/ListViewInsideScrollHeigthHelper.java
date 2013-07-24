package com.mapplas.utils.visual.helpers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mapplas.model.MoreFromDeveloperApp;

public class ListViewInsideScrollHeigthHelper {

	public void setListViewHeightBasedOnChildren(ListView listView, ArrayAdapter<MoreFromDeveloperApp> adapter) {
		if(adapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for(int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
