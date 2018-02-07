package com.raleys.app.android.models;

import java.util.ArrayList;

public class EcartPreOrderResponse
{
	public String accountId;
	public String listId;
	public int    sePoints;
	public float  productPrice;
	public float  salesTax;
	public float  crv;
	public float  fees;
	public float  totalPrice;
	public ArrayList<EcartAppointment> appointmentList;
}
