package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import com.donatos.phoenix.ui.checkout.bi;
import java.util.Arrays;

@JsonObject
public class LocationOrderType extends MenuItemBase {
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
        LocationOrderType locationOrderType = (LocationOrderType) obj;
        return C2507k.m7346a(this.name, locationOrderType.name) && C2507k.m7346a(this.id, locationOrderType.id);
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        for (bi biVar : bi.values()) {
            if (biVar.f8216f.toLowerCase().equals(this.name.toLowerCase())) {
                return biVar.f8215e;
            }
        }
        return this.name;
    }

    public bi getOrderType() {
        for (bi biVar : bi.values()) {
            if (biVar.f8216f.toLowerCase().equals(getName().toLowerCase())) {
                return biVar;
            }
        }
        return null;
    }

    public String getPlaceOrderName() {
        for (bi biVar : bi.values()) {
            if (biVar.f8215e.toLowerCase().equals(getName().toLowerCase())) {
                return biVar.f8217g;
            }
        }
        return this.name;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.name, this.id});
    }

    public LocationOrderType id(Integer num) {
        this.id = num;
        return this;
    }

    public LocationOrderType name(String str) {
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
        stringBuilder.append("class LocationOrderTypes {\n");
        stringBuilder.append("    name: ").append(toIndentedString(this.name)).append("\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
