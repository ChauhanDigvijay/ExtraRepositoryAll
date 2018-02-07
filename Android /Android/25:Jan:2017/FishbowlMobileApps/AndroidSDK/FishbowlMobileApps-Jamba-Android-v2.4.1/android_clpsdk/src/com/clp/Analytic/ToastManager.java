package com.clp.Analytic;

import android.content.Context;
import android.widget.Toast;

public class ToastManager {
	    static ToastManager instance=null;
	    Context context;
    	static boolean onOff=false;
    	
	public static boolean isOnOff() {
			return onOff;
		}

		public static void setOnOff(boolean onOff) {
			ToastManager.onOff = onOff;
		}

	public static ToastManager sharedInstance(){
		
		if(instance==null){
			instance=new ToastManager(); 
		} 
		return instance;
	}
	
	public void initWithContext(Context _context){
		this.context=_context;		
	}
	
	public void show(String msg){
		if(onOff){
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}
	}
	 
}
