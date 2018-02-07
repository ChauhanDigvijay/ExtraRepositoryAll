package com.donatos.phoenix.network.token;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class JWT {
    @JsonField(name = {"token"})
    private String token = null;
    @JsonField(name = {"userID"})
    private Integer userID = null;

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
        JWT jwt = (JWT) obj;
        return C2507k.m7346a(this.token, jwt.token) && C2507k.m7346a(this.userID, jwt.userID);
    }

    public String getToken() {
        return this.token;
    }

    public Integer getUserID() {
        return this.userID;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.token, this.userID});
    }

    public void setToken(String str) {
        this.token = str;
    }

    public void setUserID(Integer num) {
        this.userID = num;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class JWT {\n");
        stringBuilder.append("    token: ").append(toIndentedString(this.token)).append("\n");
        stringBuilder.append("    userID: ").append(toIndentedString(this.userID)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public JWT token(String str) {
        this.token = str;
        return this;
    }

    public JWT userID(Integer num) {
        this.userID = num;
        return this;
    }
}
