package com.BasicApp.ActivityModel.Menus.ProductDetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.BasicApp.Activites.Generic.BottomToolbarActivity;

import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.ModelAdapters.DrawerGridViewAdapter;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.BasicApp.Utils.StringUtilities;
import com.Preferences.FBPreferences;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Interfaces.FBMenuDrawerCallback;
import com.fishbowl.basicmodule.Models.FBMenuDrawerDetailItem;
import com.fishbowl.basicmodule.Models.FBMenuDrawerItem;
import com.fishbowl.basicmodule.Services.FBMenuService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by digvijay(dj)
 */

public class NewMenuDrawerListModelActivity extends Activity implements View.OnClickListener {
    public static NewMenuDrawerListModelActivity instance;
    public List<FBMenuDrawerDetailItem> mcatList = new ArrayList<>();
    public boolean isDownloadable = false;
    RecyclerView detail_list;
    int productCategoryId, productSubCategoryId;
    ListView menuSubCategoryList;
    ProgressBarHandler p;
    DrawerGridViewAdapter adapter;
    GridView gridView;
    private DrawerLayout drawerLayout;

    private Toolbar toolbar;
    public NewMenuDrawerListModelActivity() {
    }

    public static NewMenuDrawerListModelActivity sharedInstance() {

        if (instance == null) {
            instance = new NewMenuDrawerListModelActivity();

        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            productCategoryId = extras.getInt("productCategoryId");
            productSubCategoryId = extras.getInt("productSubCategoryId");
        }
        setContentView(R.layout.activity_menu_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FBUtils.setUpNavigationDrawerModel(NewMenuDrawerListModelActivity.this);
        p = new ProgressBarHandler(this);
        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMenuDrawerListModelActivity.this.finish();
            }
        });
        toolbar.findViewById(R.id.menu_navigator).setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridview);
        detail_list = (RecyclerView) findViewById(R.id.detail_list);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mcatList.get(position).getProductName();
                if(StringUtilities.isValidString(name)) {
                    FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(mcatList.get(position).getId()), name, FBEventSettings.FEATURE_PRODUCT_CLICK);
                }
                Intent intent = new Intent(NewMenuDrawerListModelActivity.this, NewModifierModelActivity.class);
                Bundle extras = new Bundle();
                extras.putString("productName", name);
                extras.putString("productImage", mcatList.get(position).getProductImageUrl());
                extras.putString("productDesc", mcatList.get(position).getProductLongDescription());
                extras.putInt("productID", mcatList.get(position).getId());
                extras.putInt("categoryID", productCategoryId);
                extras.putInt("subCategoryID", productSubCategoryId);
                extras.putSerializable("draweritem1", mcatList.get(position));
                //OrderProductList.sharedInstance().setCurrentAdded(mcatList.get(position));
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        getMenuDrawerListProduct();
    }

//    public void getMenuDrawerListProduct() {
////        p.show();
////        final JSONObject object = new JSONObject();
////        FBUserService.sharedInstance().getDrawerProductList(object, FBPreferences.sharedInstance(NewMenuDrawerListModelActivity.this).getStoreCode(), String.valueOf(productCategoryId), String.valueOf(productSubCategoryId), new FBUserService.FBMenuDrawerCallback() {
////
////            public void onMenuDrawerCallback(JSONObject response, Exception error) {
////                try {
////                    if (response != null) {
////                        initMenuDrawer(response);
////
////                    } else {
////                    }
////                } catch (Exception e) {
////
////                }
////            }
////
////        });
////    }


    public void getMenuDrawerListProduct() {
            p.show();
            FBMenuService.getDrawerProductList(FBPreferences.sharedInstance(NewMenuDrawerListModelActivity.this).getStoreCode(), String.valueOf(22), String.valueOf(223), new FBMenuDrawerCallback() {
                @Override
                public void onMenuDrawerCallback(FBMenuDrawerItem response, Exception error) {
                    if (response != null)

                    {
                        FBMenuDrawerDetailItem[] categories = response.getCategories();
                        mcatList = Arrays.asList(categories);
                        adapter = new DrawerGridViewAdapter(NewMenuDrawerListModelActivity.this, mcatList);
                        gridView.setAdapter(adapter);
                        p.dismiss();
                    } else {
                        p.dismiss();
                    }
                }
            });
        }


//    public void initMenuDrawer(JSONObject json) {
//        try {
//
//            JSONArray jsonArray = json.getJSONArray("");
//            if (jsonArray != null) {
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject myProductObj = jsonArray.getJSONObject(i);
//                    NewMenuDrawer getStoresObj = new NewMenuDrawer();
//                    getStoresObj.initMenuDrawer(myProductObj);
//                    mcatList.add(getStoresObj);
//                }
//                p.dismiss();
//                adapter = new DrawerGridViewAdapter(this, mcatList);
//                gridView.setAdapter(adapter);
//            }
//
//        } catch (Exception e) {
//        }
//    }
    @Override
    protected void onResume() {
        super.onResume();
        BottomToolbarActivity b = (BottomToolbarActivity) findViewById(R.id.bottom_toolbar);
        b.initBottomToolbar();

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_navigator) {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else
                drawerLayout.openDrawer(GravityCompat.END);
        }
    }}

