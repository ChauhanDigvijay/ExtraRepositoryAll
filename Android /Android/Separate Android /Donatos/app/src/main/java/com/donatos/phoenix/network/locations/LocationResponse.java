package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class LocationResponse {
    @JsonField(name = {"content"})
    private LocationResponseContent content = null;
    @JsonField(name = {"meta"})
    private MetaWithLocation meta = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public LocationResponse content(LocationResponseContent locationResponseContent) {
        this.content = locationResponseContent;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocationResponse locationResponse = (LocationResponse) obj;
        return C2507k.m7346a(this.meta, locationResponse.meta) && C2507k.m7346a(this.content, locationResponse.content);
    }

    public LocationResponseContent getContent() {
        return this.content;
    }

    public MetaWithLocation getMeta() {
        return this.meta;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.meta, this.content});
    }

    public LocationResponse meta(MetaWithLocation metaWithLocation) {
        this.meta = metaWithLocation;
        return this;
    }

    public void setContent(LocationResponseContent locationResponseContent) {
        this.content = locationResponseContent;
    }

    public void setMeta(MetaWithLocation metaWithLocation) {
        this.meta = metaWithLocation;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class LocationResponse {\n");
        stringBuilder.append("    meta: ").append(toIndentedString(this.meta)).append("\n");
        stringBuilder.append("    content: ").append(toIndentedString(this.content)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
