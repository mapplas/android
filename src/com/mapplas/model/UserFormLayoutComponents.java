package com.mapplas.model;

import android.widget.ImageView;
import android.widget.LinearLayout;

public class UserFormLayoutComponents {

	private LinearLayout blocksLayout;

	private LinearLayout pinUpsLayout;

	private LinearLayout ratesLayout;

	private LinearLayout likesLayout;

	private LinearLayout footerListRefreshLayout;

	private LinearLayout footerButtonsLayout;
	
	private ImageView refreshIcon;

	public UserFormLayoutComponents(LinearLayout blocksLayout, LinearLayout pinUpsLayout, LinearLayout ratesLayout, LinearLayout likesLayout, LinearLayout footerLayout, LinearLayout footerInfoLayout, ImageView mPrivateRefreshIcon) {
		this.blocksLayout = blocksLayout;
		this.pinUpsLayout = pinUpsLayout;
		this.ratesLayout = ratesLayout;
		this.likesLayout = likesLayout;
		this.footerListRefreshLayout = footerLayout;
		this.footerButtonsLayout = footerInfoLayout;
		this.refreshIcon = mPrivateRefreshIcon;
	}

	public LinearLayout blocksLayout() {
		return this.blocksLayout;
	}

	public LinearLayout pinUpsLayout() {
		return this.pinUpsLayout;
	}

	public LinearLayout ratesLayout() {
		return this.ratesLayout;
	}

	public LinearLayout likesLayout() {
		return this.likesLayout;
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
