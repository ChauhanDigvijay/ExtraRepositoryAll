package com.fishbowl.fbtemplate1.widget;

import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Adapter.MATabsPagerAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.TextView;



@SuppressLint("NewApi") public class SlidingTabLayout extends HorizontalScrollView {


	/**
	 * Allows complete control over the colors drawn in the tab layout. Set with
	 * {@link #setCustomTabColorizer(TabColorizer)}.
	 */
	public interface TabColorizer {

		/**
		 * @return return the color of the indicator used when {@code position} is selected.
		 */
		int getIndicatorColor(int position);

		/**
		 * @return return the color of the divider drawn to the right of {@code position}.
		 */
		int getDividerColor(int position);

	}

	private static final int TITLE_OFFSET_DIPS = 24;
	private static final int TAB_VIEW_PADDING_DIPS = 8;
	private static final int TAB_VIEW_TEXT_SIZE_SP = 12;

	private int mTitleOffset;

	private int mTabViewLayoutId;
	private int mTabViewTextViewId;

	private ViewPager mViewPager;
	private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

	private final SlidingTabStrip mTabStrip;

	public SlidingTabLayout(Context context) {
		this(context, null);
	}

	public SlidingTabLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// Disable the Scroll Bar
		setHorizontalScrollBarEnabled(false);
		// Make sure that the Tab Strips fills this View
		setFillViewport(true);

		mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

		mTabStrip = new SlidingTabStrip(context);
		addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	/**
	 * Set the custom {@link TabColorizer} to be used.
	 *
	 * If you only require simple custmisation then you can use
	 * {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)} to achieve
	 * similar effects.
	 */
	public void setCustomTabColorizer(TabColorizer tabColorizer) {
		mTabStrip.setCustomTabColorizer(tabColorizer);
	}

	/**
	 * Sets the colors to be used for indicating the selected tab. These colors are treated as a
	 * circular array. Providing one color will mean that all tabs are indicated with the same color.
	 */
	public void setSelectedIndicatorColors(int... colors) {
		mTabStrip.setSelectedIndicatorColors(colors);
	}

	/**
	 * Sets the colors to be used for tab dividers. These colors are treated as a circular array.
	 * Providing one color will mean that all tabs are indicated with the same color.
	 */
	public void setDividerColors(int... colors) {
		mTabStrip.setDividerColors(colors);
	}

	/**
	 * Set the {@link ViewPager.OnPageChangeListener}. When using {@link SlidingTabLayout} you are
	 * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
	 * that the layout can update it's scroll position correctly.
	 *
	 * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
	 */
	public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
		mViewPagerPageChangeListener = listener;
	}

	/**
	 * Set the custom layout to be inflated for the tab views.
	 *
	 * @param layoutResId Layout id to be inflated
	 * @param textViewId id of the {@link TextView} in the inflated view
	 */
	public void setCustomTabView(int layoutResId, int textViewId) {
		mTabViewLayoutId = layoutResId;
		mTabViewTextViewId = textViewId;
	}

	/**
	 * Sets the associated view pager. Note that the assumption here is that the pager content
	 * (number of tabs and tab titles) does not change after this call has been made.
	 */
	public void setViewPager(ViewPager viewPager) {
		mTabStrip.removeAllViews();

		mViewPager = viewPager;
		if (viewPager != null) {
			viewPager.setOnPageChangeListener(new InternalViewPagerListener());
			populateTabStrip();
		}
	}

	/**
	 * Create a default view to be used for tabs. This is called if a custom tab view is not set via
	 * {@link #setCustomTabView(int, int)}.
	 */


	//Digvijay Chauhan Change this to set tab according width
	protected TextView createDefaultTabView(Context context) {

		TextView textView = new TextView(context);
		//DJ 
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		textView.setWidth(size.x / 4);

		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
		textView.setTypeface(Typeface.DEFAULT_BOLD);
		textView.setTextColor(getResources().getColor(R.color.textColorfortab));

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// If we're running on Honeycomb or newer, then we can use the Theme's
			// selectableItemBackground to ensure that the View has a pressed state
			TypedValue outValue = new TypedValue();
			getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
					outValue, true);
			textView.setBackgroundResource(outValue.resourceId);
		}

		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(true);
        }*/

		int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
		int lPadding = padding;
		int rPadding = padding;
		/*  if(YipUtil.isTablet()){
        	lPadding  = lPadding*2;
        	rPadding = rPadding*2;
        }*/

		textView.setPadding(lPadding, padding+5, rPadding, padding+5);

		return textView;
	}
	//Digvijay Chauhan change this to show icon
	private void populateTabStrip() {
		final MATabsPagerAdapter adapter =(MATabsPagerAdapter) mViewPager.getAdapter();
		final View.OnClickListener tabClickListener = new TabClickListener();

		for (int i = 0; i < adapter.getCount(); i++) {
			View tabView = null;
			TextView tabTitleView = null;

			if (mTabViewLayoutId != 0) {
				// If there is a custom tab view layout id set, try and inflate it
				tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
						false);
				tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
			}

			if (tabView == null) {
				tabView = createDefaultTabView(getContext());
			}


			if (tabTitleView == null && TextView.class.isInstance(tabView)) {
				tabTitleView = (TextView) tabView;
			}

			tabTitleView.setText(adapter.getPageTitle(i));


		//	tabTitleView.setCompoundDrawablesWithIntrinsicBounds(0, adapter.getDrawableId(i), 0, 0);
			if(i==0){
				tabTitleView.setTextColor(getResources().getColor(R.color.White));
				// 	tabTitleView.setBackgroundColor(getResources().getColor(R.color.djwhite));
			//	tabTitleView.setCompoundDrawablesWithIntrinsicBounds(0, adapter.getDrawableIdss(0), 0, 0);

			}
			//Select tab if it is current
			if (mViewPager.getCurrentItem() == i){
				tabView.setSelected(true);
			}

			tabView.setOnClickListener(tabClickListener);

			mTabStrip.addView(tabView);
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		if (mViewPager != null) {
			scrollToTab(mViewPager.getCurrentItem(), 0);
		}
	}

	private void scrollToTab(int tabIndex, int positionOffset) {
		final int tabStripChildCount = mTabStrip.getChildCount();
		if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
			return;
		}

		View selectedChild = mTabStrip.getChildAt(tabIndex);
		if (selectedChild != null) {
			int targetScrollX = selectedChild.getLeft() + positionOffset;

			if (tabIndex > 0 || positionOffset > 0) {
				// If we're not at the first child and are mid-scroll, make sure we obey the offset
				targetScrollX -= mTitleOffset;
			}

			scrollTo(targetScrollX, 0);
		}
	}

	private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
		private int mScrollState;

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			int tabStripChildCount = mTabStrip.getChildCount();
			if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
				return;
			}

			mTabStrip.onViewPagerPageChanged(position, positionOffset);

			View selectedTitle = mTabStrip.getChildAt(position);
			int extraOffset = (selectedTitle != null)
					? (int) (positionOffset * selectedTitle.getWidth())
							: 0;
					scrollToTab(position, extraOffset);

					if (mViewPagerPageChangeListener != null) {
						mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
								positionOffsetPixels);
					}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			mScrollState = state;

			if (mViewPagerPageChangeListener != null) {
				mViewPagerPageChangeListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position) {
			if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
				mTabStrip.onViewPagerPageChanged(position, 0f);
				scrollToTab(position, 0);
			}
			final MATabsPagerAdapter adapter =(MATabsPagerAdapter) mViewPager.getAdapter();

			if (mViewPagerPageChangeListener != null) {
				mViewPagerPageChangeListener.onPageSelected(position);
			}
			for (int i = 0; i < mTabStrip.getChildCount(); i++) {
				if (position != i) {
					((TextView)(mTabStrip.getChildAt(i))).setTextColor(getResources().getColor(R.color.othertabs));
					((TextView)(mTabStrip.getChildAt(i))).setBackgroundColor(getResources().getColor(R.color.background_top));
				//	((TextView)(mTabStrip.getChildAt(i))).setCompoundDrawablesWithIntrinsicBounds(0, adapter.getDrawableId(i), 0, 0);
				}
			}
			((TextView)(mTabStrip.getChildAt(position))).setTextColor(getResources().getColor(R.color.White));
			//	((TextView)(mTabStrip.getChildAt(position))).setBackgroundColor(getResources().getColor(R.color.djwhite));
		//	((TextView)(mTabStrip.getChildAt(position))).setCompoundDrawablesWithIntrinsicBounds(0, adapter.getDrawableIdss(position), 0, 0);

		}

	}

	private class TabClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {


			int foundIndex = -1;
			for (int i = 0; i < mTabStrip.getChildCount(); i++) {
				if (v == mTabStrip.getChildAt(i)) {
					foundIndex = i;
				}else{
					((TextView)(mTabStrip.getChildAt(i))).setTextColor(getResources().getColor(R.color.othertabs));
					((TextView)(mTabStrip.getChildAt(i))).setBackgroundColor(getResources().getColor(R.color.background_top));
				}
			}
			if(foundIndex!=-1){
				mViewPager.setCurrentItem(foundIndex);
				((TextView)(mTabStrip.getChildAt(foundIndex))).setTextColor(getResources().getColor(R.color.White));
				//((TextView)(mTabStrip.getChildAt(foundIndex))).setBackgroundColor(getResources().getColor(R.color.djwhite));

			}
		}
	}
}
