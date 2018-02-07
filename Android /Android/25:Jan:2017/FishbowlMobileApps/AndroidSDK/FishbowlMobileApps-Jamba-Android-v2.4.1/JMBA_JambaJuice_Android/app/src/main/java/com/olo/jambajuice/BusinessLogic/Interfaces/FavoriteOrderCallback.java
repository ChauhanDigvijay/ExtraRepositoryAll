package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.FavoriteOrder;

import java.util.List;

/**
 * Created by VT017 on 3/14/2017.
 */

public interface FavoriteOrderCallback {
    public void onFavoriteCallback(List<FavoriteOrder> status, Exception e);
}
