package com.donatos.phoenix.network.validateaddress;

import com.donatos.phoenix.network.common.AddressResponse;
import p027b.p041c.C1295k;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ValidateaddressApi {
    @POST("validateAddress")
    C1295k<AddressResponse> postValidateAddress(@Body ValidateAddressAddress validateAddressAddress);
}
