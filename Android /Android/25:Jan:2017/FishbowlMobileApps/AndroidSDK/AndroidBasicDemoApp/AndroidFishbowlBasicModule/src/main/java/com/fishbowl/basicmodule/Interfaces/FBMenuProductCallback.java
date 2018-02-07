package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBMenuProductItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */


public interface FBMenuProductCallback {
    public void onMenuProductCallback(FBMenuProductItem category , Exception error);
}