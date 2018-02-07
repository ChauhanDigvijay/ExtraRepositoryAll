package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.Menu;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class LocationMenuResponseContent {
    @JsonField(name = {"menu"})
    private Menu menu = null;

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
        return C2507k.m7346a(this.menu, ((LocationMenuResponseContent) obj).menu);
    }

    public Menu getMenu() {
        return this.menu;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.menu});
    }

    public LocationMenuResponseContent menu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class LocationMenuResponseContent {\n");
        stringBuilder.append("    menu: ").append(toIndentedString(this.menu)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
