package com.BasicApp.Activites.NonGeneric.Store;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.MenuActivity;
import com.BasicApp.Adapters.LocationAdapter;
import com.BasicApp.Adapters.StoreAdapter;
import com.BasicApp.BusinessLogic.Models.LocationItem;
import com.BasicApp.BusinessLogic.Models.NewMenuDrawer;
import com.BasicApp.BusinessLogic.Models.OrderItem;
import com.BasicApp.BusinessLogic.Models.OrderProductList;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.Services.FBStoreService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.StringUtilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.BasicApp.Adapters.MenuImageAdapter.position;


public class StoreMapActivity extends FragmentActivity implements OnMarkerClickListener, OnMarkerDragListener, SearchView.OnQueryTextListener, LocationListener {
    //  LocationAdapter adapter;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543, -73.998585);
    private static final LatLng TIMES_SQUARE = new LatLng(40.7577, -73.9857);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
    private static final LatLng WALL_STREET = new LatLng(40.7064, -74.0094);
    // public static List<LocationItem> dataList;
    public static Boolean signiin = false;
    public static Boolean checkdirectmenu = false;
    public static List<LocationItem> dataList;
    private static LatLng fromPosition = null;
    private static LatLng toPosition = null;
    public FBStoreService all;
    StoreAdapter adapter1;
    Button findbtn;
    List<List<Address>> addresses;
    MarkerOptions markerOptions;
    LatLng latLng;
    String url = "http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/store/";
    LocationAdapter adapter;
    LayoutInflater inflater;
    EditText searcheditext;
    Button list_activity;
    private RelativeLayout toolbar;
    private List<FBStoresItem> storeList;
    private ImageLoader mImageLoader;
    private NetworkImageView background;
    private SearchView mSearchView;
    private DrawerLayout drawerLayout;
    private GoogleMap googleMap;
    ProgressBarHandler progressBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);
        getWindow().setWindowAnimations(0);
        progressBarHandler=new ProgressBarHandler(this);
        //   initilizeMap();

        if (!isGooglePlayServicesAvailable()) {
            this.finish();
        }
        addGoogleMap();
        all = FBStoreService.sharedInstance();
        storeList = all.allStoreFromServer;
        //checkPermissionsforlocation();
        list_activity = (Button) findViewById(R.id.list_activity);
        list_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StoreMapActivity.this, StoreListActivity.class);
                startActivity(i);
                StoreMapActivity.this.finish();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        signiin = false;
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {

            signiin = extras.getBoolean("signin", false);


        }
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar);

        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreMapActivity.this.finish();
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.findViewById(R.id.menu_navigator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else
                    drawerLayout.openDrawer(GravityCompat.END);
            }

        });

        FBUtils.setUpNavigationDrawer(StoreMapActivity.this);
        background = (NetworkImageView) findViewById(R.id.img_Back);

        FBUtils.setUpNavigationDrawer(StoreMapActivity.this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        /*if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }*/

        try {
            searcheditext = ((EditText) findViewById(R.id.search_view));
            findbtn = ((Button) findViewById(R.id.find_btn));
            findbtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v)

                {
                    progressBarHandler.show();
                    getSearchStore(searcheditext.getText().toString());

                }
            });


        } catch (InflateException e) {

        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onStart() {


        super.onResume();
        getWindow().setWindowAnimations(0);
        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();

            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
            mImageLoader.get(url, ImageLoader.getImageListener(background, R.drawable.bgimage, android.R.drawable.ic_dialog_alert));
            //background.setImageUrl(url, mImageLoader);

        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        drawerLayout.closeDrawers();
    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }


    private void checkPermissionsforlocation() {


        if ((int) Build.VERSION.SDK_INT < 23) {

            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.


                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMarkerClickListener(this);
                googleMap.setOnMarkerDragListener(this);
                LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null) {

                    addMarkers(location);
                }

                locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
            }

            return;
        } else {
            if (checkPermission()) {

                //             ToastManager.sharedInstance().show("Permission already granted");


            } else {
                //       ToastManager.sharedInstance().show("Please request permission");

                if (!checkPermission()) {
                    requestPermission();


                } else {
                    //    ToastManager.sharedInstance().show("Permission already granted.");

                }
            }
        }


    }


    private boolean checkPermission() {


        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED) {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

                googleMap.setMyLocationEnabled(true);

                googleMap.setOnMarkerClickListener(this);
                googleMap.setOnMarkerDragListener(this);
                LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null) {

                    addMarkers(location);
                }

                locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

            }
            return true;
        } else {
            return false;
        }
    }


    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            //    Toast.makeText(this,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);


        }
    }

    //    private void initilizeMap()
//    {
//        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//        if(googleMap != null)
//        {
//            googleMap.getUiSettings().setAllGesturesEnabled(false);
//        }
//    }
    private void addGoogleMap() {


        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            //googleMap.setMyLocationEnabled(true);

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
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private void setupSearchView() {
        mSearchView.setBackgroundColor(Color.TRANSPARENT);
        mSearchView.setPadding(2, 2, 2, 2);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) StoreMapActivity.this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search City or Zip");
    }

    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {

        } else {
            loadNewItemsCount(url + newText);

        }
        return false;
    }

    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    public void loadNewItemsCount(String url) {
        url = url.replaceAll(" ", "%20");
        try {

            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject userJSON = new JSONObject();
            JSONObject requestObj = new JSONObject();
            requestObj.put("data", userJSON);

            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url + "/104", requestObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    System.out.println("updatefield response: ");

                    try {

                        if (response.has("categories")) {

                            JSONArray jsonArray = response.getJSONArray("categories");

                            if (jsonArray != null && jsonArray.length() > 0) {
                                dataList = new ArrayList<LocationItem>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject wallObj = jsonArray.getJSONObject(i);
                                    if (wallObj != null) {
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

                                    if (dataList != null) {

                                        //   new GeocoderTask().execute(dataList);
                                    }
                                }

                            } else {
                                // listView.clearChoices();
                            }

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {


                        error.printStackTrace(System.out);

                    }
                }
            });
            queue.add(jsObjRequest);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

    }


    public void loadNewItemsCount11() {

        if (storeList != null) {

            //   new GeocoderTask().execute(storeList);
        }
    }

    private void addMarkers(Location location) {
        if (googleMap != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            googleMap.addMarker(new MarkerOptions().position(latLng).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.menu_myfavorites)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        }
    }

    private void addLines() {
        if (googleMap != null) {
            googleMap.addPolyline(
                    (new PolylineOptions()).add(TIMES_SQUARE, BROOKLYN_BRIDGE, LOWER_MANHATTAN, TIMES_SQUARE).width(5)
                            .color(Color.BLUE).geodesic(true));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOWER_MANHATTAN, 13));
        }
    }

    @Override

    public boolean onMarkerClick(Marker marker) {
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


            if (storeList != null) {
                double lat = 37.3462302;
                double lon = -121.9417057;
                FBStoresItem clps = FBStoreService.sharedInstance().allStoreFromServer.get(position);
                double lat1 = Double.valueOf(clps.latitude);
                double lon1 = Double.valueOf(clps.longitude);
                double earthRadius = 6371;
                double dLat = Math.toRadians(lat1 - lat);
                double dLng = Math.toRadians(lon1 - lon);
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat1)) *
                                Math.sin(dLng / 2) * Math.sin(dLng / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                float dist = (float) (earthRadius * c);
                float miles = (float) (dist * .621371);
                float miless = (float) Math.round(miles);
                Intent intent = new Intent(StoreMapActivity.this, StoreList_MapActivity.class);
                Bundle extras = new Bundle();
                FBStoresItem location = storeList.get(position);
                OrderItem order = new OrderItem();
                final ArrayList<NewMenuDrawer> lists = OrderProductList.sharedInstance().orderProductList;
                String locationstring = String.valueOf(location.getStoreID());
                order.setStoreID(locationstring);
                extras.putSerializable("location", (Serializable) location);
                extras.putSerializable("order", order);
                extras.putSerializable("historyflag", true);
                extras.putSerializable("storeDistance", miless);
                extras.putSerializable("storelocation", location.getAddress());
                extras.putSerializable("storeID", location.getStoreID());
                extras.putSerializable("storeCode", location.getStoreNumber());
                intent.putExtras(extras);
                startActivityForResult(intent, 2);

            } else {
                Intent intent = new Intent(StoreMapActivity.this, MenuActivity.class);
                Bundle extras = new Bundle();
                FBStoresItem location = storeList.get(position);
                OrderItem order = new OrderItem();
                final ArrayList<NewMenuDrawer> lists = OrderProductList.sharedInstance().orderProductList;
                String locationstring = String.valueOf(location.getStoreID());
                extras.putSerializable("location", (Serializable) location);
                extras.putSerializable("order", order);
                extras.putSerializable("historyflag", true);
                extras.putSerializable("storelocation", location.getAddress());
                extras.putSerializable("storeCode", location.getStoreNumber());
                intent.putExtras(extras);
                startActivityForResult(intent, 2);
            }

        }
        return false;
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        // do nothing during drag
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        toPosition = marker.getPosition();
        Toast.makeText(this,
                "Marker " + marker.getTitle() + " dragged from " + fromPosition + " to " + toPosition,
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        fromPosition = marker.getPosition();
        Log.d(getClass().getSimpleName(), "Drag start at: " + fromPosition);
    }

    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask1 extends AsyncTask<List<LocationItem>, Void, List<List<Address>>> {

        @Override
        protected List<List<Address>> doInBackground(List<LocationItem>... dataList) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(StoreMapActivity.this);
            List<List<Address>> addresses = new ArrayList<List<Address>>();

            try {

                List<LocationItem> location = dataList[0];
                for (LocationItem locationitem : location) {
                    List<Address> naddress = new ArrayList<Address>();

                    String address = locationitem.getAddress() + " " + locationitem.getState() + " " + locationitem.getCity();
                    // Getting a maximum of 3 Address that matches the input
                    // text
                    naddress = geocoder.getFromLocationName(address, 1);
                    addresses.add(naddress);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<List<Address>> addresses) {

            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(StoreMapActivity.this, "No Location found", Toast.LENGTH_SHORT).show();
            }


            googleMap.clear();
            int count = 0;
            for (List<Address> addresss : addresses) {


                for (Address address : addresss) {
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    String addressText = String.format("%s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                            address.getCountryName());

                    markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(addressText);
                    markerOptions.snippet("" + count);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
                    googleMap.addMarker(markerOptions);

                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
                count++;
            }

        }
    }

    private class GeocoderTask extends AsyncTask<List<FBStoresItem>, Void, List<List<Address>>> {

        @Override
        protected List<List<Address>> doInBackground(List<FBStoresItem>... dataList) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(StoreMapActivity.this);
            List<List<Address>> addresses = new ArrayList<List<Address>>();

            try {
                int count = 0;
                List<FBStoresItem> location = dataList[0];
                for (FBStoresItem locationitem : location) {
                    List<Address> naddress = new ArrayList<Address>();

                    String address = locationitem.getAddress() + " " + locationitem.getState() + " " + locationitem.getCity();
                    naddress = geocoder.getFromLocationName(address, 1);
                    addresses.add(naddress);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<List<Address>> addresses) {

            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(StoreMapActivity.this, "No Location found", Toast.LENGTH_SHORT).show();
            }


            googleMap.clear();
            int count = 0;

            for (List<Address> addresss : addresses) {


                    for (Address address : addresss) {
                        latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        String addressText = String.format("%s, %s",
                                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                                address.getCountryName());

                        markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(addressText);
                        markerOptions.snippet("" + count);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
                        googleMap.addMarker(markerOptions);

                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                    count++;
                }

        }
    }

    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {

        @Override
        protected String doInBackground(LatLng... params) {
            Geocoder geocoder = new Geocoder(StoreMapActivity.this);
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
        protected void onPostExecute(String addressText) {
            markerOptions.title(addressText);
            googleMap.addMarker(markerOptions);

        }


    }

    public void getSearchStore(String query) {

        if (StringUtilities.isValidString(query)) {
            JSONObject data = new JSONObject();
            try {
                data.put("query", query);
                data.put("radius", "1000");
                data.put("count", "10");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            FBStoreService.sharedInstance().getSearchAllStore(data, query, new FBStoreService.FBAllNewSearchStoreCallback() {
                @Override
                public void OnAllSearchStoreCallback(List<FBStoresItem> response, Exception error) {
                    progressBarHandler.dismiss();
                    List<FBStoresItem> subItems;
                    if (response != null) {
                        subItems = response.subList(0,5);
                        new GeocoderTask().execute(subItems);

                    } else {

                    }
                }


            });
        }

    }
}