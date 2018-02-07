package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.validateaddress.ValidateAddressAddress;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class Address {
    @JsonField(name = {"default"})
    private Boolean _default = null;
    @JsonField(name = {"address"})
    private ValidateAddressAddress address = null;
    @JsonField(name = {"deliveryGrid"})
    private String deliveryGrid = null;
    @JsonField(name = {"deliveryInstructions"})
    private String deliveryInstructions = null;
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"latitude"})
    private Double latitude = null;
    @JsonField(name = {"longitude"})
    private Double longitude = null;
    @JsonField(name = {"name"})
    private String name = null;
    @JsonField(name = {"storeNumber"})
    private Integer storeNumber = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public Address _default(Boolean bool) {
        this._default = bool;
        return this;
    }

    public Address address(ValidateAddressAddress validateAddressAddress) {
        this.address = validateAddressAddress;
        return this;
    }

    public Address deliveryGrid(String str) {
        this.deliveryGrid = str;
        return this;
    }

    public Address deliveryInstructions(String str) {
        this.deliveryInstructions = str;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Address address = (Address) obj;
        return C2507k.m7346a(this.id, address.id) && C2507k.m7346a(this._default, address._default) && C2507k.m7346a(this.storeNumber, address.storeNumber) && C2507k.m7346a(this.name, address.name) && C2507k.m7346a(this.address, address.address) && C2507k.m7346a(this.deliveryInstructions, address.deliveryInstructions) && C2507k.m7346a(this.deliveryGrid, address.deliveryGrid) && C2507k.m7346a(this.latitude, address.latitude) && C2507k.m7346a(this.longitude, address.longitude);
    }

    public ValidateAddressAddress getAddress() {
        if (this.address == null) {
            this.address = new ValidateAddressAddress();
        }
        return this.address;
    }

    public String getDeliveryGrid() {
        return this.deliveryGrid;
    }

    public String getDeliveryInstructions() {
        if (this.deliveryInstructions != null && (this.deliveryInstructions.equals("false") || this.deliveryInstructions.isEmpty())) {
            this.deliveryInstructions = null;
        }
        return this.deliveryInstructions;
    }

    public String getFormattedAddress() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!(getAddress().getAddress1() == null || getAddress().getAddress1().isEmpty())) {
            stringBuilder.append(getAddress().getAddress1());
        }
        if (!(getAddress().getAddress2() == null || getAddress().getAddress2().isEmpty())) {
            stringBuilder.append(", ").append(getAddress().getAddress2());
        }
        if (get_Default().booleanValue()) {
            stringBuilder.append(" <font color='#cccccc'>(Default)</font>");
        }
        return stringBuilder.toString();
    }

    public String getFormattedAddressWithoutDefault() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!(getAddress().getAddress1() == null || getAddress().getAddress1().isEmpty())) {
            stringBuilder.append(getAddress().getAddress1());
        }
        if (!(getAddress().getAddress2() == null || getAddress().getAddress2().isEmpty())) {
            stringBuilder.append(", ").append(getAddress().getAddress2());
        }
        return stringBuilder.toString();
    }

    public Integer getId() {
        return this.id;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public String getName() {
        return this.name;
    }

    public Integer getStoreNumber() {
        return this.storeNumber;
    }

    public Boolean get_Default() {
        if (this._default == null) {
            this._default = Boolean.valueOf(false);
        }
        return this._default;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this._default, this.storeNumber, this.name, this.address, this.deliveryInstructions, this.deliveryGrid, this.latitude, this.longitude});
    }

    public Address id(Integer num) {
        this.id = num;
        return this;
    }

    public Address latitude(Double d) {
        this.latitude = d;
        return this;
    }

    public Address longitude(Double d) {
        this.longitude = d;
        return this;
    }

    public Address name(String str) {
        this.name = str;
        return this;
    }

    public void setAddress(ValidateAddressAddress validateAddressAddress) {
        this.address = validateAddressAddress;
    }

    public void setDeliveryGrid(String str) {
        this.deliveryGrid = str;
    }

    public void setDeliveryInstructions(String str) {
        this.deliveryInstructions = str;
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

    public void setName(String str) {
        this.name = str;
    }

    public void setStoreNumber(Integer num) {
        this.storeNumber = num;
    }

    public void set_Default(Boolean bool) {
        this._default = bool;
    }

    public Address storeNumber(Integer num) {
        this.storeNumber = num;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class Address {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    _default: ").append(toIndentedString(this._default)).append("\n");
        stringBuilder.append("    storeNumber: ").append(toIndentedString(this.storeNumber)).append("\n");
        stringBuilder.append("    name: ").append(toIndentedString(this.name)).append("\n");
        stringBuilder.append("    address: ").append(toIndentedString(this.address)).append("\n");
        stringBuilder.append("    deliveryInstructions: ").append(toIndentedString(this.deliveryInstructions)).append("\n");
        stringBuilder.append("    deliveryGrid: ").append(toIndentedString(this.deliveryGrid)).append("\n");
        stringBuilder.append("    latitude: ").append(toIndentedString(this.latitude)).append("\n");
        stringBuilder.append("    longitude: ").append(toIndentedString(this.longitude)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
