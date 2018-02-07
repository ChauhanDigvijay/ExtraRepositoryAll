package com.BasicApp.Activites.NonGeneric.Menus.ProductCategoryDetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.BasicApp.Activites.NonGeneric.Menus.ProductDetail.NewMenuDrawerListActivity;
import com.BasicApp.Adapters.MenuSubCategoryAdapter;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Models.MenuSubCategory;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.BasicApp.Utils.StringUtilities;
import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.Preferences.FBPreferences;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Services.FBUserService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by digvijay(dj)
 */

public class NewMenuSubCategoryActivity extends Activity implements View.OnClickListener {
    public static NewMenuSubCategoryActivity instance;
    public ArrayList<MenuSubCategory> mcatList = new ArrayList<MenuSubCategory>();
    public boolean isDownloadable = false;
    ListView menuSubCategoryList;
    ImageView imageView;
    MenuSubCategoryAdapter adapter;
    ProgressBarHandler p;
    Timer t = new Timer();
    int productCategoryId, productSubCategoryId;
    String productSubCategoryName;
    int image;
    private DrawerLayout drawerLayout;
    private android.support.v7.widget.Toolbar toolbar;

    public NewMenuSubCategoryActivity() {
    }

    public static NewMenuSubCategoryActivity sharedInstance() {

        if (instance == null) {
            instance = new NewMenuSubCategoryActivity();

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
            image = extras.getInt("image");
        }
        setContentView(R.layout.activity_new_menu_subcategory);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FBUtils.setUpNavigationDrawer(NewMenuSubCategoryActivity.this);

        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMenuSubCategoryActivity.this.finish();
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);
        if (image == 0) {
            imageView.setBackgroundResource(R.drawable.menu_ham);
        } else {
            imageView.setBackgroundResource(R.drawable.menu_burger);
        }
        p = new ProgressBarHandler(this);
        getMenuSubCategory();
        getMenuDrawerListProduct();
        toolbar.findViewById(R.id.menu_navigator).setOnClickListener(this);
        menuSubCategoryList = (ListView) findViewById(R.id.menuSubCategoryList);
        menuSubCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                productSubCategoryId = mcatList.get(position).getProductSubCategoryId();
                productSubCategoryName = mcatList.get(position).getProductSubCategoryName();
                if(StringUtilities.isValidString(productSubCategoryName))
                {
                    FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(productSubCategoryId), productSubCategoryName, FBEventSettings.FEATURE_PRODUCT_CLICK);
                }
                Intent intent = new Intent(NewMenuSubCategoryActivity.this, NewMenuDrawerListActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("productCategoryId", productCategoryId);
                extras.putInt("productSubCategoryId", productSubCategoryId);
                intent.putExtras(extras);
                startActivity(intent);

            }
        });

    }


    public void getMenuSubCategory() {
        p.show();
        final JSONObject object = new JSONObject();
        //  p.show();
        FBUserService.sharedInstance().getMenuSubCategory(object, FBPreferences.sharedInstance(NewMenuSubCategoryActivity.this).getStoreCode(), String.valueOf(productCategoryId), new FBUserService.FBMenuSubCategoryCallback() {

            public void onMenuSubCategoryCallback(JSONObject response, Exception error) {
                try {
                    if (response != null) {

                        initCategoryWithJson(response);
                        p.dismiss();
                    } else {
                        p.dismiss();
                    }
                } catch (Exception e) {

                }
            }

        });
    }

    public void initCategoryWithJson(JSONObject json) {
        p.show();
        try {

            JSONArray jsonArray = json.getJSONArray("productSubCategoryList");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject myProductObj = jsonArray.getJSONObject(i);
                    MenuSubCategory getStoresObj = new MenuSubCategory();
                    getStoresObj.initMenuSubCategory(myProductObj);
                    mcatList.add(getStoresObj);
                }
                isDownloadable = true;

                adapter = new MenuSubCategoryAdapter(this, mcatList);
                menuSubCategoryList.setAdapter(adapter);
                p.dismiss();
            }

        } catch (Exception e) {
        }
    }

    public void getMenuDrawerListProduct() {

        final JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getDrawerProductList(object, FBPreferences.sharedInstance(NewMenuSubCategoryActivity.this).getStoreCode(), String.valueOf(productCategoryId), String.valueOf(223), new FBUserService.FBMenuDrawerCallback() {

            public void onMenuDrawerCallback(JSONObject response, Exception error) {
                try {
                    if (response != null) {
                    } else {
                    }
                } catch (Exception e) {

                }
            }

        });
    }

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
    }

}
