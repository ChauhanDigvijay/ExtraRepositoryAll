package com.BasicApp.BusinessLogic.Models;

import java.util.ArrayList;

public class OrderProductList {
 
	public ArrayList<NewMenuDrawer> orderProductList = new ArrayList<NewMenuDrawer>();
	public NewMenuDrawer currentAdded;
	public LocationItem currentLoaction;
	public LocationItem getCurrentLoaction() {
		return currentLoaction;
	}

	public void setCurrentLoaction(LocationItem currentLoaction) {
		this.currentLoaction = currentLoaction;
	}


	public int currentIndex; 
	private static OrderProductList instance ;
	
 
	 
	public OrderProductList() {
		// TODO Auto-generated constructor stub
	}
	
	public static OrderProductList sharedInstance(){
		
		if(instance==null){
			instance=new OrderProductList() ;
			
		}
		return instance; 
	}
	
	public void setCurrentAdded(NewMenuDrawer _currentAdded){
		currentAdded=_currentAdded;
	}


//	public void setmenuCurrentAdded(NewMenuDrawer _currentAdded){
//		currentAdded=_currentAdded;
//	}

	NewMenuDrawer getCureentItem(){
		return orderProductList.get(currentIndex); 
	} 
	
	public void addIteme(NewMenuDrawer drawitem){
		
		if(!orderProductList.contains(drawitem)){
			orderProductList.add(drawitem);
		}
	 
		 
	} 
	
	
	
}