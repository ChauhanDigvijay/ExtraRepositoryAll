package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class CartItemRecipe {
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"size"})
    private CartItemRecipeSize size = null;

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
        CartItemRecipe cartItemRecipe = (CartItemRecipe) obj;
        return C2507k.m7346a(this.id, cartItemRecipe.id) && C2507k.m7346a(this.size, cartItemRecipe.size);
    }

    public Integer getId() {
        return this.id;
    }

    public CartItemRecipeSize getSize() {
        return this.size;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.size});
    }

    public CartItemRecipe id(Integer num) {
        this.id = num;
        return this;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public void setSize(CartItemRecipeSize cartItemRecipeSize) {
        this.size = cartItemRecipeSize;
    }

    public CartItemRecipe size(CartItemRecipeSize cartItemRecipeSize) {
        this.size = cartItemRecipeSize;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class CartItemRecipe {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    size: ").append(toIndentedString(this.size)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
