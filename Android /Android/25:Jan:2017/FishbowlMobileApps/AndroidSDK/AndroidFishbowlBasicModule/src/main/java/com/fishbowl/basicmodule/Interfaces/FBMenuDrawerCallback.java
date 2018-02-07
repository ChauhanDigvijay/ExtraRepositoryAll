package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBMenuDrawerItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */

public interface FBMenuDrawerCallback{
    public void onMenuDrawerCallback(FBMenuDrawerItem response, Exception error);
}