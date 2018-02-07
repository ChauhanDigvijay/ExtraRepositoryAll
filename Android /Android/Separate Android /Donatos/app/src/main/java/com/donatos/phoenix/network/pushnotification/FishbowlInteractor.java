package com.donatos.phoenix.network.pushnotification;

import p027b.p041c.C1295k;
import p027b.p041c.p043a.p045b.C1171a;
import p027b.p041c.p066i.C1651a;

public class FishbowlInteractor {
    private final FishbowlApi mFishbowlApi;

    public FishbowlInteractor(FishbowlApi fishbowlApi) {
        this.mFishbowlApi = fishbowlApi;
    }

    public C1295k<FishbowlProfileResponse> registerProfile(FishbowlProfile fishbowlProfile) {
        return this.mFishbowlApi.registerProfile(fishbowlProfile.getEmail(), fishbowlProfile.getPhone(), fishbowlProfile.getLocation(), fishbowlProfile.getDeviceId(), fishbowlProfile.getPushToken()).subscribeOn(C1651a.m5241b()).observeOn(C1171a.m4656a());
    }
}
