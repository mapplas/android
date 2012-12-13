package com.mapplas.model;

import android.widget.ImageView;
import android.widget.LinearLayout;

public class UserFormLayoutComponents {

	private LinearLayout blocksLayout;

	private LinearLayout pinUpsLayout;

	private LinearLayout ratesLayout;

	private LinearLayout likesLayout;

	private LinearLayout footerLayout;

	private LinearLayout footerInfoLayout;
	
	private ImageView refreshIcon;

	public UserFormLayoutComponents(LinearLayout blocksLayout, LinearLayout pinUpsLayout, LinearLayout ratesLayout, LinearLayout likesLayout, LinearLayout footerLayout, LinearLayout footerInfoLayout, ImageView mPrivateRefreshIcon) {
		this.blocksLayout = blocksLayout;
		this.pinUpsLayout = pinUpsLayout;
		this.ratesLayout = ratesLayout;
		this.likesLayout = likesLayout;
		this.footerLayout = footerLayout;
		this.footerInfoLayout = footerInfoLayout;
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

	public LinearLayout footerLayout() {
		return this.footerLayout;
	}
	
	public LinearLayout footerInfoLayout() {
		return this.footerInfoLayout;
	}
	
	public ImageView refreshIcon() {
		return this.refreshIcon;
	}
}
