package com.mapplas.utils.image;

import android.content.Context;

public class PixelDensityImageChooser {

	private Context context;

	public PixelDensityImageChooser(Context context) {
		this.context = context;
	}

	public String getImageUrlDependingOnDensity(String url) {
		int size = this.getImageSize();
		
		String[] splittedUrlLogo = url.split("=");
		String base = splittedUrlLogo[0];
		String newLogoUrl = base + "=w" + size;
		return newLogoUrl;
	}

	private int getImageSize() {
		// 72 - number of dp of the logo image
		// 4 - constant made in Alberto
		return (int) Math.ceil(72 * this.context.getResources().getDisplayMetrics().density) - 4;
	}

//	private String getMobilePixelDensity() {
//
//		float density = this.context.getResources().getDisplayMetrics().density;
//
//		if(density == 0.75) {
//			return Constants.LOW_PIXEL_DENSITY;
//		}
//		else if(density == 1) {
//			return Constants.MEDIUM_PIXEL_DENSITY;
//		}
//		else if(density == 1.5) {
//			return Constants.HIGH_PIXEL_DENSITY;
//		}
//		else {
//			return Constants.EXTRA_HIGH_PIXEL_DENSITY;
//		}
//	}

}
