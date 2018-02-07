package com.donatos.phoenix.network.giftcard;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2505i;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class GiftCardResponseContent {
    @JsonField(name = {"balance"})
    private Double balance = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public GiftCardResponseContent balance(Double d) {
        this.balance = d;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return C2507k.m7346a(this.balance, ((GiftCardResponseContent) obj).balance);
    }

    public Double getBalance() {
        return this.balance;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.balance});
    }

    public void setBalance(Double d) {
        this.balance = Double.valueOf(C2505i.m7344a(d.doubleValue()));
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class GiftCardResponseContent {\n");
        stringBuilder.append("    balance: ").append(toIndentedString(this.balance)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
