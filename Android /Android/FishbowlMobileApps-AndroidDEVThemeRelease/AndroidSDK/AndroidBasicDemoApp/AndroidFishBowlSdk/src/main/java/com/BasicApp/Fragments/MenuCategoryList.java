package com.BasicApp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.BasicApp.Adapters.MenuCategoryAdapter;
import com.BasicApp.BusinessLogic.Models.MenuCategory;
import com.BasicApp.Utils.ProgressBarHandler;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBUserService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by schaudhary_ic on 26-Dec-16.
 */

public class MenuCategoryList extends Fragment {

    public ArrayList<MenuCategory> mcatList = new ArrayList<MenuCategory>();
    ListView menuCategoryList;
    public static MenuCategoryList instance;
    public boolean isDownloadable=false;
    MenuCategoryAdapter adapter;
    ProgressBarHandler p;
    Timer t = new Timer();
    public static MenuCategoryList sharedInstance() {

        if (instance == null) {
            instance = new MenuCategoryList();

        }
        return instance;
    }

    public MenuCategoryList() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_menu_category,container,false);
        p = new ProgressBarHandler(getContext());

        getMenuCategory();
        menuCategoryList  = (ListView) v.findViewById(R.id.menuCategoryList);
        menuCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
       /* adapter = new MenuCategoryAdapter(getContext(), mcatList);
        if (mcatList!=null&&mcatList.size()>0) {

            menuCategoryList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }*/
/*
        TimerTask task = new TimerTask() {

            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        menuCategoryList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                });
            }
        };  t.scheduleAtFixedRate(task, 0, 3000);*/
        return v;
    }

    public  void initCategoryWithJson(JSONObject json){
        p.show();
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

              adapter = new MenuCategoryAdapter(getContext(), mcatList);
          menuCategoryList.setAdapter(adapter);
                p.hide();
            }

        }
        catch (Exception e){}
    }

    public void getMenuCategory() {
        p.show();
        final JSONObject object = new JSONObject();
        //  p.show();
        FBUserService.sharedInstance().getMenuCategory(object, String.valueOf(104), new FBUserService.FBMenuCategoryCallback() {

            public void onMenuCategoryCallback(JSONObject response, Exception error) {
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
