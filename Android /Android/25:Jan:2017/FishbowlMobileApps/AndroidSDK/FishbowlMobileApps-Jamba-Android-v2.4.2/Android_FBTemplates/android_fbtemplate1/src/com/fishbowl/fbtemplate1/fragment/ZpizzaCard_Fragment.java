     package com.fishbowl.fbtemplate1.fragment;
     /**
      **
      * Created by Digvijay Chauhan on 14/12/15.
      */
import com.fishbowl.fbtemplate1.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

 public class ZpizzaCard_Fragment extends Fragment implements OnClickListener
 {
	Activity thisActivity;
	LayoutInflater inflater;
	private ViewGroup parentContainer = null;
	String[] mobileArray = {"E-GIFT","LOCATE A STORE","MENU","EXPLORE","OFFERS"," NUTRITIONS CALCULATORS"};
	private final int SPLASH_DISPLAY_LENGTH = 5000;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{
		
		View rootView = inflater.inflate(R.layout.activity_zpizzacard, container, false);
		this.inflater = inflater;
		rootView.setTag("USER_TAGS_TAB");
		parentContainer = container;
		thisActivity = getActivity();
		setHasOptionsMenu(true);
		return rootView;
	}	


	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		ArrayAdapter adapter = new ArrayAdapter<String>(thisActivity, R.layout.activity_list, mobileArray);
		ListView listView = (ListView)thisActivity.findViewById(R.id.zpizzacard_list);
		listView.setAdapter(adapter);
	}
	
	public void onClick(View v) 
	{
	
		
	}
}
