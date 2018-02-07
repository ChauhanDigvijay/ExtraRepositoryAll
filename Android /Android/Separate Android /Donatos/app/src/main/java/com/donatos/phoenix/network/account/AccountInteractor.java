package com.donatos.phoenix.network.account;

import com.donatos.phoenix.network.common.Address;
import com.donatos.phoenix.network.common.AddressError;
import com.donatos.phoenix.network.common.AddressResponse;
import com.donatos.phoenix.network.common.Location;
import com.donatos.phoenix.network.common.PaymentResponseContent;
import com.donatos.phoenix.network.common.User;
import com.donatos.phoenix.p134b.C2510n;
import java.util.List;
import java.util.concurrent.TimeUnit;
import p027b.p040b.C1151b;
import p027b.p040b.C1151b.C1150a;
import p027b.p040b.C1152c;
import p027b.p040b.C1160h;
import p027b.p040b.C1167n;
import p027b.p041c.C1186b;
import p027b.p041c.C1295k;
import p027b.p041c.p043a.p045b.C1171a;
import p027b.p041c.p066i.C1651a;

public class AccountInteractor {
    private final AccountApi mAccountApi;
    private final C1160h<LocationHistoryResponse> mLocationHistoryResponseProvider;
    private final C1151b<ProfileResponse> mProfileResponseProvider;

    public AccountInteractor(AccountApi accountApi, C1167n c1167n) {
        this.mAccountApi = accountApi;
        C1150a a = c1167n.m4651a();
        TimeUnit timeUnit = TimeUnit.MINUTES;
        a.f3571d = Long.valueOf(1);
        a.f3572e = timeUnit;
        this.mProfileResponseProvider = a.m4629a("profile");
        this.mLocationHistoryResponseProvider = c1167n.m4652b().m4641a("accountLocationHistory");
    }

    public C1295k<ProfileResponse> deletePayment(Integer num) {
        return this.mAccountApi.deletePayment(num).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }

    public C1295k<Location> getAccountLocation(Integer num) {
        return getAccountLocationHistory().map(AccountInteractor$$Lambda$1.lambdaFactory$()).flatMap(AccountInteractor$$Lambda$2.lambdaFactory$()).filter(AccountInteractor$$Lambda$3.lambdaFactory$(num));
    }

    public C1295k<LocationHistoryResponse> getAccountLocationHistory() {
        return this.mAccountApi.getAccountLocationHistory().subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }

    public C1295k<LocationHistoryResponse> getAccountLocationHistory(double d, double d2) {
        C2510n.m7366a(Double.valueOf(d), Double.valueOf(d2), null);
        return this.mAccountApi.getAccountLocationHistory(d, d2).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }

    public C1295k<List<Address>> getAddresses(boolean z) {
        return getProfile(z).map(AccountInteractor$$Lambda$4.lambdaFactory$());
    }

    public C1295k<ProfileResponse> getProfile(boolean z) {
        if (z) {
            C1186b.m4685a(C1152c.m4636a(this.mProfileResponseProvider)).m4688b();
        }
        return this.mAccountApi.getProfile().m4870a(this.mProfileResponseProvider.m4635b()).m4873b(C1651a.m5241b()).m4869a(C1171a.m4656a()).l_();
    }

    public C1295k<PaymentResponseContent> getStoredPayment(boolean z, Long l) {
        return getProfile(z).flatMap(AccountInteractor$$Lambda$6.lambdaFactory$(l));
    }

    public C1295k<List<PaymentResponseContent>> getStoredPayments(boolean z) {
        return getProfile(z).map(AccountInteractor$$Lambda$5.lambdaFactory$());
    }

    public C1295k<AddressError> postFavorite(Integer num) {
        return this.mAccountApi.postFavorite(num).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }

    public C1295k<AddressResponse> setAddress(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        return this.mAccountApi.setAddress(str, str2, str3, str4, str5, str6, str7).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }

    public C1295k<PaymentResponse> setPayment(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        return this.mAccountApi.setPayment(str, str2, str3, str4, str5, str6, str7).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }

    public C1295k<ProfileResponse> setProfile(User user) {
        return this.mAccountApi.setProfile(user).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }
}
