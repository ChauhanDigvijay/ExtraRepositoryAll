package com.BasicApp.Activites.NonGeneric.Store;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.MenuActivity;
import com.BasicApp.Adapters.LocationAdapter;
import com.BasicApp.Adapters.StoreAdapter;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Models.LocationItem;
import com.BasicApp.BusinessLogic.Models.NewMenuDrawer;
import com.BasicApp.BusinessLogic.Models.OrderItem;
import com.BasicApp.BusinessLogic.Models.OrderProductList;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.Preferences.FBPreferences;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.Services.FBStoreService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.GifWebView;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by digvijay(dj)
 */
public class StoreListActivity extends Activity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static Boolean signiin = false;
    public static Boolean checkdirectmenu = false;
    public static List<LocationItem> dataList;
    public StoreAdapter adapter1;
    public FBStoreService all;
    Button map_activity;
    ListView listView;
    List<LocationItem> lists;
    LocationAdapter adapter;
    EditText searcheditext;
    GifWebView view;
    Button findbtn;
    LinearLayout llProgress;
    private RelativeLayout toolbar;
    private ImageLoader mImageLoader;
    private NetworkImageView background;
    private DrawerLayout drawerLayout;
    private SearchView mSearchView;
    private List<FBStoresItem> storeList;
    ProgressBarHandler progressBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        getWindow().setWindowAnimations(0);
        all = FBStoreService.sharedInstance();
        storeList = all.allStoreFromServer;
        progressBarHandler=new ProgressBarHandler(this);
        map_activity = (Button) findViewById(R.id.map_activity);
        map_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StoreListActivity.this, StoreMapActivity.class);
                startActivity(i);
                StoreListActivity.this.finish();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        signiin = false;
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {

            signiin = extras.getBoolean("signin", false);

            checkdirectmenu = extras.getBoolean("checkdirectmenu", false);
            if (checkdirectmenu) {
                FBPreferences.sharedInstance(StoreListActivity.this).setDashboardin(true);
            } else {
                FBPreferences.sharedInstance(StoreListActivity.this).setDashboardin(false);
            }
        } else {
            FBPreferences.sharedInstance(StoreListActivity.this).setDashboardin(false);
        }
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar);

        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreListActivity.this.finish();
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

        FBUtils.setUpNavigationDrawer(StoreListActivity.this);
        background = (NetworkImageView) findViewById(R.id.img_Back);

        llProgress = (LinearLayout) findViewById(R.id.ll_progress);
        view = new GifWebView(StoreListActivity.this, "file:///android_asset/loadingpizza.gif");
        searcheditext = ((EditText) findViewById(R.id.search_view));
        findbtn = ((Button) findViewById(R.id.find_btn));
        findbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                progressBarHandler.show();
                FBAnalyticsManager.sharedInstance().track_ItemWith("", searcheditext.getText().toString(), FBEventSettings.STORE_SEARCH);
                getSearchStore(searcheditext.getText().toString());
            }
        });
        listView = (ListView) findViewById(R.id.store_list);

        loadNewItemsCount11();


    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onStart() {
        drawerLayout.closeDrawers();
        super.onResume();

        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();

            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
            mImageLoader.get(url, ImageLoader.getImageListener(background, R.drawable.bgimage, android.R.drawable.ic_dialog_alert));

        }
    }


    @Override
    protected void onResume() {

        super.onResume();
        getWindow().setWindowAnimations(0);
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) StoreListActivity.this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search City or Zip");
    }


    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            listView.clearTextFilter();
        } else {
        }
        return false;
    }

    public boolean onQueryTextSubmit(String query) {

        return false;
    }


    public void loadNewItemsCount11() {
        if (storeList != null) {

            if (storeList.size() > 0) {
                adapter1 = new StoreAdapter(StoreListActivity.this, storeList);
                listView.setAdapter(adapter1);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


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
                            Intent intent = new Intent(StoreListActivity.this, StoreList_MapActivity.class);
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
                            Intent intent = new Intent(StoreListActivity.this, MenuActivity.class);
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
                });
            }
        }



    }



    public  void getSearchStore(String query)  {

        if (StringUtilities.isValidString(query)) {
            JSONObject data = new JSONObject();
            try {
                data.put("query",query);
                data.put("radius","1000");
                data.put("count","10");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            FBStoreService.sharedInstance().getSearchAllStore(data, query, new FBStoreService.FBAllNewSearchStoreCallback() {
                @Override
                public void OnAllSearchStoreCallback(List<FBStoresItem> response, Exception error) {
                    progressBarHandler.dismiss();
                    if (response != null) {
                        adapter1 = new StoreAdapter(StoreListActivity.this, response);
                        listView.setAdapter(adapter1);
                    }
                    else
                        {
                            progressBarHandler.dismiss();
                        }
                }

                public void OnAllSearchStoreCallback(List<FBStoresItem> response, String error)

                {
                    if (response != null) {
                        adapter1 = new StoreAdapter(StoreListActivity.this, response);
                        listView.setAdapter(adapter1);
                    } else {


                    }
                }
            });
        }
    }
}