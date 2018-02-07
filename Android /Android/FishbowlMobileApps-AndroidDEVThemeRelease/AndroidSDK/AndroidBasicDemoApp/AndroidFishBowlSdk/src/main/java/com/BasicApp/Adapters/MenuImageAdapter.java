package com.BasicApp.Adapters;

/**
 **
 * Created by Digvijay Chauhan on 11/12/15.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.Category;
import com.BasicApp.BusinessLogic.Models.MenuImageItem;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.basicmodule.sdk.R;
import com.BasicApp.BusinessLogic.Models.ProductList;

import java.util.List;

public class MenuImageAdapter extends ArrayAdapter<MenuImageItem>
{

	Context context;
	List<MenuImageItem> drawerItemList;
	int layoutResID;
	public static int position = 0;
	private ImageLoader mImageLoader;
	public MenuImageAdapter(Activity context, int layoutResourceID,List<MenuImageItem> listItems)
	{
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;
		mImageLoader = CustomVolleyRequestQueue.getInstance(context)
				.getImageLoader();

	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ProductList.sharedInstance().categories.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		this.position=position;
		
		Category cat= ProductList.sharedInstance().categories.get(position);
		
		final DrawerItemHolder drawerHolder;
		View view = convertView;

		if (view == null) 
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();
			view = inflater.inflate(layoutResID, parent, false);			
			drawerHolder.icon =  (NetworkImageView) view.findViewById(R.id.menu_img);
			drawerHolder.catNumber=(TextView)view.findViewById(R.id.categoryNumber);
			drawerHolder.catText=(TextView)view.findViewById(R.id.categoryName);
			//drawerHolder.title = (TextView) view.findViewById(R.id.imageheader_textview);
		//	drawerHolder.title1 = (TextView) view.findViewById(R.id.imageheadba);
			view.setTag(drawerHolder);
		} 
		else 
		{
			drawerHolder = (DrawerItemHolder) view.getTag();

		}
		
		   ImageView   imageViewRound=(ImageView)view.findViewById(R.id.imageView_round);
		   Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.whitbackground);
		   imageViewRound.setImageBitmap(icon);

		/*	if(position % 2 == 0){
			view.setBackgroundColor(Color.rgb(238, 233, 233));
		}
		 */
		final MenuImageItem dItem = (MenuImageItem) this.drawerItemList.get(position);
		drawerHolder.icon.setImageUrl(dItem.getTitle(),mImageLoader);
		drawerHolder.catNumber.setText(String.valueOf(cat.products.size()));
		drawerHolder.catText.setText(cat.category);
		//drawerHolder.title.setText(dItem.getItemName().toUpperCase());
		//drawerHolder.title1.setText("Button,Mushroom,Tomatoes,Mozzarella,Red Onions,Bell,Peppers");
		return view;
	}

	private static class DrawerItemHolder
	{
		TextView catText,catNumber;
		NetworkImageView icon; 

	}
}