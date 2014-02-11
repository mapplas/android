package com.mapplas.utils.visual.helpers;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mapplas.app.adapters.app.AppAdapter;
import com.mapplas.utils.third_party.RefreshableListView;
import com.mapplas.utils.visual.animation.NavigationBarButtonAnimation;
import com.mapplas.utils.visual.custom_views.RobotoButton;

public class AppGetterTaskViewsContainer {

	public AppAdapter listViewAdapter;

	public RefreshableListView listView;

	public RelativeLayout searchLayout;

	public ProgressBar searchLayoutSpinner;

	public RobotoButton userProfileButton;

	public RobotoButton searchButton;
	
	public RelativeLayout navigationBar;
	
	public RelativeLayout radarLayout;

	public AppGetterTaskViewsContainer(RelativeLayout searchLayout, ProgressBar searchLayoutSpinner, RobotoButton userProfileButton, RobotoButton searchButton, RelativeLayout navigationBar, RelativeLayout radarLayout) {
		this.searchLayout = searchLayout;
		this.searchLayoutSpinner = searchLayoutSpinner;
		this.userProfileButton = userProfileButton;
		this.searchButton = searchButton;
		this.navigationBar = navigationBar;
		this.radarLayout = radarLayout;
	}

	public void hideSearchLayout() {
		this.navigationBar.setVisibility(View.VISIBLE);
		this.radarLayout.setVisibility(View.GONE);
		
		this.searchLayout.setVisibility(View.GONE);
		this.searchLayoutSpinner.setVisibility(View.GONE);
	}

	public void manageSearchAndProfileButtonAnimations(Activity activity) {
		if(this.userProfileButton.getVisibility() == View.GONE) {
			new NavigationBarButtonAnimation(userProfileButton, searchButton).startFadeInAnimation(activity);
		}
	}

}
