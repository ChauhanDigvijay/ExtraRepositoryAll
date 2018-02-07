package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.locations.Coupon;
import com.donatos.phoenix.p134b.C2505i;
import com.donatos.phoenix.p134b.C2507k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonObject
public class OrderPriceResponseContent {
    @JsonField(name = {"coupons"})
    private List<Coupon> coupons = new ArrayList();
    @JsonField(name = {"delivery"})
    private Double delivery = Double.valueOf(0.0d);
    @JsonField(name = {"discount"})
    private Double discount = Double.valueOf(0.0d);
    @JsonField(name = {"driverTip"})
    private DriverTip driverTip = new DriverTip();
    @JsonField(name = {"grandTotal"})
    private Double grandTotal = Double.valueOf(0.0d);
    @JsonField(name = {"subTotal"})
    private Double subTotal = Double.valueOf(0.0d);
    @JsonField(name = {"taxTotal"})
    private Double taxTotal = Double.valueOf(0.0d);
    @JsonField(name = {"tip"})
    private Double tip = Double.valueOf(0.0d);

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public OrderPriceResponseContent addCouponsItem(Coupon coupon) {
        this.coupons.add(coupon);
        return this;
    }

    public OrderPriceResponseContent coupons(List<Coupon> list) {
        this.coupons = list;
        return this;
    }

    public OrderPriceResponseContent delivery(Double d) {
        this.delivery = d;
        return this;
    }

    public OrderPriceResponseContent discount(Double d) {
        this.discount = d;
        return this;
    }

    public OrderPriceResponseContent driverTip(DriverTip driverTip) {
        this.driverTip = driverTip;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OrderPriceResponseContent orderPriceResponseContent = (OrderPriceResponseContent) obj;
        return C2507k.m7346a(this.coupons, orderPriceResponseContent.coupons) && C2507k.m7346a(this.subTotal, orderPriceResponseContent.subTotal) && C2507k.m7346a(this.taxTotal, orderPriceResponseContent.taxTotal) && C2507k.m7346a(this.tip, orderPriceResponseContent.tip) && C2507k.m7346a(this.discount, orderPriceResponseContent.discount) && C2507k.m7346a(this.delivery, orderPriceResponseContent.delivery) && C2507k.m7346a(this.grandTotal, orderPriceResponseContent.grandTotal);
    }

    public List<Coupon> getCoupons() {
        return this.coupons;
    }

    public Double getDelivery() {
        return this.delivery;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public DriverTip getDriverTip() {
        return this.driverTip;
    }

    public Double getGrandTotal() {
        return Double.valueOf(C2505i.m7344a(this.grandTotal.doubleValue()));
    }

    public Double getSubTotal() {
        return this.subTotal;
    }

    public Double getTaxTotal() {
        return this.taxTotal;
    }

    public Double getTip() {
        return (this.driverTip == null || this.driverTip.getIsCustom() == null) ? Double.valueOf(0.0d) : this.driverTip.getIsCustom().booleanValue() ? this.driverTip.getAmount() : this.driverTip.getPercent() == null ? Double.valueOf(0.0d) : Double.valueOf(C2505i.m7344a(getSubTotal().doubleValue() * (((double) this.driverTip.getPercent().intValue()) / 100.0d)));
    }

    public OrderPriceResponseContent grandTotal(Double d) {
        this.grandTotal = d;
        return this;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.coupons, this.subTotal, this.taxTotal, this.tip, this.discount, this.delivery, this.grandTotal});
    }

    public void setCoupons(List<Coupon> list) {
        this.coupons = list;
    }

    public void setDelivery(Double d) {
        this.delivery = d;
    }

    public void setDiscount(Double d) {
        this.discount = d;
    }

    public void setDriverTip(DriverTip driverTip) {
        this.driverTip = driverTip;
    }

    public void setGrandTotal(Double d) {
        this.grandTotal = Double.valueOf(C2505i.m7344a(d.doubleValue()));
    }

    public void setSubTotal(Double d) {
        this.subTotal = d;
    }

    public void setTaxTotal(Double d) {
        this.taxTotal = d;
    }

    public void setTip(Double d) {
    }

    public OrderPriceResponseContent subTotal(Double d) {
        this.subTotal = d;
        return this;
    }

    public OrderPriceResponseContent taxTotal(Double d) {
        this.taxTotal = d;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class OrderPriceResponse {\n");
        stringBuilder.append("    coupons: ").append(toIndentedString(this.coupons)).append("\n");
        stringBuilder.append("    subTotal: ").append(toIndentedString(this.subTotal)).append("\n");
        stringBuilder.append("    taxTotal: ").append(toIndentedString(this.taxTotal)).append("\n");
        stringBuilder.append("    tip: ").append(toIndentedString(this.tip)).append("\n");
        stringBuilder.append("    discount: ").append(toIndentedString(this.discount)).append("\n");
        stringBuilder.append("    delivery: ").append(toIndentedString(this.delivery)).append("\n");
        stringBuilder.append("    grandTotal: ").append(toIndentedString(this.grandTotal)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
