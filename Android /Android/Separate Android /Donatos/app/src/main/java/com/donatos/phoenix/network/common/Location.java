package com.donatos.phoenix.network.common;

import android.os.Build.VERSION;
import android.telephony.PhoneNumberUtils;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import com.donatos.phoenix.ui.checkout.bi;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@JsonObject
public class Location {
    private static final int TRIO_STORE_POS = 3;
    @JsonField(name = {"accepts_stored_payments"})
    private Boolean acceptsStoredPayments = null;
    @JsonField(name = {"address1"})
    private String address1 = null;
    @JsonField(name = {"address2"})
    private String address2 = null;
    @JsonField(name = {"city"})
    private String city = null;
    @JsonField(name = {"creditCards"})
    private List<LocationCreditCards> creditCards = new ArrayList();
    @JsonField(name = {"delivery"})
    private Double delivery = null;
    @JsonField(name = {"distance"})
    private Double distance = null;
    @JsonField(name = {"hours"})
    private List<LocationHours> hours = new ArrayList();
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"latitude"})
    private Double latitude = null;
    @JsonField(name = {"longitude"})
    private Double longitude = null;
    @JsonField(name = {"menu"})
    private Menu menu = null;
    @JsonField(name = {"name"})
    private String name = null;
    @JsonField(name = {"orderTypes"})
    private List<LocationOrderType> orderTypes = new ArrayList();
    @JsonField(name = {"paymentTypes"})
    private List<LocationOrderType> paymentTypes = new ArrayList();
    @JsonField(name = {"phone"})
    private String phone = null;
    @JsonField(name = {"pos"})
    private Integer pos = null;
    @JsonField(name = {"state"})
    private String state = null;
    @JsonField(name = {"zip"})
    private String zip = null;

    public class OpenClose {
        Date closesAt;
        Date opensAt;
        Date reopensAt;
    }

    private OpenClose getOpenClose() {
        OpenClose openClose = new OpenClose();
        if (!(this.hours == null || this.hours.isEmpty())) {
            Date date = new Date();
            LocationHours locationHours = (LocationHours) this.hours.get(0);
            int i = 0;
            LocationHours locationHours2 = locationHours;
            for (LocationHours locationHours3 : this.hours) {
                LocationHours locationHours4;
                if ((isSameDay(date, locationHours3.getOpen()) || isBeforeDate(locationHours3.getOpen(), date)) && (isSameDay(date, locationHours3.getClose()) || isBeforeDate(date, locationHours3.getClose()))) {
                    locationHours4 = locationHours3;
                } else if (isSameDay(date, locationHours3.getClose()) && isBeforeDate(date, locationHours3.getOpen())) {
                    locationHours4 = locationHours3;
                } else {
                    if (this.hours.size() > i + 1) {
                        locationHours4 = (LocationHours) this.hours.get(i + 1);
                        if (isSameDay(date, locationHours3.getClose()) && isBeforeDate(locationHours3.getClose(), date) && isBeforeDate(date, locationHours4.getOpen())) {
                            openClose.reopensAt = locationHours4.getOpen();
                            locationHours4 = locationHours3;
                        }
                    }
                    locationHours4 = locationHours2;
                }
                i++;
                locationHours2 = locationHours4;
            }
            openClose.opensAt = locationHours2.getOpen();
            openClose.closesAt = locationHours2.getClose();
        }
        return openClose;
    }

    private LocationOrderType getPaymentType(String str) {
        for (LocationOrderType locationOrderType : this.paymentTypes) {
            if (locationOrderType.getName().toLowerCase().equals(str.toLowerCase())) {
                return locationOrderType;
            }
        }
        return null;
    }

    private LocationHours getTodaysHours() {
        if (!(this.hours == null || this.hours.isEmpty())) {
            Date date = new Date();
            this.hours.get(0);
            int i = 0;
            for (LocationHours locationHours : this.hours) {
                if (isSameDay(date, locationHours.getFirstWebOrder())) {
                    return locationHours;
                }
                if ((isBeforeDate(locationHours.getFirstWebOrder(), date) && isSameDay(date, locationHours.getLastWebOrder())) || isBeforeDate(date, locationHours.getLastWebOrder())) {
                    return locationHours;
                }
                if (isSameDay(date, locationHours.getFirstWebOrder()) && isBeforeDate(date, locationHours.getFirstWebOrder())) {
                    return locationHours;
                }
                if (this.hours.size() > i + 1) {
                    LocationHours locationHours2 = (LocationHours) this.hours.get(i + 1);
                    if (isSameDay(date, locationHours.getLastWebOrder()) && isBeforeDate(date, locationHours2.getFirstWebOrder())) {
                        return locationHours;
                    }
                }
                i++;
            }
        }
        return null;
    }

    public static boolean isBeforeDate(Date date, Date date2) {
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        instance.setTime(date);
        instance2.setTime(date2);
        return instance.getTime().before(instance2.getTime());
    }

    public static boolean isSameDay(Date date, Date date2) {
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        if (date == null) {
            date = new Date();
        }
        instance.setTime(date);
        if (date2 == null) {
            date2 = new Date();
        }
        instance2.setTime(date2);
        return instance.get(1) == instance2.get(1) && instance.get(6) == instance2.get(6);
    }

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public Location acceptsStoredPayments(Boolean bool) {
        this.acceptsStoredPayments = bool;
        return this;
    }

    public Location addCreditCardsItem(LocationCreditCards locationCreditCards) {
        this.creditCards.add(locationCreditCards);
        return this;
    }

    public Location addHoursItem(LocationHours locationHours) {
        this.hours.add(locationHours);
        return this;
    }

    public Location addOrderTypesItem(LocationOrderType locationOrderType) {
        this.orderTypes.add(locationOrderType);
        return this;
    }

    public Location addPaymentTypesItem(LocationOrderType locationOrderType) {
        this.paymentTypes.add(locationOrderType);
        return this;
    }

    public Location address1(String str) {
        this.address1 = str;
        return this;
    }

    public Location address2(String str) {
        this.address2 = str;
        return this;
    }

    public Location city(String str) {
        this.city = str;
        return this;
    }

    public Location creditCards(List<LocationCreditCards> list) {
        this.creditCards = list;
        return this;
    }

    public Location delivery(Double d) {
        this.delivery = d;
        return this;
    }

    public Location distance(Double d) {
        this.distance = d;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Location location = (Location) obj;
        return C2507k.m7346a(this.id, location.id) && C2507k.m7346a(this.name, location.name) && C2507k.m7346a(this.address1, location.address1) && C2507k.m7346a(this.address2, location.address2) && C2507k.m7346a(this.city, location.city) && C2507k.m7346a(this.state, location.state) && C2507k.m7346a(this.zip, location.zip) && C2507k.m7346a(this.phone, location.phone) && C2507k.m7346a(this.latitude, location.latitude) && C2507k.m7346a(this.longitude, location.longitude) && C2507k.m7346a(this.delivery, location.delivery) && C2507k.m7346a(this.pos, location.pos) && C2507k.m7346a(this.menu, location.menu) && C2507k.m7346a(this.creditCards, location.creditCards) && C2507k.m7346a(this.orderTypes, location.orderTypes) && C2507k.m7346a(this.paymentTypes, location.paymentTypes) && C2507k.m7346a(this.hours, location.hours) && C2507k.m7346a(this.acceptsStoredPayments, location.acceptsStoredPayments);
    }

    public Boolean getAcceptsStoredPayments() {
        return Boolean.valueOf(this.acceptsStoredPayments == null ? false : this.acceptsStoredPayments.booleanValue());
    }

    public String getAddress1() {
        return this.address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public String getCity() {
        return this.city;
    }

    public List<LocationCreditCards> getCreditCards() {
        return this.creditCards;
    }

    public Double getDelivery() {
        return this.delivery;
    }

    public Double getDistance() {
        return this.distance;
    }

    public String getDistanceAsString() {
        return this.distance != null ? new DecimalFormat("#.#").format(this.distance) + " Miles" : "0 Miles";
    }

    public String getFullAddress() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.address1 != null) {
            stringBuilder.append(this.address1).append(" ");
        }
        if (this.address2 != null) {
            stringBuilder.append(this.address2).append(" ");
        }
        if (!(this.city == null || this.state == null || this.zip == null)) {
            stringBuilder.append(this.city).append(", ").append(this.state).append(" ").append(this.zip);
        }
        return stringBuilder.toString();
    }

    public List<LocationHours> getHours() {
        return this.hours;
    }

    public Integer getId() {
        return this.id;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public LocationHours getLocationHoursForDate(Date date) {
        for (LocationHours locationHours : this.hours) {
            if (isSameDay(date, locationHours.getFirstWebOrder())) {
                return locationHours;
            }
            if (isBeforeDate(locationHours.getFirstWebOrder(), date) && isBeforeDate(date, locationHours.getLastWebOrder())) {
                return locationHours;
            }
            if (isSameDay(date, locationHours.getLastWebOrder())) {
                return locationHours;
            }
        }
        return null;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Date getMaxDate() {
        if (this.hours == null || this.hours.isEmpty()) {
            return null;
        }
        LocationHours locationHours = (LocationHours) this.hours.get(0);
        LocationHours locationHours2 = locationHours;
        for (LocationHours locationHours3 : this.hours) {
            if (!isBeforeDate(locationHours2.getLastWebOrder(), locationHours3.getLastWebOrder())) {
                locationHours3 = locationHours2;
            }
            locationHours2 = locationHours3;
        }
        return locationHours2.getLastWebOrder();
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Date getMinDate() {
        Date date = new Date();
        if (isLocationOpen()) {
            return date;
        }
        if (this.hours == null || this.hours.isEmpty()) {
            return null;
        }
        LocationHours locationHours = (LocationHours) this.hours.get(0);
        LocationHours locationHours2 = locationHours;
        for (LocationHours locationHours3 : this.hours) {
            if (!isBeforeDate(locationHours2.getLastWebOrder(), date) || !isBeforeDate(date, locationHours3.getFirstWebOrder())) {
                locationHours = locationHours2;
            }
            locationHours2 = locationHours;
        }
        return locationHours2.getFirstWebOrder();
    }

    public String getName() {
        return this.name;
    }

    public String getOpenHoursString() {
        OpenClose openClose = getOpenClose();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a", Locale.US);
        return (openClose.opensAt == null || openClose.closesAt == null) ? "Hours are unavailable" : (isBeforeDate(date, openClose.opensAt) || isBeforeDate(openClose.closesAt, date)) ? "Opens at " + simpleDateFormat.format(openClose.opensAt) : "Open until " + simpleDateFormat.format(openClose.closesAt);
    }

    public List<LocationOrderType> getOrderTypes() {
        return this.orderTypes;
    }

    public List<LocationOrderType> getPaymentTypes() {
        List<LocationOrderType> arrayList = new ArrayList();
        LocationOrderType paymentType = getPaymentType("credit");
        if (paymentType != null) {
            arrayList.add(paymentType);
        }
        paymentType = getPaymentType("gift card");
        if (paymentType != null) {
            arrayList.add(paymentType);
        }
        paymentType = getPaymentType("cash");
        if (paymentType != null) {
            arrayList.add(paymentType);
        }
        return arrayList;
    }

    public String getPhone() {
        return this.phone == null ? "" : VERSION.SDK_INT >= 21 ? PhoneNumberUtils.formatNumber(this.phone, Locale.getDefault().getCountry()) : PhoneNumberUtils.formatNumber(this.phone);
    }

    public Integer getPos() {
        return this.pos;
    }

    public LocationOrderType getSelectedOrderType() {
        for (LocationOrderType locationOrderType : getOrderTypes()) {
            if (locationOrderType.isUserSelected().booleanValue()) {
                return locationOrderType;
            }
        }
        for (LocationOrderType locationOrderType2 : getOrderTypes()) {
            if (locationOrderType2.getOrderType().equals(bi.CARRYOUT)) {
                return locationOrderType2;
            }
        }
        return !getOrderTypes().isEmpty() ? (LocationOrderType) getOrderTypes().get(0) : null;
    }

    public String getState() {
        return this.state;
    }

    public String getZip() {
        return this.zip;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.name, this.address1, this.address2, this.city, this.state, this.zip, this.phone, this.latitude, this.longitude, this.delivery, this.pos, this.menu, this.creditCards, this.orderTypes, this.paymentTypes, this.hours, this.acceptsStoredPayments});
    }

    public Location hours(List<LocationHours> list) {
        this.hours = list;
        return this;
    }

    public Location id(Integer num) {
        this.id = num;
        return this;
    }

    public boolean isLocationOpen() {
        LocationHours todaysHours = getTodaysHours();
        if (todaysHours == null) {
            return false;
        }
        getDelivery();
        Date date = new Date();
        Date firstWebOrder = todaysHours.getFirstWebOrder();
        Date lastWebOrder = todaysHours.getLastWebOrder();
        return (date.equals(firstWebOrder) || isBeforeDate(firstWebOrder, date)) ? date.equals(lastWebOrder) || isBeforeDate(date, lastWebOrder) : false;
    }

    public boolean isTrioStore() {
        return getPos() != null && getPos().equals(Integer.valueOf(3));
    }

    public Location latitude(Double d) {
        this.latitude = d;
        return this;
    }

    public Location longitude(Double d) {
        this.longitude = d;
        return this;
    }

    public Location menu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public Location name(String str) {
        this.name = str;
        return this;
    }

    public Location orderTypes(List<LocationOrderType> list) {
        this.orderTypes = list;
        return this;
    }

    public Location paymentTypes(List<LocationOrderType> list) {
        this.paymentTypes = list;
        return this;
    }

    public Location phone(String str) {
        this.phone = str;
        return this;
    }

    public Location pos(Integer num) {
        this.pos = num;
        return this;
    }

    public void setAcceptsStoredPayments(Boolean bool) {
        this.acceptsStoredPayments = bool;
    }

    public void setAddress1(String str) {
        this.address1 = str;
    }

    public void setAddress2(String str) {
        this.address2 = str;
    }

    public void setCity(String str) {
        this.city = str;
    }

    public void setCreditCards(List<LocationCreditCards> list) {
        this.creditCards = list;
    }

    public void setDelivery(Double d) {
        this.delivery = d;
    }

    public void setDistance(Double d) {
        this.distance = d;
    }

    public void setHours(List<LocationHours> list) {
        this.hours = list;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public void setLatitude(Double d) {
        this.latitude = d;
    }

    public void setLongitude(Double d) {
        this.longitude = d;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setOrderTypes(List<LocationOrderType> list) {
        this.orderTypes = list;
    }

    public void setPaymentTypes(List<LocationOrderType> list) {
        this.paymentTypes = list;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public void setPos(Integer num) {
        this.pos = num;
    }

    public void setState(String str) {
        this.state = str;
    }

    public void setZip(String str) {
        this.zip = str;
    }

    public Location state(String str) {
        this.state = str;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class Location {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    name: ").append(toIndentedString(this.name)).append("\n");
        stringBuilder.append("    address1: ").append(toIndentedString(this.address1)).append("\n");
        stringBuilder.append("    address2: ").append(toIndentedString(this.address2)).append("\n");
        stringBuilder.append("    city: ").append(toIndentedString(this.city)).append("\n");
        stringBuilder.append("    state: ").append(toIndentedString(this.state)).append("\n");
        stringBuilder.append("    zip: ").append(toIndentedString(this.zip)).append("\n");
        stringBuilder.append("    phone: ").append(toIndentedString(this.phone)).append("\n");
        stringBuilder.append("    latitude: ").append(toIndentedString(this.latitude)).append("\n");
        stringBuilder.append("    longitude: ").append(toIndentedString(this.longitude)).append("\n");
        stringBuilder.append("    delivery: ").append(toIndentedString(this.delivery)).append("\n");
        stringBuilder.append("    pos: ").append(toIndentedString(this.pos)).append("\n");
        stringBuilder.append("    menu: ").append(toIndentedString(this.menu)).append("\n");
        stringBuilder.append("    creditCards: ").append(toIndentedString(this.creditCards)).append("\n");
        stringBuilder.append("    orderTypes: ").append(toIndentedString(this.orderTypes)).append("\n");
        stringBuilder.append("    paymentTypes: ").append(toIndentedString(this.paymentTypes)).append("\n");
        stringBuilder.append("    hours: ").append(toIndentedString(this.hours)).append("\n");
        stringBuilder.append("    acceptsStoredPayments: ").append(toIndentedString(this.acceptsStoredPayments)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public Location zip(String str) {
        this.zip = str;
        return this;
    }
}
