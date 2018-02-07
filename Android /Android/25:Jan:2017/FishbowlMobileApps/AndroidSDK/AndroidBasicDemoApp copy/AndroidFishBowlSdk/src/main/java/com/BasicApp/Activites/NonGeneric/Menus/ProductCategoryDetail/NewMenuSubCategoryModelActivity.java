package com.BasicApp.Activites.NonGeneric.Menus.ProductCategoryDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.BasicApp.Activites.Generic.BaseActivity;
import com.BasicApp.Activites.NonGeneric.Menus.ProductDetail.NewMenuDrawerListModelActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.ModelAdapters.MenuSubCategoryAdapter;
import com.BasicApp.Utils.StringUtilities;
import com.Preferences.FBPreferences;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Interfaces.FBMenuSubCategoryCallback;
import com.fishbowl.basicmodule.Models.FBMenuSubCategoryDetailItem;
import com.fishbowl.basicmodule.Models.FBMenuSubCategoryItem;
import com.fishbowl.basicmodule.Services.FBMenuService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

/**
 * Created by digvijay(dj)
 */

public class NewMenuSubCategoryModelActivity extends BaseActivity implements View.OnClickListener {
    public static NewMenuSubCategoryModelActivity instance;
    public List<FBMenuSubCategoryDetailItem> mcatList = new ArrayList<>();
    public boolean isDownloadable = false;
    ListView menuSubCategoryList;
    ImageView imageView;
    MenuSubCategoryAdapter adapter;

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

        imageView = (ImageView) findViewById(R.id.imageView);
        if (image == 0) {
            imageView.setBackgroundResource(R.drawable.menu_ham);
        } else {
            imageView.setBackgroundResource(R.drawable.menu_burger);
        }

        getSubMenuCategory();
        setUpToolBar(true,true);
        setTitle("SUBMENU");
        setBackButton(true,false);
        menuSubCategoryList = (ListView) findViewById(R.id.menuSubCategoryList);
        menuSubCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                productSubCategoryId = mcatList.get(position).getProductSubCategoryId();
                productSubCategoryName = mcatList.get(position).getProductSubCategoryName();
                if (StringUtilities.isValidString(productSubCategoryName)) {
                    FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(productSubCategoryId), productSubCategoryName, FBEventSettings.FEATURE_PRODUCT_CLICK);
                }
                NewMenuSubCategoryModelActivity.this.overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

                Intent intent = new Intent(NewMenuSubCategoryModelActivity.this, NewMenuDrawerListModelActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("productCategoryId", productCategoryId);
                extras.putInt("productSubCategoryId", productSubCategoryId);
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

    public void getSubMenuCategory() {
        enableScreen(false);
        FBMenuService.getMenuSubCategory(FBPreferences.sharedInstance(NewMenuSubCategoryModelActivity.this).getStoreCode(), String.valueOf(22), new FBMenuSubCategoryCallback() {
            @Override
            public void onMenuSubCategoryCallback(FBMenuSubCategoryItem response, Exception error) {
                if (response != null)

                {
                    FBMenuSubCategoryDetailItem[] subcategories = response.getCategories();
                    mcatList = Arrays.asList(subcategories);
                    adapter = new MenuSubCategoryAdapter(NewMenuSubCategoryModelActivity.this, mcatList);
                    menuSubCategoryList.setAdapter(adapter);


                    enableScreen(true);

                } else {
                    enableScreen(true);
                }
            }
        });
    }
}
