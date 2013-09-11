package com.mapplas.utils.cache;

import java.io.File;

public class CacheFolder {

	private String cacheDir = null;

	public CacheFolder(String cacheDir) {
		this.cacheDir = cacheDir;
	}

	public File getFileWithPath(String path) {
		return new File(cacheDir, path);
	}
	
	public String getFileWithPathString(String path) {
		return this.getFileWithPath(path).getPath();
	}

}
