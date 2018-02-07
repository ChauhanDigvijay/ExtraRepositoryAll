package com.olo.jambajuice.BusinessLogic.Services;

import android.app.Activity;

import com.olo.jambajuice.BusinessLogic.Interfaces.RewardSummaryCallback;
import com.olo.jambajuice.BusinessLogic.Models.RewardSummary;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.wearehathway.apps.spendgo.Interfaces.ISpendGoRewardSummary;
import com.wearehathway.apps.spendgo.Models.SpendGoRewardSummary;
import com.wearehathway.apps.spendgo.Services.SpendGoUserService;

import java.lang.ref.WeakReference;

/**
 * Created by Nauman Afzaal on 22/06/15.
 */
public class RewardService {
    public static RewardSummary rewardSummary = null;

    public static void getUserRewards(Activity activity, final RewardSummaryCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        User user = UserService.getUser();
        SpendGoUserService.getUserRewardSummary(user.getSpendGoId(), "", new ISpendGoRewardSummary() {
            @Override
            public void onRewardSummaryCallback(SpendGoRewardSummary spendGoRewardSummary, Exception error) {
                if (spendGoRewardSummary != null) {
                    rewardSummary = new RewardSummary(spendGoRewardSummary);
                    User user = UserService.getUser();
                    user.setTotalPoints(rewardSummary.getPoints());
                    user.setThreshold(rewardSummary.getThreshold());
                    user.setTotalRewards(rewardSummary.getRewardCount());
                    UserService.updateUserInformation();
                }
                Activity cb = callbackWeakReference.get();
                if (cb != null && !cb.isFinishing()) {
                    callback.onRewardSummaryCallback(rewardSummary, error);
                }
            }
        });
    }
}
