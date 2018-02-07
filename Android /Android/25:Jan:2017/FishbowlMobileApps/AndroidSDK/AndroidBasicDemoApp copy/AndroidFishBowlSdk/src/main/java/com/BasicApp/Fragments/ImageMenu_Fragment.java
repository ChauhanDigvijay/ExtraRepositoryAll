package com.BasicApp.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.MenuActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Models.Category;
import com.BasicApp.BusinessLogic.Models.MenuImageItem;
import com.BasicApp.BusinessLogic.Models.ProductList;
import com.BasicApp.BusinessLogic.Models.ProductList.ProductListCallBack;
import com.BasicApp.ModelAdapters.MenuImageAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageMenu_Fragment extends Fragment {

	MenuImageAdapter adapter;
	//TransparentProgressDialog	pd;
	public View v;
	ListView lv;
	ImageView imageViewRound;
	List<HashMap<String,String>> aList ;
	public static List<MenuImageItem> dataList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.fragment_menuimage, null, false);
	//	pd = new TransparentProgressDialog(getActivity());



		getActionBar().setTitle("MENU");
		getActionBar().setTitle((Html.fromHtml("<font color=\"#000\">" +"MENU"  + "</font>")));


	//	pd = new TransparentProgressDialog(getActivity());
		loadNewItemsCount();
		return v;
	}

	public ActionBar getActionBar() {
		return ((MenuActivity) getActivity()).getSupportActionBar();
	}

	public void fetchCategory() {
		if(ProductList.sharedInstance().isDownloadable==false){


			ProductList.sharedInstance().getCategory(getActivity(),new ProductListCallBack() {
				@Override
				public void onProductListCallback(final ArrayList<Category> _categories) {
					//pd.dismiss();
					adapter.notifyDataSetChanged();

				}
			});
		}else{
		//	pd.dismiss();
		}
	}


	public void loadNewItemsCount()
	{

		fetchCategory(); //add by vaseem

		try{
		//	pd.show();



			RequestQueue queue = Volley.newRequestQueue(getActivity());

			JSONObject userJSON = new JSONObject();

			JSONObject requestObj = new JSONObject();
			requestObj.put("data", userJSON);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.GET,"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/menu/104", requestObj,
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
											if(wallObj != null){

												MenuImageItem	item = new MenuImageItem(wallObj.getString("imageurl"),wallObj.getString("category"),wallObj.getString("categorydesc"),0);
												dataList.add(item);
											}

											if(dataList!=null)
											{
												if(dataList.size()>0)
												{
													adapter = new MenuImageAdapter(getActivity(), R.layout.list_menuimage,
															dataList);

													lv=(ListView)v.findViewById(R.id.image_list);

													lv.setAdapter(adapter);
													lv.setOnItemClickListener(new OnItemClickListener() {
														@Override
														public void onItemClick(AdapterView<?> parent, View view, int position,
																				long id) {
															//itemListerner.listItemSelectedListener(position);
															SubCategoryFragment chatDetailFragment = new SubCategoryFragment();

															Bundle extras = new Bundle();
															extras.putInt("position", position);
															chatDetailFragment.setArguments(extras);
															FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
															fragTransaction.hide(ImageMenu_Fragment.this);
															fragTransaction.add(R.id.ll_progress1,chatDetailFragment );
															FBAnalyticsManager.sharedInstance().track_ItemWith("",dataList.get(position).getItemName(), FBEventSettings.CATEGORY_CLICK);
															//	fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
															fragTransaction.addToBackStack(null);
															fragTransaction.commit();
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
																//    itemListerner.listItemSelectedListener(currentFirstVisibleItem);

															}
														}
													});
												}
											}
										}




									}

								}
							//	pd.dismiss();

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