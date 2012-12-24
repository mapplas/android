package com.mapplas.utils.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;

public class BitmapSaver {

	private FileManagement fileManagement;

	public BitmapSaver(FileManagement fileManagement) {
		this.fileManagement = fileManagement;
	}

	public boolean save(Bitmap bitmap, CacheFolder cacheFolder, String path) {
		boolean success;
		File imageFile = cacheFolder.getFileWithPath(path);
		if(success = fileManagement.createFile(imageFile)) {
			try {
				if(bitmap.hasAlpha()) {
					bitmap.compress(Bitmap.CompressFormat.PNG, 80, new FileOutputStream(imageFile));
				} else {
					bitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(imageFile));
				}
			} catch (FileNotFoundException e) {
				success = false;
			} catch (NullPointerException e) {
				success = false;
			}
		}
		return success;
	}

}
