package com.BasicApp.Adapters;

/**
 * Created by Digvijay Chauhan on 1/12/15.
 */

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.basicmodule.sdk.R;
import com.BasicApp.BusinessLogic.Models.OrderConfirmDrawItem;

import java.util.List;

public class OrderConfirmDrawerAdapter extends ArrayAdapter<OrderConfirmDrawItem>
{

	Activity context;
	List<OrderConfirmDrawItem> drawerItemList;
	int layoutResID;



	public OrderConfirmDrawerAdapter(Activity context, int layoutResourceID,List<OrderConfirmDrawItem> listItems) 
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

		if (view == null)
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();
			view = inflater.inflate(layoutResID, parent, false);

			/*	if(position % 2 == 0){
				view.setBackgroundColor(Color.rgb(238, 233, 233));
			}*/
			drawerHolder.ItemName = (TextView) view.findViewById(R.id.drawer_itemName);
			drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);
			drawerHolder.title = (TextView) view.findViewById(R.id.drawerTitle);
			drawerHolder.ItemName1 = (TextView) view.findViewById(R.id.sub_text_email);
			drawerHolder.title1 = (TextView) view.findViewById(R.id.text_main_name);
			drawerHolder.headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);
			drawerHolder.itemLayout = (LinearLayout) view.findViewById(R.id.itemLayout);
			drawerHolder.spinnerLayout = (LinearLayout) view.findViewById(R.id.spinnerLayout);			//	

			view.setTag(drawerHolder);

		} 		
		else
		{
			drawerHolder = (DrawerItemHolder) view.getTag();
		}

		/*	if(tv.isChecked()){
			tv.setChecked(true);
		} else {
			tv.setChecked(false); 
		}*/           

		final OrderConfirmDrawItem dItem = (OrderConfirmDrawItem) this.drawerItemList.get(position);

		if (dItem.isSpinner())
		{
			drawerHolder.headerLayout.setVisibility(LinearLayout.GONE);
			drawerHolder.itemLayout.setVisibility(LinearLayout.GONE);
			drawerHolder.spinnerLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.ItemName1.setText(dItem.getItemName());
			drawerHolder.title1.setText(dItem.getTitle());


		}
		else if (dItem.getTitle() != null)
		{
			drawerHolder.headerLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.itemLayout.setVisibility(LinearLayout.GONE);
			drawerHolder.spinnerLayout.setVisibility(LinearLayout.GONE);
			drawerHolder.title.setText(dItem.getTitle());
			Log.d("Getview","Passed4");
		}
		else
		{

			drawerHolder.headerLayout.setVisibility(LinearLayout.GONE);
			drawerHolder.spinnerLayout.setVisibility(LinearLayout.GONE);
			drawerHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.icon.setVisibility(View.VISIBLE);
			drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(dItem.getImgResID()));
			drawerHolder.ItemName.setText(dItem.getItemName());
			Log.d("Getview","Passed5");
		}

		return view;
	}

	private static class DrawerItemHolder
	{
		TextView ItemName,ItemName1, title,title1;
		ImageView icon,icon1;
		LinearLayout headerLayout, itemLayout, spinnerLayout;
		Spinner spinner;
		CheckBox tv;

	}
}