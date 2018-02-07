package com.BasicApp.Fragments;

/**
 **
 * Created by Digvijay Chauhan
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.BasicApp.Activites.NonGeneric.Home.DashboardModelActivity;
import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.MenuActivity;
import com.BasicApp.BusinessLogic.Models.Category;
import com.BasicApp.BusinessLogic.Models.MenuDrawerItem;
import com.BasicApp.BusinessLogic.Models.Product;
import com.BasicApp.BusinessLogic.Models.ProductList;
import com.BasicApp.ModelAdapters.MenuDrawerAdapterNew;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.GifWebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailMenu_Fragment extends Fragment {
	public static List<MenuDrawerItem> dataList;
	public static List<MenuDrawerItem> dataList1;
	public static MenuDrawerAdapterNew adapter;
	public View v;
	//TransparentProgressDialog pd;
	public static ListView lv;
	JSONObject wallObj;
	JSONObject wallOb1;
	Activity ref;
	JSONArray jsonArray;
	JSONArray jsonArray1;
	MenuDrawerItem item;
	LinearLayout llProgress;
	GifWebView view;
	private ViewGroup parentContainer = null;
	Activity thisActivity;
	LayoutInflater inflater;
	ListView listView;
	NetworkImageView networkImageView;
	ImageView backward;
	TextView catName;

	int catposition;
	private NetworkImageView topImage;
	private ImageLoader mImageLoader;
	public static Boolean checkback = true;
	ArrayList<Category> categories;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_menudetail, container, false);
		networkImageView = (NetworkImageView) v.findViewById(R.id.menu_img);
		mImageLoader = CustomVolleyRequestQueue.getInstance(this.getActivity())
				.getImageLoader();
		Bundle bundle = getArguments();
		catposition = bundle.getInt("position");

		topImage=(NetworkImageView)v.findViewById(R.id.menu_img);

		catName=(TextView)v.findViewById(R.id.categoryName);


		/*
		 * backward.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) {
		 *
		 *
		 * } });
		 */

		this.inflater = inflater;
		v.setTag("USER_TAGS_TAB");
		parentContainer = container;
		thisActivity = getActivity();
		setHasOptionsMenu(true);
	//	pd = new TransparentProgressDialog(getActivity());

		return v;
	}

	public void onCustomBackPressed() {

		getFragmentManager().popBackStack();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// Instantiate the RequestQueue.

		mImageLoader = CustomVolleyRequestQueue.getInstance(getActivity()).getImageLoader();
		//Image URL - This can point to any image file supported by Android

		loadNewItemsCount();

	}



	public void setHeaderImage(String url){

	//	ImageLoader imageLoader = FishbowlTemplate1App.getInstance().getImageLoader();
		mImageLoader.get(url, ImageLoader.getImageListener(topImage,R.drawable.ic_launcher, android.R.drawable.ic_dialog_alert));
		topImage.setImageUrl(url,mImageLoader);

		// mImageLoader.get(url, ImageLoader.getImageListener(topImage,R.drawable.headerone, android.R.drawable.ic_dialog_alert));
		//topImage.setImageUrl(url, mImageLoader);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}


	public void loadNewItemsCount() {

		categories=new  ArrayList<Category>();
		final Category category= ProductList.sharedInstance().categories.get(catposition);
		catName.setText(category.category);
		getActionBar().setTitle(category.category.toUpperCase());
		getActionBar().setTitle((Html.fromHtml("<font color=\"#000\">" + category.category.toUpperCase() + "</font>")));



		setHeaderImage(category.imageurl);

		adapter = new MenuDrawerAdapterNew(getActivity(),category);
		lv = (ListView) v.findViewById(R.id.detail_list);
		lv.setAdapter(adapter);



		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent,View view,int position,long id) {

			//	Intent intent = new Intent(getActivity(),ModifierModelctivity.class);

				Intent intent = new Intent(getActivity(),DashboardModelActivity.class);

				Product product =category.products.get(position);

				MenuDrawerItem	item = new MenuDrawerItem(product.iname,R.drawable.plussign,Integer.valueOf(product.itemid),10.0, product.idesc, product.imageurl);
				Bundle extras = new Bundle();

				extras.putInt("ProductPosition", position);
				extras.putInt("CategoryPosition", catposition);
				extras.putBoolean("checkback",checkback);

				//OrderProductList.sharedInstance().setCurrentAdded(item);

				intent.putExtras(extras);
				startActivityForResult(intent,2);


			}
		});


	}
	public ActionBar getActionBar() {
		return ((MenuActivity) getActivity()).getSupportActionBar();
	}


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// check if the request code is same as what is passed here it is 2
		if (requestCode == 2 && resultCode == 2) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				dataList1 = null;
				this.checkback = true;
			}
		} else {
			// checkback=false;
			dataList1 = null;
		}

	}




	public void getItemValue(int pos, Activity ref) {
		this.ref = ref;

		int finalpos = 0;
		int i;
		for (i = 0; i < dataList.size(); i++) {
			MenuDrawerItem dItem = dataList.get(i);

			int comp = dItem.getImgResID();
			if (comp == pos) {
				finalpos = i;
				break;
			}
		}

		lv.setSelection(finalpos);

		lv.smoothScrollToPosition(finalpos);

		finalpos = 0;
	}
}