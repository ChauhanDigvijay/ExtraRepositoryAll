package com.fishbowl.basicmodule.Interfaces;

import com.fishbowl.basicmodule.Models.FBDigitalEventListItem;
import com.fishbowl.basicmodule.Models.FBMobileSettingListItem;

/**
 * Created by digvijaychauhan on 31/08/17.
 */

public interface FBSettingCallback
{
    public void onFBSettingCallback(FBMobileSettingListItem mobilesetting, FBDigitalEventListItem digitalevent , Exception error);
}
