package com.donatos.phoenix.network.pushnotification;

import com.donatos.phoenix.network.account.AccountInteractor;
import com.donatos.phoenix.p134b.C2494a;
import com.donatos.phoenix.p134b.C2508l;
import com.donatos.phoenix.p134b.C2510n;
import java.util.ArrayList;
import java.util.List;
import p027b.p041c.p046b.C1172b;

public class PushNotificationSubscriptionService {
    private final AccountInteractor mAccountInteractor;
    private C2494a mDeviceUtil;
    private final List<C1172b> mDisposables = new ArrayList();
    private final FishbowlInteractor mFishbowlInteractor;

    public PushNotificationSubscriptionService(AccountInteractor accountInteractor, FishbowlInteractor fishbowlInteractor, C2494a c2494a) {
        this.mAccountInteractor = accountInteractor;
        this.mFishbowlInteractor = fishbowlInteractor;
        this.mDeviceUtil = c2494a;
    }

    static /* synthetic */ void lambda$addOrUpdateProfileSubscription$2(FishbowlProfileResponse fishbowlProfileResponse) throws Exception {
    }

    public void addOrUpdateActiveUserSubscription(String str) {
        if (!C2510n.m7367a(str)) {
            this.mDisposables.add(this.mAccountInteractor.getProfile(false).subscribe(PushNotificationSubscriptionService$$Lambda$1.lambdaFactory$(this, str), PushNotificationSubscriptionService$$Lambda$2.lambdaFactory$()));
        }
    }

    public void addOrUpdateProfileSubscription(FishbowlProfile fishbowlProfile, String str) {
        if (fishbowlProfile != null && !C2510n.m7367a(str)) {
            fishbowlProfile.setPushToken(str);
            fishbowlProfile.setDeviceId(this.mDeviceUtil.f7659a);
            this.mDisposables.add(this.mFishbowlInteractor.registerProfile(fishbowlProfile).subscribe(PushNotificationSubscriptionService$$Lambda$3.lambdaFactory$(), PushNotificationSubscriptionService$$Lambda$4.lambdaFactory$()));
        }
    }

    public void cleanUp() {
        for (C1172b c1172b : this.mDisposables) {
            C2508l.m7347a();
            C2508l.m7348a(c1172b);
        }
    }
}
