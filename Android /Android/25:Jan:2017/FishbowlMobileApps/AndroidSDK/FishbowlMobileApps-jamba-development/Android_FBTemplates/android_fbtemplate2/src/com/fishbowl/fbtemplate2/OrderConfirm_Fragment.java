package com.fishbowl.fbtemplate2;


import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class OrderConfirm_Fragment extends Fragment implements
OnItemClickListener, OnClickListener {

	private ViewGroup parentContainer = null;
	Activity thisActivity;
	LayoutInflater inflater;	

	ListView listView;
	List<DetailItem> rowItems;

	int position;
	ListView lv;
	public static Boolean checkback=false;	
	public static Boolean checkaddmore=false;	
	List_OrderConfirmAdapter adapter ;	

	List<DetailItem> listss ;
	DetailItem drawitem = null;

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = inflater.inflate(
				R.layout.activity_orderconfirm, container, false);
		this.inflater = inflater;
		rootView.setTag("USER_TAGS_TAB");
		parentContainer = container;
		thisActivity = getActivity();

		ImageView b = (ImageView) rootView.findViewById(R.id.add_btn);
		b.setOnClickListener(this);
		setHasOptionsMenu(true);
		return rootView;
	}	



	/*public void back(View v) {
		// does something very interesting
		finish();

	}*/








	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle extras = getArguments();
		Intent i=thisActivity.getIntent();
		i.putExtras(extras);
		if (extras != null) {
			drawitem = (DetailItem) extras.getSerializable("draweritem");



			listss = (List<DetailItem>) i.getSerializableExtra("draweritem1");

		}

		lv=(ListView)thisActivity.findViewById(R.id.mobile_listdf);

		adapter = new List_OrderConfirmAdapter(thisActivity,
				drawitem,listss,checkback);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}

/*
	public void onBackPressed() {
		checkaddmore=true;
		if (getFragmentManager().getBackStackEntryCount() == 0) {
			checkback=true;
			thisActivity.finish();
		} else {
			checkback=true;
			getFragmentManager().popBackStack();
		}

	}*/

	@Override
    public void onDetach() {
        super.onDetach();
        if(!checkaddmore){
        if (getFragmentManager().getBackStackEntryCount() == 0) {
			checkback=true;
			thisActivity.finish();
		} else {
			checkback=true;
			getFragmentManager().popBackStack();
		}
    }
        checkaddmore=false;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast toast = Toast.makeText(thisActivity,
				"Item " + (position + 1) + ": " + rowItems.get(position),
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		checkaddmore=true;
		if (getFragmentManager().getBackStackEntryCount() == 0) {
			checkback=true;
			thisActivity.finish();
		} else {
			checkback=true;
			getFragmentManager().popBackStack();
		}

	}
}
