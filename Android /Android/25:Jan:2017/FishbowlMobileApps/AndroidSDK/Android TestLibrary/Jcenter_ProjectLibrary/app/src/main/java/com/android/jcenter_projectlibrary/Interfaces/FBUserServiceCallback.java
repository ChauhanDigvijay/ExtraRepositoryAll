package com.android.jcenter_projectlibrary.Interfaces;

import com.android.jcenter_projectlibrary.Models.FBMember;

/**
 * Created by Nauman Afzaal on 11/05/15.
 */
public interface FBUserServiceCallback
{
    public void onUserServiceCallback(FBMember user, Exception error);
}
