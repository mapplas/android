package com.mapplas.app.adapters.more_apps;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.MoreFromDeveloperApp;
import com.mapplas.utils.cache.CacheFolderFactory;
import com.mapplas.utils.cache.ImageFileManager;
import com.mapplas.utils.network.async_tasks.LoadImageTask;
import com.mapplas.utils.network.async_tasks.TaskAsyncExecuter;

public class MoreFromDeveloperArrayAdapter extends ArrayAdapter<MoreFromDeveloperApp> {

	private ArrayList<MoreFromDeveloperApp> items;

	private Context context;

	public MoreFromDeveloperArrayAdapter(Context context, int textViewResourceId, ArrayList<MoreFromDeveloperApp> objects) {
		super(context, textViewResourceId, objects);

		this.items = objects;
		this.context = context;
	}

	@Override
	public int getCount() {
		return this.items.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(view == null) {
			view = inflater.inflate(R.layout.row_more_apps, null);
			this.initLayout(view, position);
		}
		else {
			this.initLayout(view, position);
		}

		return view;
	}

	private void initLayout(View view, int position) {
		Typeface normalTypeface = ((MapplasApplication)this.context.getApplicationContext()).getTypeFace();

		TextView title = (TextView)view.findViewById(R.id.title);
		title.setText(this.items.get(position).name());
		title.setTypeface(normalTypeface);

		TextView description = (TextView)view.findViewById(R.id.description);
		description.setText(this.items.get(position).shortDescription());
		description.setTypeface(normalTypeface);

		// Load app logo
		ImageView logo_iv = (ImageView)view.findViewById(R.id.logo);
		ImageFileManager imageFileManager = new ImageFileManager();

		String logoUrl = this.items.get(position).logo();
		if(!logoUrl.equals("")) {
			if(imageFileManager.exists(new CacheFolderFactory(this.context).create(), logoUrl)) {
				logo_iv.setImageBitmap(imageFileManager.load(new CacheFolderFactory(this.context).create(), logoUrl));
			}
			else {
				TaskAsyncExecuter imageRequest = new TaskAsyncExecuter(new LoadImageTask(this.context, logoUrl, logo_iv, imageFileManager));
				imageRequest.execute();
			}
		}
	}

}
