package com.mapplas.utils.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileManagement {

	public boolean createFile(File file) {
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				return file.createNewFile();
			} catch (IOException e) {
				return false;
			}
		}
		else {
			return true;
		}
	}
	
	public boolean delete(String path) {
		File file = new File(path);
		return file.delete();
	}

	public boolean deleteRecursive(File path) throws FileNotFoundException {
		if(!path.exists()) {
			throw new FileNotFoundException(path.getAbsolutePath());
		}
		else {
			boolean cumulativeReturn = true;
			if(path.isDirectory()) {
				for(File toDelete : path.listFiles()) {
					cumulativeReturn = cumulativeReturn && deleteRecursive(toDelete);
				}
			}
			return cumulativeReturn && path.delete();
		}
	}

	
	public boolean exists(File file) {
		return file.exists();
	}

}
