package com.donatos.phoenix.network.searchlocation;

import java.util.concurrent.TimeUnit;
import p027b.p040b.C1160h;
import p027b.p040b.C1167n;
import p027b.p041c.C1295k;
import p027b.p041c.p043a.p045b.C1171a;
import p027b.p041c.p066i.C1651a;

public class SearchLocationInteractor {
    private static final int LIFETIME_DAYS = 7;
    private final SearchLocationApi mSearchLocationApi;
    private final C1160h<SearchLocationLocation> mSearchLocationResponseProvider;

    public SearchLocationInteractor(SearchLocationApi searchLocationApi, C1167n c1167n) {
        this.mSearchLocationApi = searchLocationApi;
        this.mSearchLocationResponseProvider = c1167n.m4652b().m4640a(7, TimeUnit.DAYS).m4641a("locationSearchHistory");
    }

    static /* synthetic */ SearchLocationLocation lambda$getLocationByName$0(SearchLocationResponse searchLocationResponse) throws Exception {
        SearchLocationResult results = searchLocationResponse.getResults(Integer.valueOf(0));
        return results != null ? results.getGeometry().getLocation() : null;
    }

    public C1295k<SearchLocationLocation> getLocationByName(String str) {
        String toLowerCase = str != null ? str.trim().toLowerCase() : "";
        return this.mSearchLocationApi.getLocationByName(toLowerCase).m4872b(SearchLocationInteractor$$Lambda$1.lambdaFactory$()).m4870a(this.mSearchLocationResponseProvider.m4647b(toLowerCase)).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }
}
