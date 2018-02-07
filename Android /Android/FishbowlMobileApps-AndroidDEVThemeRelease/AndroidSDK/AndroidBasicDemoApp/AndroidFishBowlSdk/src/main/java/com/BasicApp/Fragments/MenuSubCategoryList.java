package com.BasicApp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.BasicApp.Adapters.MenuSubCategoryAdapter;
import com.BasicApp.BusinessLogic.Models.MenuSubCategory;
import com.BasicApp.Utils.ProgressBarHandler;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBUserService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by schaudhary_ic on 28-Dec-16.
 */

public class MenuSubCategoryList extends Fragment{
    public ArrayList<MenuSubCategory> mcatList = new ArrayList<MenuSubCategory>();
    ListView menuSubCategoryList;
    public static MenuSubCategoryList instance;
    public boolean isDownloadable=false;
    MenuSubCategoryAdapter adapter;
    ProgressBarHandler p;
    Timer t = new Timer();
    public static MenuSubCategoryList sharedInstance() {

        if (instance == null) {
            instance = new MenuSubCategoryList();

        }
        return instance;
    }

    public MenuSubCategoryList() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_menu_category,container,false);
        p = new ProgressBarHandler(getContext());

        getMenuSubCategory();
        menuSubCategoryList  = (ListView) v.findViewById(R.id.menuCategoryList);

        return v;
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

                adapter = new MenuSubCategoryAdapter(getContext(), mcatList);
                menuSubCategoryList.setAdapter(adapter);
                p.hide();
            }

        }
        catch (Exception e){}
    }

    public void getMenuSubCategory() {
        p.show();
        final JSONObject object = new JSONObject();
        //  p.show();
        FBUserService.sharedInstance().getMenuSubCategory(object, String.valueOf(104), String.valueOf(16), new FBUserService.FBMenuSubCategoryCallback() {

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

}
