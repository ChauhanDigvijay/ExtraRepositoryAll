package com.olo.jambajuice.BusinessLogic.Models;

import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity.PickUpDay;
import com.wearehathway.apps.olo.Models.OloBasket;
import com.wearehathway.apps.olo.Models.OloBasketProduct;
import com.wearehathway.apps.olo.Models.OloLoyaltyReward;

import java.util.ArrayList;
import java.util.Date;

import static com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity.OrderType.PICKUP;
import static com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity.PickUpDay.ASAP;

/**
 * Created by Nauman Afzaal on 29/05/15.
 */
public class Basket {
    private String id; //Guid used to identify this basket,
    private String earliestreadytime; //The earliest time at which this basket can be ready (whether pickup or delivery, asap or advance), yyyymmdd hh:mm,
    private float subtotal; //Subtotal of all products and modifiers in the order,
    private float salestax; //Estimated sales tax on the entire basket,
    private float total; //Estimated order total,
    private float coupondiscount;
    private float discount;
    private int vendorid;
    private ArrayList<BasketProduct> products;
    private ArrayList<Reward> appliedRewards;
    private String deliverymode;
    private DeliveryAddress deliveryAddress;

    //Local Parameters
    //Save promotion code locally to dispaly in basket screen.
    private String promotionCode;

    //Save offer id locally
    private String offerId;
    private int promoId;

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    private Date timeWanted;
    private PickUpDay pickUpDay;
    private BasketActivity.OrderType orderType;
    private float deliveryCost;
    private String contactnumber;
    private String leadtimeestimateminutes;

    public Basket(OloBasket oloBasket) {
        products = new ArrayList<>();
        appliedRewards = new ArrayList<>();
        orderType = PICKUP;
        pickUpDay = ASAP;
        updateBasket(oloBasket);
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public void updateBasket(OloBasket oloBasket) {
        id = oloBasket.getId();
        earliestreadytime = oloBasket.getEarliestreadytime();
        subtotal = oloBasket.getSubtotal();
        salestax = oloBasket.getSalestax();
        total = oloBasket.getTotal();
        coupondiscount = oloBasket.getCoupondiscount();
        discount = oloBasket.getDiscount();
        vendorid = oloBasket.getVendorId();
        deliveryCost = oloBasket.getCustomerhandoffcharge();
        deliverymode = oloBasket.getDeliverymode();
        deliveryAddress = new DeliveryAddress(oloBasket.getDeliveryaddress());
        contactnumber = oloBasket.getContactnumber();
        leadtimeestimateminutes = oloBasket.getLeadtimeestimateminutes();
        products.clear();
        appliedRewards.clear();
        for (OloBasketProduct product : oloBasket.getProducts()) {
            products.add(new BasketProduct(product));
        }
        if (oloBasket.getAppliedrewards() != null) {
            for (OloLoyaltyReward oloLoyaltyReward : oloBasket.getAppliedrewards()) {
                appliedRewards.add(new Reward(oloLoyaltyReward));
            }
        }

    }

    public String getId() {
        return id;
    }

    public Date getTimewanted() {
        return timeWanted;
    }

    public void setTimewanted(Date timewanted) {
        this.timeWanted = timewanted;
    }

    public String getEarliestreadytime() {
        return earliestreadytime;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public float getSalestax() {
        return salestax;
    }

    public float getTotal() {
        return total;
    }

    public float getCoupondiscount() {
        return coupondiscount;
    }

    public int getVendorid() {
        return vendorid;
    }

    public ArrayList<BasketProduct> getProducts() {
        return products;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    //Returns total number of products in basket.
    public int totalProductsCount() {
        int count = 0;
        for (BasketProduct basketProduct : products) {
            count += basketProduct.getQuantity();
        }
        return count;
    }

    public ArrayList<Reward> getAppliedRewards() {
        return appliedRewards;
    }

    public float getDiscount() {
        return discount;
    }

    public PickUpDay getPickUpDay() {
        return this.pickUpDay;
    }

    public void setPickUpDay(PickUpDay pickUpDay) {
        this.pickUpDay = pickUpDay;
    }

    public float getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(float deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getDeliverymode() {
        return deliverymode;
    }

    public void setDeliverymode(String deliverymode) {
        this.deliverymode = deliverymode;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public BasketActivity.OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(BasketActivity.OrderType orderType) {
        this.orderType = orderType;
    }

    public String getLeadtimeestimateminutes() {
        return leadtimeestimateminutes;
    }

    public void setLeadtimeestimateminutes(String leadtimeestimateminutes) {
        this.leadtimeestimateminutes = leadtimeestimateminutes;
    }
}
