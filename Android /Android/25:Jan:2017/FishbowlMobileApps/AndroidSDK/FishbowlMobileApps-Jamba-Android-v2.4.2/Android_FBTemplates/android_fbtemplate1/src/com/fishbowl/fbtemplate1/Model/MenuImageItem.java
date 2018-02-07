package com.fishbowl.fbtemplate1.Model;
/**
 **
 * Created by Digvijay Chauhan on 11/12/15.
 */
public class MenuImageItem  implements java.io.Serializable{

	String ItemName;
	int imgResID;
	String title;
	boolean isSpinner;
	int postion;
	public Double price = 0.00;
	public int quantity = 0;
	public int getPostion() {
		return postion;
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

	private Double ext = 0.00;

	public MenuImageItem(String itemName, int imgResID,Double price ) {
		ItemName = itemName;
		this.imgResID = imgResID;
		this.price=price;

	}



	public MenuImageItem(String title,String ItemName,int postion ) {

		this.title = title;
		this.ItemName = ItemName;
		this.postion = postion;
	}

	public MenuImageItem(boolean isSpinner, String title, String ItemName,
			int imgResID) {

		this.isSpinner = isSpinner;
		this.title = title;
		// TODO Auto-generated constructor stub
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
		if(obj instanceof MenuImageItem)
		{
			MenuImageItem temp = (MenuImageItem) obj;
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
