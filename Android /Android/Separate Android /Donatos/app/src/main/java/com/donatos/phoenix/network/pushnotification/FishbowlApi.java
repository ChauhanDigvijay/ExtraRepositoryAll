package com.donatos.phoenix.network.pushnotification;

import p027b.p041c.C1295k;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FishbowlApi {
    @FormUrlEncoded
    @POST("fishbowl/subscribe/push")
    C1295k<FishbowlProfileResponse> registerProfile(@Field("email") String str, @Field("phoneNumber") String str2, @Field("location") String str3, @Field("deviceId") String str4, @Field("pushToken") String str5);
}
