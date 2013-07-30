package com.mapplas.model;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mapplas.utils.visual.custom_views.RobotoButton;

public class UserFormLayoutComponents {

	private RobotoButton blocksButton;

	private RobotoButton pinUpsButton;

	private LinearLayout footerListRefreshLayout;

	private LinearLayout footerButtonsLayout;
	
	private ImageView refreshIcon;

	public UserFormLayoutComponents(RobotoButton blocksButton, RobotoButton pinUpsButton, LinearLayout footerLayout, LinearLayout footerInfoLayout, ImageView mPrivateRefreshIcon) {
		this.blocksButton = blocksButton;
		this.pinUpsButton = pinUpsButton;
		this.footerListRefreshLayout = footerLayout;
		this.footerButtonsLayout = footerInfoLayout;
		this.refreshIcon = mPrivateRefreshIcon;
	}

	public RobotoButton blocksButton() {
		return this.blocksButton;
	}

	public RobotoButton pinUpsButton() {
		return this.pinUpsButton;
	}

	public LinearLayout footerListRefreshLayout() {
		return this.footerListRefreshLayout;
	}
	
	public LinearLayout footerButtonsLayout() {
		return this.footerButtonsLayout;
	}
	
	public ImageView refreshIcon() {
		return this.refreshIcon;
	}
}
