package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class LocationStatusResponseContent {
    @JsonField(name = {"deliveryDelay"})
    private Double deliveryDelay = null;
    @JsonField(name = {"nonDeliveryDelay"})
    private Double nonDeliveryDelay = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public LocationStatusResponseContent deliveryDelay(Double d) {
        this.deliveryDelay = d;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocationStatusResponseContent locationStatusResponseContent = (LocationStatusResponseContent) obj;
        return C2507k.m7346a(this.deliveryDelay, locationStatusResponseContent.deliveryDelay) && C2507k.m7346a(this.nonDeliveryDelay, locationStatusResponseContent.nonDeliveryDelay);
    }

    public Double getDeliveryDelay() {
        return this.deliveryDelay;
    }

    public Double getNonDeliveryDelay() {
        return this.nonDeliveryDelay;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.deliveryDelay, this.nonDeliveryDelay});
    }

    public LocationStatusResponseContent nonDeliveryDelay(Double d) {
        this.nonDeliveryDelay = d;
        return this;
    }

    public void setDeliveryDelay(Double d) {
        this.deliveryDelay = d;
    }

    public void setNonDeliveryDelay(Double d) {
        this.nonDeliveryDelay = d;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class LocationStatusResponseContent {\n");
        stringBuilder.append("    deliveryDelay: ").append(toIndentedString(this.deliveryDelay)).append("\n");
        stringBuilder.append("    nonDeliveryDelay: ").append(toIndentedString(this.nonDeliveryDelay)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
