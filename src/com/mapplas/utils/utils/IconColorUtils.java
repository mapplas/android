package com.mapplas.utils.utils;

import com.mapplas.model.App;

public class IconColorUtils {

	public void parseColors(App app) {
		String icon_colors = app.getAppLogo();
		icon_colors = icon_colors.replace("[", "");
		icon_colors = icon_colors.replace("]", "");

		String[] colors = icon_colors.split(",");

		app.setIcon_color_1(colors[0].replace("\"", ""));
		app.setIcon_color_2(colors[1].replace("\"", ""));
		app.setIcon_color_3(colors[2].replace("\"", ""));
		app.setIcon_color_4(colors[3].replace("\"", ""));
		
		if((app.getIcon_color_1().equals("") && app.getIcon_color_2().equals("") && app.getIcon_color_3().equals("") && app.getIcon_color_4().equals("")) || (app.getIcon_color_1().equals("null") && app.getIcon_color_2().equals("null") && app.getIcon_color_3().equals("null") && app.getIcon_color_4().equals("null"))) {
			app.setIcon_color_1("#000000");
			app.setIcon_color_2("#888888");
			app.setIcon_color_3("#ffffff");
		}
	}
}
