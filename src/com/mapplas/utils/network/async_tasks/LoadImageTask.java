package com.mapplas.utils.network.async_tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.mapplas.utils.cache.CacheFolderFactory;
import com.mapplas.utils.cache.ImageFileManager;
import com.mapplas.utils.image.SynchronousImageLoader;

public class LoadImageTask implements AsyncTaskHandler {

	private Context context;

	private String path;

	private ImageView imageView;

	private ImageFileManager imageFileManager;

	public LoadImageTask(Context context, String path, ImageView imageView, ImageFileManager imageFileManager) {
		this.context = context;
		this.path = path;
		this.imageView = imageView;
		this.imageFileManager = imageFileManager;
	}

	public void execute() {
		SynchronousImageLoader synchronousImageLoader = new SynchronousImageLoader();
		Bitmap result = synchronousImageLoader.loadImage(new CacheFolderFactory(this.context).create(), this.path);
		if(result == null) {
			this.path = "";
		}
	}

	public void finished() {
		if(!this.path.equals("")) {
			Bitmap bitmap = this.imageFileManager.load(new CacheFolderFactory(this.context).create(), this.path);
			if(bitmap != null) {
				this.imageView.setImageBitmap(bitmap);
			}
		}
	}
}
