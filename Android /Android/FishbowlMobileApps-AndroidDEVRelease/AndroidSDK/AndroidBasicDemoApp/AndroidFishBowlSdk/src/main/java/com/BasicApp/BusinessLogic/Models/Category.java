package com.BasicApp.BusinessLogic.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Category {
	public	String successFlag;
	public	String  categoryid;
	public	String category;
	public	String categorydesc;

	public String getSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}

	public String getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategorydesc() {
		return categorydesc;
	}

	public void setCategorydesc(String categorydesc) {
		this.categorydesc = categorydesc;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	public	String imageurl;
	
	public ArrayList<Product> products=new ArrayList<Product>();
		
		public Category() {
			// TODO Auto-generated constructor stub
		}
		
		
		
		public void initWithJson(JSONObject json){ 
			try {
				  		successFlag=json.getString("successFlag");
				  		categoryid=json.getString("categoryid");
					    category=json.getString("category");
					    categorydesc=json.getString("categorydesc");
					    imageurl=json.getString("imageurl");
					    
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
		public void addProduct(Product product){
			products.add(product); 
		}
		 
		

}
