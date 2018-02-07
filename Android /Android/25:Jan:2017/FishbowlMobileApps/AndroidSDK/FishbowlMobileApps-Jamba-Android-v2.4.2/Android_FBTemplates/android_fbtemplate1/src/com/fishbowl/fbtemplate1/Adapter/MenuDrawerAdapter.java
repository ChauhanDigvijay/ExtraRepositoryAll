package com.fishbowl.fbtemplate1.Adapter;

import java.util.ArrayList;

/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */

import java.util.List;

import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MenuDrawerAdapter extends ArrayAdapter<MenuDrawerItem> 
{

	Activity context;
	List<MenuDrawerItem> drawerItemList;
	int layoutResID;

	private Filter filter;
	
	public MenuDrawerAdapter(Activity context, int layoutResourceID,List<MenuDrawerItem> listItems)
	{
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;

	}
	
	
	
	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new CustomFilter<MenuDrawerItem>(drawerItemList);
		return filter;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		final DrawerItemHolder drawerHolder;
		View view = convertView;

		/*if(position % 2 == 0){
			view.setBackgroundColor(Color.rgb(238, 233, 233));
		}*/

		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();

			view = inflater.inflate(layoutResID, parent, false);
			drawerHolder.ItemName = (TextView) view.findViewById(R.id.drawer_itemName);
			drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);
			drawerHolder.title = (TextView) view.findViewById(R.id.drawerTitle);
			drawerHolder.ItemName1 = (TextView) view.findViewById(R.id.sub_text_email);
			drawerHolder.title1 = (TextView) view.findViewById(R.id.text_main_name);
			drawerHolder.headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);
			drawerHolder.itemLayout = (LinearLayout) view.findViewById(R.id.itemLayout);
			drawerHolder.spinnerLayout = (LinearLayout) view.findViewById(R.id.spinnerLayout);
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

		final MenuDrawerItem dItem = (MenuDrawerItem) this.drawerItemList.get(position);

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
			drawerHolder.icon.setVisibility(View.GONE);
			drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(dItem.getImgResID()));
			drawerHolder.ItemName.setText(dItem.getItemName());
			Log.d("Getview","Passed5");
		}

		return view;
	}

	
	private class CustomFilter<T> extends Filter {
		 
		private ArrayList<T> sourceObjects;
 
		public CustomFilter(List<T> objects) {
			sourceObjects = new ArrayList<T>();
			synchronized (this) {
				sourceObjects.addAll(objects);
			}
		}
 
		@Override
		protected FilterResults performFiltering(CharSequence chars) {
			FilterResults result = new FilterResults();
			if(chars==null){
				// add all objects
				synchronized (this) {
					result.values = sourceObjects;
					result.count = sourceObjects.size();
				}
				return result;
			}
			String filterSeq = chars.toString().toLowerCase();
			if (filterSeq != null && filterSeq.length() > 0) {
				ArrayList<T> filter = new ArrayList<T>();
 
				for (T object : sourceObjects) {
					// the filtering itself:
					if(((MenuDrawerItem)object).getItemName()!=null){
						if (((MenuDrawerItem)object).getItemName().toLowerCase().contains(filterSeq))
							filter.add(object);
					}
				}
				result.count = filter.size();
				result.values = filter;
			} else {
				// add all objects
				synchronized (this) {
					result.values = sourceObjects;
					result.count = sourceObjects.size();
				}
			}
			return result;
		}
 
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// NOTE: this function is *always* called from the UI thread.
			ArrayList<T> filtered = (ArrayList<T>) results.values;
			notifyDataSetChanged();
			clear();
			if(filtered != null)
			for (int i = 0, l = filtered.size(); i < l; i++)
				add((MenuDrawerItem) filtered.get(i));
			notifyDataSetInvalidated();
		}
	}
	
	
	private static class DrawerItemHolder {
		TextView ItemName,ItemName1, title,title1;
		ImageView icon,icon1;
		LinearLayout headerLayout, itemLayout, spinnerLayout;
		Spinner spinner;
		CheckBox tv;

	}
}