package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBMenuSubCategoryItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */

public interface FBMenuSubCategoryCallback{
    public void onMenuSubCategoryCallback(FBMenuSubCategoryItem response, Exception error);
}