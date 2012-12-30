package com.mapplas.app.async_tasks;


public interface AsyncTaskListener {
	public void finished();
	public void cancelled();
}
