package com.BasicApp.Activites.NonGeneric.Menus.MenusLanding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.Activites.NonGeneric.Menus.ProductCategoryDetail.NewMenuSubCategoryModelActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.ModelAdapters.MenuCategoryAdapter;
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

/**
 * Created by digvijay(dj)
 */

public class NewMenuModelActivity extends BaseActivity implements View.OnClickListener {

    public static NewMenuModelActivity instance;
    public List<FBMenuCategoryDetailItem> mcatList = new ArrayList<>();
    ListView menuCategoryList;
    MenuCategoryAdapter adapter;

    int productCategoryId;
    String productCategoryName;
    private DrawerLayout drawerLayout;

    public NewMenuModelActivity() {
    }

    public static NewMenuModelActivity sharedInstance() {

        if (instance == null) {
            instance = new NewMenuModelActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu);

        getMenuCategory();

        setUpToolBar(true,true);
        setTitle("MENU");
        setBackButton(true,false);


        menuCategoryList = (ListView) findViewById(R.id.menuCategoryList);
        menuCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productCategoryId = mcatList.get(position).getProductCategoryId();
                productCategoryName = mcatList.get(position).getProductCategoryName();
                if (StringUtilities.isValidString(productCategoryName)) {
                    FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(productCategoryId), productCategoryName, FBEventSettings.CATEGORY_CLICK);
                }
                NewMenuModelActivity.this.overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

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


    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public void onClick(View v) {

    }

    public void getMenuCategory() {
        enableScreen(false);
        FBMenuService.getMenuCategory(FBPreferences.sharedInstance(NewMenuModelActivity.this).getStoreCode(), new FBMenuCategoryCallback() {
            @Override
            public void onMenuCategoryCallback(FBMenuCategoryItem response, Exception error) {
                if (response != null)

                {
                    FBMenuCategoryDetailItem[] categories = response.getCategories();
                    mcatList = Arrays.asList(categories);
                    adapter = new MenuCategoryAdapter(NewMenuModelActivity.this, mcatList);
                    menuCategoryList.setAdapter(adapter);
                    enableScreen(true);


                } else

                {
                    enableScreen(true);
                }
            }
        });
    }

}
