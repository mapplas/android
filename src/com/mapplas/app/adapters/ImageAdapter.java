
package com.mapplas.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import app.mapplas.com.R;

import com.mapplas.app.Resizer;
import com.mapplas.app.activities.AppDetail;
import com.mapplas.utils.image.DrawableBackgroundDownloader;

public class ImageAdapter extends BaseAdapter {

	int mGalleryItemBackground;

	private Context context;

	private final DrawableBackgroundDownloader mdbd = new DrawableBackgroundDownloader();

	public ArrayList<String> mImages = new ArrayList<String>();

	private Gallery mParentGallery = null;

	DisplayMetrics metrics = new DisplayMetrics();

	public ImageAdapter(Context c, Gallery gal) {
		context = c;
		mParentGallery = gal;

		((AppDetail)this.context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		// TypedArray a = c.obtainStyledAttributes(R.styleable.HelloGallery);
		// mGalleryItemBackground =
		// a.getResourceId(R.styleable.HelloGallery_android_galleryItemBackground,
		// 0);
		// a.recycle();
	}

	public int getCount() {
		return mImages.size();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = new ImageView(context);
		i.setScaleType(ScaleType.CENTER);

		Drawable defaultDrawable = this.context.getResources().getDrawable(R.drawable.ic_blank);

		int constantw = 480;

		if(metrics.widthPixels >= 480) {
			constantw = 480;
		}
		else {
			constantw = 320;
		}

		mdbd.loadDrawable(mImages.get(position), i, defaultDrawable, true, constantw, false, null);

		// i.setLayoutParams(new Gallery.LayoutParams(480, 320));
		// i.setScaleType(ImageView.ScaleType.FIT_XY);
		// i.setBackgroundDrawable(gallerybgd);

		Drawable dw = i.getDrawable();

		if(dw != null && dw != defaultDrawable) {
			float ratio = (float)dw.getIntrinsicWidth() / (float)dw.getIntrinsicHeight();
			int w = dw.getIntrinsicWidth();
			int h = dw.getIntrinsicHeight();

			w = constantw;
			h = constantw;

			if(dw.getIntrinsicWidth() > dw.getIntrinsicHeight()) {
				h = (int)(constantw / ratio);
			}
			else {
				w = (int)(constantw * ratio);
			}

			if(mParentGallery != null) {
				LayoutParams lp = mParentGallery.getLayoutParams();
				if(lp.height < h) {
					Resizer resizer = new Resizer(mParentGallery);
					resizer.start((int)(h + (12 * metrics.density)), 500 * metrics.density);
				}
			}

			// i.setLayoutParams(new Gallery.LayoutParams(w, h + ((480 - h) /
			// 2)));
			i.setLayoutParams(new Gallery.LayoutParams(w, h));
			i.setScaleType(ImageView.ScaleType.FIT_XY);

			// i.setPadding(0, (480 - h) / 2, 0, 0);
		}

		return i;
	}

	@Override
	public Object getItem(int position) {
		return mImages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
