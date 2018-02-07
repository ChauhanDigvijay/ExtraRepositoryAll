package com.donatos.phoenix.network.searchlocation;

import p027b.p041c.C1334s;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchLocationApi {
    @GET("geocode/json")
    C1334s<SearchLocationResponse> getLocationByName(@Query("address") String str);
}
