package com.fishbowl.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public class OloBasketProductBatchResult
{
    private OloBasket basket;
    private ArrayList<OloBasketProductBatchError> errors;

    public OloBasket getBasket()
    {
        return basket;
    }

    public ArrayList<OloBasketProductBatchError> getErrors()
    {
        return errors;
    }
}
