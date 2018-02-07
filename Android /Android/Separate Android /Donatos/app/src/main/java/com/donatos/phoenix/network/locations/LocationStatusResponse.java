package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class LocationStatusResponse {
    @JsonField(name = {"content"})
    private LocationStatusResponseContent content = null;
    @JsonField(name = {"meta"})
    private MetaWithLocation meta = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public LocationStatusResponse content(LocationStatusResponseContent locationStatusResponseContent) {
        this.content = locationStatusResponseContent;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocationStatusResponse locationStatusResponse = (LocationStatusResponse) obj;
        return C2507k.m7346a(this.meta, locationStatusResponse.meta) && C2507k.m7346a(this.content, locationStatusResponse.content);
    }

    public LocationStatusResponseContent getContent() {
        return this.content;
    }

    public MetaWithLocation getMeta() {
        return this.meta;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.meta, this.content});
    }

    public LocationStatusResponse meta(MetaWithLocation metaWithLocation) {
        this.meta = metaWithLocation;
        return this;
    }

    public void setContent(LocationStatusResponseContent locationStatusResponseContent) {
        this.content = locationStatusResponseContent;
    }

    public void setMeta(MetaWithLocation metaWithLocation) {
        this.meta = metaWithLocation;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class LocationStatusResponse {\n");
        stringBuilder.append("    meta: ").append(toIndentedString(this.meta)).append("\n");
        stringBuilder.append("    content: ").append(toIndentedString(this.content)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
