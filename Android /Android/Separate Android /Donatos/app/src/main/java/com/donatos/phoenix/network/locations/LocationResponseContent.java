package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.Location;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class LocationResponseContent {
    @JsonField(name = {"location"})
    private Location location = null;

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
        return C2507k.m7346a(this.location, ((LocationResponseContent) obj).location);
    }

    public Location getLocation() {
        return this.location;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.location});
    }

    public LocationResponseContent location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class LocationResponseContent {\n");
        stringBuilder.append("    location: ").append(toIndentedString(this.location)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
