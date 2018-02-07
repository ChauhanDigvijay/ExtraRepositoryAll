package com.olo.jambajuice.BusinessLogic.Notification;


import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;

/**
 * Created by Nauman Afzaal on 31/07/15.
 */
public class NotificationManager {
    public static void addTag(String tag) {
//        Set<String> tags = getTags();
//        tags.add(tag);
//        UAirship.shared().getPushManager().setTags(tags);

        AnalyticsManager.getInstance().trackEvent("push_notification", "add_tag", tag);

    }

    public static void removeTag(String tag) {
//        Set<String> tags = getTags();
//        tags.remove(tag);
//        UAirship.shared().getPushManager().setTags(tags);
        AnalyticsManager.getInstance().trackEvent("push_notification", "remove_tag", tag);

    }

//    private static Set<String> getTags()
//    {
//        Set<String> tags = UAirship.shared().getPushManager().getTags();
//        if (tags == null)
//        {
//            tags = new HashSet<String>();
//        }
//        return tags;
//    }

}
