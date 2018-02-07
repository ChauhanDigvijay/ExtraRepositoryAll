package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonObject
public class MetaWithLocationErrors {
    @JsonField(name = {"fields"})
    private List<MetaWithLocationErrorsFields> fields = new ArrayList();
    @JsonField(name = {"message"})
    private String message = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public MetaWithLocationErrors addFieldsItem(MetaWithLocationErrorsFields metaWithLocationErrorsFields) {
        this.fields.add(metaWithLocationErrorsFields);
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MetaWithLocationErrors metaWithLocationErrors = (MetaWithLocationErrors) obj;
        return C2507k.m7346a(this.message, metaWithLocationErrors.message) && C2507k.m7346a(this.fields, metaWithLocationErrors.fields);
    }

    public MetaWithLocationErrors fields(List<MetaWithLocationErrorsFields> list) {
        this.fields = list;
        return this;
    }

    public List<MetaWithLocationErrorsFields> getFields() {
        return this.fields;
    }

    public String getMessage() {
        return this.message;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.message, this.fields});
    }

    public MetaWithLocationErrors message(String str) {
        this.message = str;
        return this;
    }

    public void setFields(List<MetaWithLocationErrorsFields> list) {
        this.fields = list;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class MetaWithLocationErrors {\n");
        stringBuilder.append("    message: ").append(toIndentedString(this.message)).append("\n");
        stringBuilder.append("    fields: ").append(toIndentedString(this.fields)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
