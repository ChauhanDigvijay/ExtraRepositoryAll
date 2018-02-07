package com.BasicApp.BusinessLogic.Models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by schaudhary_ic on 28-Dec-16.
 */

public class MenuSubCategory implements Serializable {
    int productSubCategoryId;
    String productSubCategoryName;
    String productSubCategoryDescription;
    Boolean productSubCategoryImageEnable;
    String productSubCategoryImageUrl;
    Boolean productSubCategoryThumbImageEnable;
    String productSubCategoryThumbImageUrl;
    Boolean active;
    int displayOrder;
    String createdDate;
    String createdBy;
    String lastUpdatedDate;
    String lastUpdatedBy;

    public static MenuSubCategory instance ;

    public static MenuSubCategory sharedInstance(){
        if(instance==null){
            instance=new MenuSubCategory();
        }
        return instance;
    }

    public MenuSubCategory(){}
    public void initMenuSubCategory(JSONObject object) {
        try {
            productSubCategoryId = object.has("productSubCategoryId") ? object.getInt("productSubCategoryId") : 0;
            productSubCategoryName = object.has("productSubCategoryName") ? object.getString("productSubCategoryName") : null;
            productSubCategoryDescription = object.has("productSubCategoryDescription") ? object.getString("productSubCategoryDescription") : null;
            productSubCategoryImageEnable = object.has("productSubCategoryImageEnable") ? object.getBoolean("productSubCategoryImageEnable"):false;
            productSubCategoryImageUrl = object.has("productSubCategoryImageUrl") ? object.getString("productSubCategoryImageUrl"):null;
            productSubCategoryThumbImageEnable = object.has("productSubCategoryThumbImageEnable") ? object.getBoolean("productSubCategoryThumbImageEnable"):false;
            productSubCategoryThumbImageUrl = object.has("productSubCategoryThumbImageUrl") ? object.getString("productSubCategoryThumbImageUrl"):null;
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

    public void setProductSubCategoryId(int productSubCategoryId){this.productSubCategoryId = productSubCategoryId;}
    public int getProductSubCategoryId(){return productSubCategoryId;}

    public void setProductSubCategoryName(String productSubCategoryName){this.productSubCategoryName = productSubCategoryName;}
    public String getProductSubCategoryName(){return  productSubCategoryName;}

    public void setProductSubCategoryDescription(String productSubCategoryDescription){this.productSubCategoryDescription = productSubCategoryDescription;}
    public  String getProductSubCategoryDescription(){return  productSubCategoryDescription;}

    public void setProductSubCategoryImageEnable(Boolean productSubCategoryImageEnable){this.productSubCategoryImageEnable = productSubCategoryImageEnable;}
    public Boolean getProductSubCategoryImageEnable(){return  productSubCategoryImageEnable;}

    public void  setProductSubCategoryImageUrl(String productSubCategoryImageUrl){this.productSubCategoryImageUrl = productSubCategoryImageUrl;}
    public  String getProductSubCategoryImageUrl(){return productSubCategoryImageUrl;}

    public void setProductSubCategoryThumbImageEnable(Boolean productSubCategoryThumbImageEnable){this.productSubCategoryThumbImageEnable=productSubCategoryThumbImageEnable;}
    public boolean getProductSubCategoryThumbImageEnable(){return  productSubCategoryThumbImageEnable;}

    public void setProductSubCategoryThumbImageUrl(String productSubCategoryThumbImageUrl){this.productSubCategoryThumbImageUrl = productSubCategoryThumbImageUrl;}
    public  String getProductSubCategoryThumbImageUrl(){return  productSubCategoryThumbImageUrl;}

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
