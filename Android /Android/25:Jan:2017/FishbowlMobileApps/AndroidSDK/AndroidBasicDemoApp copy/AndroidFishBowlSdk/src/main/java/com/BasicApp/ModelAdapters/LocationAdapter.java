package com.BasicApp.ModelAdapters;
/**
 **
 * Created by Digvijay Chauhan on 17/12/15.
 */

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.LocationItem;
import com.basicmodule.sdk.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Digvijay Chauhan on 1/12/15.
 */

public class LocationAdapter extends ArrayAdapter<LocationItem>
{

	Activity context;
	List<LocationItem> drawerItemList;
	int layoutResID;
	private Filter filter;

	LocationItem dItem ;
	public LocationAdapter(Activity context, int layoutResourceID,
			List<LocationItem> listItems) 
	{
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub


		final DrawerItemHolder drawerHolder;
		View view = convertView;


		
		
		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();
			view = inflater.inflate(layoutResID, parent, false);
			convertView = view;
			drawerHolder.storename = (TextView) view.findViewById(R.id.storename);
			drawerHolder.storelocation = (TextView) view.findViewById(R.id.storelocation);		
			drawerHolder.storezipcode = (TextView) view.findViewById(R.id.storezipcode);
			drawerHolder.storestate = (TextView) view.findViewById(R.id.storestate);
			drawerHolder.storeno = (TextView) view.findViewById(R.id.storeno);

			view.setTag(drawerHolder);

		} else {
			drawerHolder = (DrawerItemHolder) view.getTag();

			
		}
		if (position % 2 == 1) {
			convertView.setBackgroundColor(Color.TRANSPARENT);  
		} else {
			    convertView.setBackgroundColor(Color.TRANSPARENT);  
		}
		
		
		/*if(position % 2 == 0)
		{
			view.setBackgroundColor(Color.rgb(238, 233, 233));
		}*/
		dItem = (LocationItem) this.drawerItemList.get(position);
		DecimalFormat df = new DecimalFormat("0.00##");
		drawerHolder.storename.setText(dItem.getName()); 
		drawerHolder.storelocation.setText(dItem.getAddress()); 		
		drawerHolder.storezipcode.setText(""+dItem.getZipcode()); 
		drawerHolder.storeno.setText(""+dItem.getPhone());
		drawerHolder.storestate.setText(dItem.getState()+" "+"("+dItem.getCity()+")");

		
		return view;
	}


	@Override
	public Filter getFilter() 
	{
		if (filter == null)
			filter = new CustomFilter<LocationItem>(drawerItemList);
		return filter;
	}

	private class CustomFilter<T> extends Filter
	{

		private ArrayList<T> sourceObjects;

		public CustomFilter(List<T> objects)
		{
			sourceObjects = new ArrayList<T>();
			synchronized (this) {
				sourceObjects.addAll(objects);
			}
		}

		@Override
		protected FilterResults performFiltering(CharSequence chars) 
		{
			FilterResults result = new FilterResults();
			if(chars==null)
			{
				// add all objects
				synchronized (this) 
				{
					result.values = sourceObjects;
					result.count = sourceObjects.size();
				}
				return result;
			}
			String filterSeq = chars.toString().toLowerCase();
			if (filterSeq != null && filterSeq.length() > 0) 
			{
				ArrayList<T> filter = new ArrayList<T>();

				for (T object : sourceObjects)
				{
					// the filtering itself:
					if((((LocationItem)object).getName()!=null)||(((LocationItem)object).getZipcode()!=null))
					{
						if ((((LocationItem)object).getName().toLowerCase().contains(filterSeq))||(((LocationItem)object).getZipcode().contains(filterSeq)))
							filter.add(object);
					}
				}
				result.count = filter.size();
				result.values = filter;
			} 
			else 
			{
				// add all objects
				synchronized (this) 
				{
					result.values = sourceObjects;
					result.count = sourceObjects.size();
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,FilterResults results) 
		{
			// NOTE: this function is *always* called from the UI thread.
			ArrayList<T> filtered = (ArrayList<T>) results.values;
			notifyDataSetChanged();
			clear();
			if(filtered != null)
				for (int i = 0, l = filtered.size(); i < l; i++)
					add((LocationItem) filtered.get(i));
			notifyDataSetInvalidated();
		}
	}
	private static class DrawerItemHolder 
	{
		TextView storename,ItemName1, storelocation,storezipcode,storestate,storeno;

	}
}