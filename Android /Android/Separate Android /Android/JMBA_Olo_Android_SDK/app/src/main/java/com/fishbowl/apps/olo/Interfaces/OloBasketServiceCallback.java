package com.fishbowl.apps.olo.Interfaces;

import com.fishbowl.apps.olo.Models.OloBasket;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public interface OloBasketServiceCallback
{
    public void onBasketServiceCallback(OloBasket basket, Exception error);
}
