package com.wearehathway.apps.olo.Interfaces;

import com.wearehathway.apps.olo.Models.OloBillingScheme;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 18/06/15.
 */
public interface OloBillingSchemeServiceCallback
{
    public void onBillingSchemeServiceCallback(ArrayList<OloBillingScheme> oloBillingSchemes, Exception error);
}
