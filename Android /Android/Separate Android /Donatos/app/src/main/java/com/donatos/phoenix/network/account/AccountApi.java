package com.donatos.phoenix.network.account;

import com.donatos.phoenix.network.common.AddressError;
import com.donatos.phoenix.network.common.AddressResponse;
import com.donatos.phoenix.network.common.User;
import p027b.p041c.C1295k;
import p027b.p041c.C1334s;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountApi {
    @DELETE("account/payment/{cardToken}")
    C1295k<ProfileResponse> deletePayment(@Path("cardToken") Integer num);

    @GET("account/locationHistory")
    C1295k<LocationHistoryResponse> getAccountLocationHistory();

    @GET("account/locationHistory")
    C1334s<LocationHistoryResponse> getAccountLocationHistory(@Query("lat") double d, @Query("lng") double d2);

    @GET("account/profile")
    C1334s<ProfileResponse> getProfile();

    @FormUrlEncoded
    @POST("account/favorite")
    C1295k<AddressError> postFavorite(@Field("header_id") Integer num);

    @FormUrlEncoded
    @POST("account/address")
    C1295k<AddressResponse> setAddress(@Field("address1") String str, @Field("city") String str2, @Field("state") String str3, @Field("zipcode") String str4, @Field("isDefault") String str5, @Field("id") String str6, @Field("address2") String str7);

    @FormUrlEncoded
    @POST("account/payment")
    C1295k<PaymentResponse> setPayment(@Field("cardHolderName") String str, @Field("cardNumber") String str2, @Field("expMonth") String str3, @Field("expYear") String str4, @Field("zipCode") String str5, @Field("cardToken") String str6, @Field("isDefault") String str7);

    @POST("account/profile")
    C1295k<ProfileResponse> setProfile(@Body User user);
}
