package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.MenuItemBase;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class MenuInstruction extends MenuItemBase {
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"name"})
    private String name = null;

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
        MenuInstruction menuInstruction = (MenuInstruction) obj;
        return C2507k.m7346a(this.id, menuInstruction.id) && C2507k.m7346a(this.name, menuInstruction.name);
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.name});
    }

    public MenuInstruction id(Integer num) {
        this.id = num;
        return this;
    }

    public MenuInstruction name(String str) {
        this.name = str;
        return this;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class MenuInstruction {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    name: ").append(toIndentedString(this.name)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
