package com.fishbowl.fbtemplate1.widget;

import java.io.InputStream;

import com.fishbowl.fbtemplate1.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

public class GIFView extends View {
	public Movie mMovie;
	public long movieStart;
	private int mDrawLeftPos;
	public GIFView(Context context) {
		super(context);
		initializeView();
	}

	public GIFView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView();
	}

	public GIFView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView();
	}

	private void initializeView() 
	{
		//R.drawable.loader - our animated GIF
		InputStream is = getContext().getResources().openRawResource(R.drawable.loadingpizza);
		mMovie = Movie.decodeStream(is);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		super.onDraw(canvas);
		long now = android.os.SystemClock.uptimeMillis();
		if (movieStart == 0) {
			movieStart = now;
		}
		if (mMovie != null) {
			int relTime = (int) ((now - movieStart) % mMovie.duration());
			mMovie.setTime(relTime);
			mMovie.draw( canvas, mDrawLeftPos, this.getPaddingTop() );
			this.invalidate();


		}
	} 
	private int gifId;




	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
	{   
		int p_top = this.getPaddingTop(), p_bottom = this.getPaddingBottom();

		// Calculate new desired height
		final int desiredHSpec = MeasureSpec.makeMeasureSpec(   mMovie.height() + p_top + p_bottom , MeasureSpec.EXACTLY );

		setMeasuredDimension( widthMeasureSpec, desiredHSpec );
		super.onMeasure( widthMeasureSpec, desiredHSpec );

		// Update the draw left position
		mDrawLeftPos = Math.max( ( this.getWidth() -  mMovie.width() ) / 2, 0) ;
	}

	public void setGIFResource(int resId) {
		this.gifId = resId;
		initializeView();
	}

	public int getGIFResource() {
		return this.gifId;
	}
} 