package com.donatos.phoenix.network.token;

import p027b.p041c.C1295k;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TokenApi {
    @FormUrlEncoded
    @POST("token")
    C1295k<AuthenticateResponse> authenticateUser(@Field("email") String str, @Field("password") String str2);
}
