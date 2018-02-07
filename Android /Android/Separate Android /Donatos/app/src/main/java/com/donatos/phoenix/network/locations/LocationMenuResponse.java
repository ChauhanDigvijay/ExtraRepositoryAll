package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.Meta;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class LocationMenuResponse {
    @JsonField(name = {"content"})
    private LocationMenuResponseContent content = null;
    @JsonField(name = {"meta"})
    private Meta meta = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public LocationMenuResponse content(LocationMenuResponseContent locationMenuResponseContent) {
        this.content = locationMenuResponseContent;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocationMenuResponse locationMenuResponse = (LocationMenuResponse) obj;
        return C2507k.m7346a(this.meta, locationMenuResponse.meta) && C2507k.m7346a(this.content, locationMenuResponse.content);
    }

    public LocationMenuResponseContent getContent() {
        return this.content;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.meta, this.content});
    }

    public LocationMenuResponse meta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public void setContent(LocationMenuResponseContent locationMenuResponseContent) {
        this.content = locationMenuResponseContent;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class LocationMenuResponse {\n");
        stringBuilder.append("    meta: ").append(toIndentedString(this.meta)).append("\n");
        stringBuilder.append("    content: ").append(toIndentedString(this.content)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
