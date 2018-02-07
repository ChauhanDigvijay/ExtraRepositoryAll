package com.donatos.phoenix.network.locations;

import com.donatos.phoenix.network.account.LocationHistoryResponse;
import com.donatos.phoenix.network.common.MenuItemResponse;
import com.donatos.phoenix.network.common.Order;
import com.donatos.phoenix.network.common.OrderPriceResponse;
import com.donatos.phoenix.network.common.OrderStatusResponse;
import com.donatos.phoenix.p134b.C2507k;
import com.donatos.phoenix.p134b.C2510n;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import p027b.p040b.C1151b;
import p027b.p040b.C1160h;
import p027b.p040b.C1164l;
import p027b.p040b.C1167n;
import p027b.p041c.C1295k;
import p027b.p041c.p043a.p045b.C1171a;
import p027b.p041c.p066i.C1651a;

public class LocationsInteractor {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final int LIFETIME = 24;
    private static final int LOCATIONS_LIMIT = 25;
    private final C1160h<AvailableTimes> mLocationAvailableTimesResponseProvider;
    private final C1160h<LocationHistoryResponse> mLocationHistoryResponseProvider;
    private final C1160h<LocationHistoryResponse> mLocationHistoryResponseProviderGroup;
    private final C1151b<LocationMenuResponse> mLocationMenuResponseProvider;
    private final C1151b<LocationResponse> mLocationResponseProvider;
    private final C1160h<LocationStatusResponse> mLocationStatusResponseProvider;
    private final LocationsApi mLocationsApi;
    private final C1160h<OrderPriceResponse> mOrderPriceResponseProviderGroup;
    private final C1160h<PromotionResponse> mPromotionResponseProviderGroup;

    public LocationsInteractor(LocationsApi locationsApi, C1167n c1167n) {
        this.mLocationsApi = locationsApi;
        this.mLocationResponseProvider = c1167n.m4651a().m4629a("location");
        this.mLocationMenuResponseProvider = c1167n.m4651a().m4629a("locationMenu");
        this.mLocationStatusResponseProvider = c1167n.m4652b().m4640a(30, TimeUnit.MINUTES).m4641a("locationStatus");
        this.mLocationAvailableTimesResponseProvider = c1167n.m4652b().m4640a(1, TimeUnit.MINUTES).m4641a("locationAvailableTimes");
        this.mLocationHistoryResponseProvider = c1167n.m4652b().m4640a(24, TimeUnit.HOURS).m4641a("locationHistory");
        this.mPromotionResponseProviderGroup = c1167n.m4652b().m4640a(24, TimeUnit.HOURS).m4641a("promotions");
        this.mLocationHistoryResponseProviderGroup = c1167n.m4652b().m4641a("locationHistoryPaginated");
        this.mOrderPriceResponseProviderGroup = c1167n.m4652b().m4641a("locationOrderPrice");
    }

    private static Order cloneOrderForPricing(Order order) {
        return ((Order) C2507k.m7345a(order)).isImmediate(Boolean.valueOf(false)).date(null);
    }

    private C1295k<AvailableTimes> getLocationAvailableTimes(Integer num, String str, String str2) {
        String str3;
        String str4;
        String str5;
        if (str == null) {
            str3 = ",";
        } else {
            str3 = String.format("%s,", new Object[]{str});
        }
        if (str2 == null) {
            str4 = ",";
        } else {
            str4 = String.format("%s,", new Object[]{str2});
        }
        if (num == null) {
            str5 = "";
        } else {
            str5 = String.format("%d", new Object[]{num});
        }
        return this.mLocationsApi.getLocationAvailableTimes(num, str, str2).m4870a(new C1164l(this.mLocationAvailableTimesResponseProvider, str3 + str4 + str5)).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }

    public C1295k<OrderStatusResponse> checkOrderStatus(Integer num, String str) {
        return this.mLocationsApi.checkOrderStatus(num, str).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }

    public C1295k<ConvertOrderResponse> convertOrderTo(Integer num, Integer num2, Order order) {
        return this.mLocationsApi.convertOrderTo(num, num2, order).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }

    public C1295k<LocationResponse> getLocation(Integer num) {
        return this.mLocationsApi.getLocation(num).m4870a(this.mLocationResponseProvider.m4634a()).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }

    public C1295k<List<Date>> getLocationAvailableTimesWithDelay(Integer num, String str, String str2) {
        return getLocationAvailableTimes(num, str, str2).flatMap(LocationsInteractor$$Lambda$1.lambdaFactory$()).toList().l_();
    }

    public C1295k<LocationMenuResponse> getLocationMenu(Integer num) {
        return this.mLocationsApi.getLocationMenu(num).m4870a(this.mLocationMenuResponseProvider.m4635b()).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }

    public C1295k<MenuItemResponse> getLocationMenuItemByExternalId(Integer num, String str) {
        return this.mLocationsApi.getLocationMenuItemByExternalId(num, str).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }

    public C1295k<LocationStatusResponse> getLocationStatus(Integer num) {
        return this.mLocationsApi.getLocationStatus(num).m4870a(this.mLocationStatusResponseProvider.m4647b(C2510n.m7366a(null, null, num))).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }

    public C1295k<LocationHistoryResponse> getLocationsList(Double d, Double d2, Integer num) {
        C2510n.m7366a(d, d2, num);
        return this.mLocationsApi.getLocationsList(d, d2, num, Integer.valueOf(25)).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }

    public C1295k<LocationHistoryResponse> getLocationsListPaged(Double d, Double d2, Integer num, int i) {
        return this.mLocationsApi.getLocationsList(d, d2, num, Integer.valueOf(25)).m4870a(this.mLocationHistoryResponseProviderGroup.m4647b(Integer.valueOf(i))).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }

    public C1295k<PromotionResponse> getPromotions(Integer num) {
        return this.mLocationsApi.getPromotions(num).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }

    public C1295k<OrderPriceResponse> getTheTotalPriceOfAnOrder(Integer num, Order order, boolean z) {
        String str;
        Integer headerId = order.getHeaderId();
        if (num == null) {
            str = ",";
        } else {
            str = String.format("%d,", new Object[]{num});
        }
        str = str + String.format("%d", new Object[]{headerId});
        Order cloneOrderForPricing = cloneOrderForPricing(order);
        if (z) {
            this.mOrderPriceResponseProviderGroup.m4646a(str).m4688b();
        }
        return this.mLocationsApi.getTheTotalPriceOfAnOrder(num, cloneOrderForPricing).m4870a(this.mOrderPriceResponseProviderGroup.m4647b(str)).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }

    public C1295k<OrderStatusResponse> placeOrder(Integer num, String str, Order order) {
        return this.mLocationsApi.placeOrder(num, str, order).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }
}
