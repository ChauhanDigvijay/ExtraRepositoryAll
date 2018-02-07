package com.fishbowl.fbtemplate2;


import java.io.Serializable;
/**
 * Created by Digvijay Chauhan on 7/12/15.
 */
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DetailItem_Fragment extends Fragment implements
		OnItemClickListener {
	public static List<DetailItem> dataList1;
	private ViewGroup parentContainer = null;
Activity thisActivity;
LayoutInflater inflater;	

	ListView listView;
	List<DetailItem> rowItems;

	int position;
	
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = inflater.inflate(
				R.layout.fragment_detail, container, false);
		this.inflater = inflater;
		rootView.setTag("USER_TAGS_TAB");
		parentContainer = container;
		thisActivity = getActivity();
		setHasOptionsMenu(true);
		return rootView;
	}	
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Bundle extras = getArguments();
		if (extras != null) {
			position =  extras.getInt("position");

		}
		
		initiation();

		listView = (ListView) thisActivity.findViewById(R.id.listtt);
		DetailListAdapter adapter = new DetailListAdapter(thisActivity,
				R.layout.list_item, rowItems);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		}
	
	public void initiation()
	{
		
		
		
		switch (position){  
		  case 0:
			

			  String[] descriptions = new String[] {
						"Red Onions,Bell Paper,Button,Tomatoes,Tomato Saurce",
						"Bell Paper,Button,Red Onions,Tomatoes,Tomato Saurce", "Button,Tomatoes,Tomato Saurce","Red Onions,Bell Paper,Red Onions,Bell Paper,Button,Tomatoes,Tomato Saurce",
						"Tomato Saurce,Red Onions,Bell Paper,Button,Tomatoes," };
				
				
			 String[] titles = new String[] { "Amercian Pizzas",
						"Cheese Pizzas", "Greek Pizzas", "Italian Pizzas","Mexican Pizzas" };

			  Integer[] images  = { R.drawable.pizza,
						R.drawable.pizza, R.drawable.pizza, R.drawable.pizza ,R.drawable.pizza};

			  Double[] price  = { 10.00,
					  10.00, 10.00,10.00 ,10.00};

			  
				rowItems = new ArrayList<DetailItem>();
				for (int i = 0; i < titles.length; i++) {
					DetailItem item = new DetailItem(images[i], titles[i], descriptions[i],price[i]);
					rowItems.add(item);
				}
		 break;
		  case 1:
			  
			  

				 String[] descriptions1 = new String[] {
						"Red Onions,Bell Paper,Button,Tomatoes,Tomato Saurce",
						"Bell Paper,Button,Red Onions,Tomatoes,Tomato Saurce", "Button,Tomatoes,Tomato Saurce","Red Onions,Bell Paper,Red Onions,Bell Paper,Button,Tomatoes,Tomato Saurce",
						"Tomato Saurce,Red Onions,Bell Paper,Button,Tomatoes," };
				
				
			 String[] titles1 = new String[] { "Amercian Take and Bake",
						"Cheese Take and Bake", "Italian Take and Bake", "Mexican Take and Bake","Napoli Take and Bake" };

			  Integer[] images1  = { R.drawable.take,
						R.drawable.take, R.drawable.take, R.drawable.take ,R.drawable.take};
			  
			  Double[] price1  = { 6.00,
					  6.00, 6.00,6.00 ,6.00};

				rowItems = new ArrayList<DetailItem>();
				for (int i = 0; i < titles1.length; i++) {
					DetailItem item = new DetailItem(images1[i], titles1[i], descriptions1[i],price1[i]);
					rowItems.add(item);
				}
		 // System.out.println("Enter the number two=" + (x-y));
		  break;
		  case 2:
		 // System.out.println("Enetr the number three="+ (x*y));
			  

			  String[] descriptions2 = new String[] {
						"Red Onions,Bell Paper,Button,Tomatoes,Tomato Saurce",
						"Bell Paper,Button,Red Onions,Tomatoes,Tomato Saurce", "Button,Tomatoes,Tomato Saurce","Red Onions,Bell Paper,Red Onions,Bell Paper,Button,Tomatoes,Tomato Saurce",
						"Tomato Saurce,Red Onions,Bell Paper,Button,Tomatoes," };
				
				
			 String[] titles2 = new String[] { "Amercian Take and Bake",
						"Cheese Take and Bake", "Italian Take and Bake", "Mexican Take and Bake","Napoli Take and Bake" };

			  Integer[] images2  = { R.drawable.rus,
						R.drawable.rus, R.drawable.rus, R.drawable.rus ,R.drawable.rus};

			  
			  Double[] price2  = { 3.00,
					  3.00, 3.00,3.00 ,3.00};
				rowItems = new ArrayList<DetailItem>();
				for (int i = 0; i < titles2.length; i++) {
					DetailItem item = new DetailItem(images2[i], titles2[i], descriptions2[i],price2[i]);
					rowItems.add(item);
				}
		  break;
		  case 3:
		//  System.out.println("Enter the number four="+ (x/y));
			  String[] descriptions3 = new String[] {
						"Red Onions,Bell Paper,Button,Tomatoes,Tomato Saurce",
						"Bell Paper,Button,Red Onions,Tomatoes,Tomato Saurce", "Button,Tomatoes,Tomato Saurce","Red Onions,Bell Paper,Red Onions,Bell Paper,Button,Tomatoes,Tomato Saurce",
						"Tomato Saurce,Red Onions,Bell Paper,Button,Tomatoes," };
					
				 String[] titles3 = new String[] { "Arugula Salad",
							"Chicken Caesar Salad", "Greek Salad", "Pear and Gorgonzola Salad","ZBQ Salad" };

				  Integer[] images3  = { R.drawable.sal,
							R.drawable.sal, R.drawable.sal, R.drawable.sal ,R.drawable.sal};

				  
				  Double[] price3  = { 4.00,
						  4.00, 4.00,4.00 ,4.00};
					rowItems = new ArrayList<DetailItem>();
					for (int i = 0; i < titles3.length; i++) {
						DetailItem item = new DetailItem(images3[i], titles3[i], descriptions3[i],price3[i]);
						rowItems.add(item);
					}
			  
			  
		  break;
		  case 4:
				//  System.out.println("Enter the number four="+ (x/y));
					  
				
			  String[] descriptions4 = new String[] {
						"Red Onions,Bell Paper,Button,Tomatoes,Tomato Saurce",
						 "Red Onions,Bell Paper,Red Onions,Bell Paper,Button,Tomatoes,Tomato Saurce",
						"Tomato Saurce,Red Onions,Bell Paper,Button,Tomatoes," };
				
				
			 String[] titles4 = new String[] { "SuperSub Sandwiche",
						"Tuna Sandwiche", "Turkey Sandwiche" };

			  Integer[] images4  = { R.drawable.sand,
						R.drawable.sand, R.drawable.sand, };

			  Double[] price4  = { 7.00,
					  7.00, 7.00};
			  
				rowItems = new ArrayList<DetailItem>();
				for (int i = 0; i < titles4.length; i++) {
					DetailItem item = new DetailItem(images4[i], titles4[i], descriptions4[i],price4[i]);
					rowItems.add(item);
				}
					  
				  break;
		  case 5:
				//  System.out.println("Enter the number four="+ (x/y));
					  
					
			  
				
			  String[] descriptions5 = new String[] {
				
						 "Red Onions,Bell Paper,Red Onions,Bell Paper,Button,Tomatoes,Tomato Saurce",
						"Tomato Saurce,Red Onions,Bell Paper,Button,Tomatoes," };
				
				
			 String[] titles5 = new String[] { "Chicken Penne Pesto Pastas",
						"Penne with Meatballs Pastas" };

			  Integer[] images5  = { R.drawable.rus,
						R.drawable.sharaable};

			  Double[] price5  = { 6.00,
					  6.00};
				rowItems = new ArrayList<DetailItem>();
				for (int i = 0; i < titles5.length; i++) {
					DetailItem item = new DetailItem(images5[i], titles5[i], descriptions5[i],price5[i]);
					rowItems.add(item);
				}
					  
				  break;
		  case 6:
				//  System.out.println("Enter the number four="+ (x/y));
					  
			  
				
			  String[] descriptions6 = new String[] {
						"Red Onions,Bell Paper,Button,Tomatoes,Tomato Saurce",
						
						"Tomato Saurce,Red Onions,Bell Paper,Button,Tomatoes," };
				
				
				
			 String[] titles6 = new String[] { "Garlic Bread Shareables",
						"zWings Shareables" };

			  Integer[] images6  = { R.drawable.sharaable,
						R.drawable.sharaable};
			  
			  Double[] price6  = { 5.00,
					  5.00};

				rowItems = new ArrayList<DetailItem>();
				for (int i = 0; i < titles6.length; i++) {
					DetailItem item = new DetailItem(images6[i], titles6[i], descriptions6[i],price6[i]);
					rowItems.add(item);
				}  
					  
				  break;
		  default:
		  System.out.println("Invalid Entry!");
		  }
	}
	
	
	
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	
		
		OrderConfirm_Fragment chatDetailFragment = new OrderConfirm_Fragment();
		
		DetailItem dItem = (DetailItem) rowItems.get(position);	
	//	Intent intent = new Intent();
		Bundle extras = new Bundle();
		extras.putInt("position", position);
		
	
		if(dataList1==null)	
		{
			dataList1 = new ArrayList<DetailItem>();
			dataList1.add(dItem);
			extras.putSerializable("draweritem1", (Serializable)dataList1);
			extras.putSerializable("draweritem", dItem);
		}
		else
		{
			dataList1.add(dItem);
			extras.putSerializable("draweritem1", (Serializable)dataList1);	
			extras.putSerializable("draweritem", dItem);
		}

	//	intent.putExtras(extras);
		
		chatDetailFragment.setArguments(extras);
		FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();

        fragTransaction.hide(this);
        fragTransaction.replace(R.id.main_container,chatDetailFragment );
        fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragTransaction.addToBackStack(null);
        fragTransaction.commit();
	}
}
