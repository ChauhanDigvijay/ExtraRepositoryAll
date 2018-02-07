package com.fishbowl.fbtemplate2;


/**
 * Created by Digvijay Chauhan on 7/12/15.
 */
import java.util.List;

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

public class MenuAdapter extends ArrayAdapter<Menu_Item> {

	Activity context;
	List<Menu_Item> drawerItemList;
	int layoutResID;



	public MenuAdapter(Activity context, int layoutResourceID,
			List<Menu_Item> listItems) {
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub


		final DrawerItemHolder drawerHolder;
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();

			view = inflater.inflate(layoutResID, parent, false);
			drawerHolder.ItemName = (TextView) view
					.findViewById(R.id.drawer_itemName);
			drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);


			drawerHolder.title = (TextView) view.findViewById(R.id.drawerTitle);

			drawerHolder.ItemName1 = (TextView) view
					.findViewById(R.id.sub_text_email);



			drawerHolder.title1 = (TextView) view.findViewById(R.id.text_main_name);


			drawerHolder.headerLayout = (LinearLayout) view
					.findViewById(R.id.headerLayout);
			drawerHolder.itemLayout = (LinearLayout) view
					.findViewById(R.id.itemLayout);
			drawerHolder.spinnerLayout = (LinearLayout) view
					.findViewById(R.id.spinnerLayout);			//	
			//
			view.setTag(drawerHolder);

		} else {
			drawerHolder = (DrawerItemHolder) view.getTag();

		}

		/*	if(tv.isChecked()){
			tv.setChecked(true);
		} else {
			tv.setChecked(false); 
		}*/

		final Menu_Item dItem = (Menu_Item) this.drawerItemList.get(position);

		if (dItem.isSpinner()) {
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
			drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(
					dItem.getImgResID()));
			drawerHolder.ItemName.setText(dItem.getItemName());
			Log.d("Getview","Passed5");
		}

		return view;
	}

	private static class DrawerItemHolder {
		TextView ItemName,ItemName1, title,title1;
		ImageView icon,icon1;
		LinearLayout headerLayout, itemLayout, spinnerLayout;
		Spinner spinner;
		CheckBox tv;

	}
}