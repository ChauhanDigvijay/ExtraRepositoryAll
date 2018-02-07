package com.donatos.phoenix.network.itemprice;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class ItemPriceResponseContent {
    @JsonField(name = {"itemPrice"})
    private Double itemPrice = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return C2507k.m7346a(this.itemPrice, ((ItemPriceResponseContent) obj).itemPrice);
    }

    public Double getItemPrice() {
        return this.itemPrice;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.itemPrice});
    }

    public ItemPriceResponseContent itemPrice(Double d) {
        this.itemPrice = d;
        return this;
    }

    public void setItemPrice(Double d) {
        this.itemPrice = d;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class ItemPriceResponseContent {\n");
        stringBuilder.append("    itemPrice: ").append(toIndentedString(this.itemPrice)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
