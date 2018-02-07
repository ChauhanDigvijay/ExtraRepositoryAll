package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.Meta;
import java.util.Date;
import java.util.List;

@JsonObject
public class AvailableTimes {
    @JsonField(name = {"content"})
    private List<Date> hours = null;
    @JsonField(name = {"meta"})
    private Meta meta;

    public List<Date> getHours() {
        return this.hours;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public void setHours(List<Date> list) {
        this.hours = list;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public AvailableTimes withHours(List<Date> list) {
        this.hours = list;
        return this;
    }

    public AvailableTimes withMeta(Meta meta) {
        this.meta = meta;
        return this;
    }
}
