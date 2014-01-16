package com.mapplas.utils.gallery;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import app.mapplas.com.R;

import com.mapplas.utils.image.DrawableBackgroundDownloader;

public class GalleryItemFragment extends Fragment {

	/**
	 * Key to insert the index page into the mapping of a Bundle.
	 */
	private static final String IMAGE = "image";

	private String image;
	
	private final DrawableBackgroundDownloader mdbd = new DrawableBackgroundDownloader();

	/**
	 * Instances a new fragment with a background color and an index page.
	 * 
	 * @param color
	 *            background color
	 * @param image
	 *            index page
	 * @return a new page
	 */
	public static GalleryItemFragment newInstance(String image) {

		// Instantiate a new fragment
		GalleryItemFragment fragment = new GalleryItemFragment();

		// Save the parameters
		Bundle bundle = new Bundle();
		bundle.putString(IMAGE, image);
		fragment.setArguments(bundle);
		fragment.setRetainInstance(true);

		return fragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Load parameters when the initial creation of the fragment is done
		this.image = (getArguments() != null) ? getArguments().getString(IMAGE) : "";

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.image_gallery_item, container, false);

		// Show the current page index in the view
		ImageView imageView = (ImageView)rootView.findViewById(R.id.image);
		ProgressBar progressBar = (ProgressBar)rootView.findViewById(R.id.progess_bar);
		
		this.mdbd.loadDrawable(this.image, imageView, true, 480, true, progressBar);

		// Change the background color
		rootView.setBackgroundColor(Color.BLACK);

		return rootView;
	}

}
