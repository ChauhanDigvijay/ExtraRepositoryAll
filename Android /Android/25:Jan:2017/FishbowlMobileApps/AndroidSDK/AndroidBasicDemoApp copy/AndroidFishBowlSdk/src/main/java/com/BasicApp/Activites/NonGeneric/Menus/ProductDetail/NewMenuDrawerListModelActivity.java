package com.BasicApp.Activites.NonGeneric.Menus.ProductDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.Activites.NonGeneric.Home.DashboardModelActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.ModelAdapters.DrawerGridViewAdapter;
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

public class NewMenuDrawerListModelActivity extends BaseActivity implements View.OnClickListener {
    public static NewMenuDrawerListModelActivity instance;
    public List<FBMenuDrawerDetailItem> mcatList = new ArrayList<>();
    RecyclerView detail_list;
    int productCategoryId, productSubCategoryId;

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
        setUpToolBar(true,true);
        setTitle("SUBMENU");
        setBackButton(true,false);


        gridView = (GridView) findViewById(R.id.gridview);
        detail_list = (RecyclerView) findViewById(R.id.detail_list);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mcatList.get(position).getProductName();
                if (StringUtilities.isValidString(name)) {
                    FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(mcatList.get(position).getId()), name, FBEventSettings.FEATURE_PRODUCT_CLICK);
                }
                //dj change
                //   Intent intent = new Intent(NewMenuDrawerListModelActivity.this, NewModifierModelActivity.class);


                Intent intent = new Intent(NewMenuDrawerListModelActivity.this, DashboardModelActivity.class);

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


    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public void onClick(View v) {

    }

    public void getMenuDrawerListProduct() {

        enableScreen(false);
        FBMenuService.getDrawerProductList(FBPreferences.sharedInstance(NewMenuDrawerListModelActivity.this).getStoreCode(), String.valueOf(22), String.valueOf(223), new FBMenuDrawerCallback() {
            @Override
            public void onMenuDrawerCallback(FBMenuDrawerItem response, Exception error) {
                if (response != null)

                {
                    FBMenuDrawerDetailItem[] categories = response.getCategories();
                    mcatList = Arrays.asList(categories);
                    adapter = new DrawerGridViewAdapter(NewMenuDrawerListModelActivity.this, mcatList);
                    gridView.setAdapter(adapter);
                    enableScreen(true);
                } else {
                    enableScreen(true);
                }
            }
        });
    }


}

