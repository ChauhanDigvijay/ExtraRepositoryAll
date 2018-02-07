package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class CartItemRecipeSize {
    @JsonField(name = {"id"})
    private Integer id = null;

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
        return C2507k.m7346a(this.id, ((CartItemRecipeSize) obj).id);
    }

    public Integer getId() {
        return this.id;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id});
    }

    public CartItemRecipeSize id(Integer num) {
        this.id = num;
        return this;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class CartItemRecipeSize {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
