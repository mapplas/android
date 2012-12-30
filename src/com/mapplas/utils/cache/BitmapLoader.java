package com.mapplas.utils.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;


public class BitmapLoader {

	BitmapFromStream bitmapFromStream;
	
	public BitmapLoader(BitmapFromStream bitmapFromStream) {
		this.bitmapFromStream = bitmapFromStream;
	}
	
	public Bitmap load(CacheFolder cacheFolder, String path) {
		File imageFile = cacheFolder.getFileWithPath(path);
		try {
			return bitmapFromStream.create(new FileInputStream(imageFile));
		} catch (FileNotFoundException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}
}
