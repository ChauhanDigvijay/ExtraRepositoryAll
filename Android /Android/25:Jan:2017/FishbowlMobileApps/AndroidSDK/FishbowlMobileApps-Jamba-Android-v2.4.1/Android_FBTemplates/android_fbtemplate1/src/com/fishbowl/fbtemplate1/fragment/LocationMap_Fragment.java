package com.fishbowl.fbtemplate1.fragment;

import java.io.IOException;
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
import com.fishbowl.fbtemplate1.Adapter.LocationAdapter;
import com.fishbowl.fbtemplate1.Controller.FB_DBLocation;
import com.fishbowl.fbtemplate1.Controller.FB_DBOrderConfirm;
import com.fishbowl.fbtemplate1.CoreActivity.FishbowlTemplate1App;
import com.fishbowl.fbtemplate1.Model.LocationItem;
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;
import com.fishbowl.fbtemplate1.Model.OrderItem;
import com.fishbowl.fbtemplate1.activity.TestActivity;
import com.fishbowl.fbtemplate1.widget.TransparentProgressDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

public class LocationMap_Fragment extends Fragment implements OnMarkerClickListener, OnMarkerDragListener, SearchView.OnQueryTextListener, LocationListener {
	private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543, -73.998585);
	private static final LatLng TIMES_SQUARE = new LatLng(40.7577, -73.9857);
	private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
	private static final LatLng WALL_STREET = new LatLng(40.7064, -74.0094);
	List<List<Address>> addresses;
	private static LatLng fromPosition = null;
	private static LatLng toPosition = null;
	MarkerOptions markerOptions;
	LatLng latLng;
	String url = "http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/store/";
	private SearchView mSearchView;
	TransparentProgressDialog pd;
	public static List<LocationItem> dataList;
	LocationAdapter adapter;
	private GoogleMap googleMap;

	Activity thisActivity;
	LayoutInflater inflater;
	EditText searcheditext;
	Button findbtn;
	private ViewGroup parentContainer = null;
	View rootView;
	FishbowlTemplate1App fishTApp;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
	
		if (!isGooglePlayServicesAvailable())
		{
			getActivity().finish();
		}

		if (rootView != null)
		{
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null)
				parent.removeView(rootView);
		}

		try {
			rootView = inflater.inflate(R.layout.fragment_locationmap, container, false);

			this.inflater = inflater;
			rootView.setTag("USER_TAGS_TAB");
			parentContainer = container;
			thisActivity = getActivity();
			setHasOptionsMenu(true);
			fishTApp=FishbowlTemplate1App.getInstance();
			pd = new TransparentProgressDialog(getActivity());
			searcheditext = ((EditText) rootView.findViewById(R.id.search_view));
			findbtn = ((Button) rootView.findViewById(R.id.find_btn));
			findbtn.setOnClickListener(new View.OnClickListener() 
			{

				@Override
				public void onClick(View v)

				{

					loadNewItemsCount(url + searcheditext.getText().toString());

				}
			});

			addGoogleMap();
		} catch (InflateException e) {

		}


		return rootView;
	}

	private boolean isGooglePlayServicesAvailable() 
	{
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		if (ConnectionResult.SUCCESS == status) 
		{
			return true;
		} 
		else
		{
			GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
			return false;
		}
	}


	private void addGoogleMap() 
	{

		if (googleMap == null) 
		{
			googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
			googleMap.setMyLocationEnabled(true);
			LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			String bestProvider = locationManager.getBestProvider(criteria, true);
			Location location = locationManager.getLastKnownLocation(bestProvider);
			if (location != null)
			{

				addMarkers(location);
			}

			locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

			googleMap.setOnMarkerClickListener(this);
			googleMap.setOnMarkerDragListener(this);
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TextView locationTv = (TextView) findViewById(R.id.latlongLocation);
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);
		googleMap.addMarker(new MarkerOptions().position(latLng));
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

	}

	@Override
	public void onProviderDisabled(String provider) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
		// TODO Auto-generated method stub
	}

	private void setupSearchView() 
	{
		mSearchView.setBackgroundColor(Color.TRANSPARENT);
		mSearchView.setPadding(2, 2, 2, 2);
		mSearchView.setIconifiedByDefault(false);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setSubmitButtonEnabled(true);
		mSearchView.setQueryHint("Search City or Zip");
	}

	public boolean onQueryTextChange(String newText)
	{

		if (TextUtils.isEmpty(newText)) {

		} 
		else
		{
			loadNewItemsCount(url + newText);

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
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, requestObj,new Response.Listener<JSONObject>() 
			{
				@Override
				public void onResponse(JSONObject response)
				{

					System.out.println("updatefield response: ");

					try 
					{

						if (response.has("categories")) 
						{

							JSONArray jsonArray = response.getJSONArray("categories");

							if (jsonArray != null && jsonArray.length() > 0) 
							{
								dataList = new ArrayList<LocationItem>();

								for (int i = 0; i < jsonArray.length(); i++) 
								{
									JSONObject wallObj = jsonArray.getJSONObject(i);
									if (wallObj != null)
									{
										LocationItem lt = new LocationItem();

										lt.setStoreID((wallObj.getString("storeID")));
										lt.setName(wallObj.getString("name"));
										lt.setAddress(wallObj.getString("address"));
										lt.setState((wallObj.getString("state")));
										lt.setCity(wallObj.getString("city"));
										lt.setZipcode(wallObj.getString("zipcode"));
										lt.setPhone(wallObj.getString("phone"));
										dataList.add(lt);
										/// FB_DBLocation.getInstance().createUpdateLocation(lt);
									}

									if (dataList != null)
									{

										new GeocoderTask().execute(dataList);
									}
								}

							} 
							else 
							{
								// listView.clearChoices();
							}

						}

					}

					catch (Exception ex) {
						ex.printStackTrace();

					}
				}
			}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error)
				{
					if (error != null) {

						pd.dismiss();
						error.printStackTrace(System.out);

					}
				}
			});
			queue.add(jsObjRequest);
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

	}

	// An AsyncTask class for accessing the GeoCoding Web Service
	private class GeocoderTask extends AsyncTask<List<LocationItem>, Void, List<List<Address>>> 
	{

		@Override
		protected List<List<Address>> doInBackground(List<LocationItem>... dataList)
		{
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(getActivity());
			List<List<Address>> addresses = new ArrayList<List<Address>>();

			try {

				List<LocationItem> location = dataList[0];
				for (LocationItem locationitem : location) 
				{
					List<Address> naddress = new ArrayList<Address>();

					String address =  locationitem.getAddress()+" "+locationitem.getState() +" " +locationitem.getCity();
					// Getting a maximum of 3 Address that matches the input
					// text
					naddress = geocoder.getFromLocationName(address, 1);
					addresses.add(naddress);

				}
			}

			catch (IOException e)
			{
				e.printStackTrace();
			}
			return addresses;
		}

		@Override
		protected void onPostExecute(List<List<Address>> addresses) 
		{

			if (addresses == null || addresses.size() == 0)
			{
				Toast.makeText(getActivity(), "No Location found", Toast.LENGTH_SHORT).show();
			}


			googleMap.clear();
			int count = 0;
			for (List<Address> addresss : addresses)
			{


				for (Address address : addresss) 
				{
					latLng = new LatLng(address.getLatitude(), address.getLongitude());
					String addressText = String.format("%s, %s",
							address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
									address.getCountryName());

					markerOptions = new MarkerOptions();
					markerOptions.position(latLng);
					markerOptions.title(addressText);
					markerOptions.snippet("" + count);
					markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.zlocation));
					googleMap.addMarker(markerOptions);
					
					googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
				}
				count++;
			}
			pd.dismiss();
		}
	}

	private void addMarkers(Location location) 
	{
		if (googleMap != null) 
		{
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			LatLng latLng = new LatLng(latitude, longitude);

			googleMap.addMarker(new MarkerOptions().position(latLng).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.menu_myfavorites)));
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

		}
	}

	private void addLines() 
	{
		if (googleMap != null) 
		{
			googleMap.addPolyline(
					(new PolylineOptions()).add(TIMES_SQUARE, BROOKLYN_BRIDGE, LOWER_MANHATTAN, TIMES_SQUARE).width(5)
					.color(Color.BLUE).geodesic(true));

			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOWER_MANHATTAN, 13));
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) 
	{
		Log.i("GoogleMapActivity", "onMarkerClick");

		String dj = marker.getSnippet();
		if (dj != null) {
			marker.getAlpha();

			marker.hideInfoWindow();
			double dlat = marker.getPosition().latitude;
			double dlon = marker.getPosition().longitude;
			String slat = String.valueOf(dlat);
			String slon = String.valueOf(dlon);

		//	Toast.makeText(getActivity(), "Marker Clicked: " + marker.getTitle() + "AND" + slat + "AND" + slon,Toast.LENGTH_LONG).show();
			Intent intent = new Intent(thisActivity, TestActivity.class);
			Bundle extras = new Bundle();

			if (dataList != null&&!fishTApp.getStoreActivity().signiin)
			{
				LocationItem location = dataList.get(Integer.parseInt(dj));
				FB_DBLocation.getInstance().createUpdateLocation(location);
				OrderItem order = new OrderItem();
				final List<MenuDrawerItem> lists = FB_DBOrderConfirm.getInstance().getAllItem();
				order.setStoreID(location.getStoreID());
				order.setItemlist(lists);		
				extras.putSerializable("draweritem1", (Serializable) lists);
				extras.putSerializable("order", order);
				extras.putSerializable("historyflag", true);
				extras.putSerializable("storelocation", location.getAddress());
				intent.putExtras(extras);
				startActivityForResult(intent, 2);
			}

			
		}

		return false;
	}

	@Override
	public void onMarkerDrag(Marker marker) 
	{
		// do nothing during drag
	}

	@Override
	public void onMarkerDragEnd(Marker marker) 
	{
		toPosition = marker.getPosition();
		Toast.makeText(getActivity(),
				"Marker " + marker.getTitle() + " dragged from " + fromPosition + " to " + toPosition,
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onMarkerDragStart(Marker marker) 
	{
		fromPosition = marker.getPosition();
		Log.d(getClass().getSimpleName(), "Drag start at: " + fromPosition);
	}

	private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String>
	{

		@Override
		protected String doInBackground(LatLng... params) {
			Geocoder geocoder = new Geocoder(getActivity());
			double latitude = params[0].latitude;
			double longitude = params[0].longitude;

			List<Address> addresses = null;
			String addressText = "";

			try {
				addresses = geocoder.getFromLocation(latitude, longitude, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);

				addressText = String.format("%s, %s, %s",
						address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getLocality(),
								address.getCountryName());
			}

			return addressText;
		}

		@Override
		protected void onPostExecute(String addressText)
		{
			markerOptions.title(addressText);
			googleMap.addMarker(markerOptions);

		}
	}

}