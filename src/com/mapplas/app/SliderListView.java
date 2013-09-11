package com.mapplas.app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SliderListView extends ListView {

	private Resizer resizer = new Resizer(this);

	private boolean open = false;

	public SliderListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SliderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SliderListView(Context context) {
		super(context);
	}

	public void SlideDown(int h, float v) {
		this.resizer.start(h, v);
		this.open = true;
	}

	public void SlideUp(float v) {
		this.resizer.start(0, v);
		this.open = false;
	}

	public boolean isOpen() {
		return open;
	}
}
