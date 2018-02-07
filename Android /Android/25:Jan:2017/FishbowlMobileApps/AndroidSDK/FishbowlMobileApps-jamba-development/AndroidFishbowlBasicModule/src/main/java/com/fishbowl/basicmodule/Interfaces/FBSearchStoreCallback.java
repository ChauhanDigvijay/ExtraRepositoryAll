package com.fishbowl.basicmodule.Interfaces;


import com.fishbowl.basicmodule.Models.FBStoreItem;

import java.util.List;

/**
 * Created by digvijay(dj)
 */

public interface FBSearchStoreCallback {
    public void onClypSearchStore(List<FBStoreItem> response, String error);
}
