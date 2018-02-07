package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class Coupon {
    @JsonField(name = {"code"})
    private String code = null;
    @JsonField(name = {"description"})
    private String description = null;
    @JsonField(name = {"discount"})
    private Double discount = null;
    @JsonField(name = {"type"})
    private String type = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public Coupon code(String str) {
        this.code = str;
        return this;
    }

    public Coupon description(String str) {
        this.description = str;
        return this;
    }

    public Coupon discount(Double d) {
        this.discount = d;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Coupon coupon = (Coupon) obj;
        return C2507k.m7346a(this.code, coupon.code) && C2507k.m7346a(this.description, coupon.description) && C2507k.m7346a(this.discount, coupon.discount) && C2507k.m7346a(this.type, coupon.type);
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public String getType() {
        return this.type;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.code, this.description, this.discount, this.type});
    }

    public void setCode(String str) {
        this.code = str;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setDiscount(Double d) {
        this.discount = d;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class Coupon {\n");
        stringBuilder.append("    code: ").append(toIndentedString(this.code)).append("\n");
        stringBuilder.append("    description: ").append(toIndentedString(this.description)).append("\n");
        stringBuilder.append("    discount: ").append(toIndentedString(this.discount)).append("\n");
        stringBuilder.append("    type: ").append(toIndentedString(this.type)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public Coupon type(String str) {
        this.type = str;
        return this;
    }
}
