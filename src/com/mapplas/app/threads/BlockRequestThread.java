package com.mapplas.app.threads;

import android.util.Log;

import com.mapplas.utils.NetRequests;


public class BlockRequestThread {
	
	private int uid;
	
	public BlockRequestThread(int uid) {
		this.uid = uid;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					NetRequests.UserBlocksRequest(String.valueOf(uid));
				} catch (Exception e) {
					Log.i(getClass().getSimpleName(), "Thread Action Like: " + e);
				}
			}
		};
	}
}
