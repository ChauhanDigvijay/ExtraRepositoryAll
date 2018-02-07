package com.BasicApp.ActivityModel.Menus.ProductCategoryDetail;

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

import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.BasicApp.ActivityModel.Menus.ProductDetail.NewMenuDrawerListModelActivity;

import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.ModelAdapters.MenuSubCategoryAdapter;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.BasicApp.Utils.StringUtilities;
import com.Preferences.FBPreferences;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Interfaces.FBMenuSubCategoryCallback;
import com.fishbowl.basicmodule.Models.FBMenuSubCategoryDetailItem;
import com.fishbowl.basicmodule.Models.FBMenuSubCategoryItem;
import com.fishbowl.basicmodule.Services.FBMenuService;
import com.fishbowl.basicmodule.Services.FBUserService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

/**
 * Created by digvijay(dj)
 */

public class NewMenuSubCategoryModelActivity extends Activity implements View.OnClickListener {
    public static NewMenuSubCategoryModelActivity instance;
    public List<FBMenuSubCategoryDetailItem> mcatList = new ArrayList<>();
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
    private Toolbar toolbar;

    public NewMenuSubCategoryModelActivity() {
    }

    public static NewMenuSubCategoryModelActivity sharedInstance() {

        if (instance == null) {
            instance = new NewMenuSubCategoryModelActivity();

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
        FBUtils.setUpNavigationDrawerModel(NewMenuSubCategoryModelActivity.this);

        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMenuSubCategoryModelActivity.this.finish();
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);
        if (image == 0) {
            imageView.setBackgroundResource(R.drawable.menu_ham);
        } else {
            imageView.setBackgroundResource(R.drawable.menu_burger);
        }
        p = new ProgressBarHandler(this);
        getSubMenuCategory();
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
                Intent intent = new Intent(NewMenuSubCategoryModelActivity.this, NewMenuDrawerListModelActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("productCategoryId", productCategoryId);
                extras.putInt("productSubCategoryId", productSubCategoryId);
                intent.putExtras(extras);
                startActivity(intent);

            }
        });

    }

//
//    public void getMenuSubCategory() {
//        p.show();
//        final JSONObject object = new JSONObject();
//        //  p.show();
//        FBUserService.sharedInstance().getMenuSubCategory(object, FBPreferences.sharedInstance(NewMenuSubCategoryModelActivity.this).getStoreCode(), String.valueOf(productCategoryId), new FBUserService.FBMenuSubCategoryCallback() {
//
//            public void onMenuSubCategoryCallback(JSONObject response, Exception error) {
//                try {
//                    if (response != null) {
//
//                        initCategoryWithJson(response);
//                        p.dismiss();
//                    } else {
//                        p.dismiss();
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//
//        });
//    }


    public void getSubMenuCategory() {
        p.show();
        FBMenuService.getMenuSubCategory(FBPreferences.sharedInstance(NewMenuSubCategoryModelActivity.this).getStoreCode(), String.valueOf(22), new FBMenuSubCategoryCallback() {
            @Override
            public void onMenuSubCategoryCallback(FBMenuSubCategoryItem response, Exception error) {
                if (response != null)

                {
                    FBMenuSubCategoryDetailItem[] subcategories = response.getCategories();
                    mcatList= Arrays.asList(subcategories);
                    adapter = new MenuSubCategoryAdapter(NewMenuSubCategoryModelActivity.this, mcatList);
                    menuSubCategoryList.setAdapter(adapter);


                    p.dismiss();

                } else {
                    p.dismiss();
                }
            }
        });
    }

//    public void initCategoryWithJson(JSONObject json) {
//        p.show();
//        try {
//
//            JSONArray jsonArray = json.getJSONArray("productSubCategoryList");
//            if (jsonArray != null) {
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject myProductObj = jsonArray.getJSONObject(i);
//                    MenuSubCategory getStoresObj = new MenuSubCategory();
//                    getStoresObj.initMenuSubCategory(myProductObj);
//                    mcatList.add(getStoresObj);
//                }
//                isDownloadable = true;
//
//                adapter = new MenuSubCategoryAdapter(this, mcatList);
//                menuSubCategoryList.setAdapter(adapter);
//                p.dismiss();
//            }
//
//        } catch (Exception e) {
//        }
//    }

    public void getMenuDrawerListProduct() {

        final JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getDrawerProductList(object, FBPreferences.sharedInstance(NewMenuSubCategoryModelActivity.this).getStoreCode(), String.valueOf(productCategoryId), String.valueOf(223), new FBUserService.FBMenuDrawerCallback() {

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
