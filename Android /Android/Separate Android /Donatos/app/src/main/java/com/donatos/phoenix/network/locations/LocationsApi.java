package com.donatos.phoenix.network.locations;

import com.donatos.phoenix.network.account.LocationHistoryResponse;
import com.donatos.phoenix.network.common.MenuItemResponse;
import com.donatos.phoenix.network.common.Order;
import com.donatos.phoenix.network.common.OrderPriceResponse;
import com.donatos.phoenix.network.common.OrderStatusResponse;
import p027b.p041c.C1295k;
import p027b.p041c.C1334s;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LocationsApi {
    @GET("locations/{id}/order/{guid}")
    C1295k<OrderStatusResponse> checkOrderStatus(@Path("id") Integer num, @Path("guid") String str);

    @POST("locations/{from}/convertOrderTo/{to}")
    C1295k<ConvertOrderResponse> convertOrderTo(@Path("from") Integer num, @Path("to") Integer num2, @Body Order order);

    @GET("locations/{id}")
    C1334s<LocationResponse> getLocation(@Path("id") Integer num);

    @GET("locations/{id}/availableTimes")
    C1334s<AvailableTimes> getLocationAvailableTimes(@Path("id") Integer num, @Query("date") String str, @Query("orderType") String str2);

    @GET("locations/{id}/menu")
    C1334s<LocationMenuResponse> getLocationMenu(@Path("id") Integer num);

    @GET("locations/{id}/menu/itemSearch")
    C1295k<MenuItemResponse> getLocationMenuItemByExternalId(@Path("id") Integer num, @Query("externalId") String str);

    @GET("locations/{id}/status")
    C1334s<LocationStatusResponse> getLocationStatus(@Path("id") Integer num);

    @GET("locations")
    C1334s<LocationHistoryResponse> getLocationsList(@Query("lat") Double d, @Query("lng") Double d2, @Query("location_id") Integer num, @Query("limit") Integer num2);

    @GET("locations/{id}/promotions")
    C1334s<PromotionResponse> getPromotions(@Path("id") Integer num);

    @POST("locations/{id}/orderPrice")
    C1334s<OrderPriceResponse> getTheTotalPriceOfAnOrder(@Path("id") Integer num, @Body Order order);

    @POST("locations/{id}/order/{guid}")
    C1295k<OrderStatusResponse> placeOrder(@Path("id") Integer num, @Path("guid") String str, @Body Order order);
}
