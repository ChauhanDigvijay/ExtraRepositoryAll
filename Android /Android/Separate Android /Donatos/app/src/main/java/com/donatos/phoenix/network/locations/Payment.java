package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.PaymentResponseContent;
import com.donatos.phoenix.network.giftcard.GiftCardResponseContent;
import com.donatos.phoenix.p134b.C2505i;
import com.donatos.phoenix.p134b.C2507k;
import com.donatos.phoenix.ui.checkout.bu;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@JsonObject
public class Payment {
    @JsonField(name = {"amount"})
    private Double amount;
    @JsonField(name = {"card_number"})
    private String cardNumber;
    @JsonField(name = {"card_zip"})
    private String cardZip;
    @JsonField(name = {"defaultCard"})
    private Boolean defaultCard;
    @JsonField(name = {"drivertip"})
    private Double drivertip;
    @JsonField(name = {"exp_month"})
    private Integer expMonth;
    @JsonField(name = {"exp_year"})
    private Integer expYear;
    @JsonField(name = {"gift_card"})
    private String giftCard;
    @JsonField(name = {"name_on_card"})
    private String nameOnCard;
    @JsonField(name = {"saveCard"})
    private Boolean saveCard;
    @JsonField(name = {"token"})
    private Long token;
    @JsonField(name = {"type"})
    private String type;

    public Payment() {
        this.type = null;
        this.nameOnCard = null;
        this.cardNumber = null;
        this.expMonth = null;
        this.expYear = null;
        this.cardZip = null;
        this.giftCard = null;
        this.amount = null;
        this.token = null;
        this.saveCard = null;
        this.drivertip = null;
        this.defaultCard = null;
    }

    public Payment(PaymentResponseContent paymentResponseContent, Double d) {
        this.type = null;
        this.nameOnCard = null;
        this.cardNumber = null;
        this.expMonth = null;
        this.expYear = null;
        this.cardZip = null;
        this.giftCard = null;
        this.amount = null;
        this.token = null;
        this.saveCard = null;
        this.drivertip = null;
        this.defaultCard = null;
        if (paymentResponseContent.getCardType() != null && paymentResponseContent.getCardType().contains("cash")) {
            this.type = "cash";
        } else if (paymentResponseContent.getCardType() == null || !paymentResponseContent.getCardType().contains("gift")) {
            this.type = "credit";
        } else {
            this.type = "giftcard";
            this.giftCard = paymentResponseContent.getCardLastFourDigits();
        }
        if (paymentResponseContent.getCardLastFourDigits() != null && paymentResponseContent.getCardLastFourDigits().length() > 4) {
            this.cardNumber = paymentResponseContent.getCardLastFourDigits();
        }
        this.nameOnCard = paymentResponseContent.getCardHolderName();
        this.token = paymentResponseContent.getCardToken();
        this.expMonth = getMonth(paymentResponseContent.getCardExpiration());
        this.expYear = getYear(paymentResponseContent.getCardExpiration());
        this.cardZip = paymentResponseContent.getCardZipCode();
        this.defaultCard = paymentResponseContent.getDefaultCard();
        this.amount = d;
    }

    public Payment(GiftCardResponseContent giftCardResponseContent, String str) {
        this.type = null;
        this.nameOnCard = null;
        this.cardNumber = null;
        this.expMonth = null;
        this.expYear = null;
        this.cardZip = null;
        this.giftCard = null;
        this.amount = null;
        this.token = null;
        this.saveCard = null;
        this.drivertip = null;
        this.defaultCard = null;
        this.type = "giftcard";
        this.giftCard = str;
        this.amount = giftCardResponseContent.getBalance();
    }

    private Integer getMonth(String str) {
        Integer num = null;
        if (str != null) {
            try {
                Date parse = new SimpleDateFormat("MM/yyyy", Locale.US).parse(str);
                Calendar instance = Calendar.getInstance();
                instance.setTime(parse);
                num = Integer.valueOf(instance.get(2) + 1);
            } catch (ParseException e) {
            }
        }
        return num;
    }

    private Integer getYear(String str) {
        Integer num = null;
        if (str != null) {
            try {
                Date parse = new SimpleDateFormat("MM/yyyy", Locale.US).parse(str);
                Calendar instance = Calendar.getInstance();
                instance.setTime(parse);
                num = Integer.valueOf(instance.get(1));
            } catch (ParseException e) {
            }
        }
        return num;
    }

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public Payment amount(Double d) {
        this.amount = Double.valueOf(C2505i.m7344a(d.doubleValue()));
        return this;
    }

    public Payment cardNumber(String str) {
        this.cardNumber = str;
        return this;
    }

    public Payment cardZip(String str) {
        this.cardZip = str;
        return this;
    }

    public Payment defaultCard(Boolean bool) {
        this.defaultCard = bool;
        return this;
    }

    public boolean equals(PaymentResponseContent paymentResponseContent) {
        if (paymentResponseContent == null) {
            return false;
        }
        if ((C2507k.m7346a(this.type, paymentResponseContent.getCardType()) || !(!C2507k.m7346a(this.type, "credit") || C2507k.m7346a(paymentResponseContent.getCardType(), "cash") || C2507k.m7346a(paymentResponseContent.getCardType(), "gift"))) && C2507k.m7346a(this.nameOnCard, paymentResponseContent.getCardHolderName()) && C2507k.m7346a(this.token, paymentResponseContent.getCardToken()) && C2507k.m7346a(this.expMonth, getMonth(paymentResponseContent.getCardExpiration())) && C2507k.m7346a(this.expYear, getYear(paymentResponseContent.getCardExpiration())) && C2507k.m7346a(this.cardZip, paymentResponseContent.getCardZipCode())) {
            if (C2507k.m7346a(this.giftCard, this.giftCard != null ? paymentResponseContent.getCardLastFourDigits() : null)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Payment payment = (Payment) obj;
        return C2507k.m7346a(this.type, payment.type) && C2507k.m7346a(this.nameOnCard, payment.nameOnCard) && C2507k.m7346a(this.cardNumber, payment.cardNumber) && C2507k.m7346a(this.expMonth, payment.expMonth) && C2507k.m7346a(this.expYear, payment.expYear) && C2507k.m7346a(this.cardZip, payment.cardZip) && C2507k.m7346a(this.giftCard, payment.giftCard) && C2507k.m7346a(this.amount, payment.amount) && C2507k.m7346a(this.token, payment.token) && C2507k.m7346a(this.saveCard, payment.saveCard) && C2507k.m7346a(this.drivertip, payment.drivertip) && C2507k.m7346a(this.defaultCard, payment.defaultCard);
    }

    public Payment expMonth(Integer num) {
        this.expMonth = num;
        return this;
    }

    public Payment expYear(Integer num) {
        this.expYear = num;
        return this;
    }

    public Double getAmount() {
        return this.amount;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getCardZip() {
        return this.cardZip;
    }

    public Boolean getDefaultCard() {
        return this.defaultCard;
    }

    public Double getDrivertip() {
        return this.drivertip;
    }

    public Integer getExpMonth() {
        return this.expMonth;
    }

    public Integer getExpYear() {
        return this.expYear;
    }

    public String getGiftCard() {
        return this.giftCard;
    }

    public String getNameOnCard() {
        return this.nameOnCard;
    }

    public bu getPaymentType() {
        if (getType() != null) {
            for (bu buVar : bu.values()) {
                if (buVar.f8235d.equals(getType().toLowerCase())) {
                    return buVar;
                }
            }
        }
        return null;
    }

    public Boolean getSaveCard() {
        return this.saveCard;
    }

    public Long getToken() {
        return this.token;
    }

    public String getType() {
        return this.type;
    }

    public Payment giftCard(String str) {
        this.giftCard = str;
        return this;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.type, this.nameOnCard, this.cardNumber, this.expMonth, this.expYear, this.cardZip, this.giftCard, this.amount, this.token, this.saveCard, this.drivertip, this.defaultCard});
    }

    public Payment nameOnCard(String str) {
        this.nameOnCard = str;
        return this;
    }

    public Payment saveCard(Boolean bool) {
        this.saveCard = bool;
        return this;
    }

    public void setAmount(Double d) {
        this.amount = Double.valueOf(C2505i.m7344a(d.doubleValue()));
    }

    public void setCardNumber(String str) {
        this.cardNumber = str;
    }

    public void setCardZip(String str) {
        this.cardZip = str;
    }

    public void setDefaultCard(Boolean bool) {
        this.defaultCard = bool;
    }

    public void setDrivertip(Double d) {
        this.drivertip = Double.valueOf(C2505i.m7344a(d.doubleValue()));
    }

    public void setExpMonth(Integer num) {
        this.expMonth = num;
    }

    public void setExpYear(Integer num) {
        this.expYear = num;
    }

    public void setGiftCard(String str) {
        this.giftCard = str;
    }

    public void setNameOnCard(String str) {
        this.nameOnCard = str;
    }

    public void setSaveCard(Boolean bool) {
        this.saveCard = bool;
    }

    public void setToken(Long l) {
        this.token = l;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class Payment {\n");
        stringBuilder.append("    type: ").append(toIndentedString(this.type)).append("\n");
        stringBuilder.append("    nameOnCard: ").append(toIndentedString(this.nameOnCard)).append("\n");
        stringBuilder.append("    cardNumber: ").append(toIndentedString(this.cardNumber)).append("\n");
        stringBuilder.append("    expMonth: ").append(toIndentedString(this.expMonth)).append("\n");
        stringBuilder.append("    expYear: ").append(toIndentedString(this.expYear)).append("\n");
        stringBuilder.append("    cardZip: ").append(toIndentedString(this.cardZip)).append("\n");
        stringBuilder.append("    giftCard: ").append(toIndentedString(this.giftCard)).append("\n");
        stringBuilder.append("    amount: ").append(toIndentedString(this.amount)).append("\n");
        stringBuilder.append("    token: ").append(toIndentedString(this.token)).append("\n");
        stringBuilder.append("    saveCard: ").append(toIndentedString(this.saveCard)).append("\n");
        stringBuilder.append("    drivertip: ").append(toIndentedString(this.drivertip)).append("\n");
        stringBuilder.append("    defaultCard: ").append(toIndentedString(this.defaultCard)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public Payment token(Long l) {
        this.token = l;
        return this;
    }

    public Payment type(String str) {
        this.type = str;
        return this;
    }
}
