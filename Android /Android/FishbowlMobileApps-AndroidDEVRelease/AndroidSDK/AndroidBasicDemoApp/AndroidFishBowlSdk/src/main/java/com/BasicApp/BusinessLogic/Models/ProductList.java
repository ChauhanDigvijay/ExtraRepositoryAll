package com.BasicApp.BusinessLogic.Models;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductList {
	
	public String type;
	public String message;
	public String successFlag;
	public ArrayList<Category> categories=new ArrayList<Category>();
	
	 
	private static ProductList instance ;
	
	public boolean isDownloadable=false;
	 
	public ProductList() {
		// TODO Auto-generated constructor stub
	}
	
	public static ProductList sharedInstance(){
		
		if(instance==null){
			instance=new ProductList() ;
			
		}
		return instance; 
	}
	
	
	
	public  void initCategoryWithJson(JSONObject json){
		try {
			type=json.getString("type");
		
		message=json.getString("message");
		successFlag=json.getString("successFlag");
		
		JSONArray arr=json.getJSONArray("categories"); 
		
		for (int i = 0; i < arr.length(); i++) {
			JSONObject jsonObj=arr.getJSONObject(i);
			Category category=new Category(); 
			category.initWithJson(jsonObj);
			categories.add(category); 
		}
		
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}
	
	public void getCategory(final Context context,final ProductListCallBack callback){
		
		RequestQueue queue = Volley.newRequestQueue(context);

		JSONObject userJSON = new JSONObject();

		JSONObject requestObj = new JSONObject();
		try {
			requestObj.put("data", userJSON);
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET,"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/menu/104", requestObj,
				new Response.Listener<JSONObject>() 
				{

					@Override
					public void onResponse(JSONObject arg0) {
						JSONObject obj;
						 
							  
						     initCategoryWithJson(arg0); 
						     getProduct(context,callback);
					 
						
					}},new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if(error != null){

							error.printStackTrace(System.out);

						}
					}
				});
		
		
		queue.add(jsObjRequest);
		
	}
	
	
	
	
	
	
	public  void initProductWithJson(JSONObject json){
		try {
			type=json.getString("type");
		
		message=json.getString("message");
		successFlag=json.getString("successFlag");
		
		JSONArray arr=json.getJSONArray("categories"); 
		
		for (int i = 0; i < arr.length(); i++) {
			JSONObject jsonObj=arr.getJSONObject(i);
		 Product product=new Product();
			product.initWithJson(jsonObj);
			Category category=findCategory(product);

			category.products.add(product);
		}
		
		}
		catch (JSONException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}
	
	
	
	public Category findCategory(Product product){
		Category cat=null;
		
		for (int i = 0; i < categories.size(); i++) {
			cat=categories.get(i);
			int productid=Integer.valueOf(product.categoryID);
			
			if(Integer.valueOf(cat.categoryid)==29){
				Log.d("","");
			}
			
			if(Integer.valueOf(cat.categoryid)==productid){
				cat=categories.get(i);	 
				return cat;
			} 
		} 
		
		return cat;  
	}
	
	
	public void getProduct(Context context,final ProductListCallBack callback){
		
		RequestQueue queue = Volley.newRequestQueue(context);

		JSONObject userJSON = new JSONObject();

		JSONObject requestObj = new JSONObject();
		try {
			requestObj.put("data", userJSON);
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET,"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/menuitems/104", requestObj,
				new Response.Listener<JSONObject>() 
				{

					@Override
					public void onResponse(JSONObject arg0) {
						JSONObject obj;
						 
						 
						   initProductWithJson(arg0);
						  callback.onProductListCallback(categories);
						  isDownloadable=true;
					 
						
					}},new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if(error != null){

							error.printStackTrace(System.out);

						}
					}
				});
		
		
		
		queue.add(jsObjRequest);
		
		
	}
	
	
	public interface ProductListCallBack{
		
		void onProductListCallback(ArrayList<Category> categories);
		
	}
	
	

}
