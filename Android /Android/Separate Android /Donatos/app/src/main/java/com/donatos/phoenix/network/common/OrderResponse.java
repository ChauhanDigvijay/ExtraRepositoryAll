package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;
import java.util.Date;

@JsonObject
public class OrderResponse {
    @JsonField(name = {"accepted"})
    private String accepted = null;
    @JsonField(name = {"order_header_id"})
    private Integer orderHeaderId = null;
    @JsonField(name = {"promise_time"})
    private Date promiseTime = null;
    @JsonField(name = {"received"})
    private String received = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public OrderResponse accepted(String str) {
        this.accepted = str;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OrderResponse orderResponse = (OrderResponse) obj;
        return C2507k.m7346a(this.orderHeaderId, orderResponse.orderHeaderId) && C2507k.m7346a(this.received, orderResponse.received) && C2507k.m7346a(this.accepted, orderResponse.accepted) && C2507k.m7346a(this.promiseTime, orderResponse.promiseTime);
    }

    public String getAccepted() {
        return this.accepted;
    }

    public Integer getOrderHeaderId() {
        return this.orderHeaderId;
    }

    public Date getPromiseTime() {
        return this.promiseTime;
    }

    public String getReceived() {
        return this.received;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.orderHeaderId, this.received, this.accepted, this.promiseTime});
    }

    public OrderResponse orderHeaderId(Integer num) {
        this.orderHeaderId = num;
        return this;
    }

    public OrderResponse promiseTime(Date date) {
        this.promiseTime = date;
        return this;
    }

    public OrderResponse received(String str) {
        this.received = str;
        return this;
    }

    public void setAccepted(String str) {
        this.accepted = str;
    }

    public void setOrderHeaderId(Integer num) {
        this.orderHeaderId = num;
    }

    public void setPromiseTime(Date date) {
        this.promiseTime = date;
    }

    public void setReceived(String str) {
        this.received = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class OrderResponse {\n");
        stringBuilder.append("    orderHeaderId: ").append(toIndentedString(this.orderHeaderId)).append("\n");
        stringBuilder.append("    received: ").append(toIndentedString(this.received)).append("\n");
        stringBuilder.append("    accepted: ").append(toIndentedString(this.accepted)).append("\n");
        stringBuilder.append("    promiseTime: ").append(toIndentedString(this.promiseTime)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
