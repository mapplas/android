/*
 * Copyright 2011 woozzu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.mapplas.utils.third_party;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.adapters.app.AppAdapter;
import com.mapplas.model.SuperModel;

public class RefreshableListView extends ListView {

	private View mHeaderContainer = null;

	private View mHeaderView = null;

	private ImageView mArrowOrRefresing = null;

	private TextView mText = null;
	
	private float mY = 0;

	private float mHistoricalY = 0;

	private int mHistoricalTop = 0;

	private int mInitialHeight = 0;

	private boolean mFlag = false;

	private boolean mArrowUp = false;

	private boolean mIsRefreshing = false;

	private int mHeaderHeight = 0;

	private OnRefreshListener mListener = null;

	private static final int REFRESH = 0;

	private static final int NORMAL = 1;
	
	private static final int HEADER_HEIGHT_DP = 128 + 30;
	
	private RotateAnimation reloadAnimation; 

	public RefreshableListView(final Context context) {
		super(context);
		initialize();
	}

	public RefreshableListView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public RefreshableListView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public void setOnRefreshListener(final OnRefreshListener l) {
		mListener = l;
	}

	public void completeRefreshing() {
		mHandler.sendMessage(mHandler.obtainMessage(NORMAL, mHeaderHeight, 0));
		mIsRefreshing = false;
		
		mArrowOrRefresing.setAnimation(null);
		mArrowOrRefresing.setImageResource(R.drawable.ic_pulltorefresh_arrow);
		
		invalidateViews();
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mHandler.removeMessages(REFRESH);
				mHandler.removeMessages(NORMAL);
				mY = mHistoricalY = ev.getY();
				if(mHeaderContainer.getLayoutParams() != null) {
					mInitialHeight = mHeaderContainer.getLayoutParams().height;
				}
				break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE:
				mHistoricalTop = getChildAt(0).getTop();
				break;
			case MotionEvent.ACTION_UP:
				if(!mIsRefreshing) {
					if(mArrowUp) {
						startRefreshing();
						mHandler.sendMessage(mHandler.obtainMessage(REFRESH, (int)(ev.getY() - mY) / 2 + mInitialHeight, 0));
					}
					else {
						if(getChildAt(0).getTop() == 0) {
							mHandler.sendMessage(mHandler.obtainMessage(NORMAL, (int)(ev.getY() - mY) / 2 + mInitialHeight, 0));
						}
					}
				}
				else {
					mHandler.sendMessage(mHandler.obtainMessage(REFRESH, (int)(ev.getY() - mY) / 2 + mInitialHeight, 0));
				}
				mFlag = false;
				break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(final MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_MOVE && getFirstVisiblePosition() == 0) {
			float direction = ev.getY() - mHistoricalY;
			int height = (int)(ev.getY() - mY) / 2 + mInitialHeight;
			if(height < 0) {
				height = 0;
			}

			float deltaY = Math.abs(mY - ev.getY());
			ViewConfiguration config = ViewConfiguration.get(getContext());
			if(deltaY > config.getScaledTouchSlop()) {

				// Scrolling downward
				if(direction > 0) {
					// Refresh bar is extended if top pixel of the first item is
					// visible
					if(getChildAt(0).getTop() == 0) {
						if(mHistoricalTop < 0) {

							// mY = ev.getY(); 
							// works without
							// this?mHistoricalTop = 0;
						}

						// Extends refresh bar
						setHeaderHeight(height);

						// Stop list scroll to prevent the list from
						// overscrolling
						ev.setAction(MotionEvent.ACTION_CANCEL);
						mFlag = false;
					}
				}
				else if(direction < 0) {
					// Scrolling upward

					// Refresh bar is shortened if top pixel of the first item
					// is
					// visible
					if(getChildAt(0).getTop() == 0) {
						setHeaderHeight(height);

						// If scroll reaches top of the list, list scroll is
						// enabled
						if(getChildAt(1) != null && getChildAt(1).getTop() <= 1 && !mFlag) {
							ev.setAction(MotionEvent.ACTION_DOWN);
							mFlag = true;
						}
					}
				}
			}

			mHistoricalY = ev.getY();
		}
		try {
			return super.dispatchTouchEvent(ev);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean performItemClick(final View view, final int position, final long id) {
		if(position == 0) {
			// This is the refresh header element
			return true;
		}
		else {
			return super.performItemClick(view, position - 1, id);
		}
	}

	private void initialize() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeaderContainer = inflater.inflate(R.layout.ptr_header, null);
		mHeaderView = mHeaderContainer.findViewById(R.id.all_header);
		mArrowOrRefresing = (ImageView)mHeaderContainer.findViewById(R.id.ivImage);
		mArrowOrRefresing.setAnimation(null);
		mArrowOrRefresing.setImageResource(R.drawable.ic_pulltorefresh_arrow);
		mText = (TextView)mHeaderContainer.findViewById(R.id.lblAction);
		addHeaderView(mHeaderContainer);

		mHeaderHeight = (int)(HEADER_HEIGHT_DP * getContext().getResources().getDisplayMetrics().density);
		setHeaderHeight(0);
		
		reloadAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reloadAnimation.setInterpolator(new LinearInterpolator());
		reloadAnimation.setDuration(300);
		reloadAnimation.setFillAfter(true);
		reloadAnimation.setRepeatCount(Animation.INFINITE);
		reloadAnimation.setRepeatMode(Animation.RESTART);
	}
	
	public LinearLayout getHeader() {
		return (LinearLayout)this.mHeaderContainer;
	}

	private void setHeaderHeight(final int height) {
		// Extends refresh bar
		LayoutParams lp = (LayoutParams)mHeaderContainer.getLayoutParams();
		if(lp == null) {
			lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		}
		lp.height = height;
		mHeaderContainer.setLayoutParams(lp);

		// Refresh bar shows up from bottom to top
		LinearLayout.LayoutParams headerLp = (LinearLayout.LayoutParams)mHeaderView.getLayoutParams();
		if(headerLp == null) {
			headerLp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		}
		headerLp.topMargin = -mHeaderHeight + height;
		mHeaderView.setLayoutParams(headerLp);

		if(!mIsRefreshing) {
			// If scroll reaches the trigger line, start refreshing
			if(height > mHeaderHeight && !mArrowUp) {
				mArrowOrRefresing.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate));
				mText.setText(R.string.ptr_release_to_refresh);
				rotateArrow();
				mArrowUp = true;
			}
			else if(height < mHeaderHeight && mArrowUp) {
				mArrowOrRefresing.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate));
				mText.setText(R.string.ptr_pull_to_refresh);
				rotateArrow();
				mArrowUp = false;
			}
		}
	}

	private void rotateArrow() {
		Drawable drawable = mArrowOrRefresing.getDrawable();
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.save();
		canvas.rotate(180.0f, canvas.getWidth() / 2.0f, canvas.getHeight() / 2.0f);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		canvas.restore();
		mArrowOrRefresing.setImageBitmap(bitmap);
	}

	private void startRefreshing() {
		mText.setText(R.string.ptr_refreshing);
		mIsRefreshing = true;
		mArrowOrRefresing.setImageResource(R.drawable.ic_refresh_photo);
		mArrowOrRefresing.startAnimation(reloadAnimation);

		if(mListener != null) {
			mListener.onRefresh(this);
		}
	}

	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);

			int limit = 0;
			switch (msg.what) {
				case REFRESH:
					limit = mHeaderHeight;
					break;
				case NORMAL:
					limit = (int)(30 * getContext().getResources().getDisplayMetrics().density);
					break;
			}

			// Elastic scrolling
			if(msg.arg1 >= limit) {
				setHeaderHeight(msg.arg1);
				int displacement = (msg.arg1 - limit) / 10;
				if(displacement == 0) {
					mHandler.sendMessage(mHandler.obtainMessage(msg.what, msg.arg1 - 1, 0));
				}
				else {
					mHandler.sendMessage(mHandler.obtainMessage(msg.what, msg.arg1 - displacement, 0));
				}
			}
		}

	};

	public interface OnRefreshListener {

		public void onRefresh(RefreshableListView listView);
		
	}
	
	public void updateAdapter(Context context, SuperModel model, ArrayList<ApplicationInfo> appInstalledList) {
		model.appList().sort();
		AppAdapter adapter = (AppAdapter)((HeaderViewListAdapter)this.getAdapter()).getWrappedAdapter();
		adapter.notifyDataSetChanged();
	}

}
