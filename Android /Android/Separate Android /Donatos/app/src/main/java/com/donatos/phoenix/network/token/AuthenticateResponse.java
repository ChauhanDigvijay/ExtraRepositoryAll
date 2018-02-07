package com.donatos.phoenix.network.token;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.Meta;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class AuthenticateResponse {
    @JsonField(name = {"content"})
    private JWT content = null;
    @JsonField(name = {"meta"})
    private Meta meta = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public AuthenticateResponse content(JWT jwt) {
        this.content = jwt;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AuthenticateResponse authenticateResponse = (AuthenticateResponse) obj;
        return C2507k.m7346a(this.meta, authenticateResponse.meta) && C2507k.m7346a(this.content, authenticateResponse.content);
    }

    public JWT getContent() {
        return this.content;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.meta, this.content});
    }

    public AuthenticateResponse meta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public void setContent(JWT jwt) {
        this.content = jwt;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class AuthenticateResponse {\n");
        stringBuilder.append("    meta: ").append(toIndentedString(this.meta)).append("\n");
        stringBuilder.append("    content: ").append(toIndentedString(this.content)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
