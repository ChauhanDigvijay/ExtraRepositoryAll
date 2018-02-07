package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class OrderStatusAndCart {
    @JsonField(name = {"cart"})
    private Cart cart;
    @JsonField(name = {"orderStatusResponse"})
    private OrderStatusResponse orderStatusResponse;

    public OrderStatusAndCart(OrderStatusResponse orderStatusResponse, Cart cart) {
        this.orderStatusResponse = orderStatusResponse;
        this.cart = cart;
    }

    public Cart getCart() {
        return this.cart;
    }

    public OrderStatusResponse getOrderStatusResponse() {
        return this.orderStatusResponse;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setOrderStatusResponse(OrderStatusResponse orderStatusResponse) {
        this.orderStatusResponse = orderStatusResponse;
    }
}
