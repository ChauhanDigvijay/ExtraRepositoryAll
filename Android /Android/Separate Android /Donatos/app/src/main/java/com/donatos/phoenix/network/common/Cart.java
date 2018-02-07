package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.UUID;

@JsonObject
public class Cart {
    @JsonField(name = {"guid"})
    private String guid = UUID.randomUUID().toString();
    @JsonField(name = {"isSaved"})
    private Boolean isSaved = Boolean.valueOf(false);
    @JsonField(name = {"locationId"})
    private Integer locationId = null;
    @JsonField(name = {"order"})
    private Order order = new Order();
    @JsonField(name = {"price"})
    private OrderPriceResponseContent price = new OrderPriceResponseContent();

    public Cart(Order order) {
        this.order = order;
    }

    public String getGuid() {
        return this.guid;
    }

    public Boolean getIsSaved() {
        return this.isSaved;
    }

    public Integer getLocationId() {
        return this.locationId;
    }

    public Order getOrder() {
        return this.order;
    }

    public OrderPriceResponseContent getPrice() {
        return this.price;
    }

    public void setGuid(String str) {
        this.guid = str;
    }

    public void setIsSaved(Boolean bool) {
        this.isSaved = bool;
    }

    public void setLocationId(Integer num) {
        this.locationId = num;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setPrice(OrderPriceResponseContent orderPriceResponseContent) {
        this.price = orderPriceResponseContent;
    }
}
