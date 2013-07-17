package com.mapplas.utils.visual.helpers;

public class PlayStoreLinkCreator {

	public String createLinkForApp(String app_id) {
		return "market://details?id=" + app_id;
	}
	
	public String createPlayStoreExternalLinkForApp(String app_id) {
		return "play.google.com/store/apps/details?id=" + app_id;
	}

}
