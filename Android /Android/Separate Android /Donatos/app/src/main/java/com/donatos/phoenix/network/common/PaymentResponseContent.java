package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.locations.Payment;
import com.donatos.phoenix.p134b.C2507k;
import com.donatos.phoenix.ui.checkout.C2700o;
import com.donatos.phoenix.ui.checkout.bu;
import com.donatos.phoenix.ui.common.ab;
import java.util.Arrays;
import java.util.Locale;

@JsonObject
public class PaymentResponseContent extends MenuItemBase {
    @JsonField(name = {"cardExpiration"})
    private String cardExpiration = null;
    @JsonField(name = {"cardHolderName"})
    private String cardHolderName = null;
    @JsonField(name = {"cardLastFourDigits"})
    private String cardLastFourDigits = null;
    @JsonField(name = {"cardToken"})
    private Long cardToken = null;
    @JsonField(name = {"cardType"})
    private String cardType = null;
    @JsonField(name = {"cardZipCode"})
    private String cardZipCode = null;
    @JsonField(name = {"defaultCard"})
    private Boolean defaultCard = null;
    private Double value;

    public PaymentResponseContent(Payment payment) {
        if (payment.getPaymentType().equals(bu.CASH)) {
            setName("Cash");
            setCardType("cash");
        } else if (payment.getPaymentType().equals(bu.GIFTCARD)) {
            setName("Gift Card");
            setCardType("giftcard");
            setUserSelected(Boolean.valueOf(true));
            this.value = payment.getAmount();
        } else if (payment.getPaymentType().equals(bu.CREDITCARD)) {
            if (payment.getCardNumber() == null || payment.getCardNumber().length() <= 4) {
                setCardType("credit");
            } else {
                setName(C2700o.m8050a(payment.getCardNumber()).f8427f);
                setCardType(C2700o.m8050a(payment.getCardNumber()).f8427f);
                setCardToken(Long.valueOf(Long.parseLong(payment.getCardNumber())));
            }
        }
        this.cardExpiration = String.format(Locale.US, "%02d", new Object[]{payment.getExpMonth()}) + "/" + payment.getExpYear();
        this.cardHolderName = payment.getNameOnCard();
        this.cardLastFourDigits = payment.getCardNumber();
        this.cardZipCode = payment.getCardZip();
        this.defaultCard = payment.getDefaultCard();
    }

    public PaymentResponseContent(bu buVar) {
        if (buVar.equals(bu.CASH)) {
            setName("Cash");
            setCardType("cash");
        } else if (buVar.equals(bu.GIFTCARD)) {
            setName("Gift Card");
            setCardType("giftcard");
        } else if (buVar.equals(bu.CREDITCARD)) {
            setCardType("credit");
        }
    }

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public PaymentResponseContent cardExpiration(String str) {
        this.cardExpiration = str;
        return this;
    }

    public PaymentResponseContent cardHolderName(String str) {
        this.cardHolderName = str;
        return this;
    }

    public PaymentResponseContent cardLastFourDigits(String str) {
        this.cardLastFourDigits = str;
        return this;
    }

    public PaymentResponseContent cardToken(Long l) {
        this.cardToken = l;
        return this;
    }

    public PaymentResponseContent cardType(String str) {
        this.cardType = str;
        return this;
    }

    public PaymentResponseContent cardZipCode(String str) {
        this.cardZipCode = str;
        return this;
    }

    public PaymentResponseContent defaultCard(Boolean bool) {
        this.defaultCard = bool;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PaymentResponseContent paymentResponseContent = (PaymentResponseContent) obj;
        return C2507k.m7346a(this.cardToken, paymentResponseContent.cardToken) && C2507k.m7346a(this.cardType, paymentResponseContent.cardType) && C2507k.m7346a(this.cardLastFourDigits, paymentResponseContent.cardLastFourDigits) && C2507k.m7346a(this.cardExpiration, paymentResponseContent.cardExpiration) && C2507k.m7346a(this.cardHolderName, paymentResponseContent.cardHolderName) && C2507k.m7346a(this.cardZipCode, paymentResponseContent.cardZipCode) && C2507k.m7346a(this.defaultCard, paymentResponseContent.defaultCard);
    }

    public String getCardExpiration() {
        return this.cardExpiration;
    }

    public String getCardHolderName() {
        return this.cardHolderName;
    }

    public String getCardLastFourDigits() {
        return this.cardLastFourDigits;
    }

    public Long getCardToken() {
        return this.cardToken;
    }

    public String getCardType() {
        return this.cardType;
    }

    public String getCardZipCode() {
        return this.cardZipCode;
    }

    public Boolean getDefaultCard() {
        if (this.defaultCard == null) {
            this.defaultCard = Boolean.valueOf(false);
        }
        return this.defaultCard;
    }

    public String getName() {
        String a = ab.m8118a(getCardType());
        if (getCardType().toLowerCase().equals("visa")) {
            a = "Visa";
        } else if (getCardType().toLowerCase().equals("mc")) {
            a = "MasterCard";
        } else if (getCardType().toLowerCase().equals("disc")) {
            a = "Discover";
        } else if (getCardType().toLowerCase().equals("amex")) {
            a = "American Express";
        } else if (getCardType().toLowerCase().equals("giftcard")) {
            a = "Gift Card";
        }
        StringBuilder append = new StringBuilder().append(a);
        if (getCardLastFourDigits() != null) {
            append.append(" (").append(getCardLastFourDigits().substring(getCardLastFourDigits().length() - 4)).append(")");
        }
        return append.toString();
    }

    public Double getValue() {
        return this.value;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.cardToken, this.cardType, this.cardLastFourDigits, this.cardExpiration, this.cardHolderName, this.cardZipCode, this.defaultCard});
    }

    public void setCardExpiration(String str) {
        this.cardExpiration = str;
    }

    public void setCardHolderName(String str) {
        this.cardHolderName = str;
    }

    public void setCardLastFourDigits(String str) {
        this.cardLastFourDigits = str;
    }

    public void setCardToken(Long l) {
        this.cardToken = l;
    }

    public void setCardType(String str) {
        this.cardType = str;
    }

    public void setCardZipCode(String str) {
        this.cardZipCode = str;
    }

    public void setDefaultCard(Boolean bool) {
        this.defaultCard = bool;
    }

    public void setValue(Double d) {
        this.value = d;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class SetPaymentResponseContent {\n");
        stringBuilder.append("    cardToken: ").append(toIndentedString(this.cardToken)).append("\n");
        stringBuilder.append("    cardType: ").append(toIndentedString(this.cardType)).append("\n");
        stringBuilder.append("    cardLastFourDigits: ").append(toIndentedString(this.cardLastFourDigits)).append("\n");
        stringBuilder.append("    cardExpiration: ").append(toIndentedString(this.cardExpiration)).append("\n");
        stringBuilder.append("    cardHolderName: ").append(toIndentedString(this.cardHolderName)).append("\n");
        stringBuilder.append("    cardZipCode: ").append(toIndentedString(this.cardZipCode)).append("\n");
        stringBuilder.append("    defaultCard: ").append(toIndentedString(this.defaultCard)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
