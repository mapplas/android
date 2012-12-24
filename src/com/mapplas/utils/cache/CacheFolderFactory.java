package com.mapplas.utils.cache;

import android.content.Context;

public class CacheFolderFactory {

	String cachePath;

	public CacheFolderFactory(Context context) {
		super();
		this.cachePath = context.getCacheDir().getPath();
	}

	public CacheFolder create() {
		return new CacheFolder(this.cachePath);
	}
}
