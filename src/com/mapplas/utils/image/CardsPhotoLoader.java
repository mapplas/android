package com.mapplas.utils.image;

import android.content.Context;

import com.mapplas.app.async_tasks.LoadImageTask;
import com.mapplas.app.async_tasks.TaskAsyncExecuter;

public class CardsPhotoLoader {

	private CustomImageManager customImageManager;

	public CardsPhotoLoader(CustomImageManager customImageManager) {
		this.customImageManager = customImageManager;
	}

	public String loadPhoto(String path, Context context, String action, int position) {
		if(!path.equals("")) {
			if(this.customImageManager.isImageInCache(path)) {
				return path;
			}
			else {
				this.doServerRequest(path, context, action, position);
			}
		}
		return "";
	}

	private void doServerRequest(String path, Context context, String broadcastAction, int position) {
		TaskAsyncExecuter cardRequest = new TaskAsyncExecuter(new LoadImageTask(context, path, broadcastAction, position));
		cardRequest.execute();
	}

}
