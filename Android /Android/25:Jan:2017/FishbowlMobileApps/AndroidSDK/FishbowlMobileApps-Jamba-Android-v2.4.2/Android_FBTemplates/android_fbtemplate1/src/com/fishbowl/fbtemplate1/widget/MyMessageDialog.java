package com.fishbowl.fbtemplate1.widget;
/**
 * Created by Digvijay Chauhan on 27/06/15.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MyMessageDialog extends DialogFragment {
	
	Context mContext;
	String mTitle;
	String mMesssgae;
	public MyMessageDialog(Context context, String title, String message) {
		mContext = context;
		mTitle = title;
		mMesssgae = message;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
		alertDialogBuilder.setTitle(mTitle);
		alertDialogBuilder.setMessage(mMesssgae);
		
		//null should be your on click listener
		//alertDialogBuilder.setPositiveButton("OK", null);
		
		alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});


		return alertDialogBuilder.create();
	}

}
