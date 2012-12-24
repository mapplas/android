package com.mapplas.utils.image;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.mapplas.utils.cache.CacheFolder;
import com.mapplas.utils.cache.ImageFileManager;

public class SynchronousImageLoader {

	public Bitmap loadImage(CacheFolder cacheFolder, String url) {
		Bitmap bitmap = null;
		try {
			InputStream is = getInputStream(url);

			Drawable drawable = Drawable.createFromStream(is, url);
			bitmap = ((BitmapDrawable)drawable).getBitmap();
//			bitmap = BitmapFactory.decodeStream(is);
			new ImageFileManager().save(bitmap, cacheFolder, url);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	private InputStream getInputStream(String urlString) throws MalformedURLException, IOException {
		URL url = new URL(urlString);
		URLConnection connection;
		connection = url.openConnection();
		connection.setUseCaches(true);
		connection.connect();
		InputStream response = connection.getInputStream();

		return response;
	}
}
