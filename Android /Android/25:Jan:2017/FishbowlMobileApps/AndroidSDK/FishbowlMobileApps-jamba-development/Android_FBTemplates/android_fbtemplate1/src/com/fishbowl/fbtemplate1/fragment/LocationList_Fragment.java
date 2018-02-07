package com.fishbowl.fbtemplate1.fragment;
import java.io.IOException;
import java.io.InputStream;
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
/**
 **
 * Created by Digvijay Chauhan on 14/12/15.
 */
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Adapter.LocationAdapter;
import com.fishbowl.fbtemplate1.Controller.FB_DBLocation;
import com.fishbowl.fbtemplate1.Controller.FB_DBOrderConfirm;
import com.fishbowl.fbtemplate1.CoreActivity.FishbowlTemplate1App;
import com.fishbowl.fbtemplate1.Model.LocationItem;
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;
import com.fishbowl.fbtemplate1.Model.OrderItem;
import com.fishbowl.fbtemplate1.activity.TestActivity;
import com.fishbowl.fbtemplate1.widget.GifWebView;
import com.fishbowl.fbtemplate1.widget.TransparentProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

public class LocationList_Fragment extends Fragment implements OnClickListener, SearchView.OnQueryTextListener 
{


	Activity thisActivity;
	LayoutInflater inflater;
	private ViewGroup parentContainer = null;
	public ViewPager mPager;
	ActionBar mActionbar;
	GifWebView view;
	String url="http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/store/";
	ListView listView ;
	private SearchView mSearchView;
	List<LocationItem> lists;
	public static List<LocationItem> dataList;
	LocationAdapter adapter;
	String[] mobileArray = {"E-GIFT","LOCATE A STORE","MENU","EXPLORE","OFFERS"," NUTRITIONS CALCULATORS"};
	private final int SPLASH_DISPLAY_LENGTH = 5000;
	EditText	searcheditext;
	Button findbtn;
	LinearLayout  llProgress;
	TransparentProgressDialog	pd;
	FishbowlTemplate1App fishTApp;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
	{

		View rootView = inflater.inflate(R.layout.fragment_locationlist, container, false);		
		this.inflater = inflater;
			fishTApp=FishbowlTemplate1App.getInstance();
		rootView.setTag("USER_TAGS_TAB");
		parentContainer = container;
		thisActivity = getActivity();
		setHasOptionsMenu(true);
		InputStream stream = null;
		try 
		{
			stream = getActivity().getAssets().open("loadingpizza.gif");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		llProgress = (LinearLayout)rootView. findViewById(R.id.ll_progress);
		view = new GifWebView(getActivity(), "file:///android_asset/loadingpizza.gif");
		pd = new TransparentProgressDialog(getActivity());
		searcheditext	= ((EditText)rootView.findViewById(R.id.search_view));
		findbtn	= ((Button)rootView.findViewById(R.id.find_btn));
		findbtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)

			{

				loadNewItemsCount(url+searcheditext.getText().toString()); 
			}
		});

		listView = (ListView)rootView.findViewById(R.id.store_list);

		return rootView;
	}	

	private void setupSearchView() 
	{
		mSearchView.setIconifiedByDefault(false);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setSubmitButtonEnabled(true); 
		mSearchView.setQueryHint("Search City or Zip");
	}


	public boolean onQueryTextChange(String newText) 
	{

		if (TextUtils.isEmpty(newText)) 
		{
			listView.clearTextFilter();
		} else
		{
			loadNewItemsCount(url+newText);	

		}
		return false;
	}

	public boolean onQueryTextSubmit(String query)
	{

		return false;
	}

	public void loadNewItemsCount(String url) 
	{
		try
		{
			pd.show();
			RequestQueue queue = Volley.newRequestQueue(getActivity());
			JSONObject userJSON = new JSONObject();
			JSONObject requestObj = new JSONObject();
			requestObj.put("data", userJSON);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.GET,url, requestObj,
					new Response.Listener<JSONObject>() 
					{
						@Override
						public void onResponse(JSONObject response)
						{

							System.out.println("updatefield response: " );

							try
							{

								pd.dismiss();
								if(response.has("categories")) 
								{

									JSONArray	jsonArray	 = response.getJSONArray("categories");

									if(jsonArray != null && jsonArray.length() > 0)
									{
										dataList = new ArrayList<LocationItem>();

										for( int i=0; i<jsonArray.length(); i++)
										{
											JSONObject	wallObj = jsonArray.getJSONObject(i);
											if(wallObj != null)
											{
												LocationItem lt=new LocationItem();

												lt.setStoreID((wallObj.getString("storeID")));
												lt.setName(wallObj.getString("name"));
												lt.setAddress(wallObj.getString("address"));
												lt.setState((wallObj.getString("state")));
												lt.setCity(wallObj.getString("city"));
												lt.setZipcode(wallObj.getString("zipcode"));
												lt.setPhone(wallObj.getString("phone"));
												dataList.add(lt);

											}


											if(dataList!=null)
											{

												if(dataList.size()>0)
												{

													adapter = new LocationAdapter(getActivity(),R.layout.list_location,dataList);
													listView.setAdapter(adapter);
													listView.setOnItemClickListener(new OnItemClickListener() 
													{
														@Override
														public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
														{
															Intent intent = new Intent(thisActivity,TestActivity.class);			
															Bundle extras = new Bundle();
															if( dataList!=null&& !fishTApp.getStoreActivity().signiin)	
															{
																LocationItem location=	dataList.get(position);
																FB_DBLocation.getInstance().createUpdateLocation(location);
																OrderItem order=  new OrderItem();
																final List<MenuDrawerItem> lists =FB_DBOrderConfirm.getInstance().getAllItem();
																order.setStoreID(location.getStoreID());
																order.setItemlist(lists);														
																extras.putSerializable("draweritem1", (Serializable)lists);
																extras.putSerializable("order", order);
																extras.putSerializable("historyflag", true);
																extras.putSerializable("storelocation", location.getAddress());
																intent.putExtras(extras);
																startActivityForResult(intent, 2);

															}


														
														}
													});
												}
											}
										}

									}
									else
									{
										listView.clearChoices();
									}

								}

							}

							catch(Exception ex)
							{
								ex.printStackTrace();

							}
						}
					}, new Response.ErrorListener()
					{
						@Override
						public void onErrorResponse(VolleyError error) 
						{
							if(error != null){

								pd.dismiss();
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
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);

	}
	public void onClick(View v)
	{
		

	}
}
