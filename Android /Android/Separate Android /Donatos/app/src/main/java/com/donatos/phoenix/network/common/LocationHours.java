package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.locations.LocationStatusResponse;
import com.donatos.phoenix.p134b.C2507k;
import com.donatos.phoenix.ui.checkout.bi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@JsonObject
public class LocationHours {
    @JsonField(name = {"close"})
    private Date close = null;
    @JsonField(name = {"date"})
    private Date date = null;
    @JsonField(name = {"first_web_order"})
    private Date firstWebOrder = null;
    @JsonField(name = {"last_web_order"})
    private Date lastWebOrder = null;
    @JsonField(name = {"open"})
    private Date open = null;

    private Date addMinutes(Date date, int i) {
        return new Date(date.getTime() + ((long) (60000 * i)));
    }

    private String toIndentedDate(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public LocationHours close(Date date) {
        this.close = date;
        return this;
    }

    public LocationHours date(Date date) {
        this.date = date;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocationHours locationHours = (LocationHours) obj;
        return C2507k.m7346a(this.date, locationHours.date) && C2507k.m7346a(this.open, locationHours.open) && C2507k.m7346a(this.close, locationHours.close) && C2507k.m7346a(this.firstWebOrder, locationHours.firstWebOrder) && C2507k.m7346a(this.lastWebOrder, locationHours.lastWebOrder);
    }

    public LocationHours firstWebOrder(Date date) {
        this.firstWebOrder = date;
        return this;
    }

    public Date getClose() {
        return this.close;
    }

    public Date getDate() {
        return this.date;
    }

    public Date getFirstWebOrder() {
        return this.firstWebOrder;
    }

    public Date getLastWebOrder() {
        return this.lastWebOrder;
    }

    public Date getOpen() {
        return this.open;
    }

    public List<Date> getStoreHoursByOrderType(bi biVar, LocationStatusResponse locationStatusResponse) {
        List<Date> arrayList = new ArrayList();
        Date date = new Date();
        Date date2 = this.firstWebOrder;
        date = biVar.equals(bi.DELIVERY) ? addMinutes(date, locationStatusResponse.getContent().getDeliveryDelay().intValue()) : addMinutes(date, locationStatusResponse.getContent().getNonDeliveryDelay().intValue());
        while (Location.isBeforeDate(date2, this.lastWebOrder)) {
            if (Location.isBeforeDate(date, date2)) {
                Date date3 = new Date();
                date3.setTime(date2.getTime());
                arrayList.add(date3);
            }
            date2 = addMinutes(date2, 15);
        }
        return arrayList;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.date, this.open, this.close, this.firstWebOrder, this.lastWebOrder});
    }

    public LocationHours lastWebOrder(Date date) {
        this.lastWebOrder = date;
        return this;
    }

    public LocationHours open(Date date) {
        this.open = date;
        return this;
    }

    public void setClose(Date date) {
        this.close = date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFirstWebOrder(Date date) {
        this.firstWebOrder = date;
    }

    public void setLastWebOrder(Date date) {
        this.lastWebOrder = date;
    }

    public void setOpen(Date date) {
        this.open = date;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class LocationHours {\n");
        stringBuilder.append("    date: ").append(toIndentedDate(this.date)).append("\n");
        stringBuilder.append("    open: ").append(toIndentedDate(this.open)).append("\n");
        stringBuilder.append("    close: ").append(toIndentedDate(this.close)).append("\n");
        stringBuilder.append("    firstWebOrder: ").append(toIndentedDate(this.firstWebOrder)).append("\n");
        stringBuilder.append("    lastWebOrder: ").append(toIndentedDate(this.lastWebOrder)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
