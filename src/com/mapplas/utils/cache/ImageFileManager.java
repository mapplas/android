package com.mapplas.utils.cache;

import android.graphics.Bitmap;

public class ImageFileManager {

	private static BitmapLoader bitmapLoader = null;

	private static BitmapSaver bitmapSaver = null;

	public boolean save(Bitmap bitmap, CacheFolder cacheFolder, String path) {
		//Log.d(this.getClass().getSimpleName(), "Saving bitmap to: " + path); 
		if(bitmapSaver == null) {
			bitmapSaver = new BitmapSaver(new FileManagement());
		}
		return bitmapSaver.save(bitmap, cacheFolder, path);
	}

	public Bitmap load(CacheFolder cacheFolder, String path) {
		if(bitmapLoader == null) {
			bitmapLoader = new BitmapLoader(new BitmapFromStream());
		}
		return bitmapLoader.load(cacheFolder, path);
	}
	
	public boolean move(CacheFolder cacheFolder, String fromPath, String toPath) {
		//Log.d(this.getClass().getSimpleName(), "Moving -- path of file to delete from " + fromPath + " to: " + toPath);
		Bitmap loadedBitmap = this.load(cacheFolder, fromPath);
		if (loadedBitmap == null) {
			return false;
		}
		else {
			if(this.save(loadedBitmap, cacheFolder, toPath)) {
				this.delete(cacheFolder, fromPath);
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean delete(CacheFolder cacheFolder, String path) {
		//Log.d("quomai", "Delete function -- path of file to delete: " + path);
		FileManagement fileManagement = new FileManagement();
		return fileManagement.delete(cacheFolder.getFileWithPathString(path));
	}
	
	public boolean exists(CacheFolder cacheFolder, String path) {
		FileManagement fileManagement = new FileManagement();
		return fileManagement.exists(cacheFolder.getFileWithPath(path));
	}

}
