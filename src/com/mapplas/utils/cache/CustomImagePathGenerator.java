package com.mapplas.utils.cache;


public class CustomImagePathGenerator {

	private String variable = null;
	
	public CustomImagePathGenerator(String variable) {
		this.variable = variable;
	}
	
	public String getFront() {
		return this.getRelativePath("front");
	}
	
	public String getBack() {
		return this.getRelativePath("back");
	}
	
	private String getRelativePath(String name) {
		String subFolder;
		if (this.variable == null || this.variable.equals("")) {
			subFolder = "default";
		}
		else {
			subFolder = this.variable;
		}
		return this.getRelativePathWithSubFolder(subFolder, name);
	}
	
	private String getRelativePathWithSubFolder(String subfolder, String name) {
		return "custom-photos/" + subfolder + "/" + name + ".jpg";
	}

}
