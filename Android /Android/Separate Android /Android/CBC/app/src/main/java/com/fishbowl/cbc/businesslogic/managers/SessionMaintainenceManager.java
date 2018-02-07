package com.fishbowl.cbc.businesslogic.managers;

import com.fishbowl.cbc.businesslogic.interfaces.UserServiceCallback;
import com.fishbowl.cbc.businesslogic.models.User;
import com.fishbowl.cbc.businesslogic.services.RecentOrdersService;
import com.fishbowl.cbc.businesslogic.services.UserService;
import com.parse.ParseException;
import com.parse.ParseQuery;

/**
 * Created by VT027 on 5/20/2017.
 */

public class SessionMaintainenceManager {
    public static void loadUserSession(UserServiceCallback callback) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.fromLocalDatastore();
        User user = null;
        try {
            if (query.find().size() > 0) {
                user = query.getFirst();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        callback.onUserServiceCallback(user, null);
    }

    public static void persistUserData() {
        try {
            UserService.getUser().pin();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void clearSession() {
        try {
            RecentOrdersService.clearAllData();
            User user = UserService.getUser();
            if (user != null) {
                user.unpin();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
