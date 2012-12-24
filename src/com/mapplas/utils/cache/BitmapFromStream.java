package com.mapplas.utils.cache;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapFromStream {

	public Bitmap create(InputStream stream) {
		return BitmapFactory.decodeStream(stream);
	}
}
