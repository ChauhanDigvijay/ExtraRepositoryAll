package com.BasicApp.BusinessLogic.Models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by schaudhary_ic on 14-Feb-17.
 */

public class MenuProductDetail implements Serializable {

    int productPrice;

 public  MenuProductDetail(){}
    public  void initProductDetail(JSONObject jsonObj){
        try{

            productPrice = jsonObj.has("productPrice")? jsonObj.getInt("productPrice"):0;

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setProductPrice(int productPrice){this.productPrice = productPrice;}
    public int getProductPrice(){return productPrice;}




}