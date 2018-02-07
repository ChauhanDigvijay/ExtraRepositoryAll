package com.olo.jambajuice.BusinessLogic.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Nauman Afzaal on 20/08/15.
 */
@ParseClassName("Feedback")
public class Feedback extends ParseObject
{
    private static final String emailAddress = "emailAddress";
    private static final String phoneNumber = "phoneNumber";
    private static final String feedback = "feedback";
    private static final String feedbackType = "feedbackType";
    private static final String appVersion = "appVersion";
    private static final String OS = "OS";
    private static final String OSVersion = "OSVersion";

    public Feedback()
    {
    }

    public String getEmailAddress()
    {
        return getString(emailAddress);
    }

    public void setEmailAddress(String email)
    {
        put(emailAddress, email);
    }

    public String getPhoneNumber()
    {
        return getString(phoneNumber);
    }

    public void setPhoneNumber(String phone)
    {
        put(phoneNumber, phone);
    }

    public String getFeedback()
    {
        return getString(feedback);
    }

    public void setFeedback(String feedbackComment)
    {
        put(feedback, feedbackComment);
    }

    public String getFeedbackType()
    {
        return getString(feedbackType);
    }

    public void setFeedbackType(String type)
    {
        put(feedbackType, type);
    }

    public String getAppVersion()
    {
        return getString(appVersion);
    }

    public void setAppVersion(String version)
    {
        put(appVersion, version);
    }

    public String getOS()
    {
        return getString(OS);
    }

    public void setOS(String operatingSys)
    {
        put(OS, operatingSys);
    }

    public String getOSVersion()
    {
        return getString(OSVersion);
    }

    public void setOSVersion(String osVer)
    {
        put(OSVersion, osVer);
    }

    @Override
    public void put(String key, Object value)
    {
        if (value != null)
        {
            super.put(key, value);
        }
    }
}
