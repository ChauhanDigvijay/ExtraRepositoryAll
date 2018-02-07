package com.olo.jambajuice.BusinessLogic.Models;

import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.Utils.Utils;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by Nauman on 15/05/15.
 */
public class Product implements Serializable
{

    private String name;
    private String desc;
    private int productId;
    private String ingredients;
    private String orderImageUrl;
    private String thumbImageUrl;
    private String imageUrl;
    private String timePlaced;

    //For Olo Product
    private StoreMenuProduct storeMenuProduct;


    public Product(ParseObject object)
    {
        this.name = object.getString("name");
        this.desc = object.getString("desc");
        this.productId = object.getInt("productId");
        this.imageUrl = object.getString("imageUrl");
        this.orderImageUrl = object.getString("orderImageUrl");
        this.thumbImageUrl = object.getString("thumbImageUrl");
        this.ingredients = Utils.getUnescapedString(object.getString("ingredients")).toLowerCase();
    }

    public String getName()
    {
        return name;
    }

    public String getDesc()
    {
        return desc;
    }

    public String getIngredients()
    {
        return ingredients;
    }

    public int getProductId()
    {
        return productId;
    }

    // Olo Specific Details of product
    public StoreMenuProduct getStoreMenuProduct()
    {
        return DataManager.getInstance().getProductInStoreWithChainProductId(productId);
    }

    public String getOrderImageUrl()
    {
        return orderImageUrl;
    }

    public String getThumbImageUrl()
    {
        return thumbImageUrl;
    }

    public String getTimePlaced() {
        return timePlaced;
    }

    public void setTimePlaced(String timePlaced) {
        this.timePlaced = timePlaced;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }
}
