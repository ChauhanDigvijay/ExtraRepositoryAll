package com.BasicApp.BusinessLogic.Services;

import android.app.Activity;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Interfaces.FBMenuCategoryCallback;
import com.fishbowl.basicmodule.Interfaces.FBMenuDrawerCallback;
import com.fishbowl.basicmodule.Interfaces.FBMenuProductCallback;
import com.fishbowl.basicmodule.Interfaces.FBMenuSubCategoryCallback;
import com.fishbowl.basicmodule.Models.FBMenuCategoryItem;
import com.fishbowl.basicmodule.Models.FBMenuDrawerDetailItem;
import com.fishbowl.basicmodule.Models.FBMenuDrawerItem;
import com.fishbowl.basicmodule.Models.FBMenuProductItem;
import com.fishbowl.basicmodule.Models.FBMenuSubCategoryDetailItem;
import com.fishbowl.basicmodule.Models.FBMenuSubCategoryItem;
import com.fishbowl.basicmodule.Services.FBMenuService;


/**
 * Created by digvijay(dj)
 */
public class FBAMenuService {

    public static FBAMenuService instance=null;
    public static Activity mContext;
    public static FBAMenuService sharedInstance(Activity context){

        if(instance==null){
            instance=new FBAMenuService(context);
        }

        return  instance;
    }
    public FBAMenuService(Activity context)
    {
        if(context == null);
        mContext = context;
    }




    public void getMenuCategory() {

        FBMenuService.getMenuCategory(FBPreferences.sharedInstance(mContext).getStoreCode(), new FBMenuCategoryCallback() {
            @Override
            public void onMenuCategoryCallback(FBMenuCategoryItem response, Exception error) {
                if (response != null)

                {



                } else

                {

                }
            }
        });
    }


    public void getSubMenuCategory() {

        FBMenuService.getMenuSubCategory(FBPreferences.sharedInstance(mContext).getStoreCode(), String.valueOf(22), new FBMenuSubCategoryCallback() {
            @Override
            public void onMenuSubCategoryCallback(FBMenuSubCategoryItem response, Exception error) {
                if (response != null)

                {
                    FBMenuSubCategoryDetailItem[] subcategories = response.getCategories();


                } else {

                }
            }
        });
    }


    public void getMenuDrawerListProduct() {


        FBMenuService.getDrawerProductList(FBPreferences.sharedInstance(mContext).getStoreCode(), String.valueOf(22), String.valueOf(223), new FBMenuDrawerCallback() {
            @Override
            public void onMenuDrawerCallback(FBMenuDrawerItem response, Exception error) {
                if (response != null)

                {
                    FBMenuDrawerDetailItem[] categories = response.getCategories();

                } else {

                }
            }
        });
    }

    public void getMenuProductDetail() {

        FBMenuService.getMenuProduct(String.valueOf(168589), String.valueOf(22), String.valueOf(223), String.valueOf(1017), new FBMenuProductCallback() {
            @Override
            public void onMenuProductCallback(FBMenuProductItem category, Exception error) {
                if (category != null)

                {


                } else {

                }
            }
        });
    }



}
