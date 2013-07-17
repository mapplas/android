package com.mapplas.utils.utils;

public class NumberUtils {

	public static String FormatNumber(int i) {
		return String.valueOf(i);
	}

	public static String FormatNumber(float f) {
		float ret = f;

		if(f % 1 > 0.75) {
			ret = (float)(Math.floor(f) + 1);
		}
		else if(f % 1 < 0.25) {
			ret = (float)Math.floor(f);
		}
		else {
			ret = (float)(Math.floor(f) + 0.5f);
		}

		return String.valueOf(ret);
	}

}
