package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBBonusItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */

public interface FBBonusRuleListCallback {
    public void onBonusRuleListCallback(FBBonusItem restaurants , Exception error);
}