package com.mapplas.utils.network.async_tasks;

import android.os.AsyncTask;


public class TaskAsyncExecuter extends AsyncTask<Void, Void, Void> {

	private AsyncTaskHandler task = null;

	private AsyncTaskListener listener = null;

	public TaskAsyncExecuter(AsyncTaskHandler task) {
		this.task = task;
	}

	public TaskAsyncExecuter(AsyncTaskHandler task, AsyncTaskListener listener) {
		this.task = task;
		this.listener = listener;
	}

	@Override
	protected Void doInBackground(Void... params) {
		task.execute();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if(listener != null) {
			listener.finished();
		}
		if (task != null) {
			task.finished();
		}
	}

	@Override
	protected void onCancelled() {
		if(listener != null) {
			listener.cancelled();
		}
	}
}
