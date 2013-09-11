package com.mapplas.utils.image;

import android.graphics.Bitmap;

public class ImageRepository {

	private static final int CAPACITY = 32;
	private static int startIndex = 0;
	private static int endIndex = 0;
	private static Bitmap[] arrayImages = new Bitmap[CAPACITY];
	private static String[] arrayUrls = new String[CAPACITY];
	
	
	public static Bitmap Find(String url)
	{
		Bitmap ret = null;
		
		for(int i = startIndex; i < endIndex; i++)
		{
			if(url == arrayUrls[i])
			{
				ret = arrayImages[i];
			}
		}
		
		return ret;
	}
	
	public static void Store(String url, Bitmap bmp)
	{
		
		
		if(ImageRepository.Find(url) == null)
		{
			endIndex = (endIndex + 1) % CAPACITY;
			arrayImages[endIndex] = bmp;
			arrayUrls[endIndex] = url;
		}
		
	}
	
	
	
}
