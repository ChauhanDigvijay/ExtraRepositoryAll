package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class CartItemToppings {
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"multiplier"})
    private Integer multiplier = null;
    @JsonField(name = {"placement"})
    private Integer placement = null;

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
        CartItemToppings cartItemToppings = (CartItemToppings) obj;
        return C2507k.m7346a(this.id, cartItemToppings.id) && C2507k.m7346a(this.multiplier, cartItemToppings.multiplier) && C2507k.m7346a(this.placement, cartItemToppings.placement);
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getMultiplier() {
        return this.multiplier;
    }

    public Integer getPlacement() {
        return this.placement;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.multiplier, this.placement});
    }

    public CartItemToppings id(Integer num) {
        this.id = num;
        return this;
    }

    public CartItemToppings multiplier(Integer num) {
        this.multiplier = num;
        return this;
    }

    public CartItemToppings placement(Integer num) {
        this.placement = num;
        return this;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public void setMultiplier(Integer num) {
        this.multiplier = num;
    }

    public void setPlacement(Integer num) {
        this.placement = num;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class CartItemToppings {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    multiplier: ").append(toIndentedString(this.multiplier)).append("\n");
        stringBuilder.append("    placement: ").append(toIndentedString(this.placement)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
