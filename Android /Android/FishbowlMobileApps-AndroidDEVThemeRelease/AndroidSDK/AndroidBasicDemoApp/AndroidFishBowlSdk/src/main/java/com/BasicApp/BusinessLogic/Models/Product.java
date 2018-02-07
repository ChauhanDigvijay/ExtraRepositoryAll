package com.BasicApp.BusinessLogic.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
public	String successFlag;
public	String  categoryID;
public	String category;
public	String idesc;
public	String imageurl;
public String itemid;
public String iname;
public String icost;
 
	
	public Product() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void initWithJson(JSONObject json){ 
		try {
			  		successFlag=json.getString("successFlag");
			  		categoryID=json.getString("categoryID");
				    idesc=json.getString("idesc");
				    imageurl=json.getString("imageurl"); 
				    itemid=json.getString("itemid");
				    iname=json.getString("iname");
				    icost=json.getString("icost");
				    
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void isCategoryIdEqual(Product product){
		
		if(Integer.valueOf(categoryID)==Integer.valueOf(categoryID)){
			
			
		}
		
	}

}
