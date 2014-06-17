package com.mapplas.app.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import app.mapplas.com.R;

import com.mapplas.model.Constants;
import com.mapplas.utils.gallery.GalleryFragmentPageAdapter;
import com.mapplas.utils.gallery.GalleryItemFragment;
import com.viewpagerindicator.CirclePageIndicator;
public class GalleryActivity extends FragmentActivity {

	private ArrayList<String> images;
	
	private int clickedImageIndex;

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next pages.
	 */
	ViewPager pager = null;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	GalleryFragmentPageAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.image_gallery);

		this.extractDataFromBundle();

		// Instantiate a ViewPager
		this.pager = (ViewPager)this.findViewById(R.id.pager);

		// Create an adapter with the fragments we show on the ViewPager
		GalleryFragmentPageAdapter adapter = new GalleryFragmentPageAdapter(getSupportFragmentManager());

		for(String image : this.images) {
			String[] imageSplit = image.split("=h");
			String newImageString = imageSplit[0] + "=h" + "620";
			adapter.addFragment(GalleryItemFragment.newInstance(newImageString));
		}
		
		this.pager.setAdapter(adapter);
		this.pager.setCurrentItem(this.clickedImageIndex);

		// Bind the title indicator to the adapter
		CirclePageIndicator titleIndicator = (CirclePageIndicator)findViewById(R.id.page_indicator);
		titleIndicator.setViewPager(this.pager);
		titleIndicator.setCurrentItem(this.clickedImageIndex);
	}

	private void extractDataFromBundle() {
		// Get the id of the app selected
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			if(extras.containsKey(Constants.APP_IMAGES_GALLERY) && extras.containsKey(Constants.APP_IMAGES_GALLERY_INDEX)) {
				this.images = (ArrayList<String>)extras.getStringArrayList(Constants.APP_IMAGES_GALLERY);
				this.clickedImageIndex = (int)extras.getInt(Constants.APP_IMAGES_GALLERY_INDEX);
			}
		}
	}

}
