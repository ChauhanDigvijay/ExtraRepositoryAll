package com.olo.jambajuice.BusinessLogic.Interfaces;

import com.olo.jambajuice.BusinessLogic.Models.User;

/**
 * Created by Nauman Afzaal on 07/05/15.
 */
public interface UserServiceCallback {
    public void onUserServiceCallback(User user, Exception exception);
}
