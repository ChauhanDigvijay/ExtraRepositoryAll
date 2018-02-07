package com.fishbowl.fbtemplate1.fragment;

/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
import java.util.ArrayList;
import java.util.HashMap;
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
import com.fishbowl.fbtemplate1.Adapter.MenuImageAdapter;
import com.fishbowl.fbtemplate1.Model.MenuImageItem;
import com.fishbowl.fbtemplate1.widget.TransparentProgressDialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ImageMenu_Fragment extends Fragment {
	public static ListItemSelectedListener itemListerner;
	MenuImageAdapter adapter;
	TransparentProgressDialog	pd;
	public View v;
	ListView lv;
	List<HashMap<String,String>> aList ;
	int[] flags = new int[]{
			R.drawable.pizza,
			R.drawable.take,
			R.drawable.sal,
			R.drawable.sand,
			R.drawable.shareable,
			R.drawable.rus,

	};
	public static List<MenuImageItem> dataList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		itemListerner = (ListItemSelectedListener)getActivity();	
		inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.fragment_menuimage, null, false);
		pd = new TransparentProgressDialog(getActivity());
		loadNewItemsCount();
		return v;

	}

	public void loadNewItemsCount() 
	{


		try{
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

								if(response.has("categories")) 
								{

									JSONArray	jsonArray	 = response.getJSONArray("categories");

									if(jsonArray != null && jsonArray.length() > 0)
									{
										dataList = new ArrayList<MenuImageItem>();


										for( int i=0; i<jsonArray.length(); i++)
										{
											JSONObject	wallObj = jsonArray.getJSONObject(i);
											if(wallObj != null)
											{
												MenuImageItem	item = new MenuImageItem(wallObj.getString("imageurl"),wallObj.getString("category"),0);
												dataList.add(item);

											}

											if(dataList!=null)
											{

												if(dataList.size()>0)
												{

													adapter = new MenuImageAdapter(getActivity(), R.layout.fragment_menu_image,
															dataList);

													lv=(ListView)v.findViewById(R.id.image_list);

													lv.setAdapter(adapter);

												

													lv.setOnItemClickListener(new OnItemClickListener() {
														@Override
														public void onItemClick(AdapterView<?> parent, View view, int position,
																long id) {
															itemListerner.listItemSelectedListener(position);
														}
													});
													
													lv.setOnScrollListener(new OnScrollListener(){
													    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
													      // TODO Auto-generated method stub
													  //  	itemListerner.listItemSelectedListener(firstVisibleItem);
													    	
													    	
													    }
													    public void onScrollStateChanged(AbsListView view, int scrollState) {
													      // TODO Auto-generated method stub
													     


													        if (view.getId() == lv.getId()) {
													        final int currentFirstVisibleItem = lv.getFirstVisiblePosition();
													        itemListerner.listItemSelectedListener(currentFirstVisibleItem);
													    	 
													    } 
													    }
													  });
												}
											}
										}




									}

								}
								pd.dismiss();
							}


							catch(Exception ex){
								ex.printStackTrace();

							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
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



	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);

	}


	public interface ListItemSelectedListener
	{

		public void listItemSelectedListener(int pos);
	}


	



}