package com.donatos.phoenix.network.dashboard;

import p027b.p041c.C1334s;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DashboardApi {
    @GET("dashboard")
    C1334s<DashboardResponse> getDashboard(@Query("lat") Double d, @Query("lng") Double d2, @Query("location_id") Integer num);
}
