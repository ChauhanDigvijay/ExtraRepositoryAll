package com.donatos.phoenix.network.account;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.Location;
import com.donatos.phoenix.p134b.C2507k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonObject
public class LocationHistoryResponseContent {
    @JsonField(name = {"locations"})
    private List<Location> locations = new ArrayList();

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public LocationHistoryResponseContent addLocationsItem(Location location) {
        this.locations.add(location);
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return C2507k.m7346a(this.locations, ((LocationHistoryResponseContent) obj).locations);
    }

    public List<Location> getLocations() {
        return this.locations;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.locations});
    }

    public LocationHistoryResponseContent locations(List<Location> list) {
        this.locations = list;
        return this;
    }

    public void setLocations(List<Location> list) {
        this.locations = list;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class LocationHistoryResponseContent {\n");
        stringBuilder.append("    locations: ").append(toIndentedString(this.locations)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
