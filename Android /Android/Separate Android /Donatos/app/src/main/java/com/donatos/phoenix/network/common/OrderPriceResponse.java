package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class OrderPriceResponse {
    @JsonField(name = {"content"})
    private OrderPriceResponseContent content = null;
    @JsonField(name = {"meta"})
    private Meta meta = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public OrderPriceResponse content(OrderPriceResponseContent orderPriceResponseContent) {
        this.content = orderPriceResponseContent;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OrderPriceResponse orderPriceResponse = (OrderPriceResponse) obj;
        return C2507k.m7346a(this.meta, orderPriceResponse.meta) && C2507k.m7346a(this.content, orderPriceResponse.content);
    }

    public OrderPriceResponseContent getContent() {
        return this.content;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.meta, this.content});
    }

    public OrderPriceResponse meta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public void setContent(OrderPriceResponseContent orderPriceResponseContent) {
        this.content = orderPriceResponseContent;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class OrderPriceResponse1 {\n");
        stringBuilder.append("    meta: ").append(toIndentedString(this.meta)).append("\n");
        stringBuilder.append("    content: ").append(toIndentedString(this.content)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
