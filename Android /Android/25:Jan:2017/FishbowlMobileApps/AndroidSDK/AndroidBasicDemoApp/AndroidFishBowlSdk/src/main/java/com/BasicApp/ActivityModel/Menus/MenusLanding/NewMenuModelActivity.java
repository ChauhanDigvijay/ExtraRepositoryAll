package com.BasicApp.ActivityModel.Menus.MenusLanding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.BasicApp.ActivityModel.Menus.ProductCategoryDetail.NewMenuSubCategoryModelActivity;

import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.ModelAdapters.MenuCategoryAdapter;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.BasicApp.Utils.StringUtilities;
import com.Preferences.FBPreferences;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Interfaces.FBMenuCategoryCallback;
import com.fishbowl.basicmodule.Models.FBMenuCategoryDetailItem;
import com.fishbowl.basicmodule.Models.FBMenuCategoryItem;
import com.fishbowl.basicmodule.Services.FBMenuService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

/**
 * Created by digvijay(dj)
 */

public class NewMenuModelActivity extends FragmentActivity implements View.OnClickListener {

    private Toolbar toolbar;
    public static NewMenuModelActivity instance;
    public List<FBMenuCategoryDetailItem> mcatList = new ArrayList<>();
    public boolean isDownloadable = false;
    ListView menuCategoryList;
    MenuCategoryAdapter adapter;

    ProgressBarHandler p;
    Timer t = new Timer();
    int productCategoryId;
    String productCategoryName;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FBUtils.setUpNavigationDrawerModel(NewMenuModelActivity.this);

        p = new ProgressBarHandler(this);
        getMenuCategory();

        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMenuModelActivity.this.finish();
            }
        });
        toolbar.findViewById(R.id.menu_navigator).setOnClickListener(this);
        menuCategoryList = (ListView) findViewById(R.id.menuCategoryList);
        menuCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productCategoryId = mcatList.get(position).getProductCategoryId();
                productCategoryName=  mcatList.get(position).getProductCategoryName();
                if(StringUtilities.isValidString(productCategoryName)) {
                    FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(productCategoryId), productCategoryName, FBEventSettings.CATEGORY_CLICK);
                }
                Intent intent = new Intent(NewMenuModelActivity.this, NewMenuSubCategoryModelActivity.class);
                Bundle extras = new Bundle();
                if (position == 0) {
                    extras.putInt("image", 0);
                } else if (position == 1) {
                    extras.putInt("image", 1);
                }
                extras.putInt("productCategoryId", productCategoryId);
                intent.putExtras(extras);
                startActivity(intent);

            }
        });

    }

    public NewMenuModelActivity() {
    }

    public static NewMenuModelActivity sharedInstance() {

        if (instance == null) {
            instance = new NewMenuModelActivity();
        }
        return instance;
    }


//
//    public void getMenuCategory() {
//        p.show();
//        final JSONObject object = new JSONObject();
//        FBUserService.sharedInstance().getMenuCategory(object,FBPreferences.sharedInstance(NewMenuModelActivity.this).getStoreCode(), new FBUserService.FBMenuCategoryCallback() {
//            public void onMenuCategoryCallback(JSONObject response, Exception error) {
//                try {
//                    if (response != null) {
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



    public void getMenuCategory() {
        p.show();
        FBMenuService.getMenuCategory(FBPreferences.sharedInstance(NewMenuModelActivity.this).getStoreCode(), new FBMenuCategoryCallback() {
            @Override
            public void onMenuCategoryCallback(FBMenuCategoryItem response, Exception error) {
                if (response != null)

                {
                    FBMenuCategoryDetailItem[] categories = response.getCategories();
                    mcatList= Arrays.asList(categories);
                    adapter = new MenuCategoryAdapter(NewMenuModelActivity.this, mcatList);
                    menuCategoryList.setAdapter(adapter);
                    p.dismiss();


                }
                else

                {
                    p.dismiss();
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

//    public void initCategoryWithJson(JSONObject json) {
//
//        try {
//
//            JSONArray jsonArray = json.getJSONArray("productCategoryList");
//            if (jsonArray != null) {
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject myProductObj = jsonArray.getJSONObject(i);
//                    MenuCategory getStoresObj = new MenuCategory();
//                    getStoresObj.initMenuCategory(myProductObj);
//                    mcatList.add(getStoresObj);
//                }
//                isDownloadable = true;
//
//                adapter = new MenuCategoryAdapter(this, mcatList);
//                menuCategoryList.setAdapter(adapter);
//
//            }
//
//        } catch (Exception e) {
//        }
//    }

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
