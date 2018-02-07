package com.BasicApp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.Category;
import com.basicmodule.sdk.R;

import com.BasicApp.BusinessLogic.Models.Product;

import java.util.ArrayList;

public class MenuDrawerAdapterNew extends BaseAdapter{
	
	ArrayList<Category> categories;

	Context context;
	
	Category category; 
    	
	private static class ProductHolder {
		TextView productName,productDesc,productCost;
 
	}
	
	
	public MenuDrawerAdapterNew(Context _context,Category _category ){
		context=_context;
		category=_category; 
	}
 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return category.products.size();
	} 
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProductHolder drawerHolder;
		View view = convertView;
		 Product product=  product= category.products.get(position);
	 
		
		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new ProductHolder();
			view = inflater.inflate(R.layout.headerlist, parent, false);
			convertView = view;
			drawerHolder.productName = (TextView) view.findViewById(R.id.productName);
			drawerHolder.productDesc = (TextView) view.findViewById(R.id.productDesc);	 
			drawerHolder.productCost=(TextView)view.findViewById(R.id.productCost);
			view.setTag(drawerHolder);

		} else {
			drawerHolder = (ProductHolder) view.getTag();  
		}     
		 
			 drawerHolder.productName.setText(product.iname);
			 drawerHolder.productDesc.setText(product.idesc);
			 drawerHolder.productCost.setText("$"+product.icost);   
	 
		
		return view;
	}

	public Filter getFilter(){
		Filter filter=null;
		return filter;
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 1;
	}


}
