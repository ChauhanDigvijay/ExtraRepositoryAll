package com.donatos.phoenix.network.account;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.Meta;
import com.donatos.phoenix.network.common.User;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class ProfileResponse {
    @JsonField(name = {"content"})
    private User content = null;
    @JsonField(name = {"meta"})
    private Meta meta = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public ProfileResponse content(User user) {
        this.content = user;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ProfileResponse profileResponse = (ProfileResponse) obj;
        return C2507k.m7346a(this.meta, profileResponse.meta) && C2507k.m7346a(this.content, profileResponse.content);
    }

    public User getContent() {
        return this.content;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.meta, this.content});
    }

    public ProfileResponse meta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public void setContent(User user) {
        this.content = user;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class ProfileResponse {\n");
        stringBuilder.append("    meta: ").append(toIndentedString(this.meta)).append("\n");
        stringBuilder.append("    content: ").append(toIndentedString(this.content)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
