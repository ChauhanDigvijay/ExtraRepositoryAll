package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBMenuCategoryItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */
public interface FBMenuCategoryCallback{
    public void onMenuCategoryCallback(FBMenuCategoryItem response, Exception error);
}