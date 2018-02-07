package com.fishbowl.fbtemplate1.widget;

import com.fishbowl.fbtemplate1.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TransparentProgressDialog extends Dialog 
{

	private ImageView iv;

	GifWebView view;
	public TransparentProgressDialog(Context context)
	{
		super(context, R.style.TransparentProgressDialog);
		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
		wlmp.gravity = Gravity.CENTER;
		getWindow().setAttributes(wlmp);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		setContentView(R.layout.customdialog);
	//	GIFView gview=	(GIFView) findViewById(R.id.image);
	//	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	//	RelativeLayout.LayoutParams params1=	(android.widget.RelativeLayout.LayoutParams) gview.getLayoutParams();
		
	/*	params.weight = 1.0f;
		params.gravity = Gravity.CENTER;*/
		//	view = new GifWebView(context, "file:///android_asset/loadingpizza.gif");
	//	addContentView(gview, params);
	/*	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight = 1.0f;
		params.gravity = Gravity.CENTER;
	LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);				
		layout.setLayoutParams(params);
	//	iv = new ImageView(context);
		view = new GifWebView(context, "file:///android_asset/loadingpizza.gif");
	//	view.setGravity
	//	iv.setImageResource(resourceIdOfImage);
		view.setLayoutParams(params);
		layout.addView(view, params);
*/	         
	}

	@Override
	public void show() 
	{
		super.show();
		/*RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(3000);
		iv.setAnimation(anim);
		iv.startAnimation(anim);*/
	}

}
