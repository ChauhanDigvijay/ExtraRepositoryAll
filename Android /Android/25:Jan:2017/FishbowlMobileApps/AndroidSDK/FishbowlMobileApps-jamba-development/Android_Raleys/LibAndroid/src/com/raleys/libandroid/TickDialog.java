package com.raleys.libandroid;

import android.app.Dialog;
import android.content.Context;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TickDialog extends Dialog {

	public final static int animationDelay=2000;
	int _width;
	int _height;
	Context _context;
	RelativeLayout _mainLayout;
	ImageView _tickImage;

	@SuppressWarnings("deprecation")
	public TickDialog(Context context) {
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

			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			int imageSize = (int) (_width * .20);
			_tickImage = new ImageView(_context);
			layoutParams = new RelativeLayout.LayoutParams(imageSize, imageSize);
			layoutParams.leftMargin = 0;
			layoutParams.topMargin = 0;
			layoutParams.width = imageSize;
			layoutParams.height = imageSize;
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					_mainLayout.getId());
			_mainLayout.setGravity(Gravity.CENTER);
			_mainLayout.addView(_tickImage, layoutParams);
			_tickImage.setBackgroundResource(R.drawable.success);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		setContentView(_mainLayout);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				dismiss();
				_tickImage.setAlpha(0.0f);
			}
		}, animationDelay);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

}
