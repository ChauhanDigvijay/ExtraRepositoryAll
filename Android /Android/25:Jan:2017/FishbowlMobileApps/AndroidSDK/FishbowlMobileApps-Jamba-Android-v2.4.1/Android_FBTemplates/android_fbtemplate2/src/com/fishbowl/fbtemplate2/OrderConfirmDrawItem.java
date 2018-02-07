package com.fishbowl.fbtemplate2;

/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
public class OrderConfirmDrawItem  implements java.io.Serializable{

	String ItemName;
	int imgResID;
	String title;
	boolean isSpinner;
	int postion;
	public OrderConfirmDrawItem(String itemName, int imgResID) {
		ItemName = itemName;
		this.imgResID = imgResID;
	}

	public OrderConfirmDrawItem(boolean isSpinner) {
		this(null, 0);
		this.isSpinner = isSpinner;
	}

	public OrderConfirmDrawItem(String title,int imgResID,int postion ) {
		this(null, 0);
		this.title = title;
		this.imgResID = imgResID;
		this.postion = postion;
	}

	public OrderConfirmDrawItem(boolean isSpinner, String title, String ItemName,
			int imgResID) {
		this(ItemName, imgResID);
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

}
