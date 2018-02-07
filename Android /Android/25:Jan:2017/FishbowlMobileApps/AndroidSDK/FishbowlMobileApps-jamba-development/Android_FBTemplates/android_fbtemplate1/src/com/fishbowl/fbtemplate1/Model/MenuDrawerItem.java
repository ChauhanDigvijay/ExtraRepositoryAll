package com.fishbowl.fbtemplate1.Model;
/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
public class MenuDrawerItem  implements java.io.Serializable
{
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	private long id;
	
	private int orderId;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	private int itemId;
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	String ItemName;
	String title;
	String Itemdesc;
	String imageurl;
	String gradient;
	
	private String userID;	
	int imgResID;	
	boolean isSpinner;
	int postion;
	public Double price = 0.00;
	public int quantity = 0;
	private Double ext = 0.00;

	public String getGradient() {
		return gradient;
	}
	public void setGradient(String gradient) {
		this.gradient = gradient;
	}

	public MenuDrawerItem() 
	{
	}
	public MenuDrawerItem(String itemName, int imgResID, int itemId,Double price,String desc,String imageurl ) 
	{
		ItemName = itemName;
		this.imgResID = imgResID;
		this.itemId = itemId;
		this.price=price;	
		this.Itemdesc=desc;
		this.imageurl=imageurl;
		
	}

	public MenuDrawerItem(String title,int imgResID,int postion )
	{

		this.title = title;
		this.imgResID = imgResID;
		this.postion = postion;
	}

	public MenuDrawerItem(boolean isSpinner, String title, String ItemName,
			int imgResID)
	{

		this.isSpinner = isSpinner;
		this.title = title;
		// TODO Auto-generated constructor stub
	}

	
	public String getItemdesc() {
		return Itemdesc;
	}

	public void setItemdesc(String itemdesc) {
		Itemdesc = itemdesc;
	}
	
	public int getPostion() {
		return postion;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	public void setPostion(int postion) {
		this.postion = postion;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getExt() {
		return ext;
	}

	public void setExt(Double ext) {
		this.ext = ext;
	}


	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}

	public int getImgResID() {
		return imgResID;
	}

	public void setImgResID(int imgResID) {
		this.imgResID = imgResID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isSpinner() {
		return isSpinner;
	}


	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof MenuDrawerItem)
		{
			MenuDrawerItem temp = (MenuDrawerItem) obj;
			if(this.ItemName == temp.ItemName )
				return true;
		}
		return false;

	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub

		return (this.title.hashCode());        
	}


}
