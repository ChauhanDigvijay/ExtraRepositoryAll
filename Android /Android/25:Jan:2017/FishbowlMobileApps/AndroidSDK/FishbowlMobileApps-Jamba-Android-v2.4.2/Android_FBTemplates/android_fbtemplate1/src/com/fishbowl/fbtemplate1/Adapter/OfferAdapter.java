package com.fishbowl.fbtemplate1.Adapter;
/**
 **
 * Created by Digvijay Chauhan on 7/1/16.
 */

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.CoreActivity.FishbowlTemplate1App;
import com.fishbowl.fbtemplate1.Model.OfferItem;
import com.fishbowl.fbtemplate1.util.StringUtilities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class OfferAdapter extends ArrayAdapter<OfferItem>
{

	Activity context;
	List<OfferItem> drawerItemList;
	int layoutResID;



	public OfferAdapter(Activity context, int layoutResourceID,List<OfferItem> listItems) 
	{
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final DrawerItemHolder drawerHolder;
		View view = convertView;
		ImageLoader imageLoader = FishbowlTemplate1App.getInstance().getImageLoader();
		if (view == null) 
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();
			view = inflater.inflate(layoutResID, parent, false);
			drawerHolder.icon =  (NetworkImageView) view.findViewById(R.id.offerimage);
			drawerHolder.title = (TextView) view.findViewById(R.id.offername);
			drawerHolder.title1 = (TextView) view.findViewById(R.id.offerother);
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
		final OfferItem dItem = (OfferItem) this.drawerItemList.get(position);
		if(StringUtilities.isValidString(dItem.getOfferIUrl()))
		{
			drawerHolder.icon.setImageUrl(dItem.getOfferIUrl(),imageLoader);
		}
		drawerHolder.title.setText(dItem.getOfferIName().toUpperCase());
		drawerHolder.title1.setText(dItem.getOfferIItem());
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