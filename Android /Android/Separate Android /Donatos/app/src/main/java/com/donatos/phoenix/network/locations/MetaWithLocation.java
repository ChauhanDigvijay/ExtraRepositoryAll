package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class MetaWithLocation {
    @JsonField(name = {"errors"})
    private MetaWithLocationErrors errors = null;
    @JsonField(name = {"location"})
    private MetaWithLocationLocation location = null;
    @JsonField(name = {"success"})
    private Boolean success = null;

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
        MetaWithLocation metaWithLocation = (MetaWithLocation) obj;
        return C2507k.m7346a(this.success, metaWithLocation.success) && C2507k.m7346a(this.errors, metaWithLocation.errors) && C2507k.m7346a(this.location, metaWithLocation.location);
    }

    public MetaWithLocation errors(MetaWithLocationErrors metaWithLocationErrors) {
        this.errors = metaWithLocationErrors;
        return this;
    }

    public MetaWithLocationErrors getErrors() {
        return this.errors;
    }

    public MetaWithLocationLocation getLocation() {
        return this.location;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.success, this.errors, this.location});
    }

    public MetaWithLocation location(MetaWithLocationLocation metaWithLocationLocation) {
        this.location = metaWithLocationLocation;
        return this;
    }

    public void setErrors(MetaWithLocationErrors metaWithLocationErrors) {
        this.errors = metaWithLocationErrors;
    }

    public void setLocation(MetaWithLocationLocation metaWithLocationLocation) {
        this.location = metaWithLocationLocation;
    }

    public void setSuccess(Boolean bool) {
        this.success = bool;
    }

    public MetaWithLocation success(Boolean bool) {
        this.success = bool;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class MetaWithLocation {\n");
        stringBuilder.append("    success: ").append(toIndentedString(this.success)).append("\n");
        stringBuilder.append("    errors: ").append(toIndentedString(this.errors)).append("\n");
        stringBuilder.append("    location: ").append(toIndentedString(this.location)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
