package com.olo.jambajuice.BusinessLogic.Managers;

import android.util.Log;

import com.olo.jambajuice.BusinessLogic.Interfaces.UserServiceCallback;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrderProduct;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.BusinessLogic.Services.RecentOrdersService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Nauman Afzaal on 21/05/15.
 */
public class SessionPersistenceManager {
    public static void loadUserSession(UserServiceCallback callback) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.fromLocalDatastore();
        User user = null;
        try {
            if(query.find().size()>0){
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
