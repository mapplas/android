package com.mapplas.utils.image;

import com.mapplas.utils.cache.CacheFolder;
import com.mapplas.utils.cache.ImageFileManager;

public class CustomImageManager {

	private ImageFileManager fileManager;

	private CacheFolder cacheFolder;

	public CustomImageManager(ImageFileManager imageFileManager, CacheFolder cacheFolder) {
		this.fileManager = imageFileManager;
		this.cacheFolder = cacheFolder;
	}

	public boolean isImageInCache(String path) {
		if(path.equals("")) {
			return false;
		}

		return fileManager.exists(this.cacheFolder, path);
	}
}
