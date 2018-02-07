package com.donatos.phoenix.network.token;

import com.donatos.phoenix.p134b.C2509m;
import p027b.p041c.C1295k;
import p027b.p041c.p043a.p045b.C1171a;
import p027b.p041c.p066i.C1651a;

public class TokenInteractor {
    private final C2509m mSharedPreferencesUtil;
    private final TokenApi mTokenApi;

    public TokenInteractor(TokenApi tokenApi, C2509m c2509m) {
        this.mTokenApi = tokenApi;
        this.mSharedPreferencesUtil = c2509m;
    }

    private void saveAuthTokenToSharedPrefs(AuthenticateResponse authenticateResponse) {
        this.mSharedPreferencesUtil.m7356a("auth_token", authenticateResponse.getContent().getToken());
        this.mSharedPreferencesUtil.m7357a(false);
    }

    public C1295k<AuthenticateResponse> authenticateUser(String str, String str2) {
        return this.mTokenApi.authenticateUser(str, str2).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a()).doOnNext(TokenInteractor$$Lambda$1.lambdaFactory$(this));
    }
}
