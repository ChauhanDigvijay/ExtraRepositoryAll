package com.wearehathway.apps.incomm.Interfaces;



import com.wearehathway.apps.incomm.Models.InCommBrand;
import com.wearehathway.apps.incomm.Models.InCommCountry;
import com.wearehathway.apps.incomm.Models.InCommStates;

import java.util.List;

/**
 * Created by Nauman Afzaal on 13/08/15.
 */
public interface InCommBrandsCallback
{
    public void onAllBrandsCallback(List<InCommBrand> brands, List<InCommCountry> countries, List<InCommStates> states, Exception error);
}
