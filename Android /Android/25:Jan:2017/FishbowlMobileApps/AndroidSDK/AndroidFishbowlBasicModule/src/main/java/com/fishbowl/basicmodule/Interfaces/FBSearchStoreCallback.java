package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBStoresItem;

import java.util.List;

/**
 * Created by digvijaychauhan
 */

/**
 * Created by digvijay(dj)
 */
public interface FBSearchStoreCallback {
    public void onClypSearchStore(List<FBStoresItem> response, Exception error);
}
