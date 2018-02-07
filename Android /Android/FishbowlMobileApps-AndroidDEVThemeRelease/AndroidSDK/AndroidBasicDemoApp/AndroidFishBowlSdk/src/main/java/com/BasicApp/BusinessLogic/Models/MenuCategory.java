package com.BasicApp.BusinessLogic.Models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by schaudhary_ic on 26-Dec-16.
 */

public class MenuCategory implements Serializable {
    int productCategoryId;
    String productCategoryName;
    String productCategoryDescription;
    boolean productCategoryImageEnable;
    String productCategoryImageUrl;
    boolean productCategoryThumbImageEnable;
    String productCategoryThumbImageUrl;
    boolean active;
    int displayOrder;
    String createdDate;
    String createdBy;
    String lastUpdatedDate;
    String lastUpdatedBy;

    public static MenuCategory instance ;

    public static MenuCategory sharedInstance(){
        if(instance==null){
            instance=new MenuCategory();
        }
        return instance;
    }

    public MenuCategory(){}
    public void initMenuCategory(JSONObject object) {
        try {
            productCategoryId = object.has("productCategoryId") ? object.getInt("productCategoryId") : 0;
            productCategoryName = object.has("productCategoryName") ? object.getString("productCategoryName") : null;
            productCategoryDescription = object.has("productCategoryDescription") ? object.getString("productCategoryDescription") : null;
            productCategoryImageEnable = object.has("productCategoryImageEnable") ? object.getBoolean("productCategoryImageEnable"):false;
            productCategoryImageUrl = object.has("productCategoryImageUrl") ? object.getString("productCategoryImageUrl"):null;
            productCategoryThumbImageEnable = object.has("productCategoryThumbImageEnable") ? object.getBoolean("productCategoryThumbImageEnable"):false;
            productCategoryThumbImageUrl = object.has("productCategoryThumbImageUrl") ? object.getString("productCategoryThumbImageUrl"):null;
            active = object.has("active") ? object.getBoolean("active"):false;
            displayOrder = object.has("displayOrder") ? object.getInt("displayOrder"):0;
            createdDate = object.has("createdDate") ? object.getString("createdDate"):null;
            createdBy = object.has("createdBy") ? object.getString("createdBy"):null;
            lastUpdatedDate = object.has("lastUpdatedDate") ? object.getString("lastUpdatedDate"):null;
            lastUpdatedBy = object.has("lastUpdatedBy") ? object.getString("lastUpdatedBy"):null;
        }
        catch (Exception e)
        {
        }
    }

    public void setProductCategoryId(int productCategoryId){this.productCategoryId = productCategoryId;}
    public int getProductCategoryId(){return productCategoryId;}

    public void setProductCategoryName(String productCategoryName){this.productCategoryName = productCategoryName;}
    public String getProductCategoryName(){return  productCategoryName;}

    public void setProductCategoryDescription(String productCategoryDescription){this.productCategoryDescription = productCategoryDescription;}
    public  String getProductCategoryDescription(){return  productCategoryDescription;}

    public void setProductCategoryImageEnable(Boolean productCategoryImageEnable){this.productCategoryImageEnable = productCategoryImageEnable;}
    public Boolean getProductCategoryImageEnable(){return  productCategoryImageEnable;}

    public void  setProductCategoryImageUrl(String productCategoryImageUrl){this.productCategoryImageUrl = productCategoryImageUrl;}
    public  String getProductCategoryImageUrl(){return productCategoryImageUrl;}

    public void setProductCategoryThumbImageEnable(Boolean productCategoryThumbImageEnable){this.productCategoryThumbImageEnable=productCategoryThumbImageEnable;}
    public boolean getProductCategoryThumbImageEnable(){return  productCategoryThumbImageEnable;}

    public void setProductCategoryThumbImageUrl(String productCategoryThumbImageUrl){this.productCategoryThumbImageUrl = productCategoryThumbImageUrl;}
    public  String getProductCategoryThumbImageUrl(){return  productCategoryThumbImageUrl;}

    public void setActive(Boolean active){this.active = active;}
    public Boolean getActive(){return active;}

    public void  setDisplayOrder(int displayOrder){this.displayOrder = displayOrder;}
    public int getDisplayOrder(){return  displayOrder;}

    public  void setCreatedDate(String createdDate){this.createdDate = createdDate;}
    public String getCreatedDate(){return createdDate;}

    public void setCreatedBy(String createdBy){this.createdBy= createdBy;}
    public String getCreatedBy(){return createdBy;}

    public void setLastUpdatedDate(String lastUpdatedDate){this.lastUpdatedDate = lastUpdatedDate;}
    public String getLastUpdatedDate(){return lastUpdatedDate;}

    public void setLastUpdatedBy(String lastUpdatedBy){this.lastUpdatedBy = lastUpdatedBy;}
    public String getLastUpdatedBy(){return lastUpdatedBy;}

}
