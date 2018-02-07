package com.raleys.libandroid;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ProgressDialog extends Dialog implements OnTouchListener {
	private int _width;
	private int _height;
	protected int _dialogWidth;
	protected int _dialogHeight;
	private Context _context;
	private RelativeLayout _mainLayout;
	private RelativeLayout _dialogLayout;
	private SmartTextView _messageTextView;

	private Timer _timer;
	private ArrayList<Drawable> load_array;
	private ImageView _spinner;

	@SuppressWarnings("deprecation")
	public ProgressDialog(Context context, Bitmap background, Typeface font,
			String message) {
		super(context, R.style.ModalDialog);

		try {
			setCancelable(false); // disables the back button while dialog is
									// active
			setCanceledOnTouchOutside(false);
			_context = context;
			DisplayMetrics displayMetrics = _context.getResources()
					.getDisplayMetrics(); // metrics calculated for portrait
											// mode
			Display display = ((WindowManager) _context
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();

			if (display.getHeight() > display.getWidth()) // portrait mode
			{
				_width = displayMetrics.widthPixels;
				_height = displayMetrics.heightPixels;
				;
			} else {
				_width = displayMetrics.heightPixels;
				_height = displayMetrics.widthPixels;
			}

			_mainLayout = new RelativeLayout(_context);
			_mainLayout.setOnTouchListener(this);

			int backgroundWidth = (int) (_width * .6);
			int backgroundHeight = (int) (backgroundWidth * .6);
			int dialogXOrigin = (_width - backgroundWidth) / 2;
			int dialogYOrigin = (_height - backgroundHeight) / 2;

			_dialogWidth = backgroundWidth;
			_dialogHeight = backgroundHeight;

			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = (_width - backgroundWidth) / 2;
			layoutParams.topMargin = (_height - backgroundHeight) / 2;
			// _mainLayout.addView(dialogImageView, layoutParams);

			// dialog layout is used for widget placement
			_dialogLayout = new RelativeLayout(context);
			layoutParams.leftMargin = dialogXOrigin;
			layoutParams.topMargin = dialogYOrigin;
			layoutParams.width = backgroundWidth;
			layoutParams.height = backgroundHeight;
			// _mainLayout.addView(_dialogLayout, layoutParams);

			int spinnerSize = (int) (_dialogHeight * .7);
			// Bitmap spinnerBottomBitmap =
			// BitmapFactory.decodeResource(_context.getResources(),
			// R.drawable.spinner_bottom);
			// spinnerBottomBitmap =
			// Bitmap.createScaledBitmap(spinnerBottomBitmap, spinnerSize,
			// spinnerSize, true);

			// _spinnerBottomView = new SpinnerView(_context,
			// spinnerBottomBitmap);
			// layoutParams = new
			// RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
			// RelativeLayout.LayoutParams.WRAP_CONTENT);
			// layoutParams.leftMargin = dialogXOrigin + ((_dialogWidth -
			// spinnerSize) / 2);
			// layoutParams.topMargin = dialogYOrigin + (int)(_dialogHeight *
			// .11);
			// //_mainLayout.addView(_spinnerBottomView, layoutParams);

			// Loading Intialize
			load_array = new ArrayList<Drawable>();
			Drawable spinnerTopBitmap = context.getResources().getDrawable(
					R.drawable.one);
			load_array.add(spinnerTopBitmap);

			spinnerTopBitmap = context.getResources().getDrawable(
					R.drawable.two);
			load_array.add(spinnerTopBitmap);

			spinnerTopBitmap = context.getResources().getDrawable(
					R.drawable.three);
			load_array.add(spinnerTopBitmap);

			spinnerTopBitmap = context.getResources().getDrawable(
					R.drawable.four);
			load_array.add(spinnerTopBitmap);

			spinnerTopBitmap = context.getResources().getDrawable(
					R.drawable.five);
			load_array.add(spinnerTopBitmap);

			spinnerTopBitmap = context.getResources().getDrawable(
					R.drawable.six);
			load_array.add(spinnerTopBitmap);

			spinnerTopBitmap = context.getResources().getDrawable(
					R.drawable.seven);
			load_array.add(spinnerTopBitmap);

			spinnerTopBitmap = context.getResources().getDrawable(
					R.drawable.eight);
			load_array.add(spinnerTopBitmap);

			_spinner = new ImageView(context);
			int _spinnerWidth= (int) (_width * 0.20);
			layoutParams = new RelativeLayout.LayoutParams(
					_spinnerWidth,_spinnerWidth);
			layoutParams.leftMargin = 0
					+ ((_width - _spinnerWidth) / 2);
			layoutParams.topMargin = dialogYOrigin
					+ (int) (_dialogHeight * .11);
			_mainLayout.addView(_spinner, layoutParams);

			_messageTextView = new SmartTextView(context,
					(int) (_dialogWidth * .9), (int) (_dialogHeight * .12), 1,
					Color.TRANSPARENT, Color.BLACK, font, message);
			layoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = (int) (_dialogWidth * .05);
			layoutParams.topMargin = (int) (_dialogHeight * .85);
			// _dialogLayout.addView(_messageTextView, layoutParams);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		setContentView(_mainLayout);
		getWindow().setLayout(_width, _height);
		_timer = new Timer();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		_timer.cancel();
		_spinner.setImageDrawable(null);
	}

	@Override
	public void onStart() {
		super.onStart();
		gearTurnThread();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// handles all touches that aren't handled by other UI components
		return true;
	}

	public void setMessage(String message) {
		// _messageTextView.writeText(message);
	}

	public void gearTurnThread() {
		_timer = new Timer();
		TimerTask task = (new TimerTask() {
			int index = 0;

			@Override
			public void run() {
				Activity activity = (Activity) _context;

				activity.runOnUiThread(new Runnable() {
					@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
					public void run() {
						if (index == load_array.size() - 1)
							index = 0;
						else
							index++;
						int sdk = android.os.Build.VERSION.SDK_INT;

						if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
							_spinner.setBackgroundDrawable(load_array
									.get(index));
						} else {
							_spinner.setBackground(load_array.get(index));
						}
					}
				});
			}
		});

		_timer.scheduleAtFixedRate(task, 0, 150); // in milliseconds

	}

	// private class SpinnerView extends View
	// {
	// private int _width;
	// private int _height;
	// private int _rotateDegrees = 0;
	// private Bitmap _bitmap;
	// private Matrix _matrix;
	//
	//
	// private SpinnerView(Context context, Bitmap bitmap)
	// {
	// super(context);
	// _width = bitmap.getWidth();
	// _height = bitmap.getHeight();
	// _bitmap = bitmap;
	// _matrix = new Matrix();
	// }
	//
	//
	//
	// @Override
	// public void onDraw(Canvas canvas)
	// {
	// _matrix.reset();
	// _matrix.setTranslate(0, 0);
	// _matrix.postRotate(_rotateDegrees, _width / 2, _height / 2);
	// canvas.drawBitmap(_bitmap, _matrix, null);
	// }
	// }
}
