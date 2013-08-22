package com.mapplas.utils.utils;

public class DateUtils {

	public static int Hours(String hour) {
		int ret = 0;

		try {
			ret = Integer.parseInt(hour.substring(0, hour.indexOf(':') - 1));
		} catch (Exception exc) {

		}

		return ret;
	}

	public static int Minutes(String hour) {
		int ret = 0;

		try {
			ret = Integer.parseInt(hour.substring(hour.indexOf(':') + 1));
		} catch (Exception exc) {

		}

		return ret;
	}

}
