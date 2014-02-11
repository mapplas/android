package com.mapplas.utils.visual.custom_views.autocomplete;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class CustomAutoCompleteView extends AutoCompleteTextView {

	public CustomAutoCompleteView(Context context) {
		super(context);
	}

	public CustomAutoCompleteView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomAutoCompleteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void performFiltering(final CharSequence text, final int keyCode) {
		String filterText = "";
		super.performFiltering(filterText, keyCode);
	}

	public void init() {
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Thin.ttf");
		setTypeface(tf, 1);
	}

}
