package com.BasicApp.Activites.NonGeneric.Store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.BasicApp.Activites.NonGeneric.Home.DashboardModelActivity;
import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.MenuActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Models.GetSelectedSotreDetails;
import com.BasicApp.BusinessLogic.Models.MenuDrawerItem;
import com.BasicApp.BusinessLogic.Models.NewMenuDrawer;
import com.BasicApp.BusinessLogic.Models.OrderConfirmDrawItem;
import com.BasicApp.BusinessLogic.Models.OrderItem;
import com.BasicApp.BusinessLogic.Models.OrderProductList;
import com.BasicApp.ModelAdapters.StoreDayTimeAdapter;
import com.BasicApp.Utils.FBUtils;
import com.Preferences.FBPreferences;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Interfaces.FBRestaurantServiceDetailCallback;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBUserServiceCallback;
import com.fishbowl.basicmodule.Models.FBMember;
import com.fishbowl.basicmodule.Models.FBRestaurantDetailItem;
import com.fishbowl.basicmodule.Models.FBRestaurantItem;
import com.fishbowl.basicmodule.Models.FBRestaurantListDetailItem;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Services.FBRestaurantService;
import com.fishbowl.basicmodule.Services.FBSessionService;
import com.fishbowl.basicmodule.Services.FBStoreService;
import com.fishbowl.basicmodule.Utils.StringUtilities;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StoreListMapModelActivity extends BaseActivity implements OnMarkerClickListener, OnMarkerDragListener, View.OnClickListener {

    public static Boolean checkback = false;
    public static Boolean historyflag = false;
    public static List<OrderConfirmDrawItem> dataList;
    public FBStoreService all;
    public StoreDayTimeAdapter customAdapter;
    TextView conti, storedistance;
    List<MenuDrawerItem> listss;
    OrderItem order = null;
    String storelocation;
    FBRestaurantItem location;
    LinearLayout online_layout, reservation_layout, favouritestore;
    ListView days_time_list;
    Float storeDistance;
    String storeId;
    Button start_new_order;

    int storeID;
    private GoogleMap googleMap;
    private DrawerLayout drawerLayout;
    private RelativeLayout toolbar;
    private ArrayList<GetSelectedSotreDetails> storeHourList = new ArrayList<GetSelectedSotreDetails>();
    public List<FBRestaurantDetailItem> restaurantdetailitem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_store_list__map);

        addGoogleMap();
        enableScreen(false);

        days_time_list = (ListView) findViewById(R.id.days_time_list);
        days_time_list.setVisibility(View.VISIBLE);

        all = FBStoreService.sharedInstance();


        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            location = (FBRestaurantItem) extras.getSerializable("location");
            order = (OrderItem) extras.getSerializable("order");
            listss = (List<MenuDrawerItem>) i.getSerializableExtra("draweritem1");
            historyflag = extras.getBoolean("historyflag", false);
            storelocation = extras.getString("storelocation");
            storeDistance = extras.getFloat("storeDistance");
            storeId = extras.getString("storeCode");
            storeID = extras.getInt("storeID");


        }
        online_layout = (LinearLayout) findViewById(R.id.online_layout);
        online_layout.setOnClickListener(this);
        favouritestore = (LinearLayout) findViewById(R.id.favouritestore);
        storedistance = (TextView) findViewById(R.id.storedistance);
        storedistance.setText(String.valueOf(storeDistance));
        favouritestore.setOnClickListener(this);

        start_new_order = (Button) findViewById(R.id.start_new_order);
        start_new_order.setOnClickListener(this);

        TextView T1 = (TextView) findViewById(R.id.storename);
        TextView T2 = (TextView) findViewById(R.id.storeno);
        TextView T3 = (TextView) findViewById(R.id.storelocation);


        if (StringUtilities.isValidString(order.getStoreID())) {

            T1.setText(location.getStoreName());
            T2.setText(location.getPhone());
            T3.setText(location.getAddress());

        }
       // getSotreDetail();
        getStoresDetail();


        setUpToolBar(true,true);
        setTitle("Location");
        setBackButton(true,false);
    }

    private void addGoogleMap() {


        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.setOnMarkerClickListener(StoreListMapModelActivity.this);
            googleMap.setOnMarkerDragListener(StoreListMapModelActivity.this);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.online_layout) {



            // Intent intent = new Intent(StoreListMapModelActivity.this, DeliveryActivity.class);
            Intent intent = new Intent(StoreListMapModelActivity.this, DashboardModelActivity.class);
            Bundle extras = new Bundle();
            String locationstring = String.valueOf(location.getStoreID());
            order.setStoreID(locationstring);
            order.setItemlist(listss);
            extras.putSerializable("location", (Serializable) location);
            extras.putSerializable("order", order);
            extras.putSerializable("historyflag", true);
            extras.putSerializable("storelocation", location.getAddress());
            intent.putExtras(extras);
            startActivityForResult(intent, 2);
        }
        if (v.getId() == R.id.start_new_order) {

            FBAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.START_NEW_ORDER);

           // Intent intent = new Intent(StoreListMapModelActivity.this, DeliveryActivity.class);
            Intent intent = new Intent(StoreListMapModelActivity.this, DashboardModelActivity.class);

            Bundle extras = new Bundle();
            String locationstring = String.valueOf(location.getStoreID());
            order.setStoreID(locationstring);
            order.setItemlist(listss);
            extras.putSerializable("location", (Serializable) location);
            extras.putSerializable("order", order);
            extras.putSerializable("historyflag", true);
            extras.putSerializable("storelocation", location.getAddress());
            intent.putExtras(extras);
            startActivityForResult(intent, 2);
        }
        if (v.getId() == R.id.favouritestore) {
            FBPreferences.sharedInstance(StoreListMapModelActivity.this).setStorePhoneNum(location.getPhone());
           // FBPreferences.sharedInstance(StoreListMapModelActivity.this).setStoreAddress(location.getAddress());
            FBPreferences.sharedInstance(StoreListMapModelActivity.this).setStoreCode(location.getStoreNumber());
            FBPreferences.sharedInstance(StoreListMapModelActivity.this).setStoreName(location.getStoreName());
            FBPreferences.sharedInstance(StoreListMapModelActivity.this).setStoreId("fav_store_id", location.getStoreID());
            FBPreferences.sharedInstance(StoreListMapModelActivity.this).setStoreDistance("fav_store_distance", storeDistance);
            BottomToolbarActivity b = (BottomToolbarActivity) findViewById(R.id.bottom_toolbar);
            FBAnalyticsManager.sharedInstance().track_ItemWith(location.getStoreNumber(),location.getStoreName(), FBEventSettings.UPDATE_FAVORITE_STORE);
            b.initBottomToolbar();
            favouriteStoreUpdate();

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

            if (dataList != null) {
                Intent intent = new Intent(this, StoreListMapModelActivity.class);
                Bundle extras = new Bundle();
                OrderItem order = new OrderItem();
                final ArrayList<NewMenuDrawer> lists = OrderProductList.sharedInstance().orderProductList;
                String locationstring = String.valueOf(location.getStoreID());
                order.setStoreID(locationstring);
                extras.putSerializable("location", (Serializable) location);
                extras.putSerializable("order", order);
                extras.putSerializable("historyflag", true);
                extras.putSerializable("storelocation", location.getAddress());
                intent.putExtras(extras);
                startActivityForResult(intent, 2);


            }
        } else {
            Intent intent = new Intent(this, MenuActivity.class);
            Bundle extras = new Bundle();

            OrderItem order = new OrderItem();
            final ArrayList<NewMenuDrawer> lists = OrderProductList.sharedInstance().orderProductList;
            String locationstring = String.valueOf(location.getStoreID());
            order.setStoreID(locationstring);
            extras.putSerializable("location", (Serializable) location);
            extras.putSerializable("order", order);
            extras.putSerializable("historyflag", true);
            extras.putSerializable("storelocation", location.getAddress());
            intent.putExtras(extras);
            startActivityForResult(intent, 2);
        }


        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

//    public void favouriteStoreUpdate() {
//
//        Member member = null;
//        member = FBUserService.sharedInstance().member;
//
//        JSONObject object = new JSONObject();
//        try {
//            object.put("memberid", member.customerID);
//            object.put("storeCode", storeId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        progressBarHandler.show();
//        FBUserService.sharedInstance().favourteStoreUpdate(object, new FBUserService.FBFavouriteStoreUpdateCallback() {
//            @Override
//            public void onFBFavouriteStoreUpdateCallback(JSONObject response, Exception error) {
//                if (response != null) {
//                    // FBUtils.showAlert(UserProfile_Activity.this,"Member Update Successfully");
//                    progressBarHandler.dismiss();
//
//                    getMember();
//
//                } else {
//                    FBUtils.tryHandleTokenExpiry(StoreListMapModelActivity.this, error);
//                    progressBarHandler.dismiss();
//                }
//
//            }
//        });
//    }


    public void favouriteStoreUpdate() {

       // progressBarHandler.show();
        FBRestaurantService.favourteStoreUpdate(String.valueOf(storeID), new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(FBSessionItem response, Exception error) {
                if (response != null)

                {
                  //  progressBarHandler.dismiss();

                    GetMember();


                } else {
                    FBUtils.tryHandleTokenExpiry(StoreListMapModelActivity.this, error);
                   // progressBarHandler.dismiss();
                }
            }
        });
    }


//    public void getMember() {
//        progressBarHandler.show();
//        JSONObject object = new JSONObject();
//        FBUserService.sharedInstance().getMember(object, new FBUserService.FBGetMemberCallback() {
//            public void onGetMemberCallback(JSONObject response, Exception error) {
//
//                if (response != null) {
//
//                    progressBarHandler.dismiss();
//                } else {
//                    FBUtils.tryHandleTokenExpiry(StoreListMapModelActivity.this, error);
//                }
//            }
//
//        });
//
//    }

    public void GetMember() {
        enableScreen(false);
        FBSessionService.getMember(new FBUserServiceCallback() {
            @Override
            public void onUserServiceCallback(FBMember user, Exception error) {


                if (user != null) {

                    enableScreen(true);
                } else {
                    enableScreen(true);
                    FBUtils.tryHandleTokenExpiry(StoreListMapModelActivity.this, error);
                }
            }
        });
    }


//    public void getSotreDetail() {
//        progressBarHandler.show();
//        JSONObject object = new JSONObject();
//        FBStoreService.sharedInstance().getStoreDetails(String.valueOf(storeID), new FBStoreService.FBStoreDetailCallback() {
//            public void OnFBStoreDetailCallback(JSONObject response, Exception error) {
//                try {
//                    if (response != null && error == null) {
//
//                        JSONObject fobj = response.getJSONObject("mobileStores");
//                        JSONArray jArray = fobj.getJSONArray("storeHourList");
//                        if (jArray != null) {
//                            for (int i = 0; i < jArray.length(); i++) {
//                                JSONObject jObj = jArray.getJSONObject(i);
//                                GetSelectedSotreDetails gssd = new GetSelectedSotreDetails(jObj);
//                                storeHourList.add(gssd);
//                            }
//                        }
//                        customAdapter = new StoreDayTimeAdapter(StoreListMapModelActivity.this, storeHourList);
//                        days_time_list.setAdapter(customAdapter);
//                        days_time_list.setVisibility(View.VISIBLE);
//                        progressBarHandler.dismiss();
//                    } else {
//                        FBUtils.tryHandleTokenExpiry(StoreListMapModelActivity.this, error);
//                        progressBarHandler.dismiss();
//                    }
//                } catch (JSONException e) {
//                }
//            }
//
//        });
//
//    }

        public void getStoresDetail() {

            FBRestaurantService.getRestaurantById(String.valueOf(storeID), new FBRestaurantServiceDetailCallback() {
                @Override
                public void onRestaurantServiceDetailCallback(FBRestaurantListDetailItem response, Exception error) {
                    if (response != null) {

                        FBRestaurantDetailItem[] getCategories = response.getCategories();
                        restaurantdetailitem= Arrays.asList(getCategories);
                       // customAdapter = new StoreDayTimeAdapter(StoreListMapModelActivity.this, restaurantdetailitem);
                        customAdapter = new StoreDayTimeAdapter(StoreListMapModelActivity.this, storeHourList);
                        days_time_list.setAdapter(customAdapter);
                        days_time_list.setVisibility(View.VISIBLE);
                        enableScreen(true);

                    } else {

                    }
                }
            });
        }







}