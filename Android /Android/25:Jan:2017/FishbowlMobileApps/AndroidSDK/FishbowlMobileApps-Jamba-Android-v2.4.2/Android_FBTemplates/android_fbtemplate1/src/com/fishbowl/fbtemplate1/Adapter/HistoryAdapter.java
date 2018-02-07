package com.fishbowl.fbtemplate1.Adapter;
/**
 **
 * Created by Digvijay Chauhan on 16/12/15.
 */
import java.text.DecimalFormat;
/**
 * Created by Digvijay Chauhan on 1/12/15.
 */
import java.util.List;

import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Model.OrderItem;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HistoryAdapter extends ArrayAdapter<OrderItem> 
{

	Activity context;
	List<OrderItem> drawerItemList;
	int layoutResID;



	public HistoryAdapter(Activity context, int layoutResourceID,List<OrderItem> listItems) 
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

		if (view == null) 
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();
			view = inflater.inflate(layoutResID, parent, false);
			drawerHolder.orderNo = (TextView) view.findViewById(R.id.orderno);
			drawerHolder.orderTotal  = (TextView) view.findViewById(R.id.ordertotal);
			drawerHolder.orderStatus = (TextView) view.findViewById(R.id.orderstatus);
			drawerHolder.orderDate = (TextView) view.findViewById(R.id.orderdate);
				view.setTag(drawerHolder);

		}
		else
		{
			drawerHolder = (DrawerItemHolder) view.getTag();

		}
	/*	if(position % 2 == 0){
			view.setBackgroundColor(Color.rgb(238, 233, 233));
		}*/
		final OrderItem dItem = (OrderItem) this.drawerItemList.get(position);
		DecimalFormat df = new DecimalFormat("0.00##");		
		drawerHolder.orderNo.setText("Order No"+" :"+dItem.getOrderId()); 
		drawerHolder.orderDate.setText("Order Date"+" :"+dItem.getOrdereDateNTime()); 
		drawerHolder.orderTotal.setText("Order TotalPrice"+" :"+"$" +df.format(dItem.getOrderPrice()));
		drawerHolder.orderStatus.setText("Payment Status"+" :"+"Processing"); 
		return view;
	}

	private static class DrawerItemHolder
	{
		TextView orderNo,orderTotal, orderDate,orderStatus,perprice;
		
	}

	}