package com.fishbowl.fbtemplate1.fragment;
import java.io.Serializable;
import java.util.List;

/**
 **
 * Created by Digvijay Chauhan on 23/12/15.
 */
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Adapter.LocationAdapter;
import com.fishbowl.fbtemplate1.Controller.FB_DBLocation;
import com.fishbowl.fbtemplate1.Controller.FB_DBOrderConfirm;
import com.fishbowl.fbtemplate1.CoreActivity.FishbowlTemplate1App;
import com.fishbowl.fbtemplate1.Model.LocationItem;
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;
import com.fishbowl.fbtemplate1.Model.OrderItem;
import com.fishbowl.fbtemplate1.activity.TestActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LocationFavourite_Fragment extends Fragment implements OnClickListener
{
	LocationAdapter adapter;
	List<LocationItem> lists;
	ListView listView;
	Activity thisActivity;
	LayoutInflater inflater;
	FishbowlTemplate1App fishTApp;
	private ViewGroup parentContainer = null;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_locationfavourite, container, false);
		this.inflater = inflater;
		rootView.setTag("USER_TAGS_TAB");
		parentContainer = container;
		thisActivity = getActivity();
		setHasOptionsMenu(true);
		fishTApp=FishbowlTemplate1App.getInstance();
		listView = (ListView)rootView.findViewById(R.id.favourite_list);
		loadNewItemsCount();
		return rootView;
	}

	public void loadNewItemsCount() 
	{

		lists =FB_DBLocation.getInstance().getAllLocation();
		adapter = new LocationAdapter(getActivity(),R.layout.list_location,lists);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
			{
				Intent intent = new Intent(thisActivity,TestActivity.class);		
				Bundle extras = new Bundle();
				if( lists!=null&&!fishTApp.getStoreActivity().signiin)	
				{
					LocationItem location=	lists.get(position);
					FB_DBLocation.getInstance().createUpdateLocation(location);
					OrderItem order=  new OrderItem();

					final List<MenuDrawerItem> lists =FB_DBOrderConfirm.getInstance().getAllItem();
					order.setStoreID(location.getStoreID());
					order.setItemlist(lists);
					extras.putSerializable("draweritem1", (Serializable)lists);
					extras.putSerializable("order", order);
					extras.putSerializable("historyflag", true);
					extras.putSerializable("storelocation", location.getAddress());
					intent.putExtras(extras);
					startActivityForResult(intent, 2);
				}

				

			}
		});


	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}
	
	public void onClick(View v) 
	{

	}
}
