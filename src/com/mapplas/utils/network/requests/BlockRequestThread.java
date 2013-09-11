package com.mapplas.utils.network.requests;

import com.mapplas.model.App;
import com.mapplas.utils.network.connectors.BlockRequestConnector;

public class BlockRequestThread {

	private String action;

	private App app;

	private String uid;

	public BlockRequestThread(String action, App anonLoc, String uid) {
		this.action = action;
		this.app = anonLoc;
		this.uid = uid;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					BlockRequestConnector.request(action, app.getId(), uid);
				} catch (Exception e) {
//					Log.i(getClass().getSimpleName(), "Thread Action Like: " + e);
				}
			}
		};
	}

}
