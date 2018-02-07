package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.pushnotification.FishbowlProfile;
import com.donatos.phoenix.p134b.C2507k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonObject
public class User {
    @JsonField(name = {"companyName"})
    private String companyName = null;
    @JsonField(name = {"deliveryAddresses"})
    private List<Address> deliveryAddresses = new ArrayList();
    @JsonField(name = {"email"})
    private String email = null;
    @JsonField(name = {"firstName"})
    private String firstName = null;
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"lastName"})
    private String lastName = null;
    @JsonField(name = {"phone"}, typeConverter = PhoneTypeConverter.class)
    private Phone phone = null;
    @JsonField(name = {"storedPayments"})
    private List<PaymentResponseContent> storedPayments = new ArrayList();

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public User addDeliveryAddressesItem(Address address) {
        this.deliveryAddresses.add(address);
        return this;
    }

    public User addStoredPaymentsItem(PaymentResponseContent paymentResponseContent) {
        this.storedPayments.add(paymentResponseContent);
        return this;
    }

    public User companyName(String str) {
        this.companyName = str;
        return this;
    }

    public User deliveryAddresses(List<Address> list) {
        this.deliveryAddresses = list;
        return this;
    }

    public User email(String str) {
        this.email = str;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return C2507k.m7346a(this.id, user.id) && C2507k.m7346a(this.firstName, user.firstName) && C2507k.m7346a(this.lastName, user.lastName) && C2507k.m7346a(this.email, user.email) && C2507k.m7346a(this.companyName, user.companyName) && C2507k.m7346a(this.phone, user.phone) && C2507k.m7346a(this.deliveryAddresses, user.deliveryAddresses) && C2507k.m7346a(this.storedPayments, user.storedPayments);
    }

    public User firstName(String str) {
        this.firstName = str;
        return this;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public List<Address> getDeliveryAddresses() {
        return this.deliveryAddresses;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Integer getId() {
        return this.id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Phone getPhone() {
        return this.phone;
    }

    public List<PaymentResponseContent> getStoredPayments() {
        return this.storedPayments;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.firstName, this.lastName, this.email, this.companyName, this.phone, this.deliveryAddresses, this.storedPayments});
    }

    public User id(Integer num) {
        this.id = num;
        return this;
    }

    public User lastName(String str) {
        this.lastName = str;
        return this;
    }

    public User phone(Phone phone) {
        this.phone = phone;
        return this;
    }

    public void setCompanyName(String str) {
        this.companyName = str;
    }

    public void setDeliveryAddresses(List<Address> list) {
        this.deliveryAddresses = list;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public void setFirstName(String str) {
        this.firstName = str;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public void setLastName(String str) {
        this.lastName = str;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public void setStoredPayments(List<PaymentResponseContent> list) {
        this.storedPayments = list;
    }

    public User storedPayments(List<PaymentResponseContent> list) {
        this.storedPayments = list;
        return this;
    }

    public FishbowlProfile toFishbowlProfile() {
        return new FishbowlProfile().email(getEmail()).phone(getPhone().getFullPhoneNumber());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class User {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    firstName: ").append(toIndentedString(this.firstName)).append("\n");
        stringBuilder.append("    lastName: ").append(toIndentedString(this.lastName)).append("\n");
        stringBuilder.append("    email: ").append(toIndentedString(this.email)).append("\n");
        stringBuilder.append("    companyName: ").append(toIndentedString(this.companyName)).append("\n");
        stringBuilder.append("    phone: ").append(toIndentedString(this.phone)).append("\n");
        stringBuilder.append("    deliveryAddresses: ").append(toIndentedString(this.deliveryAddresses)).append("\n");
        stringBuilder.append("    storedPayments: ").append(toIndentedString(this.storedPayments)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
