package com.fishbowl.fbtemplate2;


import java.io.IOException;
import java.io.InputStream;
/**
 * Created by Digvijay Chauhan on 7/12/15.
 */
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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;


public class Description_Fragment extends Fragment implements
OnItemClickListener {



	Activity thisActivity;
	LayoutInflater inflater;

	private ViewGroup parentContainer = null;
	public static final String[] titles = new String[] { "Pizzas",
			"Taken Bake", "Rustica Flatbreads", "Salads","Sandwiches","Pastas","Shareables"};


	public static final Integer[] images = { R.drawable.menu_pizza,R.drawable.menu_takebake, R.drawable.menurustica_flatbreads, R.drawable.menu_salads,R.drawable.menu_sandwiches,R.drawable.menu_pastas,R.drawable.menu_shareables,R.drawable.menu_pizza};

	ListView listView;
	List<DescriptionItem> rowItems;

	/** Called when the activity is first created. */




	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = inflater.inflate(
				R.layout.fragment_description, container, false);
		this.inflater = inflater;
		rootView.setTag("USER_TAGS_TAB");


		parentContainer = container;
		thisActivity = getActivity();
		setHasOptionsMenu(true);

		/*   InputStream stream = null;
	    try {
	        stream = getActivity().getAssets().open("loadingpizza.gif");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	  llProgress = (LinearLayout)rootView. findViewById(R.id.ll_progress);

	  view = new GifWebView(getActivity(), "file:///android_asset/loadingpizza.gif");*/


		return rootView;
	}	






	public void loadNewItemsCount() {


		try{

			RequestQueue queue = Volley.newRequestQueue(thisActivity);

			JSONObject userJSON = new JSONObject();

			//			userJSON.put("User-mobile", ((OyeApp)appContext).getCurrentUser().getMobile());
			JSONObject requestObj = new JSONObject();
			requestObj.put("data", userJSON);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.GET,"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/menu", requestObj,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try{

								if(response.has("categories")) {

									JSONArray jsonArray = response.getJSONArray("categories");

									if(jsonArray != null && jsonArray.length() > 0)
									{
										rowItems = new ArrayList<DescriptionItem>();
										for(int i=0; i<jsonArray.length(); i++)
										{
											JSONObject wallObj = jsonArray.getJSONObject(i);
											if(wallObj != null)
											{



												DescriptionItem item = new DescriptionItem(images[i], wallObj.getString("category"));
												rowItems.add(item);
											}

											listView = (ListView)thisActivity. findViewById(R.id.list);
											DescriptionListAdapter adapter = new DescriptionListAdapter(thisActivity,
													R.layout.list_item, rowItems);
											listView.setAdapter(adapter);
											adapter.notifyDataSetChanged();
											//		listView.setOnItemClickListener(thisActivity);


											listView.setOnItemClickListener(new OnItemClickListener() {
												@Override
												public void onItemClick(AdapterView<?> parent, View view, int position,
														long id) {

													DetailItem_Fragment chatDetailFragment = new DetailItem_Fragment();


													Bundle extras = new Bundle();
													extras.putInt("position", position);


													chatDetailFragment.setArguments(extras);
													FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
													fragTransaction.hide(Description_Fragment.this);
													fragTransaction.replace(R.id.main_container,chatDetailFragment );
													fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
													fragTransaction.addToBackStack(null);
													fragTransaction.commit();
												}
											});


										}	
									}}
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
		} catch (Exception ex){
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}




	}




	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadNewItemsCount();


	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {


		DetailItem_Fragment chatDetailFragment = new DetailItem_Fragment();


		Bundle extras = new Bundle();
		extras.putInt("position", position);


		chatDetailFragment.setArguments(extras);
		FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
		fragTransaction.hide(this);
		fragTransaction.replace(R.id.main_container,chatDetailFragment );
		fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTransaction.addToBackStack(null);
		fragTransaction.commit();




	}
}
