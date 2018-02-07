package com.BasicApp.Activites.NonGeneric.Menus.MenusLanding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.BasicApp.Activites.NonGeneric.Menus.ProductCategoryDetail.NewMenuSubCategoryActivity;
import com.BasicApp.Adapters.MenuCategoryAdapter;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Models.MenuCategory;
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

public class NewMenuActivity extends FragmentActivity implements View.OnClickListener {

    private android.support.v7.widget.Toolbar toolbar;
    public static NewMenuActivity instance;
    public ArrayList<MenuCategory> mcatList = new ArrayList<MenuCategory>();
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
        FBUtils.setUpNavigationDrawer(NewMenuActivity.this);

        p = new ProgressBarHandler(this);
        getMenuCategory();

        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMenuActivity.this.finish();
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
                Intent intent = new Intent(NewMenuActivity.this, NewMenuSubCategoryActivity.class);
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

    public NewMenuActivity() {
    }

    public static NewMenuActivity sharedInstance() {

        if (instance == null) {
            instance = new NewMenuActivity();
        }
        return instance;
    }



    public void getMenuCategory() {
        p.show();
        final JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getMenuCategory(object,FBPreferences.sharedInstance(NewMenuActivity.this).getStoreCode(), new FBUserService.FBMenuCategoryCallback() {
            public void onMenuCategoryCallback(JSONObject response, Exception error) {
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

    @Override
    protected void onResume() {
        super.onResume();
        BottomToolbarActivity b = (BottomToolbarActivity) findViewById(R.id.bottom_toolbar);
        b.initBottomToolbar();

    }

    public void initCategoryWithJson(JSONObject json) {

        try {

            JSONArray jsonArray = json.getJSONArray("productCategoryList");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject myProductObj = jsonArray.getJSONObject(i);
                    MenuCategory getStoresObj = new MenuCategory();
                    getStoresObj.initMenuCategory(myProductObj);
                    mcatList.add(getStoresObj);
                }
                isDownloadable = true;

                adapter = new MenuCategoryAdapter(this, mcatList);
                menuCategoryList.setAdapter(adapter);

            }

        } catch (Exception e) {
        }
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
