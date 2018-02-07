package com.BasicApp.Activites.NonGeneric.Menus.ProductCategoryDetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.BasicApp.Adapters.MenuSubCategoryAdapter;
import com.BasicApp.Adapters.MenuSubCategoryRecyclerViewAdapter;
import com.BasicApp.BusinessLogic.Models.MenuSubCategory;
import com.BasicApp.Utils.ProgressBarHandler;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBUserService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by schaudhary_ic on 29-Dec-16.
 */

public class NewMenuSubCategorywithRecyclerView extends Activity implements MenuSubCategoryRecyclerViewAdapter.MyClickListener {

    public ArrayList<MenuSubCategory> mcatList = new ArrayList<MenuSubCategory>();
    private RecyclerView mRecyclerView;
    private MenuSubCategoryRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";
    public static NewMenuSubCategorywithRecyclerView instance;
    public boolean isDownloadable=false;
    MenuSubCategoryAdapter adapter;
    ProgressBarHandler p;
    Timer t = new Timer();
    int productCategoryId;
    public static NewMenuSubCategorywithRecyclerView sharedInstance() {

        if (instance == null) {
            instance = new NewMenuSubCategorywithRecyclerView();

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

        }
        setContentView(R.layout.activity_new_menu_subcategory_recyclerview);
        p = new ProgressBarHandler(this);

        getMenuSubCategory();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

       /* RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);*/
    }

    public NewMenuSubCategorywithRecyclerView() {
    }

    public  void initCategoryWithJson(JSONObject json){
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
                mAdapter = new MenuSubCategoryRecyclerViewAdapter(mcatList);
                mRecyclerView.setAdapter(mAdapter);
                /*adapter = new MenuSubCategoryAdapter(this, mcatList);
                menuSubCategoryList.setAdapter(adapter);*/
                p.hide();
            }

        }
        catch (Exception e){}
    }

    public void getMenuSubCategory() {
        p.show();
        final JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getMenuSubCategory(object, String.valueOf(168589), String.valueOf(productCategoryId),  new FBUserService.FBMenuSubCategoryCallback() {

            public void onMenuSubCategoryCallback(JSONObject response, Exception error) {
                try {
                    if (response != null) {

                        initCategoryWithJson(response);
                        p.hide();
                    }
                    else {
                        p.hide();
                    }
                } catch (Exception e) {

                }
            }

        });
    }

    @Override
    public void onItemClick(int position, View v) {

    }


}
