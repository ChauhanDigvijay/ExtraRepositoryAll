package com.fishbowl.fbtemplate1.Adapter;

/**
 **
 * Created by Digvijay Chauhan on 11/12/15.
 */

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.CoreActivity.FishbowlTemplate1App;
import com.fishbowl.fbtemplate1.Model.MenuImageItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MenuImageAdapter extends ArrayAdapter<MenuImageItem> 
{

	Context context;
	List<MenuImageItem> drawerItemList;
	int layoutResID;
	public static int position = 0;


	public MenuImageAdapter(Activity context, int layoutResourceID,List<MenuImageItem> listItems)
	{
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		this.position=position;
		final DrawerItemHolder drawerHolder;
		View view = convertView;
		ImageLoader imageLoader = FishbowlTemplate1App.getInstance().getImageLoader();
		if (view == null) 
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();
			view = inflater.inflate(layoutResID, parent, false);			
			drawerHolder.icon =  (NetworkImageView) view.findViewById(R.id.menu_img);
			drawerHolder.title = (TextView) view.findViewById(R.id.imageheader_textview);
		//	drawerHolder.title1 = (TextView) view.findViewById(R.id.imageheadba);
			view.setTag(drawerHolder);
		} 
		else 
		{
			drawerHolder = (DrawerItemHolder) view.getTag();

		}

		/*	if(position % 2 == 0){
			view.setBackgroundColor(Color.rgb(238, 233, 233));
		}
		 */
		final MenuImageItem dItem = (MenuImageItem) this.drawerItemList.get(position);
		drawerHolder.icon.setImageUrl(dItem.getTitle(),imageLoader);
		drawerHolder.title.setText(dItem.getItemName().toUpperCase());
		//drawerHolder.title1.setText("Button,Mushroom,Tomatoes,Mozzarella,Red Onions,Bell,Peppers");
		return view;
	}

	private static class DrawerItemHolder
	{
		TextView ItemName,ItemName1, title,title1;
		NetworkImageView icon,icon1;
		LinearLayout headerLayout, itemLayout, spinnerLayout;
		Spinner spinner;
		CheckBox tv;

	}
}