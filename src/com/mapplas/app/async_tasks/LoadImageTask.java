package com.mapplas.app.async_tasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.mapplas.model.Constants;
import com.mapplas.utils.cache.CacheFolderFactory;
import com.mapplas.utils.image.SynchronousImageLoader;

public class LoadImageTask implements AsyncTaskHandler {

	private Context context;

	private String path;

	private String action_response;
	
	private int position;

	public LoadImageTask(Context context, String path, String action_response, int position) {
		this.context = context;
		this.path = path;
		this.action_response = action_response;
		this.position = position;
	}

	public void execute() {
		SynchronousImageLoader synchronousImageLoader = new SynchronousImageLoader();
		Bitmap result = synchronousImageLoader.loadImage(new CacheFolderFactory(this.context).create(), this.path);
		if(result == null) {
			this.path = "";
		}
	}

	public void finished() {
		Intent i = new Intent(this.action_response);
		if(this.path.length() != 0) {
			i.putExtra(Constants.MAPPLAS_EXTRA_BITMAP, path);
			i.putExtra(Constants.MAPPLAS_EXTRA_BITMAP_POSITION, this.position);
		}
		this.context.sendBroadcast(i);
	}
}
