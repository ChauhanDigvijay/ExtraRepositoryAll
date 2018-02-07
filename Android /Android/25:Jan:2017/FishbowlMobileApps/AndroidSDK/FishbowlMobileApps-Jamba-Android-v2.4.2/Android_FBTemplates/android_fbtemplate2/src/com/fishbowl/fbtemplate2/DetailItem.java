package com.fishbowl.fbtemplate2;

public class DetailItem implements java.io.Serializable {
	private int imageId;
	private String title;
	private String desc;
	public Double price = 0.00;
	public int quantity = 0;
	private Double ext = 0.00;
	
	
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
	public DetailItem(int imageId, String title, String desc,Double price) {
		this.imageId = imageId;
		this.title = title;
		this.desc = desc;
		this.price=price;
		
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return title + "\n" + desc;
	}	
}