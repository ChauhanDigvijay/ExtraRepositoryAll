package com.fishbowl.fbtemplate2;

public class DescriptionItem {
	private int imageId;
	private String title;
//	private String desc;
	
	public DescriptionItem(int imageId, String title) {
		this.imageId = imageId;
		this.title = title;
		
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}