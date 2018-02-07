package com.donatos.phoenix.network.validateaddress;

import com.donatos.phoenix.network.common.AddressResponse;
import p027b.p041c.C1295k;
import p027b.p041c.p043a.p045b.C1171a;
import p027b.p041c.p066i.C1651a;

public class ValidateAddressInteractor {
    private final ValidateaddressApi mValidateAddressApi;

    public ValidateAddressInteractor(ValidateaddressApi validateaddressApi) {
        this.mValidateAddressApi = validateaddressApi;
    }

    public C1295k<AddressResponse> postValidateAddress(ValidateAddressAddress validateAddressAddress) {
        return this.mValidateAddressApi.postValidateAddress(validateAddressAddress).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }
}
