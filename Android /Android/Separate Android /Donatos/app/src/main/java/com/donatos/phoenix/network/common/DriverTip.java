package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2505i;

@JsonObject
public class DriverTip {
    @JsonField(name = {"amount"})
    private Double amount = null;
    @JsonField(name = {"isCustom"})
    private Boolean isCustom = null;
    @JsonField(name = {"percent"})
    private Integer percent = null;

    public DriverTip amount(Double d) {
        setAmount(d);
        return this;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Boolean getIsCustom() {
        return this.isCustom;
    }

    public Integer getPercent() {
        return this.percent;
    }

    public DriverTip isCustom(Boolean bool) {
        this.isCustom = bool;
        return this;
    }

    public DriverTip percent(Integer num) {
        this.percent = num;
        return this;
    }

    public void setAmount(Double d) {
        this.amount = d == null ? null : Double.valueOf(C2505i.m7344a(d.doubleValue()));
    }

    public void setIsCustom(Boolean bool) {
        this.isCustom = bool;
    }

    public void setPercent(Integer num) {
        this.percent = num;
    }
}
