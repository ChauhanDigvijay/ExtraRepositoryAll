package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class MetaWithLocationErrorsFields {
    @JsonField(name = {"field"})
    private String field = null;
    @JsonField(name = {"message"})
    private String message = null;

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
        MetaWithLocationErrorsFields metaWithLocationErrorsFields = (MetaWithLocationErrorsFields) obj;
        return C2507k.m7346a(this.field, metaWithLocationErrorsFields.field) && C2507k.m7346a(this.message, metaWithLocationErrorsFields.message);
    }

    public MetaWithLocationErrorsFields field(String str) {
        this.field = str;
        return this;
    }

    public String getField() {
        return this.field;
    }

    public String getMessage() {
        return this.message;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.field, this.message});
    }

    public MetaWithLocationErrorsFields message(String str) {
        this.message = str;
        return this;
    }

    public void setField(String str) {
        this.field = str;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class MetaWithLocationErrorsFields {\n");
        stringBuilder.append("    field: ").append(toIndentedString(this.field)).append("\n");
        stringBuilder.append("    message: ").append(toIndentedString(this.message)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
