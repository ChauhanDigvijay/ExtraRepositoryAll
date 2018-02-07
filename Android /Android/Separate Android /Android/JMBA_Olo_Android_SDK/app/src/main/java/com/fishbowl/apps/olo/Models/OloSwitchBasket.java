package com.fishbowl.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by vthink on 04/08/16.
 */
public class OloSwitchBasket {

    private OloBasket basket;
    private ArrayList<OloBasketProduct> itemsnottransferred;

    public OloBasket getBasket() {
        return basket;
    }

    public void setBasket(OloBasket basket) {
        this.basket = basket;
    }

    public ArrayList<OloBasketProduct> getItemsnottransferred() {
        return itemsnottransferred;
    }

    public void setItemsnottransferred(ArrayList<OloBasketProduct> itemsnottransferred) {
        this.itemsnottransferred = itemsnottransferred;
    }
}
