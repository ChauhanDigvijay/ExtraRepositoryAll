package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.wearehathway.apps.olo.Models.OloBasketValidation;

/**
 * Created by Nauman Afzaal on 08/06/15.
 */
public interface BasketValidationCallback {
    public void onBasketValidated(OloBasketValidation oloBasketValidation, Exception exception);
}
