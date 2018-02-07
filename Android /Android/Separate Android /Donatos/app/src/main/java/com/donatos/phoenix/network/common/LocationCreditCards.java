package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class LocationCreditCards {
    @JsonField(name = {"description"})
    private String description = null;
    @JsonField(name = {"id"})
    private Integer id = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public LocationCreditCards description(String str) {
        this.description = str;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocationCreditCards locationCreditCards = (LocationCreditCards) obj;
        return C2507k.m7346a(this.id, locationCreditCards.id) && C2507k.m7346a(this.description, locationCreditCards.description);
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getId() {
        return this.id;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.description});
    }

    public LocationCreditCards id(Integer num) {
        this.id = num;
        return this;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class LocationCreditCards {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    description: ").append(toIndentedString(this.description)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
