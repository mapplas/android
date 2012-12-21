package com.mapplas.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.utils.network.NetworkConnectionChecker;

public class AwesomeListView extends ListView {

	// *** CONSTANTS
	// AwesomeListView States Constants
	public static final int ALV_STATE_BORN = 0;

	public static final int ALV_STATE_IDLE = 1;

	public static final int ALV_STATE_SCROLLING = 2;

	public static final int ALV_STATE_SCROLLING_TOP = 3;

	public static final int ALV_STATE_PULL = 4;

	public static final int ALV_STATE_RELASE = 5;

	public static final int ALV_STATE_REFRESHING = 6;

	public static final int ALV_STATE_GETTINGMORE = 7;

	public static final int ALV_STATE_ENDING = 8;

	public static final int ALV_STATE_END = 9;

	public static final int ALV_STATE_ENDINGPREMATURE = 10;

	public static final int ALV_STATE_ENDPREMATURE = 11;

	public static final int ALV_STATE_ENDINGTIMEOUT = 12;

	public static final int ALV_STATE_ENDTIMEOUT = 13;

	// *** FIELDS
	// AwesomeListView State
	private int state = ALV_STATE_BORN;

	private int previousState = ALV_STATE_IDLE;

	// AwesomeListView Footer
	private View viewFooter = null;

	private TextView viewTextFooter = null;

	private String strFooterNormal = "";

	private String strFooterGetting = "";

	private OnRefresh onRefreshFooter = null;

	// AwesomeListView Header
	private View viewHeader = null;

	private RelativeLayout viewInnerHeader = null;

	private TextView viewTextHeader = null;
	
	private TextView viewWifiDisabledText = null;

	private ImageView imageViewHeader = null;

	private Drawable drawableHeaderPtr = null;

	private Drawable drawableHeaderRefreshing = null;

	private String strHeaderNormal = "";

	private String strHeaderPull = "";

	private String strHeaderRelase = "";

	private String strHeaderRefreshing = "";

	private OnRefresh onRefreshHeader = null;

	private OnRelease onReleaseHeader = null;

	private RotateAnimation flipAnimation;

	private RotateAnimation reverseFlipAnimation;

	private RotateAnimation refreshAnimation;

	// AwesomeListView Pull To Refresh Variables
	private float startCoordX = 0.0f;

	private float startCoordY = 0.0f;

	private float endCoordX = 0.0f;

	private float endCoordY = 0.0f;

	private float headerOffset = 0.0f;

	private float securityPull = 11.0f;

	private float thresholdDistance = 100.0f; // It depends on screen density

	private float notPullMoveLimit = 150.0f;

	// AwesomeListView Timeouter
	private Timeouter timeouter = new Timeouter();

	private Flinger flinger = new Flinger();

	private int flingerVelocity = 430; // It depends on screen density

	public TextView mDebugText = null;

	private Context mContext = null;

	public int mScrollState = 0;

	public AwesomeListView(Context context) {
		super(context);

		this.mContext = context;

		this.Init();
	}

	public AwesomeListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.mContext = context;

		this.Init();
	}

	public AwesomeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.mContext = context;

		this.Init();
	}

	@Override
	public void addHeaderView(View v) {
		super.addHeaderView(v);

	}

	@Override
	public void addHeaderView(View v, Object data, boolean isSelectable) {
		super.addHeaderView(v, data, isSelectable);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

	}

	public void FinishRefreshing() {
		if(this.state == ALV_STATE_REFRESHING) {
			timeouter.ForceFinish();
			setState(ALV_STATE_ENDING);
			CheckAutomaticStateTransitions();
		}
	}

	private void Init() {
		flipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		flipAnimation.setInterpolator(new LinearInterpolator());
		flipAnimation.setDuration(300);
		flipAnimation.setFillAfter(true);

		reverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseFlipAnimation.setInterpolator(new LinearInterpolator());
		reverseFlipAnimation.setDuration(300);
		reverseFlipAnimation.setFillAfter(true);

		refreshAnimation = new RotateAnimation(-360, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		refreshAnimation.setInterpolator(new LinearInterpolator());
		refreshAnimation.setDuration(1500);
		refreshAnimation.setFillAfter(true);
		refreshAnimation.setRepeatCount(Animation.INFINITE);
		refreshAnimation.setRepeatMode(Animation.RESTART);

		// Variable Screen Density
		DisplayMetrics metrics = new DisplayMetrics();
		((MapplasActivity)this.mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);

		headerOffset = headerOffset * metrics.density;
		securityPull = securityPull * metrics.density;
		thresholdDistance = thresholdDistance * metrics.density;
		flingerVelocity = (int)(flingerVelocity * metrics.density);
		notPullMoveLimit = (int)(notPullMoveLimit * metrics.density);

		this.setVerticalFadingEdgeEnabled(false);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.previousState = this.state;
		this.state = state;
	}

	public void SetHeaderHeight(int height) {
		if(this.viewInnerHeader != null) {
			android.view.ViewGroup.LayoutParams mlp = (android.view.ViewGroup.LayoutParams)this.viewInnerHeader.getLayoutParams();
			mlp.height = height;
			this.viewInnerHeader.setLayoutParams(mlp);
			// this.requestLayout();

			Log.d("DEBUG AWESOME LIST VIEW", this.previousState + " >> " + this.state + " | h:" + height + " | ho: " + this.headerOffset);
			if(mDebugText != null) {
				mDebugText.setText(this.previousState + " >> " + this.state + " | h:" + height + " | ho: " + this.headerOffset);
			}
		}
	}

	public void ComputeMotionEvent(MotionEvent ev) {
		if(this.state == ALV_STATE_REFRESHING) {
			this.startCoordX = ev.getRawX();
			this.startCoordY = ev.getRawY();
			this.endCoordX = ev.getRawX();
			this.endCoordY = ev.getRawY();
			this.headerOffset = 0;
			return;
		}

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				setState(ALV_STATE_SCROLLING);
				this.startCoordX = ev.getRawX();
				this.startCoordY = ev.getRawY();
				this.endCoordX = ev.getRawX();
				this.endCoordY = ev.getRawY();

				this.headerOffset = this.endCoordY - this.startCoordY;
				this.headerOffset = (this.headerOffset < 0) ? 0 : this.headerOffset;
				break;

			case MotionEvent.ACTION_MOVE:
				if(this.state == ALV_STATE_IDLE) {
					setState(ALV_STATE_SCROLLING);
					this.startCoordX = ev.getRawX();
					this.startCoordY = ev.getRawY();
					this.endCoordX = ev.getRawX();
					this.endCoordY = ev.getRawY();
				}
				else {
					this.endCoordX = ev.getRawX();
					this.endCoordY = ev.getRawY();
				}

				this.headerOffset = this.endCoordY - this.startCoordY;
				this.headerOffset = (this.headerOffset < 0) ? 0 : this.headerOffset;

				if((this.state != ALV_STATE_PULL && this.state != ALV_STATE_RELASE) && this.headerOffset > this.notPullMoveLimit) {
					this.headerOffset = 0.0f;
				}

				if(this.state == ALV_STATE_SCROLLING_TOP) {

					this.state = ALV_STATE_PULL;
				}

				break;

			case MotionEvent.ACTION_UP:

				if(this.state == ALV_STATE_RELASE) {
					this.setState(ALV_STATE_REFRESHING);
				}
				else {
					this.setState(ALV_STATE_ENDINGPREMATURE);
				}
				this.startCoordX = 0.0f;
				this.startCoordY = 0.0f;
				this.endCoordX = 0.0f;
				this.endCoordY = 0.0f;
				this.headerOffset = 0.0f;
				break;
		}

		this.CheckAutomaticStateTransitions();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		ComputeMotionEvent(ev);

		if(this.state == ALV_STATE_PULL || this.state == ALV_STATE_RELASE) {
			return true;

		}
		else if(this.state == ALV_STATE_REFRESHING) {
			return true;
		}

		return super.dispatchTouchEvent(ev);
	}

	public void insertHeader(View v, RelativeLayout vInner, TextView tv, TextView wifiDisabledTV, ImageView iv, Drawable dPtr, Drawable dRefreshing, String strPull, String strRelase, String strRefreshing) {
		this.viewHeader = v;
		this.viewInnerHeader = vInner;
		this.viewTextHeader = tv;
		this.viewWifiDisabledText = wifiDisabledTV;
		this.imageViewHeader = iv;
		this.drawableHeaderPtr = dPtr;
		this.drawableHeaderRefreshing = dRefreshing;
		this.strHeaderNormal = strPull;
		this.strHeaderPull = strPull;
		this.strHeaderRelase = strRelase;
		this.strHeaderRefreshing = strRefreshing;

		if(this.viewTextHeader != null) {
			this.viewTextHeader.setText(strHeaderNormal);
		}

		if(this.imageViewHeader != null) {
			this.imageViewHeader.setImageDrawable(this.drawableHeaderPtr);
		}

		this.addHeaderView(v, null, false);

		ViewTreeObserver vto = this.viewHeader.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new PTROnGlobalLayoutListener());
	}

	public void InsertFooter(View v, TextView text, String strNormal, String strGetting) {
		this.viewFooter = v;
		this.viewTextFooter = text;
		this.strFooterNormal = strNormal;
		this.strFooterGetting = strGetting;

		if(this.viewTextFooter != null) {
			this.viewTextFooter.setText(this.strFooterNormal);
		}

		this.addFooterView(v);
	}

	public void CheckAutomaticStateTransitions() {
		boolean stateChanged = false;
		if(this.previousState != this.state) {
			stateChanged = true;
			// Toast.makeText(SynesthActivity.GetAppContext(),this.previousState
			// + " >> " + this.state + "", Toast.LENGTH_SHORT).show();

			Log.d("DEBUG AWESOME LIST VIEW", this.previousState + " >> " + this.state);
			if(mDebugText != null) {
				mDebugText.setText(this.previousState + " >> " + this.state);
			}
		}

		switch (this.state) {
			case ALV_STATE_IDLE:
				// Change view values
				if(stateChanged) {
					if(this.viewTextHeader != null) {
						this.viewTextHeader.setText(strHeaderNormal);
						this.viewWifiDisabledText.setVisibility(View.GONE);
					}

					if(this.imageViewHeader != null) {
						this.imageViewHeader.setImageDrawable(this.drawableHeaderPtr);
					}

					if(this.viewTextFooter != null) {
						this.viewTextFooter.setText(this.strFooterNormal);
					}

					this.SetHeaderHeight(0);
					this.scrollTo(0, 0);
					ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)this.getLayoutParams();
					params.bottomMargin = 0;
					this.setLayoutParams(params);

					this.setVerticalScrollBarEnabled(true);

				}

				if(this.previousState == ALV_STATE_GETTINGMORE) {
					if(onRefreshFooter != null) {
						onRefreshFooter.onRefresh();
					}
				}

				if(this.previousState == ALV_STATE_REFRESHING) {
					if(onRefreshHeader != null) {
						onRefreshHeader.onRefresh();
					}
				}

				break;

			case ALV_STATE_SCROLLING:
				if(stateChanged) {
					if(this.viewTextHeader != null) {
						this.viewTextHeader.setText(strHeaderNormal);
						this.viewWifiDisabledText.setVisibility(View.GONE);
					}

					if(this.imageViewHeader != null) {
						this.imageViewHeader.setImageDrawable(this.drawableHeaderPtr);
					}

					if(this.viewTextFooter != null) {
						this.viewTextFooter.setText(this.strFooterNormal);
					}

					/*
					 * this.SetHeaderHeight(0); this.scrollTo(0, 0);
					 * ViewGroup.MarginLayoutParams params =
					 * (ViewGroup.MarginLayoutParams) this.getLayoutParams();
					 * params.bottomMargin = 0; this.setLayoutParams(params);
					 */
				}

				if(this.getFirstVisiblePosition() == 0 && this.headerOffset > this.securityPull) {
					Log.d("DEBUG AWESOME LIST VIEW", " ho: " + this.headerOffset);
					if(mDebugText != null) {
						mDebugText.setText(mDebugText.getText() + " ho: " + this.headerOffset);
					}

					setState(ALV_STATE_PULL);
					return;
				}

				break;

			case ALV_STATE_SCROLLING_TOP:
				if(stateChanged) {
					if(this.viewTextHeader != null) {
						this.viewTextHeader.setText(strHeaderNormal);
						this.viewWifiDisabledText.setVisibility(View.GONE);
					}

					if(this.imageViewHeader != null) {
						this.imageViewHeader.setImageDrawable(this.drawableHeaderPtr);
					}

					if(this.viewTextFooter != null) {
						this.viewTextFooter.setText(this.strFooterNormal);
					}
				}

				break;

			case ALV_STATE_PULL:
				if(stateChanged) {
					if(this.viewTextHeader != null) {
						this.viewTextHeader.setText(strHeaderPull);
						this.viewWifiDisabledText.setVisibility(View.GONE);
					}

					if(this.imageViewHeader != null) {
						this.imageViewHeader.setImageDrawable(this.drawableHeaderPtr);
						// TODO: Aminate ImageView if comming from
						// ALV_STATE_RELASE
						if(this.previousState == ALV_STATE_RELASE) {
							this.imageViewHeader.startAnimation(reverseFlipAnimation);
						}
						else {
							this.imageViewHeader.setAnimation(null);
						}
					}

					this.setVerticalScrollBarEnabled(false);

					this.SetHeaderHeight((int)this.thresholdDistance);
					ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)this.getLayoutParams();
					params.bottomMargin = (int)(-this.thresholdDistance);
					this.setLayoutParams(params);
				}

				if(this.thresholdDistance - this.headerOffset > 0) {
					this.scrollTo(0, (int)(this.thresholdDistance - this.headerOffset));
				}
				else {
					this.scrollTo(0, 0);
				}

				if(this.headerOffset > this.thresholdDistance) {
					setState(ALV_STATE_RELASE);
					return;
				}
				else if(this.headerOffset <= 0) {
					this.SetHeaderHeight(0);
					this.scrollTo(0, 0);
					setState(ALV_STATE_SCROLLING);
					return;
				}

				Log.d("DEBUG AWESOME LIST VIEW", this.previousState + " >> " + this.state + " | ho: " + this.headerOffset);
				if(mDebugText != null) {
					mDebugText.setText(this.previousState + " >> " + this.state + " | ho: " + this.headerOffset);
				}

				break;

			case ALV_STATE_RELASE:
				if(stateChanged) {
					if(this.viewTextHeader != null) {
						this.viewTextHeader.setText(strHeaderRelase);
						this.viewWifiDisabledText.setVisibility(View.GONE);
					}

					if(this.imageViewHeader != null) {
						this.imageViewHeader.setImageDrawable(this.drawableHeaderPtr);

						this.imageViewHeader.startAnimation(flipAnimation);
					}
				}

				if(this.headerOffset < this.thresholdDistance) {
					setState(ALV_STATE_PULL);
					return;
				}
				Log.d("DEBUG AWESOME LIST VIEW", this.previousState + " >> " + this.state + " | ho: " + this.headerOffset);
				if(mDebugText != null) {
					mDebugText.setText(this.previousState + " >> " + this.state + " | ho: " + this.headerOffset);
				}

				break;

			case ALV_STATE_REFRESHING:
				if(stateChanged) {
					if(this.viewTextHeader != null) {
						this.viewTextHeader.setText(strHeaderRefreshing);
						if (!new NetworkConnectionChecker().isWifiEnabled(mContext)) {
							this.viewWifiDisabledText.setVisibility(View.VISIBLE);
						}
					}

					if(this.imageViewHeader != null) {
						this.imageViewHeader.setImageDrawable(this.drawableHeaderRefreshing);
						this.imageViewHeader.startAnimation(refreshAnimation);
						timeouter.start();
					}
				}

				if(previousState == ALV_STATE_RELASE) {
					if(this.onReleaseHeader != null) {
						this.onReleaseHeader.onRelease();
					}
				}

				break;

			case ALV_STATE_ENDING:
				if(stateChanged) {
					ViewGroup.MarginLayoutParams params3 = (ViewGroup.MarginLayoutParams)AwesomeListView.this.getLayoutParams();
					params3.bottomMargin = (int)-(this.thresholdDistance);
					this.setLayoutParams(params3);
					this.invalidate();

					this.setSelection(0);

					flinger.start(this.flingerVelocity, (int)(this.thresholdDistance));
				}
				break;

			case ALV_STATE_END:
				timeouter.start(1, ALV_STATE_IDLE, false);
				break;

			case ALV_STATE_ENDINGPREMATURE:
				if(stateChanged) {
					if(this.previousState == ALV_STATE_PULL) {
						ViewGroup.MarginLayoutParams params3 = (ViewGroup.MarginLayoutParams)AwesomeListView.this.getLayoutParams();
						params3.bottomMargin = (int)-(this.thresholdDistance);
						this.setLayoutParams(params3);
						this.invalidate();

						// this.setSelection(0);

						flinger.start(this.flingerVelocity, (int)(this.thresholdDistance));
					}
					else {
						// this.setState(ALV_STATE_IDLE);
						timeouter.start(1, ALV_STATE_IDLE, true);
					}
				}
				break;

			case ALV_STATE_ENDPREMATURE:
				// this.setSelection(0);
				timeouter.start(1, ALV_STATE_IDLE, true);
				break;

			case ALV_STATE_ENDINGTIMEOUT:
				if(stateChanged) {
					ViewGroup.MarginLayoutParams params3 = (ViewGroup.MarginLayoutParams)AwesomeListView.this.getLayoutParams();
					params3.bottomMargin = (int)-(this.thresholdDistance);
					this.setLayoutParams(params3);
					this.invalidate();

					flinger.start(this.flingerVelocity, (int)this.thresholdDistance);
				}
				break;

			case ALV_STATE_ENDTIMEOUT:
				timeouter.start(1, ALV_STATE_IDLE, false);
				break;

			case ALV_STATE_GETTINGMORE:
				if(stateChanged) {
					if(this.viewTextHeader != null) {
						this.viewTextHeader.setText(strHeaderRelase);
						this.viewWifiDisabledText.setVisibility(View.GONE);
					}

					if(this.imageViewHeader != null) {
						this.imageViewHeader.setImageDrawable(this.drawableHeaderPtr);
						// TODO: Animate ImageView
					}
				}

				break;
		}

		// if(this.state == ALV_STATE_IDLE && this.previousState !=
		// ALV_STATE_IDLE)
		// {
		// this.CheckAutomaticStateTransitions();
		// }
		setState(this.state);
	}

	public void setOnRefreshHeaderListener(AwesomeListView.OnRefresh onRefresh) {
		this.onRefreshHeader = onRefresh;
	}

	public void setOnRefreshFooterListener(AwesomeListView.OnRefresh onRefresh) {
		this.onRefreshFooter = onRefresh;
	}

	public void setOnReleasehHeaderListener(AwesomeListView.OnRelease onRelease) {
		this.onReleaseHeader = onRelease;
	}

	public interface OnRefresh {

		public void onRefresh();
	}

	public interface OnRelease {

		public void onRelease();
	}

	private class PTROnGlobalLayoutListener implements OnGlobalLayoutListener {

		@Override
		public void onGlobalLayout() {
			if(AwesomeListView.this.state == AwesomeListView.ALV_STATE_BORN) {
				// AwesomeListView.this.SetHeaderHeight(0);
				// AwesomeListView.this.scrollTo(0, 0);
				// ViewGroup.MarginLayoutParams params =
				// (ViewGroup.MarginLayoutParams)
				// AwesomeListView.this.getLayoutParams();
				// params.bottomMargin = 0;
				AwesomeListView.this.setState(AwesomeListView.ALV_STATE_IDLE);
				CheckAutomaticStateTransitions();
			}
		}
	}

	private class Flinger implements Runnable {

		private final Scroller scroller;

		private int lastY = 0;

		Flinger() {
			scroller = new Scroller(AwesomeListView.this.getContext());
		}

		void start(int initialVelocity, int maxPositionY) {
			int initialY = AwesomeListView.this.getScrollY();
			int maxY = maxPositionY; // or some appropriate max value in your
										// code
			scroller.fling(0, initialY, 0, initialVelocity, 0, 0, 0, maxY);
			Log.i("Flinger", "starting fling at " + initialY + ", velocity is " + initialVelocity + "");

			lastY = initialY;
			AwesomeListView.this.post(this);
		}

		public void run() {
			if(scroller.isFinished()) {

				switch (AwesomeListView.this.state) {
					case ALV_STATE_ENDING:
						AwesomeListView.this.setState(ALV_STATE_END);
						break;

					case ALV_STATE_ENDINGPREMATURE:
						AwesomeListView.this.setState(ALV_STATE_ENDPREMATURE);
						break;

					case ALV_STATE_ENDINGTIMEOUT:
						AwesomeListView.this.setState(ALV_STATE_ENDTIMEOUT);
						break;
				}

				AwesomeListView.this.CheckAutomaticStateTransitions();

				Log.i("Flinger", "scroller is finished, done with fling");
				return;
			}

			boolean more = scroller.computeScrollOffset();
			int y = scroller.getCurrY();
			int diff = y - lastY;
			if(diff != 0) {
				AwesomeListView.this.scrollBy(0, diff);
				AwesomeListView.this.invalidate();
				lastY = y;
			}

			if(more) {
				AwesomeListView.this.post(this);
			}

			AwesomeListView.this.CheckAutomaticStateTransitions();
		}

		boolean isFlinging() {
			return !scroller.isFinished();
		}

		void forceFinished() {
			if(!scroller.isFinished()) {
				scroller.forceFinished(true);
			}
		}
	}

	private class Timeouter implements Runnable {

		public static final long DEFAULT_TIMEOUT = 60000;

		public static final int DEFAULT_STATE = ALV_STATE_ENDINGPREMATURE;

		public long timeout = DEFAULT_TIMEOUT;

		public int finishState = DEFAULT_STATE;

		private long startTime;

		private boolean forceFinish = false;

		private boolean waitScroll = false;

		private boolean started = false;

		Timeouter() {

		}

		void start() {
			this.timeout = DEFAULT_TIMEOUT;
			this.finishState = DEFAULT_STATE;
			this.waitScroll = false;
			this.forceFinish = false;
			this.startTime = System.currentTimeMillis();
			AwesomeListView.this.post(this);

			Log.i("Timeouter", "Default Start!");
		}

		void start(long t, int s, boolean w) {
			this.timeout = t;
			this.finishState = s;
			this.waitScroll = w;
			this.forceFinish = false;
			this.startTime = System.currentTimeMillis();
			AwesomeListView.this.post(this);

			Log.i("Timeouter", "Custom Start! t=" + t + " s=" + s + " w=" + w);
		}

		public void run() {
			boolean more = !forceFinish;

			if(waitScroll && AwesomeListView.this.mScrollState != 0) {
				Log.i("Timeouter", "Waiting to finish scroll! s=" + AwesomeListView.this.mScrollState);
				AwesomeListView.this.post(this);
			}
			else {
				Log.i("Timeouter", "Not Scrolling s=" + AwesomeListView.this.mScrollState);

				if(AwesomeListView.this.imageViewHeader != null) {
					AwesomeListView.this.imageViewHeader.invalidate();
				}

				if(System.currentTimeMillis() - this.startTime > this.timeout) {
					more = false;
				}

				if(more) {
					AwesomeListView.this.post(this);
				}
				else {
					if(!forceFinish) {
						AwesomeListView.this.setState(this.finishState);
					}
				}
			}

			AwesomeListView.this.CheckAutomaticStateTransitions();
			AwesomeListView.this.invalidate();
		}

		public void ForceFinish() {
			this.forceFinish = true;
		}
	}

}
