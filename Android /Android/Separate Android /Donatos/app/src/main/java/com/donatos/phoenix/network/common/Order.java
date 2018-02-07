package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.locations.Payment;
import com.donatos.phoenix.p134b.C2507k;
import com.donatos.phoenix.ui.checkout.bi;
import com.donatos.phoenix.ui.checkout.bu;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@JsonObject
public class Order {
    @JsonField(name = {"coupons"})
    private List<String> coupons = new ArrayList();
    @JsonField(name = {"date"})
    private Date date = null;
    @JsonField(name = {"deliveryAddress"})
    private Address deliveryAddress = null;
    @JsonField(name = {"orderHeaderID"})
    private Integer headerId = null;
    @JsonField(name = {"is_favorite"})
    private Boolean isFavorite = null;
    @JsonField(name = {"is_immediate"})
    private Boolean isImmediate = Boolean.valueOf(true);
    @JsonField(name = {"items"})
    private List<CartItem> items = new ArrayList();
    @JsonField(name = {"payments"})
    private List<Payment> payments = new ArrayList();
    @JsonField(name = {"type"})
    private String type = bi.CARRYOUT.f8217g;
    @JsonField(name = {"user"})
    private User user = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public Order addCouponsItem(String str) {
        this.coupons.add(str);
        return this;
    }

    public Order addItemsItem(CartItem cartItem) {
        this.items.add(cartItem);
        return this;
    }

    public Order addPaymentsItem(Payment payment) {
        this.payments.add(payment);
        return this;
    }

    public Order coupons(List<String> list) {
        this.coupons = list;
        return this;
    }

    public Order date(Date date) {
        this.date = date;
        return this;
    }

    public Order deliveryAddress(Address address) {
        this.deliveryAddress = address;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Order order = (Order) obj;
        return C2507k.m7346a(this.headerId, order.headerId) && C2507k.m7346a(this.user, order.user) && C2507k.m7346a(this.items, order.items) && C2507k.m7346a(this.isFavorite, order.isFavorite) && C2507k.m7346a(this.date, order.date) && C2507k.m7346a(this.isImmediate, order.isImmediate) && C2507k.m7346a(this.type, order.type) && C2507k.m7346a(this.deliveryAddress, order.deliveryAddress) && C2507k.m7346a(this.coupons, order.coupons) && C2507k.m7346a(this.payments, order.payments);
    }

    public Double getAppliedPaymentAmount() {
        Double valueOf = Double.valueOf(0.0d);
        Double d = valueOf;
        for (Payment payment : getPayments()) {
            d = Double.valueOf(payment.getAmount().doubleValue() + d.doubleValue());
        }
        return d;
    }

    public List<String> getCoupons() {
        return this.coupons;
    }

    public Date getDate() {
        return this.date;
    }

    public Address getDeliveryAddress() {
        if (this.deliveryAddress == null) {
            this.deliveryAddress = new Address();
        }
        return this.deliveryAddress;
    }

    public List<Payment> getGiftCards() {
        List<Payment> arrayList = new ArrayList();
        for (Payment payment : getPayments()) {
            if (payment.getType() != null && payment.getPaymentType().equals(bu.GIFTCARD)) {
                arrayList.add(payment);
            }
        }
        return arrayList;
    }

    public Integer getHeaderId() {
        return this.headerId;
    }

    public Boolean getIsFavorite() {
        return this.isFavorite;
    }

    public Boolean getIsImmediate() {
        return this.isImmediate;
    }

    public List<CartItem> getItems() {
        return this.items;
    }

    public bi getOrderType() {
        for (bi biVar : bi.values()) {
            if (biVar.f8217g.equals(this.type)) {
                return biVar;
            }
        }
        return bi.CARRYOUT;
    }

    public List<Payment> getPayments() {
        return this.payments;
    }

    public String getType() {
        return this.type;
    }

    public User getUser() {
        return this.user;
    }

    public boolean hasCreditCard() {
        for (Payment payment : getPayments()) {
            if (payment.getType() != null && payment.getPaymentType().equals(bu.CREDITCARD)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasGiftCard() {
        for (Payment payment : getPayments()) {
            if (payment.getType() != null && payment.getPaymentType().equals(bu.GIFTCARD)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.headerId, this.user, this.items, this.isFavorite, this.date, this.isImmediate, this.type, this.deliveryAddress, this.coupons, this.payments});
    }

    public Order headerId(Integer num) {
        this.headerId = num;
        return this;
    }

    public Order isFavorite(Boolean bool) {
        this.isFavorite = bool;
        return this;
    }

    public Order isImmediate(Boolean bool) {
        this.isImmediate = bool;
        return this;
    }

    public Order items(List<CartItem> list) {
        this.items = list;
        return this;
    }

    public Order payments(List<Payment> list) {
        this.payments = list;
        return this;
    }

    public void setCoupons(List<String> list) {
        this.coupons = list;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDeliveryAddress(Address address) {
        this.deliveryAddress = address;
    }

    public void setHeaderId(Integer num) {
        this.headerId = num;
    }

    public void setIsFavorite(Boolean bool) {
        this.isFavorite = bool;
    }

    public void setIsImmediate(Boolean bool) {
        this.isImmediate = bool;
    }

    public void setItems(List<CartItem> list) {
        this.items = list;
    }

    public void setPayments(List<Payment> list) {
        this.payments = list;
    }

    public void setType(String str) {
        this.type = str;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class Order {\n");
        stringBuilder.append("    headerId: ").append(toIndentedString(this.headerId)).append("\n");
        stringBuilder.append("    user: ").append(toIndentedString(this.user)).append("\n");
        stringBuilder.append("    items: ").append(toIndentedString(this.items)).append("\n");
        stringBuilder.append("    isFavorite: ").append(toIndentedString(this.isFavorite)).append("\n");
        stringBuilder.append("    date: ").append(toIndentedString(this.date)).append("\n");
        stringBuilder.append("    isImmediate: ").append(toIndentedString(this.isImmediate)).append("\n");
        stringBuilder.append("    type: ").append(toIndentedString(this.type)).append("\n");
        stringBuilder.append("    deliveryAddress: ").append(toIndentedString(this.deliveryAddress)).append("\n");
        stringBuilder.append("    coupons: ").append(toIndentedString(this.coupons)).append("\n");
        stringBuilder.append("    payments: ").append(toIndentedString(this.payments)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public Order type(String str) {
        this.type = str;
        return this;
    }

    public Order user(User user) {
        this.user = user;
        return this;
    }
}
