package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class MetaWithLocationLocation {
    @JsonField(name = {"closed"})
    private Boolean closed = null;
    @JsonField(name = {"message"})
    private String message = null;
    @JsonField(name = {"offline"})
    private Boolean offline = null;
    @JsonField(name = {"preopen"})
    private Boolean preopen = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public MetaWithLocationLocation closed(Boolean bool) {
        this.closed = bool;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MetaWithLocationLocation metaWithLocationLocation = (MetaWithLocationLocation) obj;
        return C2507k.m7346a(this.closed, metaWithLocationLocation.closed) && C2507k.m7346a(this.offline, metaWithLocationLocation.offline) && C2507k.m7346a(this.message, metaWithLocationLocation.message) && C2507k.m7346a(this.preopen, metaWithLocationLocation.preopen);
    }

    public Boolean getClosed() {
        return this.closed;
    }

    public String getMessage() {
        return this.message;
    }

    public Boolean getOffline() {
        return this.offline;
    }

    public Boolean getPreopen() {
        return this.preopen;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.closed, this.offline, this.message, this.preopen});
    }

    public MetaWithLocationLocation message(String str) {
        this.message = str;
        return this;
    }

    public MetaWithLocationLocation offline(Boolean bool) {
        this.offline = bool;
        return this;
    }

    public MetaWithLocationLocation preopen(Boolean bool) {
        this.preopen = bool;
        return this;
    }

    public void setClosed(Boolean bool) {
        this.closed = bool;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public void setOffline(Boolean bool) {
        this.offline = bool;
    }

    public void setPreopen(Boolean bool) {
        this.preopen = bool;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class MetaWithLocationLocation {\n");
        stringBuilder.append("    closed: ").append(toIndentedString(this.closed)).append("\n");
        stringBuilder.append("    offline: ").append(toIndentedString(this.offline)).append("\n");
        stringBuilder.append("    message: ").append(toIndentedString(this.message)).append("\n");
        stringBuilder.append("    preopen: ").append(toIndentedString(this.preopen)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
