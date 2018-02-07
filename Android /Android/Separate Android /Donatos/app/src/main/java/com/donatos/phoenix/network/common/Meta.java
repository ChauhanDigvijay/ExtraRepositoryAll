package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.locations.MetaWithLocationErrors;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class Meta {
    @JsonField(name = {"errors"})
    private MetaWithLocationErrors errors = null;
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
        Meta meta = (Meta) obj;
        return C2507k.m7346a(this.success, meta.success) && C2507k.m7346a(this.errors, meta.errors);
    }

    public Meta errors(MetaWithLocationErrors metaWithLocationErrors) {
        this.errors = metaWithLocationErrors;
        return this;
    }

    public MetaWithLocationErrors getErrors() {
        return this.errors;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.success, this.errors});
    }

    public void setErrors(MetaWithLocationErrors metaWithLocationErrors) {
        this.errors = metaWithLocationErrors;
    }

    public void setSuccess(Boolean bool) {
        this.success = bool;
    }

    public Meta success(Boolean bool) {
        this.success = bool;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class Meta {\n");
        stringBuilder.append("    success: ").append(toIndentedString(this.success)).append("\n");
        stringBuilder.append("    errors: ").append(toIndentedString(this.errors)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
