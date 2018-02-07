package com.fishbowl.cbc.businesslogic.interfaces;

import com.fishbowl.cbc.businesslogic.models.User;

/**
 * Created by VT027 on 5/20/2017.
 */

public interface UserServiceCallback {
    public void onUserServiceCallback(User user, Exception exception);
}

