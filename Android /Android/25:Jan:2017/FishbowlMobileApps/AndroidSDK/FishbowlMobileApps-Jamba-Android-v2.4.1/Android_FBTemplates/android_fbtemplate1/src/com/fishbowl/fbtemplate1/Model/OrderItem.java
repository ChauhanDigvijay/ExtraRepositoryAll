package com.fishbowl.fbtemplate1.Model;
/**
 **
 * Created by Digvijay Chauhan on 14/12/15.
 */
import java.util.ArrayList;
import java.util.List;

public class OrderItem implements java.io.Serializable
{

	private long id;
	private String storeID;
	private int orderId;	
	private String orderStatus;	
	private String orderMessage;	
	public Double orderPrice = 0.00;
	private String ordereDateNTime;
	private String orderTime;
	private String orderType;

	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getStoreID() {
		return storeID;
	}
	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}
	public String getOrdereDateNTime() {
		return ordereDateNTime;
	}
	public void setOrdereDateNTime(String ordereDateNTime) {
		this.ordereDateNTime = ordereDateNTime;
	}
	private List<MenuDrawerItem> itemlist = new ArrayList<MenuDrawerItem>();



	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderMessage() {
		return orderMessage;
	}
	public void setOrderMessage(String orderMessage) {
		this.orderMessage = orderMessage;
	}
	public List<MenuDrawerItem> getItemlist() {
		return itemlist;
	}
	public void setItemlist(List<MenuDrawerItem> itemlist) {
		this.itemlist = itemlist;
	}
	public Double getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}
}
