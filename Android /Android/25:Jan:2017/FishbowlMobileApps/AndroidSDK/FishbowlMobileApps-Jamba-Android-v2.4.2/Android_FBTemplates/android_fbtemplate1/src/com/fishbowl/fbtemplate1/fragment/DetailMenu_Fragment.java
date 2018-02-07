package com.fishbowl.fbtemplate1.fragment;


import java.io.IOException;
import java.io.InputStream;
/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Adapter.MenuDrawerAdapter;
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;
import com.fishbowl.fbtemplate1.activity.TestActivity2;
import com.fishbowl.fbtemplate1.widget.GifWebView;
import com.fishbowl.fbtemplate1.widget.TransparentProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DetailMenu_Fragment extends Fragment {
	public static List<MenuDrawerItem> dataList;
	public static List<MenuDrawerItem> dataList1;
	public	static MenuDrawerAdapter adapter;
	public View v;
	TransparentProgressDialog	pd;
	public static ListView lv;	
	JSONObject wallObj;
	JSONObject wallOb1;
	Activity ref; 
	JSONArray jsonArray;
	JSONArray jsonArray1;
	MenuDrawerItem item ;
	LinearLayout llProgress;
	GifWebView	view;
	public static Boolean checkback=false;	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.fragment_menu_detai, null, false);
		InputStream stream = null;
		try 
		{
			stream = getActivity().getAssets().open("loadingpizza.gif");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		pd = new TransparentProgressDialog(getActivity());
		llProgress = (LinearLayout)v. findViewById(R.id.linear);
		view = new GifWebView(getActivity(), "file:///android_asset/loadingpizza.gif");

		loadNewItemsCount();



		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}

	public void loadNewItemsCount() 
	{
		try
		{
			pd.show();
			RequestQueue queue = Volley.newRequestQueue(getActivity());

			JSONObject userJSON = new JSONObject();

			JSONObject requestObj = new JSONObject();
			requestObj.put("data", userJSON);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.GET,"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/menu", requestObj,
					new Response.Listener<JSONObject>()
					{
						@Override
						public void onResponse(JSONObject response)
						{
							try
							{

								int i=0;
								if(response.has("categories"))
								{

									jsonArray	 = response.getJSONArray("categories");

									if(jsonArray != null && jsonArray.length() > 0)
									{
										dataList = new ArrayList<MenuDrawerItem>();


										try
										{

											RequestQueue queue = Volley.newRequestQueue(getActivity());

											JSONObject userJSON = new JSONObject();

											//			userJSON.put("UserItem-mobile", ((OyeApp)appContext).getCurrentUser().getMobile());
											JSONObject requestObj = new JSONObject();
											requestObj.put("data", userJSON);
											JsonObjectRequest jsObjRequest = new JsonObjectRequest(
													Request.Method.GET,"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/menuitems", requestObj,
													new Response.Listener<JSONObject>() 
													{
														@Override
														public void onResponse(JSONObject response) 
														{
															try
															{

																for( int i=0; i<jsonArray.length(); i++)
																{
																	JSONObject	wallObj = jsonArray.getJSONObject(i);
																	if(wallObj != null)
																	{
																		item = new MenuDrawerItem(wallObj.getString("category"),i,0);
																		dataList.add(item);
																		if(response.has("categories")) {

																			JSONArray	jsonArray1 = response.getJSONArray("categories");

																			if(jsonArray1 != null && jsonArray1.length() > 0)
																			{
																				for(int ii=0; ii<jsonArray1.length(); ii++)
																				{
																					JSONObject	wallOb1 = jsonArray1.getJSONObject(ii);
																					if(wallOb1 != null)
																					{
																						if(wallObj.getInt("categoryid")==wallOb1.getInt("categoryID"))
																						{
																							if(wallOb1.has("icost")){
																								item = new MenuDrawerItem(wallOb1.getString("iname"),R.drawable.plussign,wallOb1.getInt("itemid"),Double.parseDouble(wallOb1.getString("icost")),wallOb1.getString("idesc"),wallObj.getString("imageurl"));
																								dataList.add(item);
																							}
																						}
																					}


																				}
																			}
																		}
																	}

																	if(dataList!=null)
																	{

																		if(dataList.size()>0)
																		{
																			adapter = new MenuDrawerAdapter(getActivity(), R.layout.list_menudetailview,
																					dataList);
																			lv=(ListView)v.findViewById(R.id.detail_list);

																			lv.setAdapter(adapter);

																			lv.setOnItemClickListener(new OnItemClickListener() 
																			{
																				@Override
																				public void onItemClick(AdapterView<?> parent, View view, int position,
																						long id) {
																					Intent intent = new Intent(getActivity(),TestActivity2.class);
																					MenuDrawerItem dItem = (MenuDrawerItem) dataList.get(position);	
																					Bundle extras = new Bundle();

																					if(dataList1==null)	
																					{
																						dataList1 = new ArrayList<MenuDrawerItem>();
																						dataList1.add(dItem);
																						extras.putSerializable("draweritem1", (Serializable)dataList1);
																						extras.putSerializable("draweritem", dItem);
																						extras.putBoolean("checkback", checkback);
																					}
																					else
																					{
																						dataList1.add(dItem);
																						extras.putSerializable("draweritem1", (Serializable)dataList1);	
																						extras.putSerializable("draweritem", dItem);
																					}

																					intent.putExtras(extras);
																					startActivityForResult(intent, 2);


																				}
																			});
																		}
																	}
																}
																pd.dismiss();
															}
															catch(Exception ex){
																ex.printStackTrace();

															}
														}
													}, new Response.ErrorListener() 
													{
														@Override
														public void onErrorResponse(VolleyError error) 
														{
															if(error != null)
															{

																error.printStackTrace(System.out);

															}
														}
													});
											queue.add(jsObjRequest);
										} catch (Exception ex){
											System.out.println(ex.getMessage());
											ex.printStackTrace();
										}

									}

								}

							}


							catch(Exception ex){
								ex.printStackTrace();

							}
						}
					}, new Response.ErrorListener()
					{
						@Override
						public void onErrorResponse(VolleyError error)
						{
							if(error != null){

								error.printStackTrace(System.out);

							}
						}
					});
			queue.add(jsObjRequest);
		} 
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

	}




	public void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
		super.onActivityResult(requestCode, resultCode, data);  
		// check if the request code is same as what is passed  here it is 2  
		if(requestCode==2 && resultCode==2)  
		{  
			Bundle extras = data.getExtras();
			if (extras != null) 
			{
				dataList1=null;
				this.checkback=true;
			}  
		}
		else
		{

			//	checkback=false;
			dataList1=null;
		}

	}





	public void getItemValue(int pos,Activity ref)
	{
		this.ref=ref;

		int finalpos = 0;
		int i;
		for ( i=0; i<dataList.size(); i++) 
		{
			MenuDrawerItem dItem =dataList.get(i);

			int comp=dItem.getImgResID();
			if(comp==pos)
			{
				finalpos=i;
				break;	
			}
		}

		lv.setSelection(finalpos);

		lv.smoothScrollToPosition(finalpos);

		finalpos=0;
	}



}