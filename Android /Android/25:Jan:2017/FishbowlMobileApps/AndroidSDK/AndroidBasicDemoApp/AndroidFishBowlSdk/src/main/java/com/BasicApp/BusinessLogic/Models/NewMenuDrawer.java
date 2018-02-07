package com.BasicApp.BusinessLogic.Models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by schaudhary_ic on 03-Jan-17.
 */

public class NewMenuDrawer  implements Serializable {

    int id;
    String productName;
    String productLongDescription;
    String productShortDescription;
    String productTagDescription;
    boolean productImageEnable;
    String productImageUrl;
    boolean productThumbImageEnable;
    String productThumbImageUrl;
    double productBasePrice;
    double productBaseVolume;
    double productBaseWeight;
    boolean active;
    String created;
    String createdBy;
    String lastUpdated;
    String lastUpdatedBy;

    public long quantity = 0;
    private Double ext = 0.00;
    public Double price = 0.00;


    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public static NewMenuDrawer instance ;

    public static NewMenuDrawer sharedInstance(){
        if(instance==null){
            instance=new NewMenuDrawer();
        }
        return instance;
    }

    public NewMenuDrawer(){}

    public void initMenuDrawer(JSONObject object) {
        try {
            id = object.has("id") ? object.getInt("id") : 0;
            productName = object.has("productName") ? object.getString("productName") : null;
            productLongDescription = object.has("productLongDescription") ? object.getString("productLongDescription") : null;
            productShortDescription = object.has("productShortDescription") ? object.getString("productShortDescription"):null;
            productTagDescription = object.has("productTagDescription") ? object.getString("productTagDescription"):null;
            productImageEnable = object.has("productImageEnable") ? object.getBoolean("productImageEnable"):false;
            productImageUrl = object.has("productImageUrl") ? object.getString("productImageUrl"):null;
            productThumbImageEnable = object.has("productThumbImageEnable") ? object.getBoolean("productThumbImageEnable"):false;
            productThumbImageUrl = object.has("productThumbImageUrl") ? object.getString("productThumbImageUrl"):null;
            productBasePrice = object.has("productBasePrice") ? object.getDouble("productBasePrice"):0.0;
            productBaseVolume = object.has("productBaseVolume") ? object.getDouble("productBaseVolume"):0.0;
            productBaseWeight = object.has("productBaseWeight") ? object.getDouble("productBaseWeight"):0.0;
            active = object.has("active") ? object.getBoolean("active"):false;
            created = object.has("created") ? object.getString("created"):null;
            createdBy = object.has("createdBy") ? object.getString("createdBy"):null;
            lastUpdated = object.has("lastUpdated") ? object.getString("lastUpdated"):null;
            lastUpdatedBy = object.has("lastUpdatedBy") ? object.getString("lastUpdatedBy"):null;}
        catch (Exception e)
        {}
    }
    public void setId(int id){this.id = id;}
    public int getId(){return id;}

    public void setProductName(String productName){this.productName = productName;}
    public String getProductName(){return productName;}

    public  void setProductLongDescription(String productLongDescription){this.productLongDescription = productLongDescription;}
    public String getProductLongDescription(){return productLongDescription;}

    public void setProductShortDescription(String productShortDescription){this.productShortDescription = productShortDescription;}
    public String getProductShortDescription(){return  productShortDescription;}

    public  void setProductTagDescription(String productTagDescription){this.productTagDescription = productTagDescription;}
    public String getProductTagDescription(){return productTagDescription;}

    public void  setProductImageEnable(Boolean productImageEnable){this.productImageEnable = productImageEnable;}
    public Boolean getProductImageEnable(){return productImageEnable;}

    public void setProductImageUrl (String productImageUrl){this.productImageUrl = productImageUrl;}
    public String getProductImageUrl(){return productImageUrl;}

    public void setProductThumbImageEnable(Boolean productThumbImageEnable){this.productThumbImageEnable = productThumbImageEnable;}
    public Boolean getProductThumbImageEnable(){return productThumbImageEnable;}

    public void setProductThumbImageUrl(String productThumbImageUrl){this.productThumbImageUrl = productThumbImageUrl;}
    public String getProductThumbImageUrl(){return productThumbImageUrl;}

    public void setProductBasePrice(Double productBasePrice){this.productBasePrice = productBasePrice;}
    public Double getProductbasePrice(){return productBasePrice;}

    public  void setProductBaseVolume(Double productBaseVolume){this.productBaseVolume = productBaseVolume;}
    public Double getProductBaseVolume(){return  productBaseVolume;}

    public void setProductBaseWeight(Double productBaseWeight){this.productBaseWeight =  productBaseWeight;}
    public Double getProductBaseWeight(){return productBaseWeight;}

    public void setActive(Boolean active){this.active = active;}
    public Boolean getActive(){return active;}

    public  void setCreated(String created){this.created = created;}
    public String getCreated(){return created;}

    public void setCreatedBy(String createdBy){this.createdBy= createdBy;}
    public String getCreatedBy(){return createdBy;}

    public void setLastUpdated(String lastUpdated){this.lastUpdated = lastUpdated;}
    public String getLastUpdated(){return lastUpdated;}

    public void setLastUpdatedBy(String lastUpdatedBy){this.lastUpdatedBy = lastUpdatedBy;}
    public String getLastUpdatedBy(){return lastUpdatedBy;}

    public Double getExt() {
        return ext;
    }

    public void setExt(Double ext) {
        this.ext = ext;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}

